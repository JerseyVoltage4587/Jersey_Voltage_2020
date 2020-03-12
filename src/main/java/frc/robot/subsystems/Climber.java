/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.util.AsyncStructuredLogger;

public class Climber extends SubsystemBase {
  private Boolean m_isActive = true;
  static Climber m_Instance = null;
  private WPI_TalonSRX m_leftClimberMotor = null;
  private WPI_TalonSRX m_rightClimberMotor = null;
  private double setPoint = 0;
  private String RobotClimberState = "STARTUP";
  private long m_lastLogTime = 0;
  private ClimberLoggingData m_loggingData;
  private AsyncStructuredLogger<ClimberLoggingData> m_logger;
  /**
   * Creates a new Climber.
   */
  public Climber() {
    if (m_isActive == false) {
      return;
    }
    m_leftClimberMotor = new WPI_TalonSRX(Constants.LeftClimberMotorCAN_Address);
    m_rightClimberMotor = new WPI_TalonSRX(Constants.RightClimberMotorCAN_Address);
    m_leftClimberMotor.configFactoryDefault();
    m_rightClimberMotor.configFactoryDefault();
    m_rightClimberMotor.setNeutralMode(NeutralMode.Brake);
    m_leftClimberMotor.setNeutralMode(NeutralMode.Brake);
    m_loggingData = new ClimberLoggingData();
    RobotClimberState = "STARTUP";
    setPoint = 0;
    m_logger = new AsyncStructuredLogger<ClimberLoggingData>("Storage", /*forceUnique=*/false, ClimberLoggingData.class);
  }

  public static Climber getInstance() {
    if (m_Instance == null) {
			synchronized (Climber.class) {
        if (m_Instance == null) {
          m_Instance = new Climber();
        }
			}
		}
		return m_Instance;
  }

  public void setRobotClimberState(String x) {
    if (m_isActive == false) {
      return;
    }
    RobotClimberState = x;
  }

  public void setSetPoint(double x) {
    if (m_isActive == false) {
      return;
    }
    setPoint = x;
  }

  public void zeroClimberMotors() {
    if (m_isActive == false) {
      return;
    }
    m_leftClimberMotor.setSelectedSensorPosition(0, 0, 100);
    m_rightClimberMotor.setSelectedSensorPosition(0, 0, 100);
  }

  private double calculatePushMotorLevel(double position, double velocity) {
    double motorLevel;

    if (position >= (setPoint - Constants.PushModeSlowDownOffset)) {
      double error = setPoint - position;
      motorLevel = Constants.PushModeSlowBaseLevel + error * Constants.PushModeSlowKp;
    } 
    
    else {
      double error = (Constants.PushModeFastVelocity - velocity);
      motorLevel = Constants.PushModeFastBaseLevel + error * Constants.PushModeFastKp;
    }

    return MathUtil.clamp(motorLevel, -1, 1);
  }

  private double calculatePullMotorLevel(double position, double velocity) {
    double motorLevel;

    if (position <= setPoint + Constants.PullModeSlowDownOffset) {
      double error = (m_loggingData.LeftPosition - setPoint);
      motorLevel = Constants.PullModeSlowBaseLevel + error * Constants.PullModeSlowKp;
    }

    else {
      double error = (Constants.PullModeFastVelocity - m_loggingData.LeftVelocity);
      motorLevel = Constants.PullModeFastBaseLevel + error * Constants.PullModeFastKp;
    }

    return MathUtil.clamp(motorLevel, -1, 1);
  }

  @Override
  public void periodic() {
    
    if (m_isActive == false) {
      return;
    }

    long now = System.nanoTime();
    double lastLeftPosition;
    double lastRightPosition;
    double seconds;

    lastLeftPosition = m_loggingData.LeftPosition;
    lastRightPosition = m_loggingData.RightPosition;
    seconds = (m_lastLogTime - now) / 1000000000.0;

    m_loggingData.LeftClimberMotorLevel = m_leftClimberMotor.get();
    m_loggingData.RightClimberMotorLevel = m_rightClimberMotor.get();
    m_loggingData.LeftClimberMotorCurrent = Robot.getPDP().getCurrent(Constants.LeftClimberMotorPDP_Port);
    m_loggingData.RightClimberMotorCurrent = Robot.getPDP().getCurrent(Constants.RightClimberMotorPDP_Port);
    m_loggingData.ClimberState = RobotClimberState;
    m_loggingData.LeftEncoderReading = m_leftClimberMotor.getSelectedSensorPosition(0);
    m_loggingData.LeftPosition = m_loggingData.LeftEncoderReading / 4096.0;
    m_loggingData.LeftVelocity = Math.abs(m_loggingData.LeftPosition - lastLeftPosition) / seconds;
    m_loggingData.RightEncoderReading = m_rightClimberMotor.getSelectedSensorPosition(0);
    m_loggingData.RightPosition = m_loggingData.RightEncoderReading / 4096.0;
    m_loggingData.RightVelocity = Math.abs(m_loggingData.RightPosition - lastRightPosition) / seconds;
    m_logger.queueData(m_loggingData);

    if (RobotClimberState.equals("INITIAL")) {
      m_leftClimberMotor.set(0);
      m_rightClimberMotor.set(0);
    }

    else if (RobotClimberState.equals("PUSH")) {
      m_leftClimberMotor.set(calculatePushMotorLevel(m_loggingData.LeftPosition, m_loggingData.LeftVelocity));
      m_rightClimberMotor.set(calculatePushMotorLevel(m_loggingData.RightPosition, m_loggingData.RightVelocity));
    }

    else if (RobotClimberState.equals("PULL")) {
      m_leftClimberMotor.set(calculatePullMotorLevel(m_loggingData.LeftPosition, m_loggingData.LeftVelocity));
      m_rightClimberMotor.set(calculatePullMotorLevel(m_loggingData.RightPosition, m_loggingData.RightVelocity));
    }

    SmartDashboard.putString("Climber Status", RobotClimberState);
    SmartDashboard.putNumber("Climber Set Point", setPoint);
    SmartDashboard.putNumber("Left Climber Motor Level", m_leftClimberMotor.get());
    SmartDashboard.putNumber("Right Climber Motor Level", m_rightClimberMotor.get());
    SmartDashboard.putNumber("Right Climber Position", m_loggingData.RightPosition);
    SmartDashboard.putNumber("Right Climber Velocity", m_loggingData.RightVelocity);
    SmartDashboard.putNumber("Left Climber Velocity", m_loggingData.LeftVelocity);
    SmartDashboard.putNumber("Left Climber Position", m_loggingData.LeftPosition);

  
    m_lastLogTime = now;
  }

  public static class ClimberLoggingData {
    public double LeftClimberMotorLevel;
    public double RightClimberMotorLevel;
    public double LeftClimberMotorCurrent;
    public double RightClimberMotorCurrent;
    public int LeftEncoderReading;
    public double LeftPosition;
    public double LeftVelocity;
    public int RightEncoderReading;
    public double RightPosition;
    public double RightVelocity;
    public String ClimberState;
  }
}
