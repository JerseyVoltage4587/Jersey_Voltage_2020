package frc.robot.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

public class AsyncStructuredLogger<T> extends CrashTrackingRunnable
{
    private static String LogDirectory = "/home/lvuser";
    public static void setLogDirectory ( String dirPath )
    {
        LogDirectory = dirPath;
    }

    private final int MAX_PENDING_OBJECTS = 100;

    private String        m_baseName            = null;
    private boolean       m_forceUnique         = false;
    private String        m_dataClassName       = null;
    private PrintWriter   m_writer              = null;
    private StringBuilder m_header              = null;
    private T[]           m_pendingObjects      = null;
    private T             m_transferObject      = null;
    private long[]        m_queuedTimes         = null;
    private int           m_nextPendingObject   =   -1;
    private int           m_lastPrintedObject   =   -1;
    private int           m_skippedQueues       =    0;

    private Field[] m_fields = null;
    private enum DataFieldType { DOUBLE, FLOAT, LONG, INT, SHORT, STRING, BOOLEAN, UNRECOGNIZED }
    private DataFieldType[] m_fieldTypes = null;

    private Thread m_thread = null;

    public AsyncStructuredLogger ( String baseName, Class<T> dataClass )
    {
        this ( baseName, false, dataClass );
    }

    public AsyncStructuredLogger ( String baseName, boolean forceUnique, Class<T> dataClass )
    {
        m_baseName      = baseName;
        m_forceUnique   = forceUnique;
        m_dataClassName = dataClass.getName();

        // Build the queue array.

        try {
            m_queuedTimes = new long[MAX_PENDING_OBJECTS];
            @SuppressWarnings("unchecked")
            final T[] the_array = (T[]) Array.newInstance(dataClass,MAX_PENDING_OBJECTS);
            m_pendingObjects = the_array;
            for ( int i = 0; i < MAX_PENDING_OBJECTS ; ++i ) {
                m_queuedTimes   [i] = 0;
                m_pendingObjects[i] = dataClass.getDeclaredConstructor().newInstance();
            }
            m_transferObject = dataClass.getDeclaredConstructor().newInstance();
            m_nextPendingObject = 0;
            m_lastPrintedObject = -1;
        } catch ( Exception ex ) {
            System.err.println("AsyncStructuredLogger<"+dataClass.getName()+">  E R R O R !!");
            System.err.println("... Unable to create data object array!");
            ex.printStackTrace();
            m_pendingObjects = null;
            return;
        }

        // Interpret the fields of the data structure to be logged.

        m_fields = dataClass.getFields();
        m_fieldTypes = new DataFieldType[m_fields.length];
        m_header = new StringBuilder();
        m_header.append("\"NanoTime\",\"Skipped Queues\"");
        int i_field = -1;
        for ( Field data_field : m_fields ) {
            ++i_field;
            m_header.append(",\"").append(data_field.getName()).append("\"");
            String field_type = data_field.getType().toString();
            if ( field_type.equals("double") ) {
                m_fieldTypes[i_field] = DataFieldType.DOUBLE;
            } else if ( field_type.equals("float") ) {
                m_fieldTypes[i_field] = DataFieldType.FLOAT;
            } else if ( field_type.equals("long") ) {
                m_fieldTypes[i_field] = DataFieldType.LONG;
            } else if ( field_type.equals("int") ) {
                m_fieldTypes[i_field] = DataFieldType.INT;
            } else if ( field_type.equals("short") ) {
                m_fieldTypes[i_field] = DataFieldType.SHORT;
            } else if ( field_type.equals("class java.lang.String") ) {
                m_fieldTypes[i_field] = DataFieldType.STRING;
            } else if ( field_type.equals("boolean") ) {
                m_fieldTypes[i_field] = DataFieldType.BOOLEAN;
            } else {
                System.err.println("AsyncStructuredLogger<"+m_dataClassName+">  W A R N I N G !!");
                System.err.println("... Field "+data_field.getName()+" has unrecognized type: "+field_type);
                m_fieldTypes[i_field] = DataFieldType.UNRECOGNIZED;
            }
        }

        // Start the logging thread.

        m_thread = new Thread(this);
        m_thread.setDaemon ( true );
        m_thread.start();
    }

