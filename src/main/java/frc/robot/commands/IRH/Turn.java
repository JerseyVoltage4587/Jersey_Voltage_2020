// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.IRH;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.util.Gyro;

public class Turn extends CommandBase {
  /** Creates a new Turn. */
  double heading = 0;
  int setAngle = 0;
  public Turn(int angle) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(Robot.getDriveBase());
    setAngle = angle;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    heading = Gyro.getYaw();;
    if (setAngle > 0) {
      Robot.getDriveBase().setLeftMotorLevel(0.5);
      Robot.getDriveBase().setRightMotorLevel(-0.5);
    }
    else {
      Robot.getDriveBase().setLeftMotorLevel(-0.5);
      Robot.getDriveBase().setRightMotorLevel(0.5);
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    Robot.getDriveBase().setLeftMotorLevel(0);
    Robot.getDriveBase().setRightMotorLevel(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    setAngle -= 10;
    if (setAngle > 0) {
      if (Gyro.getYaw() > heading + setAngle) {
        return true;
      }      
    }
    else if (Gyro.getYaw() < heading + setAngle) {
      return true;
    }
    return false;
  }
}
