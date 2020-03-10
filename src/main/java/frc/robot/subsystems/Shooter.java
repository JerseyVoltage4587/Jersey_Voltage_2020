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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {
  public boolean m_isActive = true;
  static Shooter m_Instance = null;
  private static CANSparkMax m_leftShooterMotor = null;
  private static CANSparkMax m_rightShooterMotor = null;
  private ShooterLoggingData m_loggingData;
  private double setPoint = 0;
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
    m_leftShooterMotor = new CANSparkMax(Constants.LeftShooterMotorCAN_Address, MotorType.kBrushless);
    m_rightShooterMotor = new CANSparkMax(Constants.RightShooterMotorCAN_Address, MotorType.kBrushless);
    m_leftShooterEncoder = m_leftShooterMotor.getEncoder();
    m_rightShooterEncoder = m_rightShooterMotor.getEncoder();
    m_leftShooterMotor.restoreFactoryDefaults();
    m_rightShooterMotor.restoreFactoryDefaults();
    m_loggingData = new ShooterLoggingData();
    m_leftShooterMotor.set(0);
    m_rightShooterMotor.set(0);
    m_logger = new AsyncStructuredLogger<ShooterLoggingData>("Shooter", /*forceUnique=*/ false, ShooterLoggingData.class);
  }

  public static Shooter getInstance() {
    if(m_Instance == null) {
			synchronized (Shooter.class) {
				m_Instance = new Shooter();
			}
		}
		return m_Instance;
  }
  
  public double getLeftShooter() {
    if (m_isActive == false) {
      return 0;
    }
    return m_leftShooterMotor.get();
  }

  public double getRightShooter() {
    if (m_isActive == false) {
      return 0;
    }
    return m_rightShooterMotor.get();
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

  public void setShooterMotorLevel(double x) { //Positive will shoot forward
    if (m_isActive == false) {
      return;
    }
    m_leftShooterMotor.set(-1 * x);
    m_rightShooterMotor.set(x);
    SmartDashboard.putNumber("Left motor level", m_leftShooterMotor.get());
    SmartDashboard.putNumber("Right motor level", m_rightShooterMotor.get());
  }

  public void setSetPoint(double x) {
    setPoint = x;
  }

  public boolean isShooterReady() {
    return (Math.abs(setPoint - m_loggingData.AverageRPM)) <= Constants.ShooterMotorTolerance;
  }

  @Override
  public void periodic() {

    if (m_isActive == false) {
      return;
    }
    
    // This method will be called once per scheduler run
    double lastAverageRPM = m_loggingData.AverageRPM;
    double lastLeftEncoder = m_loggingData.LeftEncoderPosition;
    double lastRightEncoder = m_loggingData.RightEncoderPosition;
    long lastNanoSeconds = m_loggingData.nanoSeconds;

    m_loggingData.LeftShooterMotorLevel = m_leftShooterMotor.get();
    m_loggingData.RightShooterMotorLevel = m_rightShooterMotor.get();
    m_loggingData.LeftMotorCurrent = Robot.getPDP().getCurrent(Constants.LeftShooterMotorPDP_Port);
    m_loggingData.RightMotorCurrent = Robot.getPDP().getCurrent(Constants.RightShooterMotorPDP_Port);
    m_loggingData.LeftEncoderPosition = getLeftShooterEncoder();
    m_loggingData.RightEncoderPosition = getRightShooterEncoder();
    m_loggingData.SetPoint = setPoint;
    m_loggingData.nanoSeconds = System.nanoTime();

    double elapsedTime = ((m_loggingData.nanoSeconds - lastNanoSeconds) / 1000000000.0) / 60.0;
    double leftEncoderRevolutions = -1 * (((m_loggingData.LeftEncoderPosition - lastLeftEncoder) * 39) / 30) / elapsedTime;
    double rightEncoderRevolutions = (((m_loggingData.RightEncoderPosition - lastRightEncoder) * 39) / 30) / elapsedTime;
    double RPM = (leftEncoderRevolutions + rightEncoderRevolutions) / 2;
    m_loggingData.AverageRPM = (RPM + lastAverageRPM) / 2;
    SmartDashboard.putNumber("RPM", m_loggingData.AverageRPM);
    SmartDashboard.putBoolean("Is Shooter Ready", isShooterReady());
    SmartDashboard.putNumber("Shooter Set Point", m_loggingData.SetPoint);

    if (setPoint == 0) {
      Robot.getStorage().setShooterRunning(false);
      m_loggingData.ShooterMotorLevel = 0; 
    }

    else {
      Robot.getStorage().setShooterRunning(true);
      double error = Math.abs(setPoint - m_loggingData.AverageRPM);
       m_loggingData.ShooterMotorLevel = Constants.ShooterMotorLevel + (.00018 * error); //Figure out what to multiply the error by
      
      if (setPoint < 0) {
        Robot.getShooter().setShooterMotorLevel(-1 * m_loggingData.ShooterMotorLevel);
      }

      else {
        Robot.getShooter().setShooterMotorLevel(m_loggingData.ShooterMotorLevel);
      }
    }

    m_logger.queueData(m_loggingData);
  }

  public static class ShooterLoggingData {
    public double LeftShooterMotorLevel;
    public double RightShooterMotorLevel;
    public double LeftMotorCurrent;
    public double RightMotorCurrent;
    public double LeftEncoderPosition;
    public double RightEncoderPosition;
    public double ShooterMotorLevel;
    public double AverageRPM;
    public double SetPoint;
    public double LeftRPM;
    public double RightRPM;
    public long nanoSeconds;
  }
}
