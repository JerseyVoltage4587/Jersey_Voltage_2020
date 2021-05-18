// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.IRH;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Robot;
import frc.robot.commands.AutoMoveFoward;
import frc.robot.commands.LowerIntakeArm;

public class TryABlue extends CommandBase {
  /** Creates a new TryABlue. */
  public TryABlue() {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(Robot.getDriveBase());
    addRequirements(Robot.getIntake());
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println("Try A Blue");
    //CommandScheduler.getInstance().schedule(new LowerIntakeArm(), new AutoMoveFoward(120));
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (Robot.getIntake().hasPickedUpBall()) {
      Robot.getDriveBase().setLayout("ABlue");
    }
    else if ((Robot.getDriveBase().getPartialLeftInches() + Robot.getDriveBase().getPartialRightInches()) / 2 > 123) {
      Robot.getDriveBase().setLayout("Try BBlue");
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    Robot.getIntake().setPickedUpBall(0);
    if (Robot.getDriveBase().getLayout().equals("ABlue")) {
      CommandScheduler.getInstance().schedule(new ABlue());
    }
    else {
      CommandScheduler.getInstance().schedule(new TryBBlue());
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (Robot.getDriveBase().getLayout().equals("ABlue")) {
      return true;
    }
    else if (Robot.getDriveBase().getLayout().equals("Try ABlue")) {
      return true;
    }
    return false;
  }
}