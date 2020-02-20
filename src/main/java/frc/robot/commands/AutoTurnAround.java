/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Robot;
import frc.robot.commands.TurnToAngle;
import frc.robot.util.Gyro;;

public class AutoTurnAround extends CommandBase {
  double heading = 0;
  /**
   * Creates a new AutoTurnAround.
   */
  public AutoTurnAround() {
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    heading = Gyro.getYaw();
    SmartDashboard.putNumber("heading", heading);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (heading < 150) {
      Robot.getDriveBase().setLeftMotorLevel(0.75);
      Robot.getDriveBase().setRightMotorLevel(-0.75);
    }

    else if (heading < 165) {
      Robot.getDriveBase().setLeftMotorLevel(0.5);
      Robot.getDriveBase().setRightMotorLevel(-0.5);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (heading > 170) {
      return true;
    }

    return false;
  }
}
