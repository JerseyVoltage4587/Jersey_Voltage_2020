// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.IRH;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Robot;
import frc.robot.commands.AutoMoveFoward;
import frc.robot.commands.LowerIntakeArm;

public class TryBRed extends CommandBase {
  /** Creates a new TryBRed. */
  public TryBRed() {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(Robot.getDriveBase());
    addRequirements(Robot.getIntake());
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println("Try B Red");
    CommandScheduler.getInstance().schedule(new SequentialCommandGroup(new LowerIntakeArm(), new WaitCommand(2), new AutoMoveFoward(75)));
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (Robot.getIntake().hasPickedUpBall()) {
      Robot.getDriveBase().setLayout("BRed");
    }
    else if ((Robot.getDriveBase().getPartialLeftInches() + Robot.getDriveBase().getPartialRightInches()) / 2 > 78) {
      Robot.getDriveBase().setLayout("Try ABlue");
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    Robot.getIntake().setPickedUpBall(0);
    if (Robot.getDriveBase().getLayout().equals("BRed")) {
      CommandScheduler.getInstance().schedule(new BRed());
    }
    else {
      //CommandScheduler.getInstance().schedule(new TryABlue());
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (Robot.getDriveBase().getLayout().equals("BRed")) {
      return true;
    }
    else if (Robot.getDriveBase().getLayout().equals("Try ABlue")) {
      return true;
    }
    return false;
  }
}