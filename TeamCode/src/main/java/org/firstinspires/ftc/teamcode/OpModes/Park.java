package org.firstinspires.ftc.teamcode.OpModes;

import static java.lang.Math.toRadians;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Payload.Payload;
import org.firstinspires.ftc.teamcode.RoadRunner.MecanumDrive;

import java.util.Iterator;
import java.util.function.Function;

@SuppressWarnings("unused")
@Autonomous
public class Park extends LinearOpMode{
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

        Iterator<ColorSensor> colorIterator = hardwareMap.colorSensor.iterator();

        if(colorIterator.hasNext())
            gameState = Payload.GameState.fromSensor(colorIterator.next());
        else {
                gameState.startSlot = Payload.GameState.StartSlot.CLOSER;
                gameState.teamColor = Payload.GameState.TeamColor.BLUE;
                gameState.signalState = Payload.GameState.SignalState.MIDDLE;
            }

        boolean isBlueTeam = gameState.teamColor == Payload.GameState.TeamColor.BLUE;
        double flipY = gameState.teamColor.flipY;

        double startHeading = isBlueTeam ? toRadians(270) : toRadians(90);

        drive = new MecanumDrive(hardwareMap,
                new Pose2d(
                        gameState.startSlot.startPosX,
                        60.00 * flipY,
                        startHeading
                        ));

        Action park = drive.actionBuilder(drive.pose)
                // look at backboard
                .turnTo(toRadians(0))
                // drive to it
                .lineToX(24.00)
                // park
                .build();

        waitForStart();

        Actions.runBlocking(park);
    }
}