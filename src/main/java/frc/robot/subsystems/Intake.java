/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.util.AsyncStructuredLogger;

public class Intake extends SubsystemBase {
  public boolean m_isActive = true;
  static Intake m_Instance = null;
  private WPI_TalonSRX m_intakeMotor = null;
  private WPI_TalonSRX m_intakeArmMotor = null;
  private int m_numberOfTimesStalled = 0;
  private double m_setPoint = 0;
  private IntakeLoggingData m_loggingData;
  private AsyncStructuredLogger<IntakeLoggingData> m_logger;

  /**
   * Creates a new Intake.
   */

  public Intake() {
    if (m_isActive == false) {
      return;
    }
    m_intakeMotor = new WPI_TalonSRX(Constants.IntakeMotorCAN_Address);
    m_intakeMotor.configFactoryDefault();
    m_intakeArmMotor = new WPI_TalonSRX(Constants.IntakeMotorArmCAN_Address);
    m_intakeArmMotor.configFactoryDefault();
    m_intakeArmMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
    m_intakeArmMotor.setNeutralMode(NeutralMode.Brake);
    m_loggingData = new IntakeLoggingData();
    m_logger = new AsyncStructuredLogger<IntakeLoggingData>("Intake", /*forceUnique=*/false, IntakeLoggingData.class);
  
  }

  public static Intake getInstance() {
    if(m_Instance == null) {
			synchronized (Intake.class) {
        if(m_Instance == null) {
          m_Instance = new Intake();
        }
			}
		}
		return m_Instance;
  }

  public double getIntakeMotorLevel() {
    if (m_isActive == false) {
      return 0;
    }
    return m_intakeMotor.get();
  }

  public void zeroIntakeSensors() {
    if (m_isActive == false) {
      return;
    }
    m_intakeArmMotor.setSelectedSensorPosition(0, 0, 10);
  }

  public double getIntakeArmAngle() {
    if (m_isActive == false) {
      return 0;
    }
    return m_loggingData.ArmAngle;
  }

  public double getIntakeArmEncoder() {
    if (m_isActive == false) {
      return 0;
    }
    return m_loggingData.ArmMotorEncoder;
  }

  public boolean IsArmMotorStalled() {
    if (m_isActive == false) {
      return false;
    }
    
    return (m_numberOfTimesStalled > 3);
  }

  public void setSetPoint(double x) {
    if (m_isActive == false) {
      return;
    }

    m_setPoint = x;
  }

  public double getSetPoint() {
    if (m_isActive == false) {
      return 0;
    }

    return m_setPoint;
  }

  @Override
  public void periodic() {

    if (m_isActive == false) {
      return;
    }
    // This method will be called once per scheduler run

    m_loggingData.SetPoint = m_setPoint;
    m_loggingData.ArmMotorEncoder = m_intakeArmMotor.getSelectedSensorPosition(0);
    m_loggingData.ArmAngle = -1 * (getIntakeArmEncoder()*360.0/4096.0)*(18.0/36)*(30.0/72);
    m_loggingData.IntakeMotorLevel = m_intakeMotor.get();
    m_loggingData.IntakeArmMotorLevel = m_intakeArmMotor.get();
    m_loggingData.IntakeMotorStatorCurrent = m_intakeMotor.getStatorCurrent();
    m_loggingData.IntakeMotorSupplyCurrent = m_intakeMotor.getSupplyCurrent();
    m_loggingData.IntakeArmMotorStatorCurrent = m_intakeArmMotor.getStatorCurrent();
    m_loggingData.IntakeArmMotorSupplyCurrent = m_intakeArmMotor.getSupplyCurrent();
    m_logger.queueData(m_loggingData);

    if (m_loggingData.IntakeArmMotorStatorCurrent > Constants.IntakeArmStallCurrent) {
      m_numberOfTimesStalled += 1;
    }

    else {
      m_numberOfTimesStalled = 0;
    }

    double error = m_setPoint - m_loggingData.ArmAngle;
    double level = error * Constants.IntakeArmKp;

    if (level > 0.3) {
      level = 0.3;
    }

    else if (level < -0.4) {
      level = -0.4;
    }

    m_intakeArmMotor.set(level);
    
    if (m_setPoint > 0) {
      m_intakeMotor.set(Constants.IntakeMotorLevel);
      Robot.getStorage().setIntakeRunning(true);
    }

    else {
      m_intakeMotor.set(0);
      Robot.getStorage().setIntakeRunning(false);
    }

    SmartDashboard.putNumber("Number of times stalled", m_numberOfTimesStalled);
    SmartDashboard.putNumber("Arm Angle", getIntakeArmAngle());
  }

  public static class IntakeLoggingData {
    public double SetPoint;
    public double ArmMotorEncoder;
    public double ArmAngle;
    public double IntakeMotorLevel;
    public double IntakeArmMotorLevel;
    public double IntakeMotorStatorCurrent;
    public double IntakeMotorSupplyCurrent;
    public double IntakeArmMotorStatorCurrent;
    public double IntakeArmMotorSupplyCurrent;
  }
}
