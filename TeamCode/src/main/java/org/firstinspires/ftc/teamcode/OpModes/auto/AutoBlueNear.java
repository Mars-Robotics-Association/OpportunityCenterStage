package org.firstinspires.ftc.teamcode.OpModes.auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.OpModes.Quintus;
import org.firstinspires.ftc.teamcode.Payload.GameState;

@Autonomous
@Config
public class AutoBlueNear extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        GameState gameState = new GameState();

        //using roadrunner
        gameState.signalState = GameState.SignalState.RIGHT; //sets default state until prop detection overwrites action
        gameState.teamColor = GameState.TeamColor.BLUE; //team red
        gameState.parkSpot = GameState.ParkSpot.NEAR; //auto starts near backboard
        Quintus bot = new Quintus(gameState, this.hardwareMap, new Pose2d(12, 63, Math.toRadians(-90)));

        waitFor(5); //to avoid team prop scan issues
        waitForStart();
        bot.start();

        gameState.signalState = bot.doCameraScan();


        if(this.opModeIsActive()) {
        //call functions from Quintus
            //bot.payload.pixelArm.wrist.toGroundAngle(); //starts with gripper up
            //waitFor(1);
            bot.payload.pixelArm.gripperA.close();
            bot.payload.pixelArm.gripperB.close();
            waitFor(1);
            bot.payload.pixelArm.wrist.toStorageAngle();

            bot.placePurpPix(gameState);
            bot.placeYellowPix();
            bot.autoPark();


            bot.payload.pixelArm.wrist.toStorageAngle();
        }
    }
    public void waitFor(double timer){
        sleep((long)(timer*1e3));
    }
}
