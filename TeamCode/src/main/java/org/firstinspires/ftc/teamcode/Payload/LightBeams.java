package org.firstinspires.ftc.teamcode.Payload;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver.BlinkinPattern;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class LightBeams {
    private static final Class<RevBlinkinLedDriver> LED = RevBlinkinLedDriver.class;
    private static final Class<DistanceSensor> SENSOR = DistanceSensor.class;

    private final RevBlinkinLedDriver beamL;
    private final RevBlinkinLedDriver beamR;
    private final DistanceSensor sensorL;
    private final DistanceSensor sensorR;
    private final PixelArm pixelArm;

    LightBeams(Payload payload) {
        pixelArm = payload.pixelArm;
        HardwareMap hardware = payload.hardwareMap;

        beamL = hardware.tryGet(LED, "left_beam");
        sensorL = hardware.get(SENSOR, "left_color");

        beamR = hardware.tryGet(LED, "right_beams");
        sensorR = hardware.get(SENSOR, "right_color");
    }

    public void updateGripper(PixelArm.Gripper gripper,
                              RevBlinkinLedDriver beam,
                              DistanceSensor sensor
    ){
        boolean noPixel = sensor.getDistance(DistanceUnit.INCH) > 3;
        boolean isOpen = !gripper.isClosed();

        if(noPixel){
            if(isOpen)
                beam.setPattern(BlinkinPattern.WHITE);
            else
                beam.setPattern(BlinkinPattern.RED);
        }else{
            if(isOpen)
                beam.setPattern(BlinkinPattern.GREEN);
            else
                beam.setPattern(BlinkinPattern.BLUE);
        }
    }

    public void update(){
        if (beamL != null)updateGripper(pixelArm.gripperA, beamL, sensorL);
        if (beamR != null)updateGripper(pixelArm.gripperB, beamR, sensorR);
    }
}
