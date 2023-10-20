package org.firstinspires.ftc.teamcode.OpModes;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.RoadRunner.MecanumDrive;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("unused")
@Autonomous
public class ParkBlue extends LinearOpMode{
    static class DummyAction implements Action{
        private final String name;
        private final long estimatedTime;

        DummyAction(String name, long estimatedTime){
            this.name = name;
            this.estimatedTime = estimatedTime;
        }

        @Override
        public void preview(@NonNull Canvas fieldOverlay) {
            Action.super.preview(fieldOverlay);
        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            telemetryPacket.addLine(String.format("Emulating action %s", name));
            try {
                Thread.sleep(estimatedTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            telemetryPacket.addLine(String.format("Finished action %s", name));
            return false;
        }
    }

    private AprilTagProcessor aprilTag;
    private VisionPortal visionPortal;

    private void initVisionPortal(){
        // Create the AprilTag processor.
        aprilTag = new AprilTagProcessor.Builder()

                // The following default settings are available to un-comment and edit as needed.
                //.setDrawAxes(false)
                //.setDrawCubeProjection(false)
                //.setDrawTagOutline(true)
                //.setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
                //.setTagLibrary(AprilTagGameDatabase.getCenterStageTagLibrary())
                //.setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)

                // == CAMERA CALIBRATION ==
                // If you do not manually specify calibration parameters, the SDK will attempt
                // to load a predefined calibration for your camera.
                //.setLensIntrinsics(578.272, 578.272, 402.145, 221.506)
                // ... these parameters are fx, fy, cx, cy.

                .build();

        // Adjust Image Decimation to trade-off detection-range for detection-rate.
        // eg: Some typical detection data using a Logitech C920 WebCam
        // Decimation = 1 ..  Detect 2" Tag from 10 feet away at 10 Frames per second
        // Decimation = 2 ..  Detect 2" Tag from 6  feet away at 22 Frames per second
        // Decimation = 3 ..  Detect 2" Tag from 4  feet away at 30 Frames Per Second (default)
        // Decimation = 3 ..  Detect 5" Tag from 10 feet away at 30 Frames Per Second (default)
        // Note: Decimation can be changed on-the-fly to adapt during a match.
        //aprilTag.setDecimation(3);

        // Create the vision portal by using a builder.
        VisionPortal.Builder builder = new VisionPortal.Builder();

        // Set the camera (webcam vs. built-in RC phone camera).
        builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));

        // Choose a camera resolution. Not all cameras support all resolutions.
        //builder.setCameraResolution(new Size(640, 480));

        // Enable the RC preview (LiveView).  Set "false" to omit camera monitoring.
        //builder.enableLiveView(true);

        // Set the stream format; MJPEG uses less bandwidth than default YUY2.
        //builder.setStreamFormat(VisionPortal.StreamFormat.YUY2);

        // Choose whether or not LiveView stops if no processors are enabled.
        // If set "true", monitor shows solid orange screen if no processors enabled.
        // If set "false", monitor shows camera view without annotations.
        //builder.setAutoStopLiveView(false);

        // Set and enable the processor.
        builder.addProcessor(aprilTag);

        // Build the Vision Portal, using the above settings.
        visionPortal = builder.build();

        // Disable or re-enable the aprilTag processor at any time.
        //visionPortal.setProcessorEnabled(aprilTag, true);

    }   // end method initAprilTag()

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

        MecanumDrive drive = new MecanumDrive(hardwareMap,
                new Pose2d(-60, -12, Math.toRadians(0)));

        initVisionPortal();

        telemetry.addData("Tags found", 0);

        List<AprilTagDetection> currentDetections = new LinkedList<>();

        SignalState signalState = SignalState.MIDDLE;

//        for (int i = 0; i < 200; i++) {
//            currentDetections = aprilTag.getDetections();
//            telemetry.update();
//            telemetry.addData("Tags found", currentDetections.size());
//            if (!currentDetections.isEmpty())break;
//            sleep(20);
//        }
//
//        if(currentDetections.isEmpty())throw new RuntimeException("Failed to detect a tag within 4 seconds");
//
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

        TrajectoryActionBuilder initTrajectory = drive.actionBuilder(drive.pose);

        // Move to signal strip and place pixel
        switch(signalState){
            case LEFT: initTrajectory = initTrajectory
                    .lineToX(-35)
                    .turnTo(Math.toRadians(90)).stopAndAdd(
                            new DummyAction("PlacePixel.onGround", 200))
                    .turnTo(Math.toRadians(0));
                    break;

            case MIDDLE: initTrajectory = initTrajectory
                    .lineToX(-34).stopAndAdd(
                            new DummyAction("PlacePixel.onGround", 200));
                    break;

            case RIGHT: initTrajectory = initTrajectory
                    .lineToX(-35)
                    .turn(Math.toRadians(-90)).stopAndAdd(
                            new DummyAction("PlacePixel.onGround", 200))
                    .turnTo(Math.toRadians(0));
                    break;
        }

        // Turn and move to
        initTrajectory = initTrajectory
                .lineToX(-60)
                .splineTo(new Vector2d(-60, 35), Math.toRadians(0.00))
                .splineTo(new Vector2d(signalState.backboardXOffset, 51), Math.toRadians(90.00))
                .stopAndAdd(new DummyAction("PlacePixel.onBackstage", 1000));


        Action initTrajectoryBuilt = initTrajectory.build();

        waitForStart();

        Actions.runBlocking(initTrajectoryBuilt);
    }
}