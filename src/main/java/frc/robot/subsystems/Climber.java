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
  private Boolean m_isActive = false;
  static Climber m_Instance = null;
  private WPI_TalonSRX m_leftClimberMotor;
  private WPI_TalonSRX m_rightClimberMotor;
  /**
   * Creates a new Climber.
   */
  public Climber() {
    if (m_isActive == false) {
      return;
    }
    //m_leftClimberMotor = new WPI_TalonSRX(); //TODO Device Number
    //m_rightClimberMotor = new WPI_TalonSRX(); //TODO Device Number
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
    if (m_isActive == false) {
      return;
    }
    m_leftClimberMotor.set(x);
  }

  public void setRightClimberMotorLevel(double x) {
    if (m_isActive == false) {
      return;
    }
    m_rightClimberMotor.set(x);
  }

  @Override
  public void periodic() {
    if (m_isActive == false) {
      return;
    }
    // This method will be called once per scheduler run
  }
}
