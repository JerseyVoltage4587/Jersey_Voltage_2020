// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.IRH;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Robot;
import frc.robot.commands.AutoMoveFoward;
import frc.robot.commands.LowerIntakeArm;
import frc.robot.commands.RaiseIntakeArm;

public class Path extends CommandBase {
  /** Creates a new Path. */
  String layout = "ARed";

  public Path() {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(Robot.getDriveBase());
    addRequirements(Robot.getIntake());
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    CommandScheduler.getInstance().schedule(new LowerIntakeArm(), new AutoMoveFoward(75));
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (Robot.getIntake().IsArmMotorStalled()) {
      layout = "BRed";
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    CommandScheduler.getInstance().schedule(new RaiseIntakeArm());
    if (layout.equals("BRed")) {
      CommandScheduler.getInstance().schedule(new BRed());
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (layout.equals("BRed")) {
      return true;
    }
    else if (Robot.getDriveBase().getLeftMotorLevel() == 0 && Robot.getDriveBase().getRightMotorLevel() == 0) {
      return true;
    }
    return false;
  }
}
