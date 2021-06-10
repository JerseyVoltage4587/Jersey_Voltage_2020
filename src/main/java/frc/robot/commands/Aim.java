/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.*;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.OI;
import frc.robot.Robot;
import frc.robot.subsystems.DriveBase;

public class Aim extends CommandBase {
  private static Aim me;
  public static int tvFails = 0;
  double tv = 0;
  public static final int tvLimit = 10;

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
    double tx = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
    tv = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0);

    if (Math.abs(OI.getInstance().getDrive()) > 0.10) {
      return;
    }
    if (Math.abs(OI.getInstance().getTurn()) > 0.10) {
      return;
    }

    if (tv == 0) {
      tvFails++;
      SmartDashboard.putString("Aim", String.format("tvfails: %d", tvFails));
      if (tvFails < tvLimit)
        CommandScheduler.getInstance().schedule(new SequentialCommandGroup(new WaitCommand(.1), new Aim()));      
    }
    else if (Math.abs(tx) > 2) {
      SmartDashboard.putString("Aim", String.format("tx: %f", tx));
      CommandScheduler.getInstance().schedule(new SequentialCommandGroup(new TurnToAngle(tx), new WaitCommand(.2), new Aim()));
      tvFails = 0;
    }
    else
    {
      SmartDashboard.putString("Aim", "end");
      CommandScheduler.getInstance().schedule(new MoveToShootingPosition());
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    DriveBase.getInstance().setSafetyEnabled(true);
  }

  @Override
  public boolean isFinished()
  {
    return true;
  }
}