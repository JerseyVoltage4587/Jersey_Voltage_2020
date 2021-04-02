// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.IRH;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Robot;
import frc.robot.commands.AutoMoveFoward;
import frc.robot.commands.LowerIntakeArm;

public class TryBBlue extends CommandBase {
  /** Creates a new TryBBlue. */
  public TryBBlue() {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(Robot.getDriveBase());
    addRequirements(Robot.getIntake());
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println("Try B Blue");
    CommandScheduler.getInstance().schedule(new LowerIntakeArm(), new AutoMoveFoward(30));
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (Robot.getIntake().hasPickedUpBall()) {
      Robot.getDriveBase().setLayout("BBlue");
    }
    else if ((Robot.getDriveBase().getPartialLeftInches() + Robot.getDriveBase().getPartialRightInches()) / 2 > 33) {
      Robot.getDriveBase().setLayout("ARed");
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    Robot.getIntake().setPickedUpBall(0);
    if (Robot.getDriveBase().getLayout().equals("BBlue")) {
      CommandScheduler.getInstance().schedule(new BBlue());
    }
    else {
      CommandScheduler.getInstance().schedule(new ARed());
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (Robot.getDriveBase().getLayout().equals("BBlue")) {
      return true;
    }
    else if (Robot.getDriveBase().getLayout().equals("ARed")) {
      return true;
    }
    return false;
  }
}