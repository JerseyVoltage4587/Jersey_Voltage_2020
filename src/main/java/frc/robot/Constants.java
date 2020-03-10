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
    public static final int LeftClimberMotorCAN_Address = 7; //Final for now
    public static final int RightClimberMotorCAN_Address = 10; //Final for now
    public static final int RightShooterMotorCAN_Address = 45; //Final
    public static final int LeftShooterMotorCAN_Address = 46; //Final
    //PDP Ports
    public static final int RightTalon2PDP_Port = 0;
    public static final int RightVictor21PDP_Port = 1;
    public static final int StorageSeparatorMotorPDP_Port = 4;
    public static final int StorageBeltMotorPDP_Port = 3;
    public static final int IntakeArmPDP_Port = 12;
    public static final int IntakePDP_Port = 5;
    public static final int StorageToShooterMotorPDP_Port = 3;
    public static final int LeftShooterMotorPDP_Port = 13;
    public static final int RightShooterMotorPDP_Port = 2;
    public static final int LeftClimberMotorPDP_Port = 10;
    public static final int RightClimberMotorPDP_Port = 6;
    public static final int LeftVictor11PDP_Port = 14;
    public static final int LeftTalon1PDP_Port = 15;
    //Motor Levels
    public static double IntakeMotorLevel = -0.42;
    public static double ShooterMotorLevel = 0.753;
    public static double StorageBeltMotorLevelFull = 0.38;
    public static double StorageBeltMotorLevelFeed = 0.28;
    public static double ClimberMotorLevel = 0.3;
    public static double StorageSeparatorMotorLevel = 0.4;
    public static double StorageToShooterMotorLevelForward = -0.5;
    public static double StorageToShooterMotorLevelBackward = 0.15;
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

    public static final double ShooterMotorTolerance = 20;

    public static final double IntakeArmKp = 0.0125;

    public static final double PushModeSlowDownOffset = 70;
    public static final double PushModeSlowBaseLevel = 0.07;
    public static final double PushModeSlowKp = 0.01;
    public static final double PushModeFastVelocity = 100;
    public static final double PushModeFastBaseLevel = 0.07;
    public static final double PushModeFastKp = 0.015;

    public static final double PullModeSlowDownOffset = 70;
    public static final double PullModeSlowBaseLevel = 0.3;
    public static final double PullModeSlowKp = 0.02;
    public static final double PullModeFastVelocity = 100;
    public static final double PullModeFastBaseLevel = 0.5;
    public static final double PullModeFastKp = 0.03;
}
