/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Intake extends SubsystemBase {
  static Intake m_Instance = null;
  private WPI_TalonSRX m_intakeMotorTALON = null;
  private WPI_VictorSPX m_intakeMotorVICTOR = null;
  private WPI_TalonSRX m_intakeArmMotor = null;
  private String intakeArmMotorStatus = null;
  /**
   * Creates a new Intake.
   */
  public Intake() {
    //m_intakeMotorTALON = new WPI_TalonSRX; //TODO find out device number and whether it's a VictorSPX or a TalonSRX
    //m_intakeMotorVICTOR = new WPI_VictorSPX();
    //m_intakeArmMotor = new WPI_TalonSRX(); //TODO find out device number
  }

  public static Intake getInstance() {
    if(m_Instance == null) {
			synchronized (Intake.class) {
				m_Instance = new Intake();
			}
		}
		return m_Instance;
  }

  public void raiseIntake() {
    m_intakeArmMotor.set(Constants.IntakeArmMotorLevel);
    intakeArmMotorStatus = "UP";
  }

  public void lowerIntake() {
    m_intakeArmMotor.set(-1 * Constants.IntakeArmMotorLevel);
    intakeArmMotorStatus = "DOWN";
  }

  public void startIntakeMotors() {
    if (intakeArmMotorStatus.equals("DOWN")) {
      m_intakeMotorTALON.set(0.7); //TODO Talon or Victor
      m_intakeMotorVICTOR.set(0.7);
    }
  }

  public void stopIntakeMotors() {
    if (intakeArmMotorStatus.equals("DOWN")) {
      m_intakeMotorTALON.set(0); //TODO Talon or Victor
      m_intakeMotorVICTOR.set(0);
    }
  }

  public void setIntakeArmSensor(int a, int b, int c) {
    m_intakeArmMotor.setSelectedSensorPosition(a, b, c);
  }

  public void setIntakeSensor(int a, int b, int c) {
    m_intakeArmMotor.setSelectedSensorPosition(a, b, c);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
