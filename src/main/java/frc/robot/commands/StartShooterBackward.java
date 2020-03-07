/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;

public class StartShooterBackward extends CommandBase {
  
  /**
   * Creates a new StartShooter.
   */
  public StartShooterBackward() {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(Robot.getShooter());
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Robot.getShooter().setSetPoint(-2838);
    
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    if (Robot.getShooter().isShooterReady()) {
      Robot.getStorage().setShooterReady(true);
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return Robot.getShooter().isShooterReady();
  }
}
