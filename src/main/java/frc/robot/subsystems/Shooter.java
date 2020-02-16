/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {
  static Shooter m_Instance = null;
  private WPI_TalonSRX m_shooterMotor = null;
  private Encoder m_shooterEncoder;
  /**
   * Creates a new Shooter.
   */
  public Shooter() {
    //m_shooterMotor = new WPI_TalonSRX();
  }

  public static Shooter getInstance() {
    if(m_Instance == null) {
			synchronized (Shooter.class) {
				m_Instance = new Shooter();
			}
		}
		return m_Instance;
  }

  public static void setShooterRPM() {
    
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
