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


        redNear.runAction(redNear.getDrive().actionBuilder(new Pose2d(12, -65, Math.toRadians(90))) //red near middle
                .splineTo(new Vector2d(12, -30), Math.toRadians(90)) //place
                .setReversed(true)
                .splineTo(new Vector2d(12, -50), Math.toRadians(-90)) //back up
                .setReversed(false)
                .splineTo(new Vector2d(25, -45), Math.toRadians(0)) //turn
                .splineTo(new Vector2d(50, -36), Math.toRadians(0)) //go to mid back board
                .build());

        redFar.runAction(redNear.getDrive().actionBuilder(new Pose2d(-36, -65, Math.toRadians(90))) //red near middle
                .splineTo(new Vector2d(-36, -30), Math.toRadians(90)) //place
                .build());

        blueNear.runAction(redNear.getDrive().actionBuilder(new Pose2d(12, 65, Math.toRadians(-90))) //red near middle
                .splineTo(new Vector2d(12, 30), Math.toRadians(-90)) //place
                .setReversed(true)
                .splineTo(new Vector2d(12, 50), Math.toRadians(90)) //back up
                .setReversed(false)
                .splineTo(new Vector2d(25, 45), Math.toRadians(0)) //turn
                .splineTo(new Vector2d(50, 36), Math.toRadians(0)) //go to mid back board
                .build());

        blueFar.runAction(redNear.getDrive().actionBuilder(new Pose2d(-36, 65, Math.toRadians(-90))) //red near middle
                .splineTo(new Vector2d(-36, 30), Math.toRadians(-90)) //place
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
