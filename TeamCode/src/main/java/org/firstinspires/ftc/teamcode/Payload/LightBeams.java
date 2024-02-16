package org.firstinspires.ftc.teamcode.Payload;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver.BlinkinPattern;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class LightBeams {

    enum Color{
        WHITE(BlinkinPattern.WHITE, 4),
        RED(BlinkinPattern.RED, 3),
        GREEN(BlinkinPattern.GREEN, 2),
        BLUE(BlinkinPattern.BLUE, 1),
        ;

        public final BlinkinPattern pattern;
        public final int priority;

        Color(BlinkinPattern pattern, int priority) {
            this.pattern = pattern;
            this.priority = priority;
        }
    }

    enum RunMode{
        DUAL_BEAMS(true, true),
        LEFT_ONLY(true, false),
        RIGHT_ONLY(false, true),
        OFFLINE(false, false);

        public final boolean hasLeft;
        public final boolean hasRight;

        public static RunMode resolve(boolean left, boolean right){
            for (RunMode runMode : RunMode.values())
                if((runMode.hasLeft == left) && (runMode.hasRight == right))
                    return runMode;

            throw new IllegalStateException("This shouldn't be happening...");
        }

        public String getMessage(){
            switch(this){
                case DUAL_BEAMS: return "Both LightBeams are online!";
                case LEFT_ONLY: return "No right LightBeam was found";
                case RIGHT_ONLY: return "No left LightBeam was found";
                case OFFLINE: return "No LightBeams were detected!";
            }

            throw new IllegalStateException("This shouldn't be happening...");
        }

        RunMode(boolean hasLeft, boolean hasRight){
            this.hasLeft = hasLeft;
            this.hasRight = hasRight;
        }
    }

    private static final Class<RevBlinkinLedDriver> LED = RevBlinkinLedDriver.class;
    private static final Class<Rev2mDistanceSensor> SENSOR = Rev2mDistanceSensor.class;

    private final RevBlinkinLedDriver beamL;
    private final RevBlinkinLedDriver beamR;
    private final Rev2mDistanceSensor sensorL;
    private final Rev2mDistanceSensor sensorR;
    private final PixelArm pixelArm;
    private final Telemetry telemetry;
    private final RunMode runMode;

    LightBeams(Payload payload) {
        pixelArm = payload.pixelArm;
        telemetry = payload.opMode.telemetry;
        HardwareMap hardware = payload.hardwareMap;

        beamL = hardware.tryGet(LED, "left_beam");
        sensorL = hardware.get(SENSOR, "left_gripper_dist");

        beamR = hardware.tryGet(LED, "right_beams");
        sensorR = hardware.get(SENSOR, "right_gripper_dist");

        runMode = RunMode.resolve(beamL != null, beamR != null);
    }

    public Color determineColor(PixelArm.Gripper gripper,
                              Rev2mDistanceSensor sensor
    ){
        boolean hasPixel = sensor.getDistance(DistanceUnit.INCH) < 2;
        boolean isOpen = !gripper.isClosed();

        telemetry.addLine(String.format("%s has a pixel: %s", gripper.side, hasPixel));

        if(hasPixel){
            if(isOpen)
                return Color.GREEN;
            else
                return Color.BLUE;
        }else{
            if(isOpen)
                return Color.WHITE;
            else
                return Color.RED;
        }
    }

    public void handleDualMode(){
        Color left = determineColor(pixelArm.gripperA, sensorL);
        Color right = determineColor(pixelArm.gripperB, sensorR);

        beamL.setPattern(left.pattern);
        beamL.setPattern(right.pattern);
    }

    public void handleSingleMode(RevBlinkinLedDriver targetBeam){
        Color left = determineColor(pixelArm.gripperA, sensorL);
        Color right = determineColor(pixelArm.gripperB, sensorR);

        if(left.priority < right.priority)
            targetBeam.setPattern(left.pattern);
        else
            targetBeam.setPattern(right.pattern);
    }

    public void update(){
        switch (runMode){
            case DUAL_BEAMS: handleDualMode(); break;
            case LEFT_ONLY: handleSingleMode(beamL); break;
            case RIGHT_ONLY: handleSingleMode(beamR); break;
        }

        telemetry.addLine(runMode.getMessage());
    }
}
