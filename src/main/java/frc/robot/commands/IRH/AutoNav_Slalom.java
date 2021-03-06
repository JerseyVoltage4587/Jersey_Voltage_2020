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
public class AutoNav_Slalom extends SequentialCommandGroup {
  /** Creates a new AutoNav_Slalom. */
  public AutoNav_Slalom() {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    clearGroupedCommands();
    addCommands(new SequentialCommandGroup(new AutoMoveFoward(35, 0), new WaitCommand(0.15), new Turn(-90), new WaitCommand(0.15), new AutoMoveFoward(35, -90), new WaitCommand(0.15), new Turn(0), new WaitCommand(0.15), new AutoMoveFoward(152, 0), new WaitCommand(0.15), new Turn(90), new WaitCommand(0.15), new AutoMoveFoward(35, 90), new WaitCommand(0.15), new Turn(0), new WaitCommand(0.15), new AutoMoveFoward(38, 0), new WaitCommand(0.15), new Turn(-90), new WaitCommand(0.15), new AutoMoveFoward(35, -90), new WaitCommand(0.15), new Turn(180), new WaitCommand(0.15), new AutoMoveFoward(35, 180), new WaitCommand(0.15), new Turn(90), new WaitCommand(0.15), new AutoMoveFoward(35, 90), new WaitCommand(0.15), new Turn(180), new WaitCommand(0.15), new AutoMoveFoward(152, 180), new WaitCommand(0.15), new Turn(-90), new WaitCommand(0.15), new AutoMoveFoward(35, -90), new WaitCommand(0.15), new Turn(180), new WaitCommand(0.15), new AutoMoveFoward(35, 180), new WaitCommand(0.15)));
  }
}
