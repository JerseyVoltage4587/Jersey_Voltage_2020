package frc.robot.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

public class AsyncAdHocLogger extends CrashTrackingRunnable
{
    private static String LogDirectory = "/home/lvuser";
    public static void setLogDirectory ( String dirPath )
    {
        LogDirectory = dirPath;
    }

    private StringBuilder qdbg = null;
    private StringBuilder pdbg = null;

    PrintStream m_writer = null;

    private String m_baseName = null;

    private final int MAX_PENDING_MESSAGES = 100;
    private final int MAX_PENDING_ITEMS    = 10 * MAX_PENDING_MESSAGES;
    private final int STRING_ITEM          = 0;
    private final int DOUBLE_ITEM          = 1;
    private final int LONG_ITEM            = 2;
    private int[]    m_pendingFirsts       = null;
    private int[]    m_pendingLasts        = null;
    private int[]    m_pendingItems        = null;
    private String[] m_pendingStrings      = null;
    private double[] m_pendingDoubles      = null;
    private long  [] m_pendingLongs        = null;
    private int      m_nextPendingMessage  = 0;
    private int      m_lastPrintedMessage  = -1;
    private int      m_nextPendingItem     = 0;
    private int      m_skipped             = 0;
    private int      m_lastReportedSkips   = 0;
    // For pending message "i", the first queued item index will be stored in m_pendingFirsts[i], and the last queued
    // item index will be stored in m_pendingLasts[i]. Calls these ifirst and ilast.
    // Every item between ifirst and ilast will be printed as a single line to the output.
    // For each i_item in the range from ifirst to ilast, inclusive, m_pendingItems[i_item] will specify the type of
    // item to be printed, as STRING_ITEM, DOUBLE_ITEM, or LONG_ITEM. The value itself will be found in the appropriate
    // array -- m_pendingStrings, m_pendingDoubles, or m_pendingLongs -- at index i_item.
    // When m_pendingFirsts[i] == -1, that means that the i'th message slot is open.
    // When m_pendingFirsts[i] != -1, but m_pendingLasts[i] == -1, that means the i'th message is being queued.
    // When m_pendingLasts[i] != -1, that means the i'th message is ready to be printed.

    private Thread m_thread = null;

    public AsyncAdHocLogger ( String baseName )
    {
        this ( baseName, false );
    }

    public AsyncAdHocLogger ( String baseName, boolean forceUnique )
    {
        m_baseName = baseName;
        qdbg = new StringBuilder();
        pdbg = new StringBuilder();
        try {
            if(baseName.equals("")){
                m_writer = System.out;
            }else{
                StringBuilder file_name = new StringBuilder();
                file_name.append(baseName);
                if ( forceUnique ) {
                    file_name.append("-").append(System.nanoTime());
                }
                file_name.append(".txt");
                File log_path = new File ( LogDirectory, file_name.toString() );
                m_writer = new PrintStream ( log_path );
            }
        } catch ( FileNotFoundException ex ) {
            System.err.println("AsyncAdHocLogger("+m_baseName+")  E R R O R !!");
            System.err.println("... Unable to create log file!");
            ex.printStackTrace();
            m_writer = null;
            return;
        }
        m_writer.println ( "AsyncAdHocLogger  S T A R T I N G" );
        m_writer.println ( new Date().toString() );
        m_writer.flush();

        m_pendingFirsts  = new int[MAX_PENDING_MESSAGES];
        m_pendingLasts   = new int[MAX_PENDING_MESSAGES];
        m_pendingItems   = new int   [MAX_PENDING_ITEMS];
        m_pendingStrings = new String[MAX_PENDING_ITEMS];
        m_pendingDoubles = new double[MAX_PENDING_ITEMS];
        m_pendingLongs   = new long  [MAX_PENDING_ITEMS];
        for ( int i = 0 ; i < MAX_PENDING_MESSAGES ; ++i ) {
            m_pendingFirsts[i] = -1;
            m_pendingLasts [i] = -1;
        }
        for ( int i = 0 ; i < MAX_PENDING_ITEMS ; ++i ) {
            m_pendingItems  [i] = -1;
            m_pendingStrings[i] = null;
            m_pendingDoubles[i] = -1;
            m_pendingLongs  [i] = -1;
        }

        m_thread = new Thread(this);
        m_thread.setDaemon ( true );
        m_thread.start();
    }

