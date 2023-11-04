package org.firstinspires.ftc.teamcode.OpModes;

import static java.lang.Math.toRadians;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Payload.Payload;
import org.firstinspires.ftc.teamcode.Payload.PixelArm;
import org.firstinspires.ftc.teamcode.RoadRunner.MecanumDrive;

import java.util.function.Function;

@SuppressWarnings("unused")
@Autonomous
public class ParkBlue extends LinearOpMode{

    private MecanumDrive drive;

    private <A> Action $(A component, Function<A, Action> operation){
        if(component == null)return telemetryPacket -> false;
        return operation.apply(component);
    }
    private <A> Action $seq(A component, Function<A, Action[]> operation){
        if(component == null)return telemetryPacket -> false;
        return new SequentialAction(operation.apply(component));
    }
    private <A> Action $par(A component, Function<A, Action[]> operation){
        if(component == null)return telemetryPacket -> false;
        return new ParallelAction(operation.apply(component));
    }

    private TrajectoryActionBuilder path(){return drive.actionBuilder(drive.pose);}

    public void runOpMode() {
        Telemetry telemetry = new MultipleTelemetry(super.telemetry, FtcDashboard.getInstance().getTelemetry());

        Payload payload = new Payload(hardwareMap, drive, true);

        Payload.GameState gameState = payload.gameState;

        boolean isBlueTeam = gameState.teamColor == Payload.GameState.TeamColor.BLUE;
        double flipY = gameState.teamColor.flipY;

        double startHeading = isBlueTeam ? toRadians(270) : toRadians(90);

        drive = new MecanumDrive(hardwareMap,
                new Pose2d(
                        gameState.startSlot.posX,
                        60.00 * flipY,
                        startHeading
                        ));

        double pixelAngle = 0;
        switch (gameState.signalState){
            case LEFT  : pixelAngle = startHeading + toRadians(90);
            break;
            case MIDDLE: pixelAngle = startHeading;
            break;
            case RIGHT : pixelAngle = startHeading - toRadians(90);
        }

        Action grabAndPlacePurplePixel = drive.actionBuilder(drive.pose)
                // drive up to Box of Signals
                .lineToY(34.00 * flipY)
                // look at correct pixel
                .turnTo(pixelAngle)
                // grab said pixel
                .stopAndAdd( $(payload.pixelArm, PixelArm::grab) )
                // look at backboard
                .turnTo(0)
                // drive to it
                .lineToX(24.00)
                // raise lift and align with backboard
                .stopAndAdd( $par(payload, (c) -> new Action[]{
                        c.pixelArm.moveLift(4.0),
                        c.highLevel.alignWithBackboard()
                }))
                // place pixel
                .stopAndAdd( $(payload.pixelArm, PixelArm::placeOnBoard) )
                .build();

        waitForStart();

        Actions.runBlocking(grabAndPlacePurplePixel);
    }
}