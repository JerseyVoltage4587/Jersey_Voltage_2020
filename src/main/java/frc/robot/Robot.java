/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import javax.print.event.PrintJobListener;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.Aim;
import frc.robot.commands.AutoMoveFoward;
import frc.robot.commands.DefaultDriveBaseCommand;
import frc.robot.commands.StartKicker;
import frc.robot.commands.StartShooterForward;
import frc.robot.commands.StopShooter;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.DriveBase;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Storage;
import frc.robot.util.Gyro;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static Robot me;

  public static Robot getInstance() {
    return me;
  }

  private static PowerDistributionPanel m_PDP;

  public static PowerDistributionPanel getPDP() {
    if (m_PDP == null) {
      m_PDP = new PowerDistributionPanel();
    }
    return m_PDP;
  }

  public static DriveBase getDriveBase() {
    return DriveBase.getInstance();
  }

  public static Intake getIntake() {
    return Intake.getInstance();
  }

  public static Shooter getShooter() {
    return Shooter.getInstance();
  }

  public static Storage getStorage() {
    return Storage.getInstance();
  }

  public static Climber getClimber() {
    return Climber.getInstance();
  }

  public static OI getOI() {
    return OI.getInstance();
  }

  public static Gyro getGyro() {
    return Gyro.getInstance();
  }

  public static Aim getAim() {
    return Aim.getInstance();
  }

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    getDriveBase().setDefaultCommand(new DefaultDriveBaseCommand());
    CameraServer.getInstance();
    getDriveBase().zeroDriveSensors();
    getIntake().zeroIntakeSensors();
    getClimber();
    getShooter();
    getStorage();
    getOI();
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for
   * items like diagnostics that you want ran during disabled, autonomous,
   * teleoperated and test.
   *
   * <p>
   * This runs after the mode specific periodic functions, but before LiveWindow
   * and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler. This is responsible for polling buttons, adding
    // newly-scheduled
    // commands, running already-scheduled commands, removing finished or
    // interrupted commands,
    // and running subsystem periodic() methods. This must be called from the
    // robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
    SmartDashboard.putNumber("tv", NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0));
    SmartDashboard.putNumber("ty", NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0));
    SmartDashboard.putNumber("tx", NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0));
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
   * This autonomous runs the autonomous command selected by your
   * {@link RobotContainer} class.
   */
  @Override
  public void autonomousInit() {
    // m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    // schedule the autonomous command (example)
    // if (m_autonomousCommand != null) {
    // m_autonomousCommand.schedule();
    // }
    getStorage().setShooterRunning(false);
    getStorage().setShooterReady(false);
    CommandScheduler.getInstance().schedule(new StartShooterForward(), new SequentialCommandGroup(new AutoMoveFoward(66),new WaitCommand(1), new StartKicker()));
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    
  }

  @Override
  public void teleopInit() {
    CommandScheduler.getInstance().schedule(new StopShooter());
    getClimber().setRobotClimberState("INITIAL");
    getClimber().zeroClimberMotors();
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    
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
