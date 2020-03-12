/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.networktables.NetworkTableInstance;
<<<<<<< HEAD
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
=======
>>>>>>> a373c27e30a772308d42cd3bf99c0a4de3ebc847
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class PositionRobotToShootForward extends CommandBase {
  /**
   * Creates a new PositionRobotToShootForward.
   */
  public PositionRobotToShootForward() {
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
<<<<<<< HEAD
    SmartDashboard.putString("Position Robot", "initialize");
=======
>>>>>>> a373c27e30a772308d42cd3bf99c0a4de3ebc847
    NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").forceSetNumber(3);
    CommandScheduler.getInstance().schedule(new SequentialCommandGroup(new Aim(), new WaitCommand(.2), new MoveToShootingPosition()));        
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
<<<<<<< HEAD
    SmartDashboard.putString("Position Robot", "end");
=======
>>>>>>> a373c27e30a772308d42cd3bf99c0a4de3ebc847
    NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").forceSetNumber(1);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
