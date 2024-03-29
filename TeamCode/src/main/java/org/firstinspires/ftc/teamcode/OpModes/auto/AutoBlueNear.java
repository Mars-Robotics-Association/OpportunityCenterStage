package org.firstinspires.ftc.teamcode.OpModes.auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.OpModes.Quintus;
import org.firstinspires.ftc.teamcode.Payload.Camera;
import org.firstinspires.ftc.teamcode.Payload.GameState;

@Autonomous
public class AutoBlueNear extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        GameState gameState = new GameState();

        //using roadrunner
        gameState.signalState = GameState.SignalState.RIGHT; //sets default state until prop detection overwrites action
        gameState.teamColor = GameState.TeamColor.BLUE; //team red
        gameState.parkSpot = GameState.ParkSpot.NEAR; //auto starts near backboard
        Quintus bot = new Quintus(gameState, this, new Pose2d(12, 63, Math.toRadians(-90)));
        bot.setLinearOpMode(this);

        waitFor(5); //to avoid team prop scan issues
        bot.setColorThreshold(); //to update for changes in light / setting
        waitForStart();

        gameState.signalState = bot.doCameraScan();

        updateTelemetry(telemetry);

        if (this.opModeIsActive()) {
        //call functions from Quintus
            //waitFor(1);
            bot.payload.pixelArm.gripperA.close();
            bot.payload.pixelArm.gripperB.close();
            waitFor(1);
            bot.payload.pixelArm.wrist.toPropAngle();
            bot.placePurpPix(gameState);
            bot.placeYellowPix();
            bot.autoPark();


        }
    }

    public void waitFor(double timer){
        sleep((long)(timer*1e3));
    }
}
