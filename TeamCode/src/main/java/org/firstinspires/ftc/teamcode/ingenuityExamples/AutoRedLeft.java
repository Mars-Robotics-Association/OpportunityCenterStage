package org.firstinspires.ftc.teamcode.ingenuityExamples;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.teamcode.RoadRunner.MecanumDrive;

@Autonomous(name = "Auto Red Left", group = "AutonomousCompetition")
public final class AutoRedLeft extends LinearOpMode {
    private DcMotor armMotor;
    private Servo gripper;
    private Servo wrist;
    private TouchSensor touchSensor;

    @Override
    public void runOpMode() throws InterruptedException {
        armMotor = hardwareMap.dcMotor.get("armMotor");
        gripper = hardwareMap.servo.get("gripper");
        wrist = hardwareMap.servo.get("wrist");
        touchSensor = hardwareMap.get(TouchSensor.class, "sensor_touch");

        wrist.setPosition(.8);
        gripper.setPosition(.51);

        // stow the arm and gripper
        while (!touchSensor.isPressed()) {
            armMotor.setPower(0.3);
        }
        armMotor.setPower(0);


        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(-36, -64, Math.toRadians(90)));

        waitForStart();

        Actions.runBlocking(
                drive.actionBuilder(drive.pose)
                        .splineTo(new Vector2d(-36,-36.3), Math.toRadians(90))
                        .splineTo(new Vector2d(-36,-12), Math.toRadians(0))
                        .splineTo(new Vector2d(22.7, -12),Math.toRadians(0))
                        .splineTo(new Vector2d(46.9, -13.5),Math.toRadians(270))
                        .build());
    }
}
