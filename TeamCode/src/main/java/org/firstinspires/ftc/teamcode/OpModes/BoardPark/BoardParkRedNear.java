package org.firstinspires.ftc.teamcode.OpModes.BoardPark;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@SuppressWarnings("unused")
@Autonomous
public final class BoardParkRedNear extends BoardPark {
    @Override
    public void runOpMode() {
        initCoreSimple(new Pose2d(11, -61, 0));

        flipY = -1;

        waitForStart();

        Actions.runBlocking(mainAction());
    }
}
