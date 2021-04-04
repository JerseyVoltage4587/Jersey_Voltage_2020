/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Robot;

public class AutoMoveFoward extends CommandBase {
  int setDistance = 0;
  double rightMotorLevelChange = 0.35;
  double leftInches = 0;
  double startLeftInches = 0;
  double startRightInches = 0;
  double rightInches = 0;
  double averageInches = 0;
  /**
   * Creates a new AutoMoveFoward.
   */
  public AutoMoveFoward(int distance) {
    setDistance = distance;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(Robot.getDriveBase());
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Robot.getDriveBase().setSafetyEnabled(false);
    Robot.getDriveBase().zeroDriveSensors();
    Robot.getDriveBase().setRightMotorLevel(0.5);
    Robot.getDriveBase().setLeftMotorLevel(0.5);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    leftInches = Robot.getDriveBase().getLeftDistanceInches();
    rightInches = Robot.getDriveBase().getRightDistanceInches();
    if (leftInches < rightInches - 5) {
      rightMotorLevelChange -= 0.03;
      Robot.getDriveBase().setRightMotorLevel(rightMotorLevelChange);
    }
    if (leftInches > rightInches + 5) {
      rightMotorLevelChange += 0.03;
      Robot.getDriveBase().setRightMotorLevel(rightMotorLevelChange);
    }
    averageInches = (leftInches + rightInches) / 2;
    if (averageInches > (setDistance - 40)) {  
      Robot.getDriveBase().setRightMotorLevel(0.4);
      Robot.getDriveBase().setLeftMotorLevel(0.4);
    }
    else if (averageInches > (setDistance - 20)) {
      Robot.getDriveBase().setRightMotorLevel(0.3);
      Robot.getDriveBase().setLeftMotorLevel(0.3);
    }
    else if (averageInches > (setDistance - 10)) {
      Robot.getDriveBase().setRightMotorLevel(0.2);
      Robot.getDriveBase().setLeftMotorLevel(0.2);
    }
    else {  
      Robot.getDriveBase().setRightMotorLevel(0.5);
      Robot.getDriveBase().setLeftMotorLevel(0.5);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    System.out.println("Move: Done\n" + averageInches);
    Robot.getDriveBase().setLeftMotorLevel(0);
    Robot.getDriveBase().setRightMotorLevel(0);
    CommandScheduler.getInstance().schedule(new WaitCommand(0.15));
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    System.out.println(averageInches);
    if (Robot.getOI().getDrive() > 0.7) {
      return true;
    }

    if (averageInches >= setDistance) {
      return true;
    }

    return false;
  }
}
