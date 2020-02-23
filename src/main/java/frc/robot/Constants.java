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
    public static final int LeftTalon1CAN_Address = 1;
    public static final int LeftVictor1CAN_Address = 11;
    public static final int LeftTalon1PDP_Port = 15;
    public static final int LeftVictor11PDP_Port = 14;
    public static final int RightTalon2PDP_Port = 0;
    public static final int RightVictor21PDP_Port = 1;
    public static final int RightTalon2CAN_Address = 2;
    public static final int RightVictor21CAN_Address = 21;
    public static int IntakeArmPWM_Address;
    public static int IntakeArmPDP_Port;
    public static int IntakePWM_Address;
    public static int IntakePDP_Port;
    public static final double IntakeArmMotorLevel = 0.65;
    public static double IntakeMotorLevel;
    public static int ShooterMotorPWM_Address;
    public static final double ShooterMotorLevel = 0.7;
    public static int ShooterMotorPDP_Port;
    public static int FeederMotorPWM_Address;
    public static int FeederMotorPDP_Port;
    public static int ClimberMotorPWM_Address;
    public static int ClimberMotorPDP_Port;
    public static final int DriveBaseEncoderTics = 4096;
    public static final double WheelDiameter = 6; // inches
}
