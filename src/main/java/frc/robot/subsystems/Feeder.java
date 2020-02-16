/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Feeder extends SubsystemBase {
  static Feeder m_Instance = null;
  private WPI_TalonSRX m_feederMotor = null;
  /**
   * Creates a new Feeder.
   */
  public Feeder() {
    //m_feederMotor = new WPI_TalonSRX();
  }

  public static Feeder getInstance() {
    if(m_Instance == null) {
			synchronized (Feeder.class) {
				m_Instance = new Feeder();
			}
		}
		return m_Instance;
  }

  public static void setFeederMotorLevel() {
    
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
