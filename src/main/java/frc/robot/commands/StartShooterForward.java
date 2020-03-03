/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.Robot;

public class StartShooterForward extends CommandBase {
  double lastLeftEncoder = 0;
  double lastRightEncoder = 0;
  double leftEncoder = 0;
  double rightEncoder = 0;
  int leftInitialEncoder = 0;
  int rightInitialEncoder = 0;
  int count = 0;
  double leftEncoderRevolutions = 0;
  double rightEncoderRevolutions = 0;
  double RPM = 0;
  double averageRPM = 0;
  double lastAverageRPM = 0;
  double totalRPM = 0;
  double elapsedTime = 0;
  double motorLevel = 0;
  long nanoSeconds = 0;
  long lastNanoSeconds = 0;
  /**
   * Creates a new StartShooterForward.
   */
  public StartShooterForward() {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(Robot.getShooter(), Robot.getStorage());
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    motorLevel = Constants.ShooterMotorLevel;
    Robot.getShooter().setShooterMotorLevel(motorLevel);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double leftEncoder = -1 * Robot.getShooter().getLeftShooterEncoder();
    double rightEncoder = -1 * Robot.getShooter().getRightShooterEncoder();
    long nanoSeconds = System.nanoTime();

    elapsedTime = ((nanoSeconds - lastNanoSeconds) / 1000000000.0) / 60.0;
    leftEncoderRevolutions = ((leftEncoder - lastLeftEncoder) / 4096.0) / elapsedTime;
    rightEncoderRevolutions = ((rightEncoder - lastRightEncoder) / 4096.0) / elapsedTime;
    RPM = (leftEncoderRevolutions + rightEncoderRevolutions) / 2;
    averageRPM = (RPM + lastAverageRPM) / 2;
    totalRPM += averageRPM + lastAverageRPM;
    
    if (lastAverageRPM < 2818 || lastAverageRPM > 2858) {
      double error = Math.abs(lastAverageRPM - 2838);
       motorLevel = 0.7 + (.00009 * error);
    }
    
    Robot.getShooter().setShooterMotorLevel(motorLevel);

    lastAverageRPM = RPM;
    lastLeftEncoder = leftEncoder;
    lastRightEncoder = rightEncoder;
    lastNanoSeconds = nanoSeconds;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (lastAverageRPM > 2818 && lastAverageRPM < 2858) {
      Robot.getStorage().setShooterReady(true);
      return true;
    }

    return false;
  }
}
