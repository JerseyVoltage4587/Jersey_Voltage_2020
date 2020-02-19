/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climber extends SubsystemBase {
  static Climber m_Instance = null;
  private WPI_TalonSRX m_leftClimberMotor;
  private WPI_TalonSRX m_rightClimberMotor;
  /**
   * Creates a new Climber.
   */
  public Climber() {
    m_leftClimberMotor = new WPI_TalonSRX(2);
    m_rightClimberMotor = new WPI_TalonSRX(3);
  }

  public static Climber getInstance() {
    if(m_Instance == null) {
			synchronized (Climber.class) {
				m_Instance = new Climber();
			}
		}
		return m_Instance;
  }

  public void setLeftClimberMotorLevel(double x) {
    m_leftClimberMotor.set(x);
  }

  public void setRightClimberMotorLevel(double x) {
    m_rightClimberMotor.set(x);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
