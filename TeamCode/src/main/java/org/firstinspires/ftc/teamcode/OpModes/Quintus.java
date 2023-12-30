package org.firstinspires.ftc.teamcode.OpModes;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Payload.GameState;
import org.firstinspires.ftc.teamcode.Payload.Payload;
import org.firstinspires.ftc.teamcode.RoadRunner.MecanumDrive;

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
    //Place purple pixel next to team prop
    public void placePurpPix(int parkPos, int color){
        /* take: the position found in detectPropPos
        - have code (case) for positions
        drive.pos(); //drive to pos
        2. place pixel :)
        3. move away from lines (case) (back towards the wall or go through towards mid)
         */

        //drive.pos();
        payload.pixelArm.gripperB.open(); //place pixel
        if (parkPos==0 && color==0) { //0==near wall and red 1==mid and blue
            //drive.pos(); //back up
            //drive.pos(); //drive right to corner
        }
        else if (parkPos==0 && color==1){
            //drive.pos(); //back up
            //drive.pos(); //drive left to corner
        }
        else if (parkPos==1 && color==0) {
            //drive.pos(); //back up
            //drive.pos(); //drive left
            //drive.pos(); //drive forward
            //drive.pos(); //drive right towards corner
        }
        else if (parkPos==1 && color==1){
            //drive.pos(); //back up
            //drive.pos(); //drive right
            //drive.pos(); //drive forward
            //drive.pos(); //drive left through curtain
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
