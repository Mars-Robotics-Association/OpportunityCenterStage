package org.firstinspires.ftc.teamcode.Payload;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.teamcode.RoadRunner.MecanumDrive;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

@SuppressWarnings("unused")
public final class HighLevel {
    private final Payload payload;
    private final GameState gameState;
    private final MecanumDrive drive;

    HighLevel(Payload payload){
        this.payload = payload;
        gameState = payload.gameState;
        drive = payload.drive;
    }

    //---------------------------------------------------------
    //Common autonomous functions
    //TODO:
    //Detect position of team prop (opencv or queen team prop)
    /*public GameState.SignalState detectPropPos(){
        payload.camera.waitForNextScan();

        Camera.SearchRegion positiveRegion = null;

        for (Camera.SearchRegion region :
                Camera.SearchRegions.getAllRegions()) {
            if(!region.foundProp)continue;
            positiveRegion = region;
            break;
        }

        if (Camera.SearchRegions.LEFT.equals(positiveRegion)) {
            return GameState.SignalState.LEFT;
        } else if (Camera.SearchRegions.MIDDLE.equals(positiveRegion)) {
            return GameState.SignalState.MIDDLE;
        } else if (Camera.SearchRegions.RIGHT.equals(positiveRegion)) {
            return GameState.SignalState.RIGHT;
        }else {
            RobotLog.ee("HighLevel#detectPropPos", "Camera failed to detect a prop. Try tuning Search Regions and their thresholds");
            return null;
        }
    }*/

    enum PropPosition{
        BLUE_NEAR_LEFT(16.5, 33, 0),
        BLUE_NEAR_MIDDLE(11.5, 33.3, 270),
        BLUE_NEAR_RIGHT(8.5, 33, 180),

        BLUE_FAR_LEFT(16.5-52, 33, 0),
        BLUE_FAR_MIDDLE(11.5-52, 33.3, 270),
        BLUE_FAR_RIGHT(8.5-52, 33, 180),

        RED_NEAR_LEFT(BLUE_NEAR_RIGHT),
        RED_NEAR_MIDDLE(BLUE_NEAR_MIDDLE),
        RED_NEAR_RIGHT(BLUE_NEAR_LEFT),

        RED_FAR_LEFT(BLUE_FAR_RIGHT),
        RED_FAR_MIDDLE(BLUE_FAR_MIDDLE),
        RED_FAR_RIGHT(BLUE_FAR_LEFT);

        PropPosition propPositionFromGameState(GameState gameState){
            PropPosition[] values = PropPosition.values();

            int idx = (gameState.teamColor == GameState.TeamColor.RED) ? 6 : 0;
            idx += (gameState.parkSpot == GameState.ParkSpot.FAR) ? 3 : 0;
            idx += gameState.signalState.ordinal();
            return values[idx];
        }

        private final double x;
        private final double y;
        private final double heading;

        PropPosition(double x, double y, double heading){
            this.x = x;
            this.y = y;
            this.heading = heading;
        }

        PropPosition(PropPosition similarTo){
            this.x = similarTo.x;
            this.y = -similarTo.y;
            if(similarTo.heading == 270)
                this.heading = 90;
            else
                this.heading = similarTo.heading;
        }
    }

    //Place purple pixel next to team prop
    public void placePurplePixel(boolean useGate){
        /* take: the position foundProp in detectPropPos
        - have code (case) for positions
        drive.pos(); //drive to pos
        2. place pixel :)
        3. move away from lines (case) (back towards the wall or go through towards mid)
         */

        //drive.pos();
        payload.pixelArm.gripperB.open(); //place pixel



//        if (parkPos==0 && color==0) { //0==near wall and red 1==mid and blue
//            //drive.pos(); //back up
//            //drive.pos(); //drive right to corner
//        }
//        else if (parkPos==0 && color==1){
//            //drive.pos(); //back up
//            //drive.pos(); //drive left to corner
//        }
//        else if (parkPos==1 && color==0) {
//            //drive.pos(); //back up
//            //drive.pos(); //drive left
//            //drive.pos(); //drive forward
//            //drive.pos(); //drive right towards corner
//        }
//        else if (parkPos==1 && color==1){
//            //drive.pos(); //back up
//            //drive.pos(); //drive right
//            //drive.pos(); //drive forward
//            //drive.pos(); //drive left through curtain
//        }


    }
    //Place yellow pixel in correct position
    public void placeYellowPix(int propPos){
        payload.pixelArm.lift.setHeight(8); //raise lift, TODO: find inches value
        // TODO: this doesn't work right now, but Santi will fix it very soon :)
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
                .build();
    }
}
