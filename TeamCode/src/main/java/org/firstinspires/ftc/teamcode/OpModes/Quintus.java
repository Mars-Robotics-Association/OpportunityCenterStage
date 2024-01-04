package org.firstinspires.ftc.teamcode.OpModes;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Payload.GameState;
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
    private final GameState gameState;
    public final MecanumDrive drive;

    Quintus(GameState gameState, HardwareMap hardwareMap){
        this.gameState = gameState;
        drive = new MecanumDrive(hardwareMap, new Pose2d(12, 60, Math.toRadians(270)));
        payload = new Payload(hardwareMap, drive);
    }


//---------------------------------------------------------
    //Common autonomous functions
    //TODO:
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
    public void placePurpPix(int parkPos, int color,int propPos){
        //parkPos = near/far
        //color = reb/blue
        //propPos: used for cases (how?)

        if (parkPos==0 && color==0) { //red near
            switch(propPos) {
                case 0://line near backboard
                    Actions.runBlocking(drive.actionBuilder(drive.pose)
                            .splineTo(new Vector2d(12, -35), Math.toRadians(0))
                            .build());
                    break;
                case 1://mid line
                    Actions.runBlocking(drive.actionBuilder(drive.pose)
                            .splineTo(new Vector2d(12, -30), Math.toRadians(90))
                            .build());
                    break;
                case 2://far line
                    Actions.runBlocking(drive.actionBuilder(drive.pose)
                            .splineTo(new Vector2d(12, -35), Math.toRadians(180))
                            .build());
                    break;
                }
            payload.pixelArm.gripperB.open(); //place pixel
            Actions.runBlocking(drive.actionBuilder(drive.pose) //back away
                    .splineTo(new Vector2d(12, -60), Math.toRadians(90))
                    .splineTo(new Vector2d(12, -60), Math.toRadians(0))
                    .build());

        }
        else if (parkPos==0 && color==1){ //blue near
            switch(propPos) {
                case 0://line near backboard
                    Actions.runBlocking(drive.actionBuilder(drive.pose)
                            .splineTo(new Vector2d(12, 35), Math.toRadians(0))
                            .build());
                    break;
                case 1://mid line
                    Actions.runBlocking(drive.actionBuilder(drive.pose)
                            .splineTo(new Vector2d(12, 30), Math.toRadians(-90))
                            .build());
                    break;
                case 2://far line
                    Actions.runBlocking(drive.actionBuilder(drive.pose)
                            .splineTo(new Vector2d(12, 35), Math.toRadians(-180))
                            .build());
                    break;
                }
            payload.pixelArm.gripperB.open(); //place pixel
            Actions.runBlocking(drive.actionBuilder(drive.pose) //back away
                    .splineTo(new Vector2d(12, 60), Math.toRadians(-90))
                    .splineTo(new Vector2d(12, 60), Math.toRadians(0))
                    .build());
            }
        else if (parkPos==1 && color==0) { //red far
            //drive.pos(); //back up
            //drive.pos(); //drive left
            //drive.pos(); //drive forward
            //drive.pos(); //drive right towards corner
            switch(propPos) {
                case 0://line near backboard
                    Actions.runBlocking(drive.actionBuilder(drive.pose)
                            .splineTo(new Vector2d(-35, -35), Math.toRadians(0))
                            .build());
                    break;
                case 1://mid line
                    Actions.runBlocking(drive.actionBuilder(drive.pose)
                            .splineTo(new Vector2d(-35, -30), Math.toRadians(90))
                            .build());
                    break;
                case 2://far line
                    Actions.runBlocking(drive.actionBuilder(drive.pose)
                            .splineTo(new Vector2d(-35, -35), Math.toRadians(180))
                            .build());
                    break;
            }
            payload.pixelArm.gripperB.open(); //place pixel
            Actions.runBlocking(drive.actionBuilder(drive.pose)
                    .splineTo(new Vector2d(-35, -60), Math.toRadians(90)) //back away
                    .splineTo(new Vector2d(-35, -60), Math.toRadians(180)) //turn to back
                    .splineTo(new Vector2d(-57, -30), Math.toRadians(90)) //go around lines
                    .splineTo(new Vector2d(-35, -12), Math.toRadians(0)) //turn to towards back board
                    .splineTo(new Vector2d(32, -12), Math.toRadians(0)) //go under curtain
                    .build());
        }
        else if (parkPos==1 && color==1){ //blue far
            //drive.pos(); //back up
            //drive.pos(); //drive right
            //drive.pos(); //drive forward
            //drive.pos(); //drive left through curtain
            switch(propPos) {
                case 0://line near backboard
                    Actions.runBlocking(drive.actionBuilder(drive.pose)
                            .splineTo(new Vector2d(-35, 35), Math.toRadians(0))
                            .build());
                    break;
                case 1://mid line
                    Actions.runBlocking(drive.actionBuilder(drive.pose)
                            .splineTo(new Vector2d(-35, 30), Math.toRadians(-90))
                            .build());
                    break;
                case 2://far line
                    Actions.runBlocking(drive.actionBuilder(drive.pose)
                            .splineTo(new Vector2d(-35, 35), Math.toRadians(-180))
                            .build());
                    break;
            }
            payload.pixelArm.gripperB.open(); //place pixel
            Actions.runBlocking(drive.actionBuilder(drive.pose)
                    .splineTo(new Vector2d(-35, 60), Math.toRadians(-90)) //back away
                    .splineTo(new Vector2d(-35, 60), Math.toRadians(-180)) //turn to back
                    .splineTo(new Vector2d(-57, 30), Math.toRadians(-90)) //go around lines
                    .splineTo(new Vector2d(-35, 12), Math.toRadians(0)) //turn to towards back board
                    .splineTo(new Vector2d(32, 12), Math.toRadians(0)) //go under curtain
                    .build());
        }

    }


    //Place yellow pixel in correct position
    public void placeYellowPix(int propPos){
        payload.pixelArm.lift.setHeight(8); //raise lift, TODO find inches value
        switch(propPos){
            case 0:
                //drive to pos 0
                break;
            case 1:
                //drive to pos 1
                break;
            case 2:
                //drive to pos 2
                break;
        }
        payload.pixelArm.gripperA.open(); //open left? gripper (gripper with yellow pixel)
        //drive.pos();  //drive backwards away from board TODO find pos
        //lower lift
        payload.pixelArm.lift.setHeight(0);
    }

    //Park (close → corner; far → middle)
    public void autoPark(int parkpos, int color){
        /* take: if park in corner or mid
        1. (case) drive to end of auto pos
         */
        if (parkpos==0 && color==0){
            //drive.pos(); //right
            //drive.pos(); //forward
        }
        else if (parkpos==0 && color==1){
            //drive.pos(); //left
            //drive.pos(); //forward
        }
        else if (parkpos==1 && color==0){
            //drive.pos(); //left
            //drive.pos(); //forward
        }
        else if (parkpos==1 && color==1) {
            //drive.pos(); //right
            //drive.pos(); //forward
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

        AprilTagDetection tag = payload.camera.findTagWithID(id);
        Pose2d rawPose = new Pose2d(new Vector2d(tag.rawPose.x,tag.rawPose.y), new Rotation2d(0,0));

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
                .build());
    }
}
