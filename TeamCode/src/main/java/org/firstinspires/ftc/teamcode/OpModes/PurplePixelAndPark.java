package org.firstinspires.ftc.teamcode.OpModes;

import static java.lang.Math.toRadians;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Payload.Payload;
import org.firstinspires.ftc.teamcode.RoadRunner.MecanumDrive;

import java.util.function.Function;

@SuppressWarnings("unused")
@Autonomous
public class PurplePixelAndPark extends LinearOpMode{

    private MecanumDrive drive;
    private Payload payload;

    private Action $(Function<Payload, Action> operation){
        if(payload == null)return telemetryPacket -> false;
        return operation.apply(payload);
    }

    private TrajectoryActionBuilder path(){return drive.actionBuilder(drive.pose);}

    public void runOpMode() {
        Telemetry telemetry = new MultipleTelemetry(super.telemetry, FtcDashboard.getInstance().getTelemetry());

        payload = new Payload(hardwareMap, drive, true);

        Payload.GameState gameState = payload.gameState;

        boolean isBlueTeam = gameState.teamColor == Payload.GameState.TeamColor.BLUE;
        double flipY = gameState.teamColor.flipY;

        double startHeading = isBlueTeam ? toRadians(270) : toRadians(90);

        drive = new MecanumDrive(hardwareMap,
                new Pose2d(
                        gameState.startSlot.startPosX,
                        60.00 * flipY,
                        startHeading
                        ));

        double pixelAngle = 0;
        switch (gameState.signalState){
            case LEFT  : pixelAngle = startHeading + toRadians(90);
            break;
            case RIGHT : pixelAngle = startHeading - toRadians(90);
            break;
            case MIDDLE: pixelAngle = startHeading;
        }

        Action grabAndPlacePurplePixel = drive.actionBuilder(drive.pose)
                // drive up to Box of Signals
                .lineToY(36.00 * flipY)
                // look at correct pixel
                .turnTo(pixelAngle)
                // grab said pixel
                .stopAndAdd( $(Payload::grabPixel) )
                // look at backboard
                .turnTo(toRadians(0))
                // drive to it
                .lineToX(24.00)
                // raise lift and align with backboard
                .stopAndAdd(new ParallelAction(
                        $(c -> c.raiseLift(6.0)),
                        $(Payload::alignWithBackboard)
                ))
                // place pixel
                .stopAndAdd( $(Payload::placeOnBoard) )
                .build();

        waitForStart();

        Actions.runBlocking(grabAndPlacePurplePixel);
    }
}