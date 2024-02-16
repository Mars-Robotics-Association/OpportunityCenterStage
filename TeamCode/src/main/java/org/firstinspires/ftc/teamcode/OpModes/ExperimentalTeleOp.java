package org.firstinspires.ftc.teamcode.OpModes;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Payload.Payload;
import org.firstinspires.ftc.teamcode.Payload.PixelArm;
import org.firstinspires.ftc.teamcode.RoadRunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.utils;

@TeleOp
@Config
public class ExperimentalTeleOp extends OpMode {

    enum LiftPreset {
        GROUND (0, 1),
        BOARD_L(12, 1 << 1),
        BOARD_H(24, 1 << 2);

        public static LiftPreset resolve(int inputMask){
            for (LiftPreset preset : LiftPreset.values())
                if ((inputMask & preset.mask) == preset.mask)return preset;

            return null;
        }

        private final double height;
        public final int mask;

        public int apply(boolean condition){
            return condition ? mask : 0;
        }

        LiftPreset(double height, int mask){
            this.height = height;
            this.mask = mask;
        }
    }

    private MecanumDrive drive;
    private Payload payload;

    private final utils.Debouncer gripperLeft = new utils.Debouncer(this, 0.5);
    private final utils.Debouncer gripperRight = new utils.Debouncer(this, 0.5);

    @Override
    public void init() {
        drive = new MecanumDrive(hardwareMap, new Pose2d(12, 60, Math.toRadians(270)));
        payload = new Payload(this, drive);
    }

    @Override
    public void loop() {
        drive.setDrivePowers(new PoseVelocity2d(
                new Vector2d(
                        -gamepad1.left_stick_y,
                        -gamepad1.left_stick_x
                ), -gamepad1.right_stick_x));

        liftPresets: if(gamepad1.left_trigger > 0.2f){
            PixelArm pixelArm = payload.pixelArm;

            int mask = 0;

            mask |= LiftPreset.GROUND .apply(gamepad1.a);
            mask |= LiftPreset.BOARD_L.apply(gamepad1.x);
            mask |= LiftPreset.BOARD_H.apply(gamepad1.y);

            LiftPreset preset = LiftPreset.resolve(mask);

            if(preset == null)break liftPresets;
            pixelArm.lift.setLiftHeight(preset.height);

            if(preset == LiftPreset.GROUND)
                 pixelArm.wrist.toGroundAngle();
            else pixelArm.wrist.toBoardAngle();

        }

        if (gamepad1.left_bumper && gripperLeft.poll())
            payload.pixelArm.gripperA.toggle();
        if (gamepad1.right_bumper && gripperRight.poll())
            payload.pixelArm.gripperB.toggle();
    }
}
