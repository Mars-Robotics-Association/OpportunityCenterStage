package org.firstinspires.ftc.teamcode.Payload;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class CollisionAvoidance {
    final Class<Rev2mDistanceSensor> SENSOR = Rev2mDistanceSensor.class;

    Rev2mDistanceSensor leftSensor;
    Rev2mDistanceSensor rightSensor;

    static double LEFT_BLUE = 18;
    static double LEFT_RED = 18.0;
    static double RIGHT_BLUE = 18.0;
    static double RIGHT_RED = 18.0;

    private final Payload payload;

    CollisionAvoidance(Payload payload){
        this.payload = payload;

        HardwareMap hardwareMap = payload.hardwareMap;

        leftSensor = hardwareMap.tryGet(SENSOR, "left_dist");
        rightSensor = hardwareMap.tryGet(SENSOR, "right_dist");
    }

    public boolean shouldStop(){
        if(leftSensor == null || rightSensor == null)return false;

        boolean shouldStop = false;

        double leftReading = leftSensor.getDistance(DistanceUnit.INCH);
        double rightReading = rightSensor.getDistance(DistanceUnit.INCH);

        switch(payload.gameState.teamColor){
            case BLUE:
                shouldStop = leftReading <= LEFT_BLUE || rightReading <= RIGHT_BLUE;
                break;
            case RED:
                shouldStop = leftReading <= LEFT_RED || rightReading <= RIGHT_RED;
        }

        return false;
    }
}
