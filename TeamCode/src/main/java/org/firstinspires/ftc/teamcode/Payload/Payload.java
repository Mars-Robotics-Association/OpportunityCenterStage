package org.firstinspires.ftc.teamcode.Payload;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.RoadRunner.MecanumDrive;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Payload {


    public GameState gameState;
    public Camera camera = null;
    public PixelArm pixelArm = null;
    public HighLevel highLevel = null;
    final MecanumDrive drive;

    @SuppressWarnings("unused")
    public static class GameState{
        /** Is null if not derived from the color sensor */
        @Nullable
        public ColorSensorChoice colorChoice;

        public enum ColorSensorChoice{
            YELLOW(1,1,0, TeamColor.BLUE, StartSlot.CLOSER ),
            GREEN (0,1,1, TeamColor.BLUE, StartSlot.FARTHER),
            BLACK (0,0,0, TeamColor.RED , StartSlot.CLOSER ),
            WHITE (1,1,1, TeamColor.RED , StartSlot.FARTHER);

            public final double r1, g1, b1;

            public double lastDistance = 0;

            public final TeamColor teamColor;
            public final StartSlot startSlot;

            void updateDistance(double r2, double g2, double b2){
                double r3 = (r1 - r2);
                double g3 = (g1 - g2);
                double b3 = (b1 - b2);

                lastDistance = Math.sqrt(r3 * r3 + g3 * g3 + b3 * b3);
            }

            ColorSensorChoice(double r, double g, double b,
                              TeamColor teamColor,
                              StartSlot startSlot){
                this.r1 = r;
                this.g1 = g;
                this.b1 = b;
                this.teamColor = teamColor;
                this.startSlot = startSlot;
            }
        }

        public GameState(){}

        public static double[] debugRGBReadings = new double[3];

        public static GameState fromSensor(ColorSensor colorSensor){
            debugRGBReadings = new double[]{
                    colorSensor.red(),
                    colorSensor.green(),
                    colorSensor.blue()
            };

            double r = debugRGBReadings[0] / 255;
            double g = debugRGBReadings[1] / 255;
            double b = debugRGBReadings[2] / 255;

            ColorSensorChoice[] colorChoices = ColorSensorChoice.values();

            for (ColorSensorChoice colorChoice : colorChoices) {
                colorChoice.updateDistance(r, g, b);
            }

            Stream<ColorSensorChoice> results = Arrays.stream(colorChoices).sorted((
                    Comparator.comparingDouble(o -> o.lastDistance)));

            ColorSensorChoice result = results.collect(Collectors.toList()).get(0);

            GameState state = new GameState();
            state.colorChoice = result;
            state.startSlot = result.startSlot;
            state.teamColor = result.teamColor;
            return state;
        }

        public enum SignalState{ LEFT, MIDDLE, RIGHT }
        public enum StartSlot{
            CLOSER(12.00), FARTHER(-36.00);

            public final double startPosX;

            StartSlot(double startPosX){
                this.startPosX = startPosX;
            }
        }
        public enum TeamColor{
            BLUE(1), RED(-1);

            public final double flipY;

            TeamColor(double flipY){
                this.flipY = flipY;
            }
        }

        public SignalState signalState;
        public StartSlot startSlot;
        public TeamColor teamColor;
    }

    public Payload(HardwareMap hardwareMap, MecanumDrive drive, boolean disable) {
        gameState = new GameState();

        this.drive = drive;
        if(disable)return;
        camera = new Camera(hardwareMap);
        pixelArm = new PixelArm(hardwareMap);
        highLevel = new HighLevel(this);
    }
}
