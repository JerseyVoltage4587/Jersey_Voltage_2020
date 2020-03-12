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
  public boolean m_isActive = true;
  static Storage m_Instance = null;
  private WPI_TalonSRX m_storageBeltMotor = null;
  private WPI_TalonSRX m_storageToShooterMotor = null;
  public boolean m_isPassing = false;
  private boolean m_isIntakeRunning = false;
  private boolean m_isShooterRunning = false;
  private boolean m_isShooterReady = false;
  private StorageLoggingData m_loggingData;
  private AsyncStructuredLogger<StorageLoggingData> m_logger;

  /**
   * Creates a new Storage.
   */

  public Storage() {
    if (m_isActive == false) {
      return;
    }
    m_storageBeltMotor = new WPI_TalonSRX(Constants.StorageBeltMotorCAN_Address);
    m_storageToShooterMotor = new WPI_TalonSRX(Constants.StorageToShooterMotorCAN_Address);
    m_storageBeltMotor.configFactoryDefault();
    m_storageToShooterMotor.configFactoryDefault();
    m_loggingData = new StorageLoggingData();
     
    m_logger = new AsyncStructuredLogger<StorageLoggingData>("Storage", /*forceUnique=*/false, StorageLoggingData.class);
  }

  public static Storage getInstance() {
    if(m_Instance == null) {
			synchronized (Storage.class) {
        if (m_Instance == null) {
          m_Instance = new Storage();
        }
			}
		}
		return m_Instance;
  }

  public void setStorageMotorLevel(double x) {
    m_storageBeltMotor.set(x);
  }

  public void setIntakeRunning(boolean x) {
    m_isIntakeRunning = x;
    updateStorageToShooterMotor();
    updateStorageMotors();
  }

  public void setShooterRunning(boolean x) {
    m_isShooterRunning = x;
    updateStorageMotors();
    updateStorageToShooterMotor();
  }

  public void setShooterReady(boolean x) {
    m_isShooterReady = x;
    updateStorageMotors();
    updateStorageToShooterMotor();
  }

  public void setPassing(boolean x){
    m_isPassing = x;
    updateStorageMotors();
    updateStorageToShooterMotor();
  }

  private void updateStorageMotors() {
    
    if (m_isActive == false) {
      return;
    }

    if (m_isIntakeRunning) {
      m_storageBeltMotor.set(Constants.StorageBeltMotorLevelFull);
    }

    else if (m_isPassing) {
      m_storageBeltMotor.set(Constants.StorageBeltMotorLevelPass);
    }

    else if (m_isShooterRunning) {
      
    }

    else if (m_isShooterReady) {
      m_storageBeltMotor.set(Constants.StorageBeltMotorLevelFull);
    }

    else {
      m_storageBeltMotor.set(0);
    }
  }

  private void updateStorageToShooterMotor() {
    
    if (m_isActive == false) {
      return;
    }

    if (m_isPassing) {
      m_storageToShooterMotor.set(Constants.StorageToShooterMotorLevelBackward);
    }
    
    else if (m_isIntakeRunning) {
      m_storageToShooterMotor.set(Constants.StorageToShooterMotorLevelBackward);
    }

    else if (m_isShooterRunning) {
      
    }

    else if (m_isShooterReady){
      m_storageToShooterMotor.set(Constants.StorageToShooterMotorLevelForward);
    }

    else {
      m_storageToShooterMotor.set(0);
    }
  }

  @Override
  public void periodic() {

    if (m_isActive == false) {
      return;
    }
    // This method will be called once per scheduler run
    m_loggingData.BeltMotorLevel = m_storageBeltMotor.get();
    m_loggingData.BeltMotorCurrent = Robot.getPDP().getCurrent(Constants.StorageBeltMotorPDP_Port);
    m_logger.queueData(m_loggingData);
  }

  public static class StorageLoggingData {
    public double BeltMotorLevel;
    public double BeltMotorCurrent;
  }
}