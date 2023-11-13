package org.firstinspires.ftc.teamcode.OpModes;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Payload.Payload;
import org.firstinspires.ftc.teamcode.RoadRunner.MecanumDrive;

@TeleOp
@Config
public class RefinedTeleOp extends OpMode {

    private MecanumDrive drive;
    private Payload payload;

    public static double LIFT_POWER = .5;

    @Override
    public void init() {
        drive = new MecanumDrive(hardwareMap, new Pose2d(12, 60, Math.toRadians(270)));
        payload = new Payload(hardwareMap, drive, false);
    }

    @Override
    public void loop() {
        drive.setDrivePowers(new PoseVelocity2d(
                new Vector2d(
                        -gamepad1.left_stick_y,
                        -gamepad1.left_stick_x
                ), -gamepad1.right_stick_x));

        double liftPower = 0;

        if (gamepad1.dpad_up)liftPower++;
        if (gamepad1.dpad_down)liftPower--;

        liftPower *= LIFT_POWER;

        payload.pixelArm.lift.override(liftPower);
    }
}
