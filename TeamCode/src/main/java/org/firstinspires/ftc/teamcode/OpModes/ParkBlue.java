package org.firstinspires.ftc.teamcode.OpModes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.GainControl;
import org.firstinspires.ftc.teamcode.Roadrunner.drive.OppyMecanumDrive;
import org.firstinspires.ftc.teamcode.Roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.Roadrunner.trajectorysequence.TrajectorySequenceBuilder;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
@Autonomous
public class ParkBlue extends LinearOpMode{

    private AprilTagProcessor aprilTag;
    private VisionPortal visionPortal;

    private void initVisionPortal(){
        // Summon the AprilTag Processor (Tfod has been packaged into another processor)
        aprilTag = new AprilTagProcessor.Builder().build();

        // Summon the VisionPortal
        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .addProcessor(aprilTag)
                .build();

        // Make sure camera is streaming before we try to set the exposure controls
        telemetry.addData("Camera", "Waiting for streaming");
        telemetry.update();
        while (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {
            sleep(20);
        }
        telemetry.addData("Camera", "Ready");
        telemetry.update();

        ExposureControl exposureControl = visionPortal.getCameraControl(ExposureControl.class);
        GainControl gainControl = visionPortal.getCameraControl(GainControl.class);

        // TODO: test if Mode.ShutterPriority also works with less config
        boolean exposureSuccess = exposureControl.setMode(ExposureControl.Mode.Manual);
        sleep(20);
        assert exposureSuccess;

        exposureControl.setExposure(6, TimeUnit.MILLISECONDS);
        gainControl.setGain(250);
        sleep(20);
    }

    private enum SignalState{
        LEFT(-42),
        MIDDLE(-36),
        RIGHT(-29.5);

        public final double backboardXOffset;

        SignalState(double setBackboardXOffset){
            backboardXOffset = setBackboardXOffset;
        }
    }

    public void runOpMode() throws InterruptedException {
        Telemetry telemetry = new MultipleTelemetry(super.telemetry, FtcDashboard.getInstance().getTelemetry());

        OppyMecanumDrive drive = new OppyMecanumDrive(hardwareMap);

        SignalState signalState = SignalState.MIDDLE;

//        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
//        for (AprilTagDetection detection : currentDetections) {
//            if (detection.metadata != null) {
//                telemetry.addData("Unknown Target", "Tag ID %d is not in TagLibrary\n", detection.id);
//                continue;
//            }
//
//            double relativePos = detection.ftcPose.y;
//
//            if(Math.abs(relativePos) <= 10.875){
//                signalState = SignalState.MIDDLE;
//            } else if (relativePos > 0) {
//                signalState = SignalState.RIGHT;
//            } else {
//                signalState = SignalState.LEFT;
//            }
//        }

        if(signalState == null)throw new RuntimeException(String.format("Failed to detect signal state (found %d tags)", 0));

        Pose2d startPose = new Pose2d(-60,12,Math.toRadians(0));

        TrajectorySequenceBuilder initTrajectory = drive.trajectorySequenceBuilder(
                new Pose2d(-60,12,Math.toRadians(0))
        );

        // Move to signal strip and place pixel
        switch(signalState){
            case LEFT:
                initTrajectory
                        .forward(25)
                        .turn(Math.toRadians(90))
                        .addDisplacementMarker(() -> {/*PlacePixel.onGround*/})
                        .turn(Math.toRadians(-90))
                        .back(25);
                break;
            case MIDDLE:
                initTrajectory
                        .forward(26)
                        .addDisplacementMarker(() -> {/*PlacePixel.onGround*/})
                        .back(26);
                break;
            case RIGHT:
                initTrajectory
                        .forward(25)
                        .turn(Math.toRadians(-90))
                        .addDisplacementMarker(() -> {/*PlacePixel.onGround*/})
                        .turn(Math.toRadians(90))
                        .back(25);
        }

        // Turn and move to
        initTrajectory
                .splineTo(new Vector2d(-60, 35), Math.toRadians(0.00))
                .splineTo(new Vector2d(signalState.backboardXOffset, 51), Math.toRadians(90.00))
                .addDisplacementMarker(() -> {/*PlacePixel.onBackstage*/});


        TrajectorySequence initTrajectoryBuilt = initTrajectory.build();

        waitForStart();

        drive.followTrajectorySequence(initTrajectoryBuilt);
    }
}