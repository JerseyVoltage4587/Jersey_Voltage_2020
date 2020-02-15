/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.*;

import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;



/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  DifferentialDrive m_drive = null;
  //private Command m_autonomousCommand;
  private WPI_TalonSRX m_talon1;
  private WPI_TalonSRX m_talon2;
  private WPI_VictorSPX m_victor11;
  private WPI_VictorSPX m_victor21;
  private Joystick m_joy = null;
  //private Spark m_spark4;
  //private Spark m_spark5;
  //private Spark m_spark6;
  //private RobotContainer m_robotContainer;
  //private JoystickButton buttonA1, leftBumper1, rightBumper1, buttonB1, buttonX1;
  //private JoyButton leftTrigger1, rightTrigger1;
  //private static Robot me;

  //public static Robot getInstance() {
    //return me;
  //}

  /*public double getTalonMotorLevel() {
    return m_talon.get();
  }

  public void setTalonMotorLevel(double x) {
    m_talon.set(x);
  }

  public void setSpark4MotorLevel(double x) {
    m_spark4.set(x);
  }
  public double getSpark5MotorLevel() {
    return m_spark5.get();
  }

  public void setSpark5MotorLevel(double x) {
    m_spark5.set(x);
  }
  public double getSpark6MotorLevel() {
    return m_spark6.get();
  }

  public void setSpark6MotorLevel(double x) {
    m_spark6.set(x);
  }
  */
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    //me = this;
    //m_robotContainer = new RobotContainer();
    m_talon1 = new WPI_TalonSRX(1);
    m_talon1 = new WPI_TalonSRX(2);
    m_victor11 = new WPI_VictorSPX(11);
    m_victor21 = new WPI_VictorSPX(21);
    //m_spark4 = new Spark(4);
    //m_spark5 = new Spark(5);
    //m_spark6 = new Spark(6);
    m_joy = new Joystick(0);
    m_victor11.follow(m_talon1);
    m_victor21.follow(m_talon2);
    m_talon1.setInverted(false);
    m_talon2.setInverted(true);
    m_victor11.setInverted(InvertType.FollowMaster);
    m_victor21.setInverted(InvertType.FollowMaster);
    m_drive = new DifferentialDrive(m_talon1, m_talon2);
    m_drive.setRightSideInverted(false);
    //buttonA1 = new JoystickButton(m_joy, 1);
    //buttonB1 = new JoystickButton(m_joy, 2);
    //buttonX1 = new JoystickButton(m_joy, 3);
    //leftBumper1 = new JoystickButton(m_joy, 5);
    //rightBumper1 = new JoystickButton(m_joy, 6);
    //leftTrigger1 = new JoyButton(m_joy, JoyButton.JoyDir.DOWN, 2);
    //rightTrigger1 = new JoyButton(m_joy, JoyButton.JoyDir.DOWN, 3);
    //leftTrigger1.whenPressed(new ToggleTalonMotorLevel());
    //buttonB1.whenPressed(new ToggleTalonFoward());
    //buttonX1.whenPressed(new ToggleTalonBackward());
    //rightTrigger1.whileHeld(new RunSpark4());
    //rightBumper1.whenPressed(new ToggleSpark5Forward());
    //leftBumper1.whenPressed(new ToggleSpark5Backward());
    //buttonA1.whenPressed(new ToggleSpark6());
    
    m_joy = new Joystick(0);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
  }

  /**
   * This function is called once each time the robot enters Disabled mode.
   */
  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
  }

  /**
   * This autonomous runs the autonomous command selected by your {@link RobotContainer} class.
   */
  @Override
  public void autonomousInit() {
    //m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    // schedule the autonomous command (example)
    //if (m_autonomousCommand != null) {
//      m_autonomousCommand.schedule();
    //}
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    //if (m_autonomousCommand != null) {
//      m_autonomousCommand.cancel();
    //}
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    double forward = -1.0 * m_joy.getY();	// Sign this so forward is positive
    double turn = +1.0 * m_joy.getRawAxis(4);

    if (Math.abs(forward) < 0.10) {
			forward = 0;
		}
		if (Math.abs(turn) < 0.10) {
			turn = 0;
    }
    
    m_drive.arcadeDrive(forward, turn);
  }

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
