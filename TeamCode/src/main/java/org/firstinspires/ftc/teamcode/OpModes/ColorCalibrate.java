package org.firstinspires.ftc.teamcode.OpModes;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Payload.GameState;
import org.firstinspires.ftc.teamcode.Payload.Camera;

@Autonomous
@Config
public class ColorCalibrate extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        GameState gameState = new GameState();
        Quintus bot = new Quintus(gameState, this.hardwareMap, new Pose2d(12, -63, Math.toRadians(90)));

        waitFor(5); //to avoid team prop scan issues
        waitForStart();
        bot.setLinearOpMode(this);

        gameState.signalState = bot.doCameraScan();

        while(this.opModeIsActive()) {
            double newThreshold = Camera.SearchRegion.LEFT.coverage * 1.2;
            telemetry.addData("leftColor = ", newThreshold);
            updateTelemetry(telemetry);
        }
    }
    public void waitFor(double timer){
        sleep((long)(timer*1e3));
    }
}
