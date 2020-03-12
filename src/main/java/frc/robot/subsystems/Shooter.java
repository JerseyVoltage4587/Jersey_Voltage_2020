
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
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {
  public boolean m_isActive = true;
  static Shooter m_Instance = null;
  private ShooterLoggingData m_loggingData;
  private AsyncStructuredLogger<ShooterLoggingData> m_logger;
  private static CANSparkMax m_leftShooterMotor = null;
  private static CANSparkMax m_rightShooterMotor = null;
  private CANEncoder m_leftShooterEncoder;
  private CANEncoder m_rightShooterEncoder;
  private CANPIDController m_pidController;
  private double setPoint = 0;
  public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, maxRPM;
  /**
   * Creates a new Shooter.
   */
  public Shooter() {
    if (m_isActive == false) {
      return;
    }
    m_leftShooterMotor = new CANSparkMax(Constants.LeftShooterMotorCAN_Address, MotorType.kBrushless);
    m_rightShooterMotor = new CANSparkMax(Constants.RightShooterMotorCAN_Address, MotorType.kBrushless);
    m_leftShooterMotor.follow(m_rightShooterMotor, /*invert=*/ true);
    m_leftShooterEncoder = m_leftShooterMotor.getEncoder();
    m_rightShooterEncoder = m_rightShooterMotor.getEncoder();
    m_leftShooterMotor.restoreFactoryDefaults();
    m_rightShooterMotor.restoreFactoryDefaults();
    m_loggingData = new ShooterLoggingData();
    m_leftShooterMotor.set(0);
    m_rightShooterMotor.set(0);
    m_leftShooterMotor.follow(m_rightShooterMotor, /*invert=*/ true);
    m_pidController = m_rightShooterMotor.getPIDController();

    //PID coefficients
    kP = 6e-5; 
    kI = 0;
    kD = 0; 
    kIz = 0; 
    kFF = 0.0002; 
    kMaxOutput = 1; 
    kMinOutput = -1;
    maxRPM = 5700;

    //Set PID coefficients
    m_pidController.setP(kP);
    m_pidController.setI(kI);
    m_pidController.setD(kD);
    m_pidController.setIZone(kIz);
    m_pidController.setFF(kFF);
    m_pidController.setOutputRange(kMinOutput, kMaxOutput);

    //Display PID coefficients on SmartDashboard
    SmartDashboard.putNumber("P Gain", kP);
    SmartDashboard.putNumber("I Gain", kI);
    SmartDashboard.putNumber("D Gain", kD);
    SmartDashboard.putNumber("I Zone", kIz);
    SmartDashboard.putNumber("Feed Forward", kFF);
    SmartDashboard.putNumber("Max Output", kMaxOutput);
    SmartDashboard.putNumber("Min Output", kMinOutput);

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
  }

  public void setSetPoint(double x) {
    setPoint = x;
  }

  @Override
  public void periodic() {

    if (m_isActive == false) {
      return;
    }

    //Read PID coefficients from SmartDashboard
    double p = SmartDashboard.getNumber("P Gain", 0);
    double i = SmartDashboard.getNumber("I Gain", 0);
    double d = SmartDashboard.getNumber("D Gain", 0);
    double iz = SmartDashboard.getNumber("I Zone", 0);
    double ff = SmartDashboard.getNumber("Feed Forward", 0);
    double max = SmartDashboard.getNumber("Max Output", 0);
    double min = SmartDashboard.getNumber("Min Output", 0);

    //If PID coefficients on SmartDashboard have changed, write new values to controller
    if (p != kP) {
      m_pidController.setP(p);
      kP = p;
    }

    if (i != kI) {
      m_pidController.setI(i);
      kI = i;
    }

    if (d != kD) {
      m_pidController.setD(d);
      kD = d;
    }

    if (iz != kIz) {
      m_pidController.setIZone(iz);
      kIz = iz;
    }

    if (ff != kFF) {
      m_pidController.setFF(ff);
      kFF = ff;
    }

    if ((max != kMaxOutput) || (min != kMinOutput)) { 
      m_pidController.setOutputRange(min, max); 
      kMinOutput = min; kMaxOutput = max; 
    }

    m_pidController.setReference(setPoint, ControlType.kVelocity);
    
    SmartDashboard.putNumber("SetPoint", setPoint);
    SmartDashboard.putNumber("ProcessVariable", m_rightShooterEncoder.getVelocity());

    m_loggingData.LeftShooterMotorLevel = m_leftShooterMotor.get();
    m_loggingData.RightShooterMotorLevel = m_rightShooterMotor.get();
    m_loggingData.LeftMotorCurrent = Robot.getPDP().getCurrent(Constants.LeftShooterMotorPDP_Port);
    m_loggingData.RightMotorCurrent = Robot.getPDP().getCurrent(Constants.RightShooterMotorPDP_Port);
    m_loggingData.LeftEncoderPosition = getLeftShooterEncoder();
    m_loggingData.RightEncoderPosition = getRightShooterEncoder();
    m_loggingData.SetPoint = setPoint;
    m_loggingData.nanoSeconds = System.nanoTime();

    m_logger.queueData(m_loggingData);
  }

  public static class ShooterLoggingData {
    public double LeftShooterMotorLevel;
    public double RightShooterMotorLevel;
    public double LeftMotorCurrent;
    public double RightMotorCurrent;
    public double LeftEncoderPosition;
    public double RightEncoderPosition;
    public double SetPoint;
    public long nanoSeconds;
  }
}
