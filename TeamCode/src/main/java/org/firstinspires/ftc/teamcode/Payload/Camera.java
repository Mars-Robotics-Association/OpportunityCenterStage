package org.firstinspires.ftc.teamcode.Payload;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase;
import org.firstinspires.ftc.vision.apriltag.AprilTagPoseFtc;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.ArrayList;
import java.util.List;

public class Camera {
    private AprilTagProcessor aprilTag;
    private VisionPortal visionPortal;

    public Camera(HardwareMap hardwareMap, GameState.TeamColor teamColor) {
        aprilTag = new AprilTagProcessor.Builder()
                // The following default settings are available to un-comment and edit as needed.
                .setDrawAxes(true)
                .setDrawCubeProjection(true)
                .setDrawTagOutline(true)
                .setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
                .setTagLibrary(AprilTagGameDatabase.getCenterStageTagLibrary())
                .setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)
                .build();

        aprilTag.setDecimation(3);

        // Create the vision portal by using a builder.
        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .enableLiveView(true)
                .setStreamFormat(VisionPortal.StreamFormat.YUY2)
                .addProcessor(aprilTag)
                .build();
    }

    public AprilTagDetection findTagWithID(int desiredTag){
        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        for (AprilTagDetection detection : currentDetections)
            if ((detection.metadata != null) && (detection.id == desiredTag))return detection;

        return null;
    }

    public enum AlignmentState{
        ACTIVE,
        WITHIN_TOLERANCE,
        LOST_TARGET
    }

    public AlignmentController alignmentController(int desiredTag, Vector2d translateOffset, double tolerance){
        return new AlignmentController(desiredTag, translateOffset, tolerance);
    }

    public class AlignmentController{
        private final int desiredTag;
        private final Vector2d translateOffset;
        private final double tolerance;

        AlignmentController(int desiredTag, Vector2d translateOffset, double tolerance){
            this.desiredTag = desiredTag;
            this.translateOffset = translateOffset;
            this.tolerance = tolerance;
        }

        private AlignmentState state = AlignmentState.ACTIVE;

        public AlignmentState getState() {
            return state;
        }

        static final double GAIN_X = 0.02;
        static final double GAIN_Y = 0.02;
        static final double GAIN_YAW = 0.01;

        public PoseVelocity2d obtainErrors() {
            AprilTagDetection tag = findTagWithID(desiredTag);
            // there is nothing we can do  -Napoleon
            if(tag == null){
                state = AlignmentState.LOST_TARGET;
                return null;
            }

            AprilTagPoseFtc ftcPose = tag.ftcPose;

            Vector2d tagVec = new Vector2d(ftcPose.y, ftcPose.x);

            Vector2d errorVec = tagVec.minus(translateOffset);
            if(errorVec.dot(errorVec) <= tolerance){
                state = AlignmentState.WITHIN_TOLERANCE;
                return null;
            }
            Vector2d powerVec = new Vector2d(errorVec.x * GAIN_X, errorVec.y * GAIN_Y);

            state = AlignmentState.ACTIVE;
            return new PoseVelocity2d(powerVec, -ftcPose.yaw * GAIN_YAW);
        }
    }
}
