package org.firstinspires.ftc.teamcode.OpModes;


import static org.firstinspires.ftc.teamcode.utils.boolToDir;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Payload.Payload;
import org.firstinspires.ftc.teamcode.RoadRunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.utils.Debouncer;

@TeleOp
@Config
public class RefinedTeleOp extends OpMode {

    public static double LIFT_SPEED = 1;

    private MecanumDrive drive;
    private Payload payload;

    private final Debouncer gripperLeft = new Debouncer(this, 0.5);
    private final Debouncer gripperRight = new Debouncer(this, 0.5);
    private double lastRuntime;

    @Override
    public void init() {
        drive = new MecanumDrive(hardwareMap, new Pose2d(12, 60, Math.toRadians(270)));
        payload = new Payload(hardwareMap, drive, false);
    }

    @Override
    public void loop() {
        double deltaTime = getRuntime() - lastRuntime;
        lastRuntime = getRuntime();

        drive.setDrivePowers(new PoseVelocity2d(
                new Vector2d(
                        -gamepad1.left_stick_y,
                        -gamepad1.left_stick_x
                ), -gamepad1.right_stick_x));

        double liftSpeed = boolToDir(gamepad1.dpad_up, gamepad1.dpad_down, LIFT_SPEED);

        payload.pixelArm.lift.motor.setPower(liftSpeed);

        if (gripperLeft.poll(gamepad1.left_bumper))
            payload.pixelArm.gripperA.toggle();
        if (gripperRight.poll(gamepad1.right_bumper))
            payload.pixelArm.gripperB.toggle();

        if (gamepad1.dpad_left)
            payload.pixelArm.wrist.toGroundAngle();
        if (gamepad1.dpad_right)
            payload.pixelArm.wrist.toBoardAngle();
    }

    @Override
    public void stop() {
        payload.pixelArm.wrist.toStorageAngle();

        super.stop();
    }
}
