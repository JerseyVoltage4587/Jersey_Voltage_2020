/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {
  static Shooter m_Instance = null;
  private static Spark m_shooterMotorLeft = null;
  private static Spark m_shooterMotorRight = null;
  //private static ? m_StorageToShooterMotor = null;
  private Encoder m_shooterEncoder = null;
  /**
   * Creates a new Shooter.
   */
  public Shooter() {
    //m_shooterMotorLeft = new Spark();
    //m_shooterMotorRight = new Spark();
    //m_StorageToShooterMotor = new ?();
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
    //m_StorageToShooterMotor.set(x);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
