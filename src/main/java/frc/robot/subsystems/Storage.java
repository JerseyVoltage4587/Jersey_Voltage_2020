/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Storage extends SubsystemBase {
  static Storage m_Instance = null;
  private WPI_TalonSRX m_storageBeltMotor = null;
  private WPI_TalonSRX m_storageSeparatorMotor = null;
  /**
   * Creates a new Storage.
   */
  public Storage() {
	  //m_storageBeltMotor = new WPI_TalonSRX(0); //TODO find out device number
    //m_storageSeparatorMotor = new WPI_TalonSRX(); //TODO find out device number
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
    m_storageBeltMotor.set(x);
  }

  public void setStoragerSeparatorMotorLevel(double x) {
    m_storageSeparatorMotor.set(x);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
