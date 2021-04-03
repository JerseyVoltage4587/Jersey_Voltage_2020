/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.util.AsyncStructuredLogger;
import frc.robot.util.Gyro;

public class DriveBase extends SubsystemBase {
  private boolean m_isActive = true;
  public DifferentialDrive m_drive = null;
  static DriveBase m_Instance = null;
  public WPI_TalonSRX m_lefttalon1;
  public WPI_TalonSRX m_righttalon2;
  private WPI_VictorSPX m_leftvictor11;
  private WPI_VictorSPX m_rightvictor21;
  private double LeftMotorLevel;
  private double RightMotorLevel;
  private double partialLeftInches = 0;
  private double partialRightInches = 0;
  private DriveBaseLoggingData m_loggingData;
  private AsyncStructuredLogger<DriveBaseLoggingData> m_logger;
  private long m_lastLogTime = 0;
  private String layout = "";


  /**
   * Creates a new DriveBase.
   */
  public DriveBase() {
    if (m_isActive == false) {
      return;
    }
    m_lefttalon1 = new WPI_TalonSRX(Constants.LeftTalon1CAN_Address);
    m_righttalon2 = new WPI_TalonSRX(Constants.RightTalon2CAN_Address);
    m_leftvictor11 = new WPI_VictorSPX(Constants.LeftVictor1CAN_Address);
    m_rightvictor21 = new WPI_VictorSPX(Constants.RightVictor21CAN_Address);
    m_lefttalon1.configFactoryDefault();
    m_leftvictor11.configFactoryDefault();
    m_righttalon2.configFactoryDefault(); 
    m_rightvictor21.configFactoryDefault();
    m_lefttalon1.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
    m_righttalon2.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
    m_lefttalon1.setNeutralMode(NeutralMode.Brake);
    m_leftvictor11.setNeutralMode(NeutralMode.Brake);
    m_righttalon2.setNeutralMode(NeutralMode.Brake);
    m_rightvictor21.setNeutralMode(NeutralMode.Brake);
    m_leftvictor11.follow(m_lefttalon1);
    m_rightvictor21.follow(m_righttalon2);
    m_lefttalon1.setInverted(false);
    m_righttalon2.setInverted(true);
    m_leftvictor11.setInverted(InvertType.FollowMaster);
    m_rightvictor21.setInverted(InvertType.FollowMaster);
    m_drive = new DifferentialDrive(m_lefttalon1, m_righttalon2);
    m_drive.setSafetyEnabled(false);
    m_drive.setRightSideInverted(false);
    m_loggingData = new DriveBaseLoggingData();
    m_logger = new AsyncStructuredLogger<DriveBaseLoggingData>("DriveBase", /*forceUnique=*/false, DriveBaseLoggingData.class);
  }

  public static DriveBase getInstance() {
    if (m_Instance == null) {
      synchronized (DriveBase.class) {
        if (m_Instance == null) {
          m_Instance = new DriveBase();
        }
      }
    }
    return m_Instance;
  }

  public void setLeftMotorLevel(double x) {
    if (m_isActive == false) {
      return;
    }
    LeftMotorLevel = x;
    m_lefttalon1.set(LeftMotorLevel);
  }

  public double getLeftMotorLevel() {
    if (m_isActive == false) {
      return -1;
    }
    return LeftMotorLevel;
  }

  public void setRightMotorLevel(double x) {
    if (m_isActive == false) {
      return;
    }
    RightMotorLevel = x;
    m_righttalon2.set(RightMotorLevel);
  }

  public double getRightMotorLevel() {
    if (m_isActive == false) {
      return -1;
    }
    return RightMotorLevel;
  }

  public void setLayout(String l) {
    layout = l;
  }

  public String getLayout() {
    return layout;
  }

  public void setSafetyEnabled(boolean x){
    if (m_isActive == false) {
      return;
    }
    m_drive.setSafetyEnabled(x);
  }

  public void zeroDriveSensors() {
    if (m_isActive == false) {
      return;
    }
    m_lefttalon1.setSelectedSensorPosition(0, 0, 10);
    m_righttalon2.setSelectedSensorPosition(0, 0, 10);
    Gyro.getInstance();
    Gyro.reset();
  }

  public void arcadeDrive(double forward, double turn) {
    if (m_isActive == false) {
      return;
    }
    m_drive.arcadeDrive(forward, turn);
  }

  @Override
  public void periodic() {

    if (m_isActive == false) {
      return;
    }

    // This method will be called once per scheduler run
    long now = System.nanoTime();
    double lastLeftPosition = m_loggingData.LeftPosition;
    double lastLeftVelocity = m_loggingData.LeftVelocity;
    double lastRightPosition = m_loggingData.RightPosition;
    double lastRightVelocity = m_loggingData.RightVelocity;

    m_loggingData.LeftMotorLevel = m_lefttalon1.get();
    m_loggingData.LeftMotor1_SupplyCurrent = m_lefttalon1.getSupplyCurrent();
    m_loggingData.LeftMotor1_StatorCurrent = m_lefttalon1.getStatorCurrent();
    m_loggingData.LeftMotor2_SupplyCurrent = Robot.getPDP().getCurrent(Constants.LeftVictor11PDP_Port);
    m_loggingData.LeftEncoderReading = getLeftEncoder();
    m_loggingData.LeftPosition = getLeftDistanceInches();
    m_loggingData.LeftVelocity = getRateOfChange(lastLeftPosition, m_loggingData.LeftPosition, m_lastLogTime, now);
    m_loggingData.LeftAcceleration = getRateOfChange(lastLeftVelocity, m_loggingData.LeftVelocity, m_lastLogTime, now);

    m_loggingData.RightMotorLevel = m_righttalon2.get();
    m_loggingData.RightMotor1_SupplyCurrent = m_righttalon2.getSupplyCurrent();
    m_loggingData.RightMotor1_StatorCurrent = m_righttalon2.getStatorCurrent();
    m_loggingData.RightMotor2_SupplyCurrent = Robot.getPDP().getCurrent(Constants.RightVictor21PDP_Port);
    m_loggingData.RightEncoderReading = getRightEncoder();
    m_loggingData.RightPosition = getRightDistanceInches();
    m_loggingData.RightVelocity = getRateOfChange(lastRightPosition, m_loggingData.RightPosition, m_lastLogTime, now);
    m_loggingData.RightAcceleration = getRateOfChange(lastRightVelocity, m_loggingData.RightVelocity, m_lastLogTime, now);

    m_loggingData.Heading = Gyro.getYaw();
    m_logger.queueData(m_loggingData);
    m_lastLogTime = now;

    SmartDashboard.putNumber("Left Distance", m_loggingData.LeftPosition);
    SmartDashboard.putNumber("Right Distance", m_loggingData.RightPosition);
    SmartDashboard.putNumber("Distance", (m_loggingData.LeftPosition + m_loggingData.RightPosition) / 2);
    SmartDashboard.putNumber("Heading", Gyro.getYaw());
  }

  public double getLeftEncoder() {
    if (m_isActive == false) {
      return 0;
    }
    return -1 * m_lefttalon1.getSelectedSensorPosition(0);
  }

  public double getLeftDistanceInches() {
    return getLeftEncoder() * Constants.DriveBaseWheelDiameter * Math.PI / Constants.DriveBaseEncoderTics;
  }

  public double getRightEncoder() {
    if (m_isActive == false) {
      return 0;
    }
    return -1 * m_righttalon2.getSelectedSensorPosition(0);
  }

  public double getRightDistanceInches() {
    return getRightEncoder() * Constants.DriveBaseWheelDiameter * Math.PI / Constants.DriveBaseEncoderTics;
  }

  private double getRateOfChange(double initialValue, double finalValue, long initialTime, long finalTime) {
    return (finalValue - initialValue) / (finalTime - initialTime);
  }

  public void setPartialInches(double L, double R) {
    partialLeftInches = L;
    partialRightInches = R;
  }

  public double getPartialLeftInches() {
    return partialLeftInches;
  }

  public double getPartialRightInches() {
    return partialRightInches;
  }

  public static class DriveBaseLoggingData {
    public double LeftMotorLevel;
    public double LeftMotor1_SupplyCurrent;
    public double LeftMotor1_StatorCurrent;
    public double LeftMotor2_SupplyCurrent;
    public double LeftEncoderReading;
    public double LeftPosition;
    public double LeftVelocity;
    public double LeftAcceleration;
    public double RightMotorLevel;
    public double RightMotor1_SupplyCurrent;
    public double RightMotor1_StatorCurrent;
    public double RightMotor2_SupplyCurrent;
    public double RightEncoderReading;
    public double RightPosition;
    public double RightVelocity;
    public double RightAcceleration;
    public double Heading;
  }
}
