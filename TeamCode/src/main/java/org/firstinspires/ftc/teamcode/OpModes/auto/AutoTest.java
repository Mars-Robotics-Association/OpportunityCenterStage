package org.firstinspires.ftc.teamcode.OpModes.auto;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.OpModes.Quintus;
import org.firstinspires.ftc.teamcode.Payload.GameState;
import org.firstinspires.ftc.teamcode.Payload.Payload;
import org.firstinspires.ftc.teamcode.RoadRunner.MecanumDrive;

@SuppressWarnings("unused")
@Autonomous
public class AutoTest extends LinearOpMode {
    static double POWER = 0.6;

    DcMotorEx leftFront;
    DcMotorEx leftBack;
    DcMotorEx rightBack;
    DcMotorEx rightFront;

    @Override
    public void runOpMode() throws InterruptedException {
        {
            leftFront = hardwareMap.get(DcMotorEx.class, "FL");
            leftBack = hardwareMap.get(DcMotorEx.class, "BL");
            rightBack = hardwareMap.get(DcMotorEx.class, "BR");
            rightFront = hardwareMap.get(DcMotorEx.class, "FR");

            leftFront.setDirection(DcMotor.Direction.REVERSE);
            leftBack.setDirection(DcMotor.Direction.REVERSE);
            rightBack.setDirection(DcMotor.Direction.FORWARD);
            rightFront.setDirection(DcMotor.Direction.FORWARD);

            leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }

        Payload payload = new Payload(hardwareMap, null);
        payload.pixelArm.wrist.toStorageAngle();


        //using roadrunner
        GameState gameState = new GameState();
        gameState.signalState = GameState.SignalState.MIDDLE; //prop on middle line
        gameState.teamColor = GameState.TeamColor.RED; //team red
        gameState.parkSpot = GameState.ParkSpot.NEAR; //auto starts near backboard
        Quintus bot = new Quintus(new GameState(), this.hardwareMap, new Pose2d(12,-65,90));
        MecanumDrive drive = bot.drive;

        waitForStart();


        /*Actions.runBlocking(
                drive.actionBuilder(drive.pose)
                        .splineTo(new Vector2d(48, 0), Math.toRadians(0))
                        .build());
                // may need to adjust tics per inch*/

        //call function from Quintus
        bot.placePurpPix();


        payload.pixelArm.wrist.toStorageAngle();
    }
}
