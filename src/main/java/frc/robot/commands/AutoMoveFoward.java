/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.util.*;

public class AutoMoveFoward extends CommandBase {
  int setDistance = 0;
  double rightMotorLevelChange = 0.36;
  double leftInches = 0;
  double startLeftInches = 0;
  double startRightInches = 0;
  double rightInches = 0;
  double averageInches = 0;
  double m_heading = 0;
  boolean first = true;
  /**
   * Creates a new AutoMoveFoward.
   */
  public AutoMoveFoward(int distance, double heading) {
    setDistance = distance;
    m_heading = heading;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(Robot.getDriveBase());
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println("Move " + setDistance + " S T A R T");
    Robot.getDriveBase().setSafetyEnabled(false);
    Robot.getDriveBase().zeroDriveSensors(false);
    System.out.println("Move " + ((Robot.getDriveBase().getLeftDistanceInches() + Robot.getDriveBase().getRightDistanceInches()) / 2) + " Z E R O");
    Robot.getDriveBase().setRightMotorLevel(0.36);
    Robot.getDriveBase().setLeftMotorLevel(0.4);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (first) {
      return;
    }
    leftInches = Robot.getDriveBase().getLeftDistanceInches();
    rightInches = Robot.getDriveBase().getRightDistanceInches();
    averageInches = (leftInches + rightInches) / 2;
    double delta = Gyro.getYaw() - m_heading;

    if (delta > 180) {
      delta -= 360;
    }

    if (delta < -180) {
      delta += 360;
    }

    if (Math.abs(delta) > 2) {
      if (delta < 0) {
        rightMotorLevelChange -= 0.01;
        Robot.getDriveBase().setRightMotorLevel(rightMotorLevelChange);
      }
      else if (delta > 0) {
        rightMotorLevelChange += 0.01;
        Robot.getDriveBase().setRightMotorLevel(rightMotorLevelChange);
      }
    }
    
    else {
      rightMotorLevelChange = 0.36;
      Robot.getDriveBase().setRightMotorLevel(rightMotorLevelChange);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    System.out.println("Move " + averageInches + " D O N E");
    Robot.getDriveBase().setLeftMotorLevel(0);
    Robot.getDriveBase().setRightMotorLevel(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (first) {
      first = false;
      return false;
    }
    if (Robot.getOI().getDrive() > 0.7) {
      return true;
    }

    if (averageInches >= setDistance) {
      return true;
    }

    return false;
  }
}
