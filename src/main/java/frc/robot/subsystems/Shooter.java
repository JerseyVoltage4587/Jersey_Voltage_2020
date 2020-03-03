/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import frc.robot.Robot;
import frc.robot.Constants;
import frc.robot.util.AsyncStructuredLogger;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {
  public boolean m_isActive = false;
  static Shooter m_Instance = null;
  private static CANSparkMax m_leftShooterMotor = null;
  private static CANSparkMax m_rightShooterMotor = null;
  private ShooterLoggingData m_loggingData;
  private AsyncStructuredLogger<ShooterLoggingData> m_logger;
  private CANEncoder m_leftShooterEncoder;
  private CANEncoder m_rightShooterEncoder;
  /**
   * Creates a new Shooter.
   */
  public Shooter() {
    if (m_isActive == false) {
      return;
    }
    m_leftShooterMotor = new CANSparkMax(9, MotorType.kBrushless);
    m_rightShooterMotor = new CANSparkMax(8, MotorType.kBrushless);
    m_leftShooterEncoder = m_leftShooterMotor.getEncoder();
    m_rightShooterEncoder = m_rightShooterMotor.getEncoder();
    m_loggingData = new ShooterLoggingData();
    m_logger = new AsyncStructuredLogger<ShooterLoggingData>("Shooter", /*forceUnique=*/false, ShooterLoggingData.class);
  }

  public static Shooter getInstance() {
    if(m_Instance == null) {
			synchronized (Shooter.class) {
				m_Instance = new Shooter();
			}
		}
		return m_Instance;
  }

  public double getLeftShooterEncoder() {
    if (m_isActive == false) {
      return 0;
    }
    return m_leftShooterEncoder.getPosition();
  }

  public double getRightShooterEncoder() {
    if (m_isActive == false) {
      return 0;
    }
    return m_rightShooterEncoder.getPosition();
  }

  public void setShooterMotorLevel(double x) {
    if (m_isActive == false) {
      return;
    }
    m_leftShooterMotor.set(x);
    m_rightShooterMotor.set(x);
  }

  @Override
  public void periodic() {
    if (m_isActive == false) {
      return;
    }
    // This method will be called once per scheduler run
    m_loggingData.LeftShooterMotorLevel = m_leftShooterMotor.get();
    m_loggingData.RightShooterMotorLevel = m_rightShooterMotor.get();
    m_loggingData.LeftMotorCurrent = Robot.getPDP().getCurrent(Constants.LeftShooterMotorPDP_Port);
    m_loggingData.RightMotorCurrent = Robot.getPDP().getCurrent(Constants.RightShooterMotorPDP_Port);
    m_loggingData.LeftEncoderPosition = getLeftShooterEncoder();
    m_loggingData.RightEncoderPosition = getRightShooterEncoder();
    m_loggingData.ShooterMotorMotorLevel = (((m_leftShooterMotor.get() + m_rightShooterMotor.get()) / 2) / (Math.PI * Constants.ShooterWheelDiameter));
    m_logger.queueData(m_loggingData);
  }

  public class ShooterLoggingData {
    double LeftShooterMotorLevel;
    double RightShooterMotorLevel;
    double LeftMotorCurrent;
    double RightMotorCurrent;
    double LeftEncoderPosition;
    double RightEncoderPosition;
    double ShooterMotorMotorLevel;
  }
}
