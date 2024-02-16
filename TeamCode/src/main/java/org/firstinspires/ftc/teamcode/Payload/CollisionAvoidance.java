package org.firstinspires.ftc.teamcode.Payload;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class CollisionAvoidance {
    CollisionAvoidance(Payload payload){}

    public boolean shouldStop(double minimumDistance){
        return false;
    }
}
