/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;

public class MoveToShootingPosition extends CommandBase {
  double ty = 0;
  /**
   * Creates a new MoveToShootingPosition.
   */
  public MoveToShootingPosition() {
    
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(Robot.getDriveBase());
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").forceSetNumber(0);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    ty = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);

    if (ty < -3) {
      Robot.getDriveBase().setLeftMotorLevel(-0.2);
      Robot.getDriveBase().setRightMotorLevel(-0.2);
    }

    if (ty > 3) {
      Robot.getDriveBase().setLeftMotorLevel(0.2);
      Robot.getDriveBase().setRightMotorLevel(0.2);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    Robot.getDriveBase().setLeftMotorLevel(0);
    Robot.getDriveBase().setRightMotorLevel(0);
    NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").forceSetNumber(2);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (ty > 3 || ty < -3) {
      return false;
    }
    return true;
  }
}
