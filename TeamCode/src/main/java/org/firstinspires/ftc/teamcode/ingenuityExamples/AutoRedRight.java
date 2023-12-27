package org.firstinspires.ftc.teamcode.ingenuityExamples;


import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.tuning.TuningOpModes;

@Autonomous(name = "Auto Red Right", group = "AutonomousCompetition")
public final class AutoRedRight extends LinearOpMode {
    private Motor armMotor;
    private Servo gripper;
    private Servo wrist;
    private TouchSensor touchSensor;

    @Override
    public void runOpMode() throws InterruptedException {
        if (TuningOpModes.DRIVE_CLASS.equals(MecanumDrive.class)) {
            armMotor = new Motor(hardwareMap, "armMotor", Motor.GoBILDA.RPM_312);
            gripper = hardwareMap.servo.get("gripper");
            wrist = hardwareMap.servo.get("wrist");
            touchSensor = hardwareMap.get(TouchSensor.class, "sensor_touch");

            wrist.setPosition(.8);
            gripper.setPosition(.51);

            // stow the arm and gripper
            while (!touchSensor.isPressed()) {
                armMotor.set(0.3);
            }
            armMotor.set(0);


            MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(-11, -64, Math.toRadians(90)));

            waitForStart();

            Actions.runBlocking(
                    drive.actionBuilder(drive.pose)
                            .splineTo(new Vector2d(12,-36.3), Math.toRadians(180))
                            .splineTo(new Vector2d(43.5,-34), Math.toRadians(0))
                            .splineTo(new Vector2d(47.1, -58.7),Math.toRadians(270))
                            .build());



        } else {
            throw new AssertionError();
        }
    }
}

