/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climber extends SubsystemBase {
  static Climber m_Instance = null;
  //private ? m_climberMotor1;
  //private ? m_climberMotor2;
  /**
   * Creates a new Climber.
   */
  public Climber() {

  }

  public static Climber getInstance() {
    if(m_Instance == null) {
			synchronized (Climber.class) {
				m_Instance = new Climber();
			}
		}
		return m_Instance;
  }

  public static void setClimberMotorLevel() {
    
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
