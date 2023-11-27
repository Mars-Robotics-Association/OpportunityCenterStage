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
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Payload.Payload;
import org.firstinspires.ftc.teamcode.Payload.Payload.*;
import org.firstinspires.ftc.teamcode.Payload.PixelArm;
import org.firstinspires.ftc.teamcode.RoadRunner.MecanumDrive;

import java.util.Iterator;

import kotlin.jvm.functions.Function0;

@SuppressWarnings("unused")
@Autonomous
public class PurplePixelAndPark extends LinearOpMode{

    interface VoidFunction{void invoke();}

    private MecanumDrive drive;

    private Action conv(VoidFunction operation){
        operation.invoke();
        return telemetryPacket -> false;
    }

    private Action conv(VoidFunction operation, Function0<Boolean> waitUntil){
        operation.invoke();
        return telemetryPacket -> waitUntil.invoke();
    }

    private TrajectoryActionBuilder path(){return drive.actionBuilder(drive.pose);}

    public void runOpMode() {
        Telemetry telemetry = new MultipleTelemetry(super.telemetry, FtcDashboard.getInstance().getTelemetry());

        Payload payload = new Payload(hardwareMap, drive, false);

        GameState gameState = payload.gameState;

        Iterator<ColorSensor> colorIterator = hardwareMap.colorSensor.iterator();

        if(colorIterator.hasNext())
            gameState = GameState.fromSensor(colorIterator.next());
        else {
            gameState.startSlot = GameState.StartSlot.CLOSER;
            gameState.teamColor = GameState.TeamColor.BLUE;
            gameState.signalState = GameState.SignalState.MIDDLE;
        }

        boolean isBlueTeam = gameState.teamColor == GameState.TeamColor.BLUE;
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

        PixelArm arm = payload.pixelArm;

        Action grabAndPlacePurplePixel = drive.actionBuilder(drive.pose)
                // drive up to Box of Signals
                .lineToY(36.00 * flipY)
                .lineToX(drive.pose.position.x - 2)
                // look at correct pixel
                .turnTo(pixelAngle)
                // grab said pixel
                .stopAndAdd( conv(arm.gripperB::close) )
                // look at backboard
                .turnTo(toRadians(0))
                // drive to it
                .lineToX(24.00)
                // raise lift and align with backboard
                .stopAndAdd(new ParallelAction(
                        conv(() -> arm.lift.gotoHeight(6.0), arm.lift::isBusy),
                        payload.highLevel.alignWithBackboard()
                ))
                // place pixel
                .stopAndAdd(conv(payload.pixelArm.gripperB::open))
                .build();

        waitForStart();

        Actions.runBlocking(grabAndPlacePurplePixel);
    }
}