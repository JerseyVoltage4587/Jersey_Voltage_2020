/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.OI;

public class DriveBase extends SubsystemBase {
  DifferentialDrive m_drive = null;
  static DriveBase m_Instance = null;
  public WPI_TalonSRX m_lefttalon1;
  public WPI_TalonSRX m_righttalon2;
  private WPI_VictorSPX m_leftvictor11;
  private WPI_VictorSPX m_rightvictor21;
  private double LeftMotorLevel;
  private double RightMotorLevel;

  /**
   * Creates a new DriveBase.
   */
  public DriveBase() {
    m_lefttalon1 = new WPI_TalonSRX(1);
    m_righttalon2 = new WPI_TalonSRX(2);
    m_leftvictor11 = new WPI_VictorSPX(11);
    m_rightvictor21 = new WPI_VictorSPX(21);
    m_leftvictor11.follow(m_lefttalon1);
    m_rightvictor21.follow(m_righttalon2);
    m_lefttalon1.setInverted(false);
    m_righttalon2.setInverted(true);
    m_leftvictor11.setInverted(InvertType.FollowMaster);
    m_rightvictor21.setInverted(InvertType.FollowMaster);
    m_drive = new DifferentialDrive(m_lefttalon1, m_righttalon2);
    m_drive.setRightSideInverted(false);
  }

  public static DriveBase getInstance() {
    if (m_Instance == null) {
      synchronized (DriveBase.class) {
        m_Instance = new DriveBase();
      }
    }
    return m_Instance;
  }

  public void Drive() {
    double forward = 0;
    double turn = 0;

    if (Math.abs(OI.getInstance().getDrive()) < 0.10) {
			forward = 0;
		}
		if (Math.abs(OI.getInstance().getTurn()) < 0.10) {
			turn = 0;
    }
    
    m_drive.arcadeDrive(forward, turn);
  }

  public void setLeftMotorLevel(double x) {
    LeftMotorLevel = x;
    m_lefttalon1.set(LeftMotorLevel);
  }

  public void setRightMotorLevel(double x) {
    RightMotorLevel = x;
    m_righttalon2.set(RightMotorLevel);
  }

  public void setSafetyEnabled(boolean x){
    m_drive.setSafetyEnabled(x);
  }

  public void setLeftSensor(int a, int b, int c) {
    m_lefttalon1.setSelectedSensorPosition(a, b, c);
  }

  public void setRightSensor(int a, int b, int c) {
    m_righttalon2.setSelectedSensorPosition(a, b, c);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    Drive();
  }
}