    public void queueData ( T dataObject )
    {
        if ( m_pendingObjects == null ) {
            // The constructor failed, so we can't do anything.
            return;
        }
        if ( m_queuedTimes[m_nextPendingObject] != 0 ) {
            // This is an overrun situation. The data in m_nextPendingObject hasn't been printed yet.
            ++m_skippedQueues;
            return;
        }
        synchronized(this) {
            m_queuedTimes[m_nextPendingObject] = System.nanoTime();
            T target = m_pendingObjects[m_nextPendingObject];
            for ( int i_field = 0 ; i_field < m_fields.length ; ++i_field ) {
                Field f = m_fields[i_field];
                try {
                    switch ( m_fieldTypes[i_field] ) {
                        case DOUBLE: f.setDouble ( target, f.getDouble(dataObject) ); break;
                        case FLOAT:  f.setFloat  ( target, f.getFloat (dataObject) ); break;
                        case LONG:   f.setLong   ( target, f.getLong  (dataObject) ); break;
                        case INT:    f.setInt    ( target, f.getInt   (dataObject) ); break;
                        case SHORT:  f.setShort  ( target, f.getShort (dataObject) ); break;
                        case STRING: f.set       ( target, f.get      (dataObject) ); break;
                        case BOOLEAN:f.setBoolean( target,f.getBoolean(dataObject) ); break;
                        case UNRECOGNIZED: break;
                    }
                } catch ( Exception ex ) {
                    System.err.println("AsyncStructuredLogger<"+m_dataClassName+">  E R R O R !!");
                    System.err.println("... Field "+f.getName()+" cannot be copied!");
                    System.err.println("... dataObject class = "+dataObject.getClass().getName());
                }
            }
            m_nextPendingObject = (m_nextPendingObject + 1) % MAX_PENDING_OBJECTS;
        }
    }

    public void runCrashTracked()
    {
        try {
            StringBuilder file_name = new StringBuilder();
            file_name.append(m_baseName);
            if ( m_forceUnique ) {
                file_name.append("-").append(System.nanoTime());
            }
            file_name.append(".csv");
            File log_path = new File ( LogDirectory, file_name.toString() );
            m_writer = new PrintWriter ( log_path );
            System.out.println("Opened File " + log_path);
        } catch ( FileNotFoundException ex ) {
            System.err.println("AsyncStructuredLogger<"+m_dataClassName+">  E R R O R !!");
            System.err.println("... Unable to create log file!");
            ex.printStackTrace();
            m_writer = null;
            return;
        }
        m_writer.println(m_header.toString());
        m_writer.flush();

        StringBuilder line = new StringBuilder();

        while ( true ) {
            int i_print = (m_lastPrintedObject + 1) % MAX_PENDING_OBJECTS;
            if ( m_queuedTimes[i_print] != 0 ) {
                line.setLength(0);
                long queued_time;
                int n_skipped_queues;
                synchronized(this) {
                    queued_time      = m_queuedTimes[i_print];
                    n_skipped_queues = m_skippedQueues;
                    T data_object = m_pendingObjects[i_print];
                    for ( int i_field = 0 ; i_field < m_fields.length ; ++i_field ) {
                        Field f = m_fields[i_field];
                        try {
                            switch ( m_fieldTypes[i_field] ) {
                                case DOUBLE:  f.setDouble (m_transferObject,f.getDouble (data_object)); break;
                                case FLOAT:   f.setFloat  (m_transferObject,f.getFloat  (data_object)); break;
                                case LONG:    f.setLong   (m_transferObject,f.getLong   (data_object)); break;
                                case INT:     f.setInt    (m_transferObject,f.getInt    (data_object)); break;
                                case SHORT:   f.setShort  (m_transferObject,f.getShort  (data_object)); break;
                                case STRING:  f.set       (m_transferObject,f.get       (data_object)); break;
                                case BOOLEAN: f.setBoolean(m_transferObject,f.getBoolean(data_object)); break;
                                case UNRECOGNIZED: break;
                            }
                        } catch ( Exception ex ) {
                        }
                    }
                    m_queuedTimes[i_print] = 0;
                }
                line.append(queued_time).append(",").append(n_skipped_queues);
                for ( int i_field = 0 ; i_field < m_fields.length ; ++i_field ) {
                    line.append(",");
                    Field f = m_fields[i_field];
                    try {
                        switch ( m_fieldTypes[i_field] ) {
                            case DOUBLE: line.append(f.getDouble(m_transferObject)); break;
                            case FLOAT:  line.append(f.getFloat (m_transferObject)); break;
                            case LONG:   line.append(f.getLong  (m_transferObject)); break;
                            case INT:    line.append(f.getInt   (m_transferObject)); break;
                            case SHORT:  line.append(f.getShort (m_transferObject)); break;
                            case STRING: line.append("\"")
                                             .append(f.get(m_transferObject).toString())
                                             .append("\"");                          break;
                            case BOOLEAN:line.append(f.getBoolean(m_transferObject));break;
                            case UNRECOGNIZED: break;
                        }
                    } catch ( Exception ex ) {
                    }
                }
                m_writer.println(line.toString());
                m_lastPrintedObject = i_print;
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
            int i_print = (m_lastPrintedObject + 1) % MAX_PENDING_OBJECTS;
            if ( m_queuedTimes[i_print] == 0 ) {
                break;
            } else {
                delay(10);
            }
        }
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

    private static class TestDataClass
    {
        public double d1, d2, d3;
        public int    i1, i2, i3;
        public String s1;
    }

    public static void main ( String argv[] )
    {
        AsyncStructuredLogger.setLogDirectory(".");
        long t0 = System.nanoTime();
        AsyncStructuredLogger<TestDataClass> asl = new AsyncStructuredLogger<TestDataClass> (
                                                        "asl", /*forceUnique=*/false, TestDataClass.class );
        long t1 = System.nanoTime();
        System.out.println("Constructor execution time = "+((t1 - t0)/1000000)+" msec");

        long usec, min_usec = -1, max_usec = -1, tot_usec = -1, n_loop = 0;

        int loop_count = 10000;

        TestDataClass tdc = new TestDataClass();
        for ( int i_loop = 1; i_loop <= loop_count ; ++i_loop ) {
            if ( (i_loop % 500) == 0 ) System.out.println("... start iteration "+i_loop+" of "+loop_count);
            tdc.d1 = i_loop * 0.1;
            tdc.d2 = i_loop * 0.2;
            tdc.d3 = i_loop * 0.3;
            tdc.i1 = i_loop * 10;
            tdc.i2 = i_loop * 20;
            tdc.i3 = i_loop * 30;
            tdc.s1 = Integer.toString(i_loop);
            t0 = System.nanoTime();
            asl.queueData ( tdc );
            t1 = System.nanoTime();
            usec = (t1 - t0) / 1000;
            if ( i_loop == 1 ) {
                min_usec = usec;
                max_usec = usec;
                tot_usec = usec;
                n_loop   = 1;
            } else {
                if ( usec < min_usec ) { min_usec = usec; }
                if ( usec > max_usec ) { max_usec = usec; }
                tot_usec += usec;
                ++n_loop;
            }
            //System.out.println("Queue "+i_loop+" in "+((t1 - t0)/1000)+" usec: "+npo+" -> "+asl.m_nextPendingObject);

            delay(5);
        }
        System.out.println ( "Iterations........: "+n_loop );
        System.out.println ( "... Min Queue usec: "+min_usec );
        System.out.println ( "... Max Queue usec: "+max_usec );
        System.out.println ( "... Avg Queue usec: "+((tot_usec + (n_loop/2))/n_loop) );
        System.out.println ( "... Skipped Queues: "+asl.m_skippedQueues );

        t0 = System.nanoTime();
        asl.flush();
        t1 = System.nanoTime();
        System.out.println("Flush execution time = "+((t1 - t0)/1000000)+" msec");
    }
}
