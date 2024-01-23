package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeBlueDark;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeBlueLight;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeRedDark;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeRedLight;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepAuto {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);
        /*GameState gameState = new GameState();
        gameState.signalState = GameState.SignalState.MIDDLE; //prop on middle line
        gameState.teamColor = GameState.TeamColor.RED; //team red
        gameState.parkSpot = GameState.ParkSpot.NEAR; //auto starts near backboard

        TODO: left for red side doesn't == left for blue side!
         */

        RoadRunnerBotEntity redNear = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .setColorScheme(new ColorSchemeRedLight())
                .build();
        RoadRunnerBotEntity redFar = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .setColorScheme(new ColorSchemeRedDark())
                .build();
        RoadRunnerBotEntity blueNear = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .setColorScheme(new ColorSchemeBlueLight())
                .build();
        RoadRunnerBotEntity blueFar = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .setColorScheme(new ColorSchemeBlueDark())
                .build();


    //prop in the middle
//        redNear.runAction(redNear.getDrive().actionBuilder(new Pose2d(12, -65, Math.toRadians(90))) //red near middle
//                .splineTo(new Vector2d(12, -30), Math.toRadians(90)) //place
//                .setReversed(true)
//                .splineTo(new Vector2d(12, -50), Math.toRadians(-90)) //back up
//                .setReversed(false)
//                .splineTo(new Vector2d(25, -45), Math.toRadians(0)) //turn
//                .splineTo(new Vector2d(50, -36), Math.toRadians(0)) //go to mid back board
//                .setReversed(true)
//                .splineTo(new Vector2d(40, -36), Math.toRadians(180)) //backup
//                .setReversed(false)
//                .splineTo(new Vector2d(54, -60), Math.toRadians(0)) //park in corner
//                .build());
//
//        redFar.runAction(redFar.getDrive().actionBuilder(new Pose2d(-36, -65, Math.toRadians(90))) //red far middle
//                .splineTo(new Vector2d(-36, -30), Math.toRadians(90)) //place
//                .setReversed(true)
//                .splineTo(new Vector2d(-36, -50), Math.toRadians(-90)) //back away
//                .setReversed(false)
//                .splineTo(new Vector2d(-49, -45), Math.toRadians(180)) //turn to back
//                .splineTo(new Vector2d(-57, -30), Math.toRadians(90)) //go around lines
//                .splineTo(new Vector2d(-36, -12), Math.toRadians(0)) //turn to towards back board
//                .splineTo(new Vector2d(30, -12), Math.toRadians(0)) //go under curtain
//                .splineTo(new Vector2d(50, -36), Math.toRadians(0)) //go to mid back board
//                .setReversed(true)
//                .splineTo(new Vector2d(40, -36), Math.toRadians(180)) //backup
//                .setReversed(false)
//                .splineTo(new Vector2d(54, -12), Math.toRadians(0)) //park in corner
//                .build());
//
//        blueNear.runAction(blueNear.getDrive().actionBuilder(new Pose2d(12, 65, Math.toRadians(-90))) //blue near middle
//                .splineTo(new Vector2d(12, 30), Math.toRadians(-90)) //place
//                .setReversed(true)
//                .splineTo(new Vector2d(12, 50), Math.toRadians(90)) //back up
//                .setReversed(false)
//                .splineTo(new Vector2d(25, 45), Math.toRadians(0)) //turn
//                .splineTo(new Vector2d(50, 36), Math.toRadians(0)) //go to mid back board
//                .setReversed(true)
//                .splineTo(new Vector2d(40, 36), Math.toRadians(180)) //backup
//                .setReversed(false)
//                .splineTo(new Vector2d(54, 60), Math.toRadians(0)) //park in corner
//                .build());
//
//        blueFar.runAction(blueFar.getDrive().actionBuilder(new Pose2d(-36, 65, Math.toRadians(-90))) //blue far middle
//                .splineTo(new Vector2d(-36, 30), Math.toRadians(-90)) //place
//                .setReversed(true)
//                .splineTo(new Vector2d(-36, 50), Math.toRadians(90)) //back away
//                .setReversed(false)
//                .splineTo(new Vector2d(-49, 45), Math.toRadians(-180)) //turn to back
//                .splineTo(new Vector2d(-57, 30), Math.toRadians(-90)) //go around lines
//                .splineTo(new Vector2d(-36, 12), Math.toRadians(0)) //turn to towards back board
//                .splineTo(new Vector2d(30, 12), Math.toRadians(0)) //go under curtain
//                .splineTo(new Vector2d(50, 36), Math.toRadians(0)) //go to mid back board
//                .setReversed(true)
//                .splineTo(new Vector2d(40, 36), Math.toRadians(180)) //backup
//                .setReversed(false)
//                .splineTo(new Vector2d(54, 12), Math.toRadians(0)) //park in corner
//                .build());


    //prop on left (far from backboard)
//        redNear.runAction(redNear.getDrive().actionBuilder(new Pose2d(12, -65, Math.toRadians(90))) //red near left
//                .splineTo(new Vector2d(10, -33), Math.toRadians(180)) //place
//                .setReversed(true)
//                .splineTo(new Vector2d(12, -50), Math.toRadians(-90)) //back up
//                .setReversed(false)
//                .splineTo(new Vector2d(25, -45), Math.toRadians(0)) //turn
//                .splineTo(new Vector2d(50, -29), Math.toRadians(0)) //go to left back board
//                .setReversed(true)
//                .splineTo(new Vector2d(40, -29), Math.toRadians(180)) //backup
//                .setReversed(false)
//                .splineTo(new Vector2d(54, -60), Math.toRadians(0)) //park in corner
//                .build());
//
//        redFar.runAction(redFar.getDrive().actionBuilder(new Pose2d(-36, -65, Math.toRadians(90))) //red far left
//                .splineTo(new Vector2d(-38, -33), Math.toRadians(180)) //place
//                .setReversed(true)
//                .splineTo(new Vector2d(-36, -50), Math.toRadians(-90)) //back away
//                .setReversed(false)
//                .splineTo(new Vector2d(-49, -45), Math.toRadians(180)) //turn to back
//                .splineTo(new Vector2d(-57, -30), Math.toRadians(90)) //go around lines
//                .splineTo(new Vector2d(-36, -12), Math.toRadians(0)) //turn to towards back board
//                .splineTo(new Vector2d(30, -12), Math.toRadians(0)) //go under curtain
//                .splineTo(new Vector2d(50, -29), Math.toRadians(0)) //go to left back board
//                .setReversed(true)
//                .splineTo(new Vector2d(40, -29), Math.toRadians(180)) //backup
//                .setReversed(false)
//                .splineTo(new Vector2d(54, -12), Math.toRadians(0)) //park in corner
//                .build());
//
//        blueNear.runAction(blueNear.getDrive().actionBuilder(new Pose2d(12, 65, Math.toRadians(-90))) //blue near left
//                .splineTo(new Vector2d(10, 33), Math.toRadians(-180)) //place
//                .setReversed(true)
//                .splineTo(new Vector2d(12, 50), Math.toRadians(90)) //back up
//                .setReversed(false)
//                .splineTo(new Vector2d(25, 45), Math.toRadians(0)) //turn
//                .splineTo(new Vector2d(50, 29), Math.toRadians(0)) //go to left back board
//                .setReversed(true)
//                .splineTo(new Vector2d(40, 29), Math.toRadians(180)) //backup
//                .setReversed(false)
//                .splineTo(new Vector2d(54, 60), Math.toRadians(0)) //park in corner
//                .build());
//
//        blueFar.runAction(blueFar.getDrive().actionBuilder(new Pose2d(-36, 65, Math.toRadians(-90))) //blue far left
//                .splineTo(new Vector2d(-38, 33), Math.toRadians(-180)) //place
//                .setReversed(true)
//                .splineTo(new Vector2d(-36, 50), Math.toRadians(90)) //back away
//                .setReversed(false)
//                .splineTo(new Vector2d(-49, 45), Math.toRadians(-180)) //turn to back
//                .splineTo(new Vector2d(-57, 36), Math.toRadians(-90)) //go around lines
//                .splineTo(new Vector2d(-36, 12), Math.toRadians(0)) //turn to towards back board
//                .splineTo(new Vector2d(30, 12), Math.toRadians(0)) //go under curtain
//                .splineTo(new Vector2d(50, 29), Math.toRadians(0)) //go to left back board
//                .setReversed(true)
//                .splineTo(new Vector2d(40, 29), Math.toRadians(180)) //backup
//                .setReversed(false)
//                .splineTo(new Vector2d(54, 12), Math.toRadians(0)) //park in corner
//                .build());

    //prop on right (close to backboard)
        redNear.runAction(redNear.getDrive().actionBuilder(new Pose2d(12, -65, Math.toRadians(90))) //red near right
                .splineTo(new Vector2d(14, -33), Math.toRadians(0)) //place
                .setReversed(true)
                .splineTo(new Vector2d(12, -50), Math.toRadians(-90)) //back up
                .setReversed(false)
                .splineTo(new Vector2d(25, -45), Math.toRadians(0)) //turn
                .splineTo(new Vector2d(50, -42), Math.toRadians(0)) //go to right back board
                .setReversed(true)
                .splineTo(new Vector2d(40, -42), Math.toRadians(180)) //backup
                .setReversed(false)
                .splineTo(new Vector2d(54, -60), Math.toRadians(0)) //park in corner
                .build());

        redFar.runAction(redFar.getDrive().actionBuilder(new Pose2d(-36, -65, Math.toRadians(90))) //red far right
                .splineTo(new Vector2d(-34, -33), Math.toRadians(0)) //place
                .setReversed(true)
                .splineTo(new Vector2d(-36, -50), Math.toRadians(-90)) //back away
                .setReversed(false)
                .splineTo(new Vector2d(-49, -45), Math.toRadians(180)) //turn to back
                .splineTo(new Vector2d(-57, -30), Math.toRadians(90)) //go around lines
                .splineTo(new Vector2d(-36, -12), Math.toRadians(0)) //turn to towards back board
                .splineTo(new Vector2d(30, -12), Math.toRadians(0)) //go under curtain
                .splineTo(new Vector2d(50, -42), Math.toRadians(0)) //go to left back board
                .setReversed(true)
                .splineTo(new Vector2d(40, -42), Math.toRadians(180)) //backup
                .setReversed(false)
                .splineTo(new Vector2d(54, -12), Math.toRadians(0)) //park in corner
                .build());

        blueNear.runAction(blueNear.getDrive().actionBuilder(new Pose2d(12, 65, Math.toRadians(-90))) //blue near right
                .splineTo(new Vector2d(14, 33), Math.toRadians(0)) //place
                .setReversed(true) 
                .splineTo(new Vector2d(12, 50), Math.toRadians(90)) //back up
                .setReversed(false)
                .splineTo(new Vector2d(25, 45), Math.toRadians(0)) //turn
                .splineTo(new Vector2d(50, 42), Math.toRadians(0)) //go to mid back board
                .setReversed(true)
                .splineTo(new Vector2d(40, 42), Math.toRadians(180)) //backup
                .setReversed(false)
                .splineTo(new Vector2d(54, 60), Math.toRadians(0)) //park in corner
                .build());

        blueFar.runAction(blueFar.getDrive().actionBuilder(new Pose2d(-36, 65, Math.toRadians(-90))) //blue far right
                .splineTo(new Vector2d(-34, 33), Math.toRadians(0)) //place
                .setReversed(true)
                .splineTo(new Vector2d(-36, 50), Math.toRadians(90)) //back away
                .setReversed(false)
                .splineTo(new Vector2d(-49, 45), Math.toRadians(-180)) //turn to back
                .splineTo(new Vector2d(-57, 36), Math.toRadians(-90)) //go around lines
                .splineTo(new Vector2d(-36, 12), Math.toRadians(0)) //turn to towards back board
                .splineTo(new Vector2d(30, 12), Math.toRadians(0)) //go under curtain
                .splineTo(new Vector2d(50, 42), Math.toRadians(0)) //go to left back board
                .setReversed(true)
                .splineTo(new Vector2d(40, 42), Math.toRadians(180)) //backup
                .setReversed(false)
                .splineTo(new Vector2d(54, 12), Math.toRadians(0)) //park in corner
                .build());



        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(redNear)
                .addEntity(redFar)
                .addEntity(blueNear)
                .addEntity(blueFar)
                .start();

    }


    }
