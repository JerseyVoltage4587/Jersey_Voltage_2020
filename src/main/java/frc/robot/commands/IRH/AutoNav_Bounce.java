// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.IRH;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.AutoMoveFoward;
import frc.robot.commands.AutoTurnAround;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class AutoNav_Bounce extends SequentialCommandGroup {
  /** Creates a new AutoNav_Bounce. */
  public AutoNav_Bounce() {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    clearGroupedCommands();
    //addCommands(new SequentialCommandGroup(new AutoMoveFoward(45), new Turn(-90), new AutoMoveFoward(60), new AutoTurnAround(), new AutoMoveFoward(60), new Turn(-90), new AutoMoveFoward(30), new Turn(90), new AutoMoveFoward(60), new Turn(-90), new AutoMoveFoward(60), new Turn(-90), new AutoMoveFoward(120), new AutoTurnAround(), new AutoMoveFoward(120), new Turn(-90), new AutoMoveFoward(90), new Turn(-90), new AutoMoveFoward(120), new AutoTurnAround(), new AutoMoveFoward(60), new Turn(-90), new AutoMoveFoward(60)));
  }
}
