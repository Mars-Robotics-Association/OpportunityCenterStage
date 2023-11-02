package org.firstinspires.ftc.teamcode.OpModes;

import static java.lang.Math.toRadians;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Payload.Camera;
import org.firstinspires.ftc.teamcode.Payload.Camera.AlignmentState;
import org.firstinspires.ftc.teamcode.Payload.Camera.AlignmentController;
import org.firstinspires.ftc.teamcode.Payload.Payload;
import org.firstinspires.ftc.teamcode.Payload.PixelArm;
import org.firstinspires.ftc.teamcode.RoadRunner.MecanumDrive;

@SuppressWarnings("unused")
@Autonomous
public class ParkBlue extends LinearOpMode{
    final StartSlot START_SLOT = StartSlot.CLOSER;

    private MecanumDrive drive;
    private Payload payload;

    private enum SignalState{ LEFT, MIDDLE, RIGHT }
    private enum StartSlot{ CLOSER, FARTHER }
    public Action runAlignment(AlignmentController controller){
        return telemetryPacket -> {
            PoseVelocity2d errors = controller.obtainErrors();
            switch (controller.getState()){
                case ACTIVE:
                    drive.setDrivePowers(errors);
                    return true;
                case LOST_TARGET:
                case WITHIN_TOLERANCE:
                default: // << why is this case necessary
                    drive.setDrivePowers(new PoseVelocity2d(new Vector2d(0, 0), 0));
                    return false;
            }
        };
    }

    public AlignmentController alignWithTag(int tag, PixelArm.Gripper.Side side){
        return payload.camera.alignmentController(tag, new Vector2d(6, side.offset), 1);
    }

    public void runOpMode() throws InterruptedException {
        Telemetry telemetry = new MultipleTelemetry(super.telemetry, FtcDashboard.getInstance().getTelemetry());

        payload = new Payload(hardwareMap);

        drive = new MecanumDrive(hardwareMap,
                new Pose2d(12.00, 60.00, toRadians(270)));

        Action closerTrajectory = drive.actionBuilder(drive.pose)
                .splineTo(new Vector2d(14, 35), toRadians(270))
                .stopAndAdd(payload.pixelArm.grab())
                .stopAndAdd(payload.pixelArm.moveLift(6))
                .turnTo(toRadians(0))
                .splineTo(new Vector2d(40, 35), toRadians(0))
                .stopAndAdd(payload.pixelArm.placeOnBoard())
                .build();

        Action fartherTrajectory = drive.actionBuilder(drive.pose)
                .splineTo(new Vector2d(14, 35), toRadians(270))
                .stopAndAdd(payload.pixelArm.grab())
                .stopAndAdd(payload.pixelArm.moveLift(6))
                .setTangent(toRadians(0))
                .splineTo(new Vector2d(40, 35), toRadians(0))
                .stopAndAdd(payload.pixelArm.placeOnBoard())
                .build();

        waitForStart();

        if(START_SLOT == StartSlot.CLOSER)Actions.runBlocking(closerTrajectory);
        if(START_SLOT == StartSlot.FARTHER)Actions.runBlocking(fartherTrajectory);
    }
}