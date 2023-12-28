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

    //Common autonomous functions
    //TODO: psuedocode


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
