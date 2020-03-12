/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.*;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Robot;

public class Aim extends CommandBase {
  private static Aim me;

  public static Aim getInstance() {
    return me;
  }

  /**
   * Creates a new Aim.
   */
  public Aim() {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(Robot.getDriveBase());
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    //NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").forceSetNumber(0);
    double tx = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
    if (Math.abs(tx) > 2) {
      CommandScheduler.getInstance().schedule(new SequentialCommandGroup(new TurnToAngle(tx), new WaitCommand(.2), new Aim()));
    }
    SmartDashboard.putNumber("Aim", tx);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    //NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").forceSetNumber(2);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