    // Queue a string
    public AsyncAdHocLogger q ( String value )
    {
        if ( m_writer == null ) {
            // The constructor failed, so we can't do anything.
            return this;
        }
        synchronized(this) {
            if ( m_pendingItems[m_nextPendingItem] != -1 ) {
                // This is an overrun situation. The data in m_nextPendingItem hasn't been printed yet.
                ++m_skipped;
                return this;
            }
            if ( m_pendingFirsts[m_nextPendingMessage] != -1 ) {
                if ( m_pendingLasts[m_nextPendingMessage] != -1 ) {
                    // This is an overrun situation. The next pending message slot is still in use.
                    ++m_skipped;
                    return this;
                }
            }
            if ( m_pendingFirsts[m_nextPendingMessage] == -1 ) {
                m_pendingFirsts[m_nextPendingMessage] = m_nextPendingItem;
            }
            m_pendingItems  [m_nextPendingItem] = STRING_ITEM;
            m_pendingStrings[m_nextPendingItem] = value;
            m_nextPendingItem = (m_nextPendingItem + 1) % MAX_PENDING_ITEMS;
        }
        return this;
    }

    // Queue a double
    public AsyncAdHocLogger q ( double value )
    {
        if ( m_writer == null ) {
            // The constructor failed, so we can't do anything.
            return this;
        }
        synchronized(this) {
            if ( m_pendingItems[m_nextPendingItem] != -1 ) {
                // This is an overrun situation. The data in m_nextPendingItem hasn't been printed yet.
                ++m_skipped;
                return this;
            }
            if ( m_pendingFirsts[m_nextPendingMessage] != -1 ) {
                if ( m_pendingLasts[m_nextPendingMessage] != -1 ) {
                    // This is an overrun situation. The next pending message slot is still in use.
                    ++m_skipped;
                    return this;
                }
            }
            if ( m_pendingFirsts[m_nextPendingMessage] == -1 ) {
                m_pendingFirsts[m_nextPendingMessage] = m_nextPendingItem;
            }
            m_pendingItems  [m_nextPendingItem] = DOUBLE_ITEM;
            m_pendingDoubles[m_nextPendingItem] = value;
            m_nextPendingItem = (m_nextPendingItem + 1) % MAX_PENDING_ITEMS;
        }
        return this;
    }

    // Queue an integer
    public AsyncAdHocLogger q ( long value )
    {
        if ( m_writer == null ) {
            // The constructor failed, so we can't do anything.
            return this;
        }
        synchronized(this) {
            if ( m_pendingItems[m_nextPendingItem] != -1 ) {
                // This is an overrun situation. The data in m_nextPendingItem hasn't been printed yet.
                ++m_skipped;
                return this;
            }
            if ( m_pendingFirsts[m_nextPendingMessage] != -1 ) {
                if ( m_pendingLasts[m_nextPendingMessage] != -1 ) {
                    // This is an overrun situation. The next pending message slot is still in use.
                    ++m_skipped;
                    return this;
                }
            }
            if ( m_pendingFirsts[m_nextPendingMessage] == -1 ) {
                m_pendingFirsts[m_nextPendingMessage] = m_nextPendingItem;
            }
            m_pendingItems  [m_nextPendingItem] = LONG_ITEM;
            m_pendingLongs  [m_nextPendingItem] = value;
            m_nextPendingItem = (m_nextPendingItem + 1) % MAX_PENDING_ITEMS;
        }
        return this;
    }

    // Release the message currently being constructed.
    public AsyncAdHocLogger go()
    {
        if ( m_writer == null ) {
            // The constructor failed, so we can't do anything.
            return this;
        }
        synchronized(this) {
            if ( m_pendingLasts[m_nextPendingMessage] != -1 ) {
                // This is an overrun situation. The next pending message slot is still in use.
                return this;
            }
            int last_queued_item = (m_nextPendingItem - 1) % MAX_PENDING_ITEMS;
            m_pendingLasts[m_nextPendingMessage] = last_queued_item;
            m_nextPendingMessage = (m_nextPendingMessage + 1) % MAX_PENDING_MESSAGES;
        }
        return this;
    }

    public AsyncAdHocLogger go ( String value )
    {
        return q(value).go();
    }

    public AsyncAdHocLogger go ( double value )
    {
        return q(value).go();
    }

    public AsyncAdHocLogger go ( long value )
    {
        return q(value).go();
    }

    public void runCrashTracked()
    {
        if ( m_writer == null ) return;
        StringBuilder line = new StringBuilder();
        while ( true ) {
            int i_print = (m_lastPrintedMessage + 1) % MAX_PENDING_MESSAGES;
            if ( m_skipped != m_lastReportedSkips ) {
                line.setLength(0);
                line.append("Skipped ")
                    .append(m_skipped - m_lastReportedSkips)
                    .append(" queuing calls since last report. Total skips now = ")
                    .append(m_skipped);
                m_writer.println(line.toString());
                m_writer.flush();
                m_lastReportedSkips = m_skipped;
            }
            if ( m_pendingLasts[i_print] != -1 ) {
                line.setLength(0);
                synchronized(this) {
                    for ( int i_item = m_pendingFirsts[i_print] ; i_item <= m_pendingLasts[i_print] ; ++i_item ) {
                        //if ( i_item > m_pendingFirsts[i_print] ) {
                        //    line.append(" ");
                        //}
                        switch ( m_pendingItems[i_item] ) {
                            case STRING_ITEM: line.append(m_pendingStrings[i_item]); break;
                            case DOUBLE_ITEM: line.append(m_pendingDoubles[i_item]); break;
                            case LONG_ITEM  : line.append(m_pendingLongs  [i_item]); break;
                        }
                        m_pendingItems[i_item] = -1;
                    }
                    m_pendingFirsts[i_print] = -1;
                    m_pendingLasts [i_print] = -1;
                    m_lastPrintedMessage = i_print;
                }
                m_writer.println(line.toString());
                m_writer.flush();
            } else {
                delay(10);
            }
        }
    }

    private static final long MAX_FLUSH_WAIT = 1 * 1000 * 1000 * 1000; // nanoseconds

    public void flush()
    {
        if ( m_writer == null ) return;
        long flush_start = System.nanoTime();
        while ( (System.nanoTime() - flush_start) < MAX_FLUSH_WAIT ) {
            int i_print = (m_lastPrintedMessage + 1) % MAX_PENDING_MESSAGES;
            if ( m_pendingLasts[i_print] == -1 ) {
                break;
            } else {
                delay(10);
            }
        }
        m_writer.println ( "AsyncAdHocLogger  F I N I S H E D" );
        m_writer.flush();
    }

    private static void delay ( long milliseconds )
    {
        Object lock = new Object();
        try {
            synchronized(lock) {
                lock.wait(milliseconds);
            }
        } catch ( InterruptedException ex ) {
        }
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public static void main ( String argv[] )
    {
        AsyncAdHocLogger.setLogDirectory(".");
        long t0 = System.nanoTime();
        AsyncAdHocLogger ahl = new AsyncAdHocLogger ( "ahl", /*forceUnique=*/false );
        long t1 = System.nanoTime();
        System.out.println("Constructor execution time = "+((t1 - t0)/1000000)+" msec");

        for ( int iq = 0 ; iq < 20 ; ++iq ) {
            t0 = System.nanoTime();
            ahl.q("iq = ").q(iq).q(", another string").go();
            t1 = System.nanoTime();
            System.out.println("Queue "+iq+" execution time = "+((t1 - t0)/1000)+" usec");
            //delay(1);
        }
        System.out.println("m_skipped = "+ahl.m_skipped);

        t0 = System.nanoTime();
        ahl.flush();
        t1 = System.nanoTime();
        System.out.println("Flush execution time = "+((t1 - t0)/1000000)+" msec");

        System.out.println("Queue Debug:");
        System.out.println(ahl.qdbg.toString());
        System.out.println("Print Debug:");
        System.out.println(ahl.pdbg.toString());
    }
}
