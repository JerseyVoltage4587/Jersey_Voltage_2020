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

public class Storage extends SubsystemBase {
  public boolean m_isActive = false;
  static Storage m_Instance = null;
  private WPI_TalonSRX m_storageBeltMotor = null;
  private WPI_TalonSRX m_storageSeparatorMotor = null;
  private StorageLoggingData m_loggingData;
  private AsyncStructuredLogger<StorageLoggingData> m_logger;
  /**
   * Creates a new Storage.
   */
  public Storage() {
    if (m_isActive == false) {
      return;
    }
	  m_storageBeltMotor = new WPI_TalonSRX(3);
    m_storageSeparatorMotor = new WPI_TalonSRX(3);
    m_loggingData = new StorageLoggingData();
    m_logger = new AsyncStructuredLogger<StorageLoggingData>("Storage", /*forceUnique=*/false, StorageLoggingData.class);
  }

  public static Storage getInstance() {
    if(m_Instance == null) {
			synchronized (Storage.class) {
				m_Instance = new Storage();
			}
		}
		return m_Instance;
  }

  public void setStoragerBeltMotorLevel(double x) {
    if (m_isActive == false) {
      return;
    }
    m_storageBeltMotor.set(x);
  }

  public void setStoragerSeparatorMotorLevel(double x) {
    if (m_isActive == false) {
      return;
    }
    m_storageSeparatorMotor.set(x);
  }

  @Override
  public void periodic() {
    if (m_isActive == false) {
      return;
    }
    // This method will be called once per scheduler run
    m_loggingData.BeltMotorLevel = m_storageBeltMotor.get();
    m_loggingData.SeparatorMotorLevel = m_storageSeparatorMotor.get();
    m_loggingData.BeltMotorCurrent = Robot.getPDP().getCurrent(Constants.StorageBeltMotorPDP_Port);
    m_loggingData.SeparatorMotorCurrent = Robot.getPDP().getCurrent(Constants.StorageSeparatorMotorPDP_Port);
  }

  public class StorageLoggingData {
    double BeltMotorLevel;
    double SeparatorMotorLevel;
    double BeltMotorCurrent;
    double SeparatorMotorCurrent;
  }
}
