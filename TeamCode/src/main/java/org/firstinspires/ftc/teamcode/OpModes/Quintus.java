package org.firstinspires.ftc.teamcode.OpModes;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Payload.GameState;
import org.firstinspires.ftc.teamcode.Payload.GameState.ParkSpot;
import org.firstinspires.ftc.teamcode.Payload.GameState.TeamColor;
import org.firstinspires.ftc.teamcode.Payload.GameState.SignalState;
import org.firstinspires.ftc.teamcode.Payload.Payload;
import org.firstinspires.ftc.teamcode.RoadRunner.MecanumDrive;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;


/*
Robot class for Quintus
This class will be called by opmodes and contains the payload, drive, and references the gamestate
It will contain all common functionality of the robot including navigation, payload automation, and more
 */
public class Quintus
{
    private final Payload payload;
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

    public static int colorVar = 1; //if red, y and rotation variables are negative. If blue, they are positive

    public void start(){
        if (gameState.teamColor == TeamColor.BLUE){
            colorVar = 1;
        }
        else if (gameState.teamColor == TeamColor.RED){
            colorVar = -1;
        }
    }



    //Detect position of team prop (opencv or queen team prop)
    public int detectPropPos(){
        /* takes: camera image, if blue/red
        1. take the camera image,
        2. determine which of three boxes has highest percent of red/blue pixels in photo,
        3. output which position the team prop is in
        */
        return 2;
    }

    //Place purple pixel next to team prop and get into position for yellow pixel placement
    public void placePurpPix() {
        if (gameState.parkSpot == ParkSpot.NEAR) { //robot starts in position nearest to backboard
            if (gameState.teamColor == TeamColor.BLUE) { //left
                switch (gameState.signalState) {
                    case RIGHT://line near backboard
                        Actions.runBlocking(drive.actionBuilder(drive.pose)
                                .splineTo(new Vector2d(14, 33), Math.toRadians(0)) //approach line
                                .build());
                        break;
                    case MIDDLE://mid line
                        Actions.runBlocking(drive.actionBuilder(drive.pose)
                                .splineTo(new Vector2d(12, 30), Math.toRadians(-90)) //approach line
                                .build());
                        break;
                    case LEFT://far line
                        Actions.runBlocking(drive.actionBuilder(drive.pose)
                                .splineTo(new Vector2d(10, 33), Math.toRadians(-180)) //approach line
                                .build());
                        break;
                }
            } else if (gameState.teamColor == TeamColor.RED) { //left
                switch (gameState.signalState) {
                    case RIGHT://far line
                        Actions.runBlocking(drive.actionBuilder(drive.pose)
                                .splineTo(new Vector2d(10, -33), Math.toRadians(180)) //approach line
                                .build());
                        break;
                    case MIDDLE://mid line
                        Actions.runBlocking(drive.actionBuilder(drive.pose)
                                .splineTo(new Vector2d(12, -30), Math.toRadians(-90)) //approach line
                                .build());
                        break;
                    case LEFT://line near backboard
                        Actions.runBlocking(drive.actionBuilder(drive.pose)
                                .splineTo(new Vector2d(14, -33), Math.toRadians(0)) //approach line
                                .build());
                        break;
                }
            }
            payload.pixelArm.gripperB.open(); //place pixel
            Actions.runBlocking(drive.actionBuilder(drive.pose)
                    .setReversed(true)
                    .splineTo(new Vector2d(12, 50 * colorVar), Math.toRadians(90 * colorVar)) //back up
                    .setReversed(false)
                    .splineTo(new Vector2d(25, 45 * colorVar), Math.toRadians(0)) //turn towards back
                    .build());

        } else if (gameState.parkSpot == ParkSpot.FAR) { //robot starts in far position
            if (gameState.teamColor == TeamColor.BLUE) { //blue team
                switch (gameState.signalState) {
                    case RIGHT://line near backboard
                        Actions.runBlocking(drive.actionBuilder(drive.pose)
                                .splineTo(new Vector2d(-34, 33), Math.toRadians(0)) //approach line
                                .build());
                        break;
                    case MIDDLE://mid line
                        Actions.runBlocking(drive.actionBuilder(drive.pose)
                                .splineTo(new Vector2d(-36, 30), Math.toRadians(-90)) //approach line
                                .build());
                        break;
                    case LEFT://far line
                        Actions.runBlocking(drive.actionBuilder(drive.pose)
                                .splineTo(new Vector2d(-38, 33), Math.toRadians(-180)) //approach line
                                .build());
                        break;
                }
            } else if (gameState.teamColor == TeamColor.RED) { //red team
                switch (gameState.signalState) {
                    case RIGHT://far line
                        Actions.runBlocking(drive.actionBuilder(drive.pose)
                                .splineTo(new Vector2d(-38, -33), Math.toRadians(180)) //approach line
                                .build());
                        break;
                    case MIDDLE://mid line
                        Actions.runBlocking(drive.actionBuilder(drive.pose)
                                .splineTo(new Vector2d(-36, -30), Math.toRadians(90)) //approach line
                                .build());
                        break;
                    case LEFT://line near backboard
                        Actions.runBlocking(drive.actionBuilder(drive.pose)
                                .splineTo(new Vector2d(-34, -33), Math.toRadians(-0)) //approach line
                                .build());
                        break;
                }
            }

            payload.pixelArm.gripperB.open(); //place pixel
            Actions.runBlocking(drive.actionBuilder(drive.pose)
                    .setReversed(true)
                    .splineTo(new Vector2d(-36, 50 * colorVar), Math.toRadians(90 * colorVar)) //back away
                    .setReversed(false)
                    .splineTo(new Vector2d(-49, 45 * colorVar), Math.toRadians(-180 * colorVar)) //turn to back
                    .splineTo(new Vector2d(-57, 30 * colorVar), Math.toRadians(-90 * colorVar)) //go around lines
                    .splineTo(new Vector2d(-36, 12 * colorVar), Math.toRadians(0)) //turn to towards back board
                    .splineTo(new Vector2d(30, 12 * colorVar), Math.toRadians(0)) //go under curtain
                    .build());
        }
    }



//Place yellow pixel in correct position
    public void placeYellowPix(){
        payload.pixelArm.lift.setHeight(8); //raise lift, TODO find inches value
        switch(gameState.signalState){
                case LEFT:
                    if (gameState.teamColor == TeamColor.BLUE) { //blue team
                        Actions.runBlocking(drive.actionBuilder(drive.pose)
                                .splineTo(new Vector2d(50, 42), Math.toRadians(0)) // approach left backboard
                                .build());
                        payload.pixelArm.gripperA.open(); //place pixel
                        Actions.runBlocking(drive.actionBuilder(drive.pose)
                                .setReversed(true)
                                .splineTo(new Vector2d(40, 42), Math.toRadians(180)) // back up
                                .setReversed(false)
                                .build());
                    }
                    else if (gameState.teamColor == TeamColor.RED) { //red team
                        Actions.runBlocking(drive.actionBuilder(drive.pose)
                                .splineTo(new Vector2d(50, -29), Math.toRadians(0)) // approach left backboard
                                .build());
                        payload.pixelArm.gripperA.open(); //place pixel
                        Actions.runBlocking(drive.actionBuilder(drive.pose)
                                .setReversed(true)
                                .splineTo(new Vector2d(40, -29), Math.toRadians(180)) // back up
                                .setReversed(false)
                                .build());
                    }
                    break;

            case MIDDLE:
                if (gameState.teamColor == TeamColor.BLUE) { //blue team
                    Actions.runBlocking(drive.actionBuilder(drive.pose)
                            .splineTo(new Vector2d(50, 36), Math.toRadians(0)) // approach left backboard
                            .build());
                    payload.pixelArm.gripperA.open(); //place pixel
                    Actions.runBlocking(drive.actionBuilder(drive.pose)
                            .setReversed(true)
                            .splineTo(new Vector2d(40, 36), Math.toRadians(180)) // back up
                            .setReversed(false)
                            .build());
                }
                else if (gameState.teamColor == TeamColor.RED) { //red team
                    Actions.runBlocking(drive.actionBuilder(drive.pose)
                            .splineTo(new Vector2d(50, -42), Math.toRadians(0)) // approach left backboard
                            .build());
                    payload.pixelArm.gripperA.open(); //place pixel
                    Actions.runBlocking(drive.actionBuilder(drive.pose)
                            .setReversed(true)
                            .splineTo(new Vector2d(40, -42), Math.toRadians(180)) // back up
                            .setReversed(false)
                            .build());
                }
                break;
            case RIGHT:
                if (gameState.teamColor == TeamColor.BLUE) { //blue team
                    Actions.runBlocking(drive.actionBuilder(drive.pose)
                            .splineTo(new Vector2d(50, 29), Math.toRadians(0)) // approach left backboard
                            .build());
                    //wrist&lift set up
                    payload.pixelArm.gripperA.open(); //place pixel
                    Actions.runBlocking(drive.actionBuilder(drive.pose)
                            .setReversed(true)
                            .splineTo(new Vector2d(40, 29), Math.toRadians(180)) // back up
                            .setReversed(false)
                            .build());
                }
                else if (gameState.teamColor == TeamColor.RED) { //red team
                    Actions.runBlocking(drive.actionBuilder(drive.pose)
                            .splineTo(new Vector2d(50, -42), Math.toRadians(0)) // approach left backboard
                            .build());
                    payload.pixelArm.gripperA.open(); //place pixel
                    Actions.runBlocking(drive.actionBuilder(drive.pose)
                            .setReversed(true)
                            .splineTo(new Vector2d(40, -42), Math.toRadians(180)) // back up
                            .setReversed(false)
                            .build());
                }
                break;
        }
        payload.pixelArm.gripperA.open(); //open left? gripper (gripper with yellow pixel)
        //drive.pos();  //drive backwards away from board TODO find pos
        //lower lift
        payload.pixelArm.lift.setHeight(0);
    }

//Park (near → corner; far → middle)
    public void autoPark(){
        if (gameState.parkSpot == ParkSpot.NEAR){
           Actions.runBlocking(drive.actionBuilder(drive.pose)
                .splineTo(new Vector2d(54, -60*colorVar), Math.toRadians(0)) // approach left backboard
                .build());
        }
        else if (gameState.parkSpot == ParkSpot.FAR){
            Actions.runBlocking(drive.actionBuilder(drive.pose)
                .splineTo(new Vector2d(54, -12*colorVar), Math.toRadians(0)) // approach left backboard
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

        //AprilTagDetection tag = payload.camera.findTagWithID(id);
        //Pose2d rawPose = new Pose2d(new Vector2d(tag.rawPose.x,tag.rawPose.y), new Rotation2d(0,0));

        //assert rawPose != null;
        return computeActualPosition(new Pose2d(0,0,0));
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
