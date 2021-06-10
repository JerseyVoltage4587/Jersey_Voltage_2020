/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.subsystems.DriveBase;

public class MoveToShootingPosition extends CommandBase {
  double ty = 0;
  double tv = 0;
  public static int tvFails = 0;
  public static final int tvLimit = 100;
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
    tvFails = 0;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    ty = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
    tv = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0);
    if (tv == 0) {
      Robot.getDriveBase().setLeftMotorLevel(0);
      Robot.getDriveBase().setRightMotorLevel(0);
    }

    else if (ty < 5) {
      Robot.getDriveBase().setLeftMotorLevel(-0.2);
      Robot.getDriveBase().setRightMotorLevel(-0.2);
    }

    else if (ty > 10) {
      Robot.getDriveBase().setLeftMotorLevel(0.2);
      Robot.getDriveBase().setRightMotorLevel(0.2);
    }
    SmartDashboard.putString("MoveToPosition", "executing");
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    Robot.getDriveBase().setLeftMotorLevel(0);
    Robot.getDriveBase().setRightMotorLevel(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (tv == 0) {
      tvFails++;
      return tvFails > tvLimit;
    }

    else if (ty > 10 || ty < 5) {
      return false;
    }
    SmartDashboard.putString("MoveToPosition", "end");
    DriveBase.getInstance().setSafetyEnabled(true);
    return true;
  }
}
