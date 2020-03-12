/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.OI;
import frc.robot.subsystems.DriveBase;
import frc.robot.util.Gyro;

public class TurnToAngle extends CommandBase {
  private double m_angle;
  private double m_finalAngle;
  private boolean m_ifInitialized = false;
  /**
   * Creates a new TurnToAngle.
   */
  public TurnToAngle(double angle) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_angle = angle;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    
    m_ifInitialized = false;
    DriveBase.getInstance().setSafetyEnabled(false);
    SmartDashboard.putString("TurnToAngle", "initialize");
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double heading = Gyro.getYaw();
    if (m_ifInitialized == false){
      if (Gyro.IMU_IsCalibrating()) {
        return;
      }
      m_ifInitialized = true;
      m_finalAngle = heading + m_angle;
    }
    double delta = m_finalAngle - heading;
    if (delta < -180){
      delta += 360;
    }

    if (delta > 180){
      delta -= 360;
    }

    if (delta > 10) {
      DriveBase.getInstance().setLeftMotorLevel(.25);
      DriveBase.getInstance().setRightMotorLevel(-.25);
    }

    else if (delta < -10) {
      DriveBase.getInstance().setLeftMotorLevel(-.25);
      DriveBase.getInstance().setRightMotorLevel(.25);
    }

    else if (delta > 0) {
      DriveBase.getInstance().setLeftMotorLevel(.175);
      DriveBase.getInstance().setRightMotorLevel(-.175);
    }

    else if (delta < 0) {
      DriveBase.getInstance().setLeftMotorLevel(-.175);
      DriveBase.getInstance().setRightMotorLevel(.175);
    } 
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    DriveBase.getInstance().setLeftMotorLevel(0);
    DriveBase.getInstance().setRightMotorLevel(0);
    DriveBase.getInstance().setSafetyEnabled(true);
    SmartDashboard.putString("TurnToAngle", "end");
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
    
    if (delta > 180){
      delta = 360 - delta;
    }

    if (delta <= 2){
      return true;
    }
    return false;
  }
}
