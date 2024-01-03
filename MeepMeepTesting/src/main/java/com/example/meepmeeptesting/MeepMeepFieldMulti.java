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

// santi-cam: 👏😲
public class MeepMeepFieldMulti {
    public enum autoStart {RED_STACK1, RED_STACK2, RED_STACK3, RED_BACK, BLUE_STACK, BLUE_BACK }
    public static autoStart start = autoStart.RED_BACK;

    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(600, 240);
        RoadRunnerBotEntity redBot1 = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 13)
                .setColorScheme(new ColorSchemeRedDark())
                .build();
        RoadRunnerBotEntity redBot2 = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 13)
                .setColorScheme(new ColorSchemeRedLight())
                .build();
        RoadRunnerBotEntity blueBot1 = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 13)
                .setColorScheme(new ColorSchemeBlueDark())
                .build();
        RoadRunnerBotEntity blueBot2 = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 13)
                .setColorScheme(new ColorSchemeBlueLight())
                .build();


        blueBot1.runAction(blueBot1.getDrive().actionBuilder(new Pose2d(-34, 62, Math.toRadians(90)))
                .setReversed(true)
                .splineTo(new Vector2d(-44, 32), Math.toRadians(-120))  // To the spike mark
                .setReversed(false)
                .splineTo(new Vector2d(-50, 42), Math.toRadians(180))  // Back away from spike mark
                .splineTo(new Vector2d(-57, 32), Math.toRadians(-90))  // Approach bridge
                .splineTo(new Vector2d(-57, 24), Math.toRadians(-90))  // Approach bridge
                .splineTo(new Vector2d(-20, 12), Math.toRadians(0))  // Approach bridge
                .splineTo(new Vector2d(26, 12), Math.toRadians(0))  // Go under bridge
                .splineTo(new Vector2d(48, 36), Math.toRadians(0))  // Go to backdrop
                .setReversed(true)
                .splineTo(new Vector2d(40, 30), Math.toRadians(-90))  // Go back along wall
                .splineTo(new Vector2d(52, 13), Math.toRadians(-45))  // Go under bridge
                .build());

        blueBot2.runAction(blueBot2.getDrive().actionBuilder(new Pose2d(12, 62, Math.toRadians(90)))
                .setReversed(true)
                .splineTo(new Vector2d(4, 32), Math.toRadians(-120))  // To the spike mark
                .setReversed(false)
                .splineTo(new Vector2d(18, 42), Math.toRadians(0))  // Back away from spike mark
                .splineTo(new Vector2d(48, 36), Math.toRadians(0))  // Go to backdrop
                .setReversed(true)
                .splineTo(new Vector2d(40, 42), Math.toRadians(90))  // Go back along wall
                .splineTo(new Vector2d(52, 58), Math.toRadians(0))  // Go under bridge
                .build());

        redBot1.runAction(redBot1.getDrive().actionBuilder(new Pose2d(-34, -62, Math.toRadians(-90)))
                .setReversed(true)
                .splineTo(new Vector2d(-44, -32), Math.toRadians(120))  // To the spike mark
                .setReversed(false)
                .splineTo(new Vector2d(-50, -42), Math.toRadians(180))  // Back away from spike mark
                .splineTo(new Vector2d(-57, -32), Math.toRadians(90))  // Approach bridge
                .splineTo(new Vector2d(-57, -24), Math.toRadians(90))  // Approach bridge
                .splineTo(new Vector2d(-20, -12), Math.toRadians(0))  // Approach bridge.splineTo(new Vector2d(26, -16), Math.toRadians(0))  // Go under bridge
                .splineTo(new Vector2d(26, -12), Math.toRadians(0))  // Go under bridge
                .splineTo(new Vector2d(48, -36), Math.toRadians(0))  // Go to backdrop
                .setReversed(true)
                .splineTo(new Vector2d(40, -30), Math.toRadians(90))  // Back away from backdrop
                .splineTo(new Vector2d(52, -13), Math.toRadians(45))  // Park
                .build());

        redBot2.runAction(redBot2.getDrive().actionBuilder(new Pose2d(12, -62, Math.toRadians(-90)))
                .setReversed(true)
                .splineTo(new Vector2d(4, -32), Math.toRadians(120))  // To the spike mark
                .setReversed(false)
                .splineTo(new Vector2d(18, -42), Math.toRadians(0))  // Back away from spike mark
                .splineTo(new Vector2d(48, -36), Math.toRadians(0))  // Go to backdrop
                .setReversed(true)
                .splineTo(new Vector2d(40, -42), Math.toRadians(-90))  // Back away from backdrop
                .splineTo(new Vector2d(52, -58), Math.toRadians(0))  // Park
                .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(redBot1)
                .addEntity(redBot2)
                .addEntity(blueBot1)
                .addEntity(blueBot2)
                .start();
    }
}