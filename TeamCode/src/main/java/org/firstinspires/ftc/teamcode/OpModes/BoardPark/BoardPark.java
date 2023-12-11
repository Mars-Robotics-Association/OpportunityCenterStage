package org.firstinspires.ftc.teamcode.OpModes.BoardPark;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Payload.Payload;
import org.firstinspires.ftc.teamcode.Payload.PixelArm;
import org.firstinspires.ftc.teamcode.RoadRunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.utils;

public abstract class BoardPark extends LinearOpMode {
    private static Rotation2d faceX(double x){
        return new Vector2d(x, 0).angleCast();
    }

    private static Rotation2d faceY(double y){
        return new Vector2d(0, y).angleCast();
    }

    protected MecanumDrive drive;
    protected Payload payload;

    protected double flipY;
    private Action placePixel(){
        PixelArm pixelArm = payload.pixelArm;
        DcMotor liftMotor = payload.pixelArm.lift.motor;
        BoardPark opMode = this;

        return new Action() {
            private boolean initPass = true;
            private utils.Debouncer debouncer;

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (initPass) {
                    liftMotor.setPower(1);
                    debouncer = new utils.Debouncer(opMode, 500);
                    initPass = false;
                    return true;
                }
                if (debouncer.poll()) {
                    liftMotor.setPower(0);
                    pixelArm.gripperA.open();
                    pixelArm.gripperB.open();
                    return false;
                }
                return false;
            }
        };
    }

    protected void initCoreSimple(Pose2d initPose){
        drive = new MecanumDrive(hardwareMap, initPose);
        payload = new Payload(hardwareMap, drive);
    }

    protected Action mainAction(){
        return drive.actionBuilder(drive.pose)
                .setTangent(faceY(flipY))
                .lineToYConstantHeading(58.5 * flipY)
                .setTangent(faceX(1))
                .lineToXConstantHeading(35)
                .setTangent(faceY(flipY))
                .lineToYConstantHeading(35.5 * flipY)
                .setTangent(faceX(1))
                .stopAndAdd(placePixel())
                .lineToXConstantHeading(50)
                .build();
    }
}
