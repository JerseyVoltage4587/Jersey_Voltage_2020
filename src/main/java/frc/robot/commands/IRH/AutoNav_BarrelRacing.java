// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.IRH;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.AutoMoveFoward;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class AutoNav_BarrelRacing extends SequentialCommandGroup {
  /** Creates a new AutoNav_BarrelRace. */
  public AutoNav_BarrelRacing() {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    clearGroupedCommands();
    addCommands(new SequentialCommandGroup(new AutoMoveFoward(150), new WaitCommand(0.15), new Turn(90), new WaitCommand(0.15), new AutoMoveFoward(34), new WaitCommand(0.15), new Turn(180), new WaitCommand(0.15), new AutoMoveFoward(51), new WaitCommand(0.15), new Turn(-90), new WaitCommand(0.15), new AutoMoveFoward(42), new WaitCommand(0.15), new Turn(0), new WaitCommand(0.15), new AutoMoveFoward(107), new WaitCommand(0.15), new Turn(-90), new WaitCommand(0.15), new AutoMoveFoward(59), new WaitCommand(0.15), new Turn(180), new WaitCommand(0.15), new AutoMoveFoward(59), new WaitCommand(0.15), new Turn(90), new WaitCommand(0.15), new AutoMoveFoward(53), new WaitCommand(0.15), new Turn(0), new WaitCommand(0.15), new AutoMoveFoward(90), new WaitCommand(0.15), new Turn(90), new WaitCommand(0.15), new AutoMoveFoward(43), new WaitCommand(0.15), new Turn(180)));
  }
}
