package org.firstinspires.ftc.teamcode.OpModes;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Payload.Payload;
import org.firstinspires.ftc.teamcode.RoadRunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.utils;
import org.firstinspires.ftc.teamcode.utils.Debouncer;

@TeleOp
@Config
public class RefinedTeleOp extends OpMode {

    public static double LIFT_SPEED = 1;
    public static double SLO_MO = 0.3;

    private MecanumDrive drive;
    private Payload payload;

    private final Debouncer gripperLeft = new Debouncer(this, 0.5);
    private final Debouncer gripperRight = new Debouncer(this, 0.5);

    @Override
    public void init() {
        drive = new MecanumDrive(hardwareMap, new Pose2d(12, 60, Math.toRadians(270)));
        payload = new Payload(hardwareMap, drive);
    }

    @Override
    public void loop() {
        double slowMul = gamepad1.a ? SLO_MO : 1;

        drive.setDrivePowers(new PoseVelocity2d(
                new Vector2d(
                        -gamepad1.left_stick_y * slowMul,
                        -gamepad1.left_stick_x * slowMul
                ), -gamepad1.right_stick_x * slowMul));

        double liftSpeed = utils.boolsToDir(gamepad1.dpad_up, gamepad1.dpad_down, LIFT_SPEED);

        payload.pixelArm.lift.motor.setPower(liftSpeed);

        if (gripperLeft.poll(gamepad1.left_bumper))
            payload.pixelArm.gripperA.toggle();
        if (gripperRight.poll(gamepad1.right_bumper))
            payload.pixelArm.gripperB.toggle();

        if (gamepad1.dpad_left)
            payload.pixelArm.wrist.toGroundAngle();
        if (gamepad1.dpad_right)
            payload.pixelArm.wrist.toBoardAngle();

        if (gamepad1.right_bumper) payload.intake.intake();
        else if (gamepad1.left_bumper) payload.intake.outtake();
        else payload.intake.stop();
    }
}
