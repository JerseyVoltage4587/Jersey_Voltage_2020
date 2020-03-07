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
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.util.AsyncStructuredLogger;

public class Intake extends SubsystemBase {
  private double IntakeArmMotorStatorCurrent;
  public boolean m_isActive = false;
  static Intake m_Instance = null;
  private WPI_TalonSRX m_intakeMotor = null;
  private WPI_TalonSRX m_intakeArmMotor = null;
  private String intakeArmMotorStatus = null;
  private int m_numberOfTimesStalled = 0;
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
    m_intakeArmMotor.setNeutralMode(NeutralMode.Brake);
    m_loggingData = new IntakeLoggingData();
    intakeArmMotorStatus = "UP";
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

  public void setIntakeMotorLevel(double x) {
    m_intakeMotor.set(x);
  }

  public double getIntakeMotorLevel() {
    return m_intakeMotor.get();
  }

  public void raiseIntake() {
    if (m_isActive == false) {
      return;
    }
    stopIntakeMotors();
    m_intakeArmMotor.set(Constants.IntakeArmMotorLevelUp);
    intakeArmMotorStatus = "UP";
  }

  public void lowerIntake() {
    if (m_isActive == false) {
      return;
    }
    m_intakeArmMotor.set(0.3);
    intakeArmMotorStatus = "DOWN";
  }

  public void stopIntakeArmMotor() {
    if (m_isActive == false) {
      return;
    }
    m_intakeArmMotor.set(0);
    Robot.getStorage().setIntakeRunning(false);
    intakeArmMotorStatus = "STOPPED";
  }

  public void startIntakeMotors() {
    if (m_isActive == false) {
      return;
    }
    if (intakeArmMotorStatus.equals("DOWN")) {
      m_intakeMotor.set(-1 * Constants.IntakeMotorLevel);
      Robot.getStorage().setIntakeRunning(true);
    }
  }

  public void stopIntakeMotors() {
    if (m_isActive == false) {
      return;
    }
    m_intakeMotor.set(0);
  }

  public void zeroIntakeSensors() {
    if (m_isActive == false) {
      return;
    }
    m_intakeArmMotor.setSelectedSensorPosition(0, 0, 10);
    m_intakeMotor.setSelectedSensorPosition(0, 0, 10);
  }

  public boolean IsArmMotorStalled() {
    return (m_numberOfTimesStalled > 3);
  }

  public String getIntakeArmMotorStatus() { 
    return intakeArmMotorStatus;
  }

  @Override
  public void periodic() {
    if (m_isActive == false) {
      return;
    }
    // This method will be called once per scheduler run
    m_loggingData.IntakeArmState = intakeArmMotorStatus;
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
    SmartDashboard.putNumber("Stator Current", m_loggingData.IntakeArmMotorStatorCurrent);
    SmartDashboard.putNumber("Times Stalled", m_numberOfTimesStalled);
    SmartDashboard.putString("Arm Status", intakeArmMotorStatus);
  }

  public static class IntakeLoggingData {
    String IntakeArmState;
    double IntakeMotorLevel;
    double IntakeArmMotorLevel;
    double IntakeMotorStatorCurrent;
    double IntakeMotorSupplyCurrent;
    double IntakeArmMotorStatorCurrent;
    double IntakeArmMotorSupplyCurrent;
  }
}
