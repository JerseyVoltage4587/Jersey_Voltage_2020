/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.OI;
import frc.robot.Robot;
import frc.robot.subsystems.DriveBase;

public class DefaultDriveBaseCommand extends CommandBase {
  /**
   * Creates a new DefaultDriveBase.
   */
  public DefaultDriveBaseCommand() {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(DriveBase.getInstance());
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Robot.getDriveBase().setSafetyEnabled(true);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double forward = OI.getInstance().getDrive();
    double turn = OI.getInstance().getTurn();

    if (Math.abs(forward) < 0.10) {
			forward = 0;
		}
		if (Math.abs(turn) < 0.10) {
			turn = 0;
    }

    Robot.getDriveBase().m_drive.arcadeDrive(forward, turn);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    Robot.getDriveBase().setSafetyEnabled(false);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}