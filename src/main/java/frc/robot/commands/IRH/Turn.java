/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.IRH;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.OI;
import frc.robot.Robot;
import frc.robot.util.Gyro;

public class Turn extends CommandBase {
  private double m_angle = 0;
  private double m_finalAngle = 0;
  private boolean m_ifInitialized = false;
  private double m_lastHeading = 0;
  private int m_number_of_stalled = 0;
  private int m_finished_counter = 0;
  /**
   * Creates a new TurnToAngle.
   */
  public Turn(double angle) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(Robot.getDriveBase());
    m_angle = angle;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println("Turn " + Gyro.getYaw() + " S T A R T");
    m_ifInitialized = false;
    Robot.getDriveBase().setSafetyEnabled(false);
    m_number_of_stalled = 0;
    m_finished_counter = 0;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double heading = Gyro.getYaw();
    if ((heading - m_lastHeading) < 1) {
      m_number_of_stalled += 1;
    }
    else {
      m_number_of_stalled = 0;
    }

    if (m_ifInitialized == false){
      if (Gyro.IMU_IsCalibrating()) {
        return;
      }
      m_ifInitialized = true;
      m_finalAngle = m_angle;
    }
    double delta = m_finalAngle - heading;
    if (delta < -180) {
      delta += 360;
    }

    if (delta > 180) {
      delta -= 360;
    }

    if (delta > 10) {
      Robot.getDriveBase().setLeftMotorLevel(.35);
      Robot.getDriveBase().setRightMotorLevel(-.35);
    }

    else if (delta < -10) {
      Robot.getDriveBase().setLeftMotorLevel(-.35);
      Robot.getDriveBase().setRightMotorLevel(.35);
    }

    else if (delta > 0) {
      Robot.getDriveBase().setLeftMotorLevel(.3);
      Robot.getDriveBase().setRightMotorLevel(-.3);
    }

    else if (delta < 0) {
      Robot.getDriveBase().setLeftMotorLevel(-.3);
      Robot.getDriveBase().setRightMotorLevel(.3);
    }

    
    if (m_number_of_stalled > 5) {
      if (delta > 10) {
        Robot.getDriveBase().setLeftMotorLevel(.41);
        Robot.getDriveBase().setRightMotorLevel(-.41);
      }
  
      else if (delta < -10) {
        Robot.getDriveBase().setLeftMotorLevel(-.41);
        Robot.getDriveBase().setRightMotorLevel(.41);
      }

      else if (delta > 0) {
        Robot.getDriveBase().setLeftMotorLevel(.4);
        Robot.getDriveBase().setRightMotorLevel(-.4);
      }
  
      else if (delta < 0) {
        Robot.getDriveBase().setLeftMotorLevel(-.4);
        Robot.getDriveBase().setRightMotorLevel(.4);
      }
    }

    if (m_number_of_stalled > 10) {
      if (delta > 10) {
        Robot.getDriveBase().setLeftMotorLevel(.475);
        Robot.getDriveBase().setRightMotorLevel(-.475);
      }
  
      else if (delta < -10) {
        Robot.getDriveBase().setLeftMotorLevel(-.475);
        Robot.getDriveBase().setRightMotorLevel(.475);
      }

      else if (delta > 0) {
        Robot.getDriveBase().setLeftMotorLevel(.45);
        Robot.getDriveBase().setRightMotorLevel(-.45);
      }
  
      else if (delta < 0) {
        Robot.getDriveBase().setLeftMotorLevel(-.45);
        Robot.getDriveBase().setRightMotorLevel(.45);
      }
    }

    m_lastHeading = heading;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    System.out.println("Turn " + Gyro.getYaw() + " D O N E");
    Robot.getDriveBase().setLeftMotorLevel(0);
    Robot.getDriveBase().setRightMotorLevel(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (Math.abs(OI.getInstance().getDrive()) > 0.10) {
      return true;
    }

    if (Math.abs(OI.getInstance().getTurn()) > 0.10) {
      return true;
    }

    if (m_ifInitialized == false) {
      return false;
    }

    double heading = Gyro.getYaw();
    double delta = Math.abs(heading - m_finalAngle);
    
    if (delta > 180) {
      delta = 360 - delta;
    }

    if (delta <= 2){
      m_finished_counter += 1;
      if (m_finished_counter > 3) {
        return true;
      }
    }
    return false;
  }
}