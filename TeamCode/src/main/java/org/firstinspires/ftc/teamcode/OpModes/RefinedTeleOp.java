package org.firstinspires.ftc.teamcode.OpModes;

import android.os.SystemClock;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.RoadRunner.MecanumDrive;

@TeleOp
public class RefinedTeleOp extends OpMode {

    private MecanumDrive drive;
    private IMU imu;

    double lastHeading;
    long lastNanoTime;

    @Override
    public void init() {
        drive = new MecanumDrive(hardwareMap, new Pose2d(12, 60, Math.toRadians(270)));
        imu = drive.imu;
        lastHeading = 0;
        lastNanoTime = SystemClock.elapsedRealtimeNanos();

        telemetry = new MultipleTelemetry(super.telemetry, FtcDashboard.getInstance().getTelemetry());
    }

    @Override
    public void loop() {
        drive.setDrivePowers(new PoseVelocity2d(
                new Vector2d(
                        -gamepad1.left_stick_y,
                        -gamepad1.left_stick_x
                ), -gamepad1.right_stick_x));

        YawPitchRollAngles angles = imu.getRobotYawPitchRollAngles();
        telemetry.addData("Actual Angles", angles);

        long currentNanoTime = SystemClock.elapsedRealtimeNanos();
        double deltaTime = (double)(currentNanoTime - lastNanoTime) / 1e-9;
        lastHeading += deltaTime * -gamepad1.right_stick_x;
        lastNanoTime = currentNanoTime;

        telemetry.addData("Predicted Heading", lastHeading);

        telemetry.update();
    }
}
