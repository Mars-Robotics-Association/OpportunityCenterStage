package org.firstinspires.ftc.teamcode.OpModes.auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.OpModes.Quintus;
import org.firstinspires.ftc.teamcode.Payload.GameState;

@Autonomous
@Config
@SuppressWarnings("unused")

public class AutoBlueNear extends LinearOpMode {
    static double POWER = 0.6;

    public static GameState.SignalState POS = GameState.SignalState.MIDDLE;

    DcMotorEx leftFront;
    DcMotorEx leftBack;
    DcMotorEx rightBack;
    DcMotorEx rightFront;
    private Quintus bot;
    private GameState gameState;

    @Override
    public void runOpMode() throws InterruptedException {

        gameState = new GameState();

        //using roadrunner
        gameState.signalState = GameState.SignalState.MIDDLE; //prop on middle line
        gameState.teamColor = GameState.TeamColor.BLUE; //team red
        gameState.parkSpot = GameState.ParkSpot.NEAR; //auto starts near backboard
        bot = new Quintus(gameState, this.hardwareMap, new Pose2d(12, 65, Math.toRadians(-90)));



        waitForStart();
        bot.start();

        Actions.runBlocking(bot.waitForScan());
        gameState.signalState = bot.getScanResult(); //prop detections


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
        double startTime = this.time;
        while(this.time<startTime+timer){}
    }
}
