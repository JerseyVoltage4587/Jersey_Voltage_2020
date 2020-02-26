/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants.  This class should not be used for any other purpose.  All constants should be
 * declared globally (i.e. public static).  Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    //CAN and PWM Addresses
    public static final int LeftTalon1CAN_Address = 1;
    public static final int LeftVictor1CAN_Address = 11;
    public static final int RightTalon2CAN_Address = 2;
    public static final int RightVictor21CAN_Address = 21;
    public static int StorageSeparatorMotorPWM_Address = 3;
    public static int StorageBeltMotorPWM_Address = 4;
    public static int IntakeArmPWM_Address = 5;
    public static int IntakePWM_Address = 6;
    public static int LeftClimberMotorPWM_Address = 7;
    public static int RightClimberMotorPWM_Address = 8;
    public static int RightShooterMotorCAN_Address = 9;
    public static int LeftShooterMotorCAN_Address = 10;
    //PDP Ports
    public static final int RightTalon2PDP_Port = 0;
    public static final int RightVictor21PDP_Port = 1;
    public static int StorageSeparatorMotorPDP_Port = 2; //TODO Find Actual PDP Port
    public static int StorageBeltMotorPDP_Port = 3; //TODO Find Actual PDP Port
    public static int IntakeArmPDP_Port = 4; //TODO Find Actual PDP Port
    public static int LeftShooterMotorPDP_Port = 5; //TODO Find Actual PDP Port
    public static int RightShooterMotorPDP_Port = 6; //TODO Find Actual PDP Port
    public static int IntakePDP_Port = 7; //TODO Find Actual PDP Port
    public static int LeftClimberMotorPDP_Port = 8; //TODO Find Actual PDP Port
    public static int RightClimberMotorPDP_Port = 9; //TODO Find Actual PDP Port
    public static final int LeftVictor11PDP_Port = 14;
    public static final int LeftTalon1PDP_Port = 15;
    //Motor Levels
    public static double IntakeArmMotorLevel = 0.65; //TODO Ask Mechanical or Drivers what they want the Motor Level to be
    public static double IntakeMotorLevel; //TODO Ask Mechanical or Drivers what they want the Motor Level to be
    public static double ShooterMotorLevel = 0.7; //TODO Ask Mechanical or Drivers what they want the Motor Level to be
    //Encoder Tics
    public static final int DriveBaseEncoderTics = 4096;
    public static final int ShooterEncoderTics = 4096;
    //Wheel Diameters
    public static final double DriveBaseWheelDiameter = 6; // inches
    public static final double ShooterWheelDiameter = 6; // inches
}
