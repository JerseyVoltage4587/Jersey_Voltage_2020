/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.util.AsyncStructuredLogger;

public class Climber extends SubsystemBase {
  private Boolean m_isActive = false;
  static Climber m_Instance = null;
  private WPI_TalonSRX m_leftClimberMotor = null;
  private WPI_TalonSRX m_rightClimberMotor = null;
  private ClimberLoggingData m_loggingData;
  private AsyncStructuredLogger<ClimberLoggingData> m_logger;
  /**
   * Creates a new Climber.
   */
  public Climber() {
    if (m_isActive == false) {
      return;
    }
    m_leftClimberMotor = new WPI_TalonSRX(Constants.LeftClimberMotorPWM_Address);
    m_rightClimberMotor = new WPI_TalonSRX(Constants.RightClimberMotorPWM_Address);
    m_loggingData = new ClimberLoggingData();
    m_logger = new AsyncStructuredLogger<ClimberLoggingData>("Storage", /*forceUnique=*/false, ClimberLoggingData.class);
  }

  public static Climber getInstance() {
    if(m_Instance == null) {
			synchronized (Climber.class) {
				m_Instance = new Climber();
			}
		}
		return m_Instance;
  }

  public void RunClimberMotor() {
    if (m_isActive == false) {
      return;
    }
    m_leftClimberMotor.set(Constants.ClimberMotorLevel);
    m_rightClimberMotor.set(Constants.ClimberMotorLevel);
  }

  @Override
  public void periodic() {
    if (m_isActive == false) {
      return;
    }
    // This method will be called once per scheduler run
    m_loggingData.LeftClimberMotorLevel = m_leftClimberMotor.get();
    m_loggingData.RightClimberMotorLevel = m_rightClimberMotor.get();
    m_loggingData.LeftClimberMotorCurrent = Robot.getPDP().getCurrent(Constants.LeftClimberMotorPDP_Port);
    m_loggingData.RightClimberMotorCurrent = Robot.getPDP().getCurrent(Constants.RightClimberMotorPDP_Port);
  }

  public class ClimberLoggingData {
    double LeftClimberMotorLevel;
    double RightClimberMotorLevel;
    double LeftClimberMotorCurrent;
    double RightClimberMotorCurrent;
  }
}
