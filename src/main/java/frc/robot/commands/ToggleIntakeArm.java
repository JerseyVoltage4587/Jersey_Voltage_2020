/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Robot;

public class ToggleIntakeArm extends CommandBase {
  /**
   * Creates a new ToggleIntakeArm.
   */
  public ToggleIntakeArm() {
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if (Robot.getIntake().getIntakeArmMotorStatus().equals("DOWN")) {
      CommandScheduler.getInstance().schedule(new RaiseIntakeArm());
    }

    else if (Robot.getIntake().getIntakeArmMotorStatus().equals("UP")) {
      CommandScheduler.getInstance().schedule(new LowerIntakeArm());
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
