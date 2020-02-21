/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {
  static Shooter m_Instance = null;
  private static Spark m_shooterMotorLeft = null;
  private static Spark m_shooterMotorRight = null;
  private static WPI_TalonSRX m_storageKickerTALON = null;
  private static WPI_VictorSPX m_storageKickerVICTOR = null;
  /**
   * Creates a new Shooter.
   */
  public Shooter() {
    //m_shooterMotorLeft = new WPI_Spark(); //TODO find out device number
    //m_shooterMotorRight = new WPI_Spark(); //TODO find out device number
    //m_storageKickerTALON = new WPI_TalonSRX(); //TODO find out device number and whether it's a VictorSPX or a TalonSRX
    //m_storageKickerVICTOR = new WPI_VictorSPX();
  }

  public static Shooter getInstance() {
    if(m_Instance == null) {
			synchronized (Shooter.class) {
				m_Instance = new Shooter();
			}
		}
		return m_Instance;
  }

  public static void setShooterRPM(double x) {
    //m_shooterMotorLeft.set(x);
    //m_shooterMotorRight.set(x);
  }

  public static void setStorageToShooterRPM(double x) {
    //m_storageKicker.set(x);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
