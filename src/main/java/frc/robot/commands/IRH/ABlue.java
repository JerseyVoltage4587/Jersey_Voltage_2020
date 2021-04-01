// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.IRH;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.AutoMoveFoward;
import frc.robot.commands.LowerIntakeArm;
import frc.robot.commands.RaiseIntakeArm;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class ABlue extends SequentialCommandGroup {
  /** Creates a new ABlue. */
  public ABlue() {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(new SequentialCommandGroup(new LowerIntakeArm(), new Turn(90), new AutoMoveFoward(90), new Turn(90), new AutoMoveFoward(30), new Turn(90), new AutoMoveFoward(60), new Turn(90), new AutoMoveFoward(90), new RaiseIntakeArm(), new AutoMoveFoward(75)));
  }
}
