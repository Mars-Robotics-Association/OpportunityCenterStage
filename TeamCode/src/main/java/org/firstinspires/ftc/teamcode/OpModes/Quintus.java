package org.firstinspires.ftc.teamcode.OpModes;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.firstinspires.ftc.teamcode.Payload.Camera;
import org.firstinspires.ftc.teamcode.Payload.GameState;
import org.firstinspires.ftc.teamcode.Payload.GameState.ParkSpot;
import org.firstinspires.ftc.teamcode.Payload.GameState.SignalState;
import org.firstinspires.ftc.teamcode.Payload.GameState.TeamColor;
import org.firstinspires.ftc.teamcode.Payload.Payload;
import org.firstinspires.ftc.teamcode.RoadRunner.MecanumDrive;

/*
Robot class for Quintus
This class will be called by opmodes and contains the payload, drive, and references the gamestate
It will contain all common functionality of the robot including navigation, payload automation, and more
 */
public class Quintus
{
    public final Payload payload;
    private GameState gameState;
    public final MecanumDrive drive;

    public Quintus(GameState gameState, HardwareMap hardwareMap, Pose2d startingPos){
        this.gameState = gameState;
        drive = new MecanumDrive(hardwareMap, startingPos);
        payload = new Payload(hardwareMap, drive);
    }


//---------------------------------------------------------
    //Common autonomous functions
    //TODO:

    public static int colorVar = -1; //if red, y and rotation variables are negative. If blue, they are positive
    public void start(){
        if (gameState.teamColor == TeamColor.BLUE){
            colorVar = 1;
        }
        else if (gameState.teamColor == TeamColor.RED){
            colorVar = -1;
        }
    }

    //Detect position of team prop (opencv or queen team prop)
    public @Nullable SignalState doCameraScan() {
        Camera.SearchRegion mostLikely = Camera.SearchRegion.RIGHT;

        mostLikely.coverage = Math.max(mostLikely.coverage, 2);

        for (Camera.SearchRegion region : Camera.SearchRegion.values())
            if(region.coverage > mostLikely.coverage)
                mostLikely = region;

        switch (mostLikely){
            case LEFT:
                gameState.signalState = SignalState.LEFT;break;
            case MIDDLE:
                gameState.signalState = SignalState.MIDDLE;break;
            case RIGHT: default:
                gameState.signalState = SignalState.RIGHT;break;
        }

        return gameState.signalState;
    }

    //Place purple pixel next to team prop and get into position for yellow pixel placement
    public void placePurpPix(GameState myGameState) {
        gameState = myGameState;
        if (gameState.parkSpot == ParkSpot.NEAR) { //robot starts in position nearest to backboard
            if (gameState.teamColor == TeamColor.BLUE) { //left
                switch (gameState.signalState) {
                    case LEFT://line near backboard
                        Actions.runBlocking(drive.actionBuilder(drive.pose)
                                .lineToY(33)
                                .turnTo(0)
                                .build());
                        break;
                    case MIDDLE://mid line
                        Actions.runBlocking(drive.actionBuilder(drive.pose)
                                .lineToY(36)
                                .build());
                        break;
                    case RIGHT://far line
                        Actions.runBlocking(drive.actionBuilder(drive.pose)
                                .lineToY(33)
                                .turnTo(Math.toRadians(-180))
                                .build());
                        break;
                }
            } else if (gameState.teamColor == TeamColor.RED) { //left
                switch (gameState.signalState) {
                    case LEFT://far line  -- yay --
                        Actions.runBlocking(drive.actionBuilder(drive.pose)
                                .lineToY(-33)
                                .turnTo(Math.toRadians(180))
                                .build());
                        break;
                    case MIDDLE://mid line  -- yay --
                        Actions.runBlocking(drive.actionBuilder(drive.pose)
                                .lineToY(-36)
                                .build());
                        break;
                    case RIGHT://line near backboard
                        Actions.runBlocking(drive.actionBuilder(drive.pose)
                                .lineToY(-33)
                                .turnTo(0)
                                .build());
                        break;
                }
            }
            //TODO: shorter than storage angle, make wait fctn
            payload.pixelArm.wrist.toGroundAngle();
            payload.pixelArm.gripperB.open(); //place pixel
            payload.pixelArm.wrist.toStorageAngle();
            Actions.runBlocking(drive.actionBuilder(drive.pose)
                    .turnTo(Math.toRadians(-90*colorVar))
                    .lineToYConstantHeading(50*colorVar) //sets reversed to true
                    .setReversed(false)
                    .splineTo(new Vector2d(25, 45 * colorVar), Math.toRadians(0)) //turn towards back
                    .build());

        } else if (gameState.parkSpot == ParkSpot.FAR) { //robot starts in far position
            if (gameState.teamColor == TeamColor.BLUE) { //blue team
                switch (gameState.signalState) {
                    case LEFT://line near backboard
                        Actions.runBlocking(drive.actionBuilder(drive.pose)
                                .lineToY(33)
                                .turnTo(0)
                                .build());
                        break;
                    case MIDDLE://mid line
                        Actions.runBlocking(drive.actionBuilder(drive.pose)
                                .lineToY(36)
                                .build());
                        break;
                    case RIGHT://far line
                        Actions.runBlocking(drive.actionBuilder(drive.pose)
                                .lineToY(33)
                                .turnTo(Math.toRadians(180))
                                .build());
                        break;
                }
            } else if (gameState.teamColor == TeamColor.RED) { //red team
                switch (gameState.signalState) {
                    case RIGHT://far line
                        Actions.runBlocking(drive.actionBuilder(drive.pose)
                                .lineToY(-33)
                                .turnTo(Math.toRadians(180))
                                .build());
                        break;
                    case MIDDLE://mid line
                        Actions.runBlocking(drive.actionBuilder(drive.pose)
                                .lineToY(-36)
                                .build());
                        break;
                    case LEFT://line near backboard
                        Actions.runBlocking(drive.actionBuilder(drive.pose)
                                .lineToY(-33)
                                .turnTo(Math.toRadians(0))
                                .build());
                        break;
                }
            }

            payload.pixelArm.wrist.toGroundAngle();
            payload.pixelArm.gripperB.open(); //place pixel
            payload.pixelArm.wrist.toStorageAngle();
            Actions.runBlocking(drive.actionBuilder(drive.pose)
                    .turnTo(Math.toRadians(-90*colorVar))
                    .lineToYConstantHeading(60*colorVar) //sets reversed to true
                    .setReversed(false)
                    .splineTo(new Vector2d(-56, 36 * colorVar), Math.toRadians(-90 * colorVar)) //turn to back
                    .splineTo(new Vector2d(-24, 12 * colorVar), Math.toRadians(0)) //turn to towards back board
                    .splineTo(new Vector2d(30, 12 * colorVar), Math.toRadians(0)) //go under curtain
                    .build());
        }
    }



//Place yellow pixel in correct position
    public void placeYellowPix(){
        payload.pixelArm.lift.setLiftHeight(12); //raise lift
        switch(gameState.signalState){
                case LEFT:
                    if (gameState.teamColor == TeamColor.BLUE) { //blue team
                        Actions.runBlocking(drive.actionBuilder(drive.pose)
                                .splineTo(new Vector2d(54, 42), Math.toRadians(0)) // approach left backboard
                                .build());
                        payload.pixelArm.gripperA.open(); //place pixel
                        Actions.runBlocking(drive.actionBuilder(drive.pose)
                                .setReversed(true)
                                .splineTo(new Vector2d(40, 42), Math.toRadians(180)) // back up
                                .build());
                    }
                    else if (gameState.teamColor == TeamColor.RED) { //red team  -- yay --
                        Actions.runBlocking(drive.actionBuilder(drive.pose)
                                .splineTo(new Vector2d(54, -30), Math.toRadians(0)) // approach left backboard
                                .build());
                        payload.pixelArm.gripperA.open(); //place pixel
                        Actions.runBlocking(drive.actionBuilder(drive.pose)
                                .setReversed(true)
                                .splineTo(new Vector2d(40, -30), Math.toRadians(180)) // back up
                                .build());
                    }
                    break;

            case MIDDLE:
                if (gameState.teamColor == TeamColor.BLUE) { //blue team
                    Actions.runBlocking(drive.actionBuilder(drive.pose)
                            .splineTo(new Vector2d(54, 36), Math.toRadians(0)) // approach left backboard
                            .build());
                    payload.pixelArm.gripperA.open(); //place pixel
                    Actions.runBlocking(drive.actionBuilder(drive.pose)
                            .setReversed(true)
                            .splineTo(new Vector2d(40, 36), Math.toRadians(180)) // back up
                            .build());
                }
                else if (gameState.teamColor == TeamColor.RED) { //red team -- yay --
                    Actions.runBlocking(drive.actionBuilder(drive.pose)
                            .splineTo(new Vector2d(54, -36), Math.toRadians(0)) // approach left backboard
                            .build());
                    payload.pixelArm.wrist.toBoardAngle();
                    payload.pixelArm.gripperA.open(); //place pixel
                    Actions.runBlocking(drive.actionBuilder(drive.pose)
                            .setReversed(true)
                            .splineTo(new Vector2d(40, -36), Math.toRadians(180)) // back up
                            .build());
                }
                break;
            case RIGHT:
                if (gameState.teamColor == TeamColor.BLUE) { //blue team
                    Actions.runBlocking(drive.actionBuilder(drive.pose)
                            .splineTo(new Vector2d(54, 29), Math.toRadians(0)) // approach left backboard
                            .build());
                    //wrist&lift set up
                    payload.pixelArm.gripperA.open(); //place pixel
                    Actions.runBlocking(drive.actionBuilder(drive.pose)
                            .setReversed(true)
                            .splineTo(new Vector2d(40, 29), Math.toRadians(180)) // back up
                            .build());
                }
                else if (gameState.teamColor == TeamColor.RED) { //red team
                    Actions.runBlocking(drive.actionBuilder(drive.pose)
                            .splineTo(new Vector2d(54, -42), Math.toRadians(0)) // approach left backboard
                            .build());
                    payload.pixelArm.gripperA.open(); //place pixel
                    Actions.runBlocking(drive.actionBuilder(drive.pose)
                            .setReversed(true)
                            .splineTo(new Vector2d(40, -42), Math.toRadians(180)) // back up
                            .build());
                }
                break;
        }
        //payload.pixelArm.lift.setLiftHeight(0); //lower lift
    }

//Park (near → corner; far → middle)
    public void autoPark(){
        payload.pixelArm.lift.setLiftHeight(0); //lower lift
        if (gameState.parkSpot == ParkSpot.NEAR){
           Actions.runBlocking(drive.actionBuilder(drive.pose)
                .splineTo(new Vector2d(48, 60*colorVar), Math.toRadians(0)) // approach left backboard
                .build());
        }
        else if (gameState.parkSpot == ParkSpot.FAR){
            Actions.runBlocking(drive.actionBuilder(drive.pose)
                .splineTo(new Vector2d(54, 12*colorVar), Math.toRadians(0)) // approach left backboard
                .build());
        }
    }
//--------------------------------------------------------------------------------------

    //Positional util functions
    private Pose2d computeActualPosition(Pose2d readingSample){
        Pose2d robotPose = drive.pose;
        Vector2d position = robotPose.heading.times(readingSample.position);
        Rotation2d heading = robotPose.heading.times(readingSample.heading);

        return new Pose2d(position, heading);
    }

    public Pose2d getBackboardTagPose(){
        int id = 0;

        switch (gameState.teamColor){
            case BLUE: id += 0;break;
            case RED: id += 3;break;
        }

        switch (gameState.signalState){
            case LEFT: id += 0;break;
            case RIGHT: id += 2;break;
            case MIDDLE: id += 1;break;
        }

        Pose2d rawPose = payload.camera.findTagWithID(id);

        assert rawPose != null;
        return computeActualPosition(rawPose);
    }

    //Methods for moving to known locations
    public Action alignWithBackboard(){
        // get pose of tag in global-pose
        Pose2d tagPose = getBackboardTagPose();

        // move back a bit
        Pose2d targetPose = new Pose2d(tagPose.position.plus(new Vector2d(-6, 0)), drive.pose.heading);

        // go there as smooth as Rick Astley
        return drive.actionBuilder(drive.pose)
                .setTangent(drive.pose.heading)
                .splineToLinearHeading(targetPose, targetPose.heading)
                .build();
    }
}
