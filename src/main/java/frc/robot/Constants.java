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
    //CAN Addresses
    public static final int LeftTalon1CAN_Address = 1;
    public static final int LeftVictor1CAN_Address = 11;
    public static final int RightTalon2CAN_Address = 2;
    public static final int RightVictor21CAN_Address = 21;
    public static final int StorageSeparatorMotorCAN_Address = 6; //Final
    public static final int StorageBeltMotorCAN_Address = 5; //Final
    public static final int IntakeMotorArmCAN_Address = 3; //Final
    public static final int IntakeMotorCAN_Address = 8; //Final
    public static final int StorageToShooterMotorCAN_Address = 4; //Final
    public static int LeftClimberMotorCAN_Address = 7; //Final for now
    public static int RightClimberMotorCAN_Address = 0; //Final for now
    public static int RightShooterMotorCAN_Address = 45; //Final
    public static int LeftShooterMotorCAN_Address = 46; //Final
    //PDP Ports
    public static final int RightTalon2PDP_Port = 0;
    public static final int RightVictor21PDP_Port = 1;
    public static final int StorageSeparatorMotorPDP_Port = 4; //Final
    public static int StorageBeltMotorPDP_Port = 3; //Final for now
    public static int IntakeArmPDP_Port = 12; //Final for now
    public static int IntakePDP_Port = 5; //Final for now
    public static final int StorageToShooterMotorPDP_Port = 3; //Final
    public static int LeftShooterMotorPDP_Port = 6; //Final for now
    public static int RightShooterMotorPDP_Port = 8; //Not enough Motor Controllers
    public static int LeftClimberMotorPDP_Port = 10; //Final for now
    public static int RightClimberMotorPDP_Port = 5; //Final for now
    public static final int LeftVictor11PDP_Port = 14;
    public static final int LeftTalon1PDP_Port = 15;
    //Motor Levels
    public static double IntakeMotorLevel = 0.4;
    public static double ShooterMotorLevel = 0.5;
    public static double StorageBeltMotorLevelFull = 0.5;
    public static double StorageBeltMotorLevelFeed = 0.3;
    public static double ClimberMotorLevel = 0.4;
    public static double StorageSeparatorMotorLevel = 0.4;
    public static double StorageToShooterMotorLevelForward = -0.4;
    public static double StorageToShooterMotorLevelBackward = 0.27;
    public static double IntakeArmMotorLevelUp = -0.4;
    public static double IntakeArmMotorLevelDown = 0.3;
    //Motor Stall Currents
    public static final double IntakeArmStallCurrent = 20;
    //Encoder Tics
    public static final int DriveBaseEncoderTics = 4096;
    public static final int ShooterEncoderTics = 4096;
    //Wheel Diameters
    public static final double DriveBaseWheelDiameter = 6; // inches
    public static final double ShooterWheelDiameter = 6; // inches
    //Degrees
    public static final double IntakeArmDegrees = 87;
}
