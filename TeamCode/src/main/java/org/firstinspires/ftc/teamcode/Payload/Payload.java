package org.firstinspires.ftc.teamcode.Payload;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.RoadRunner.MecanumDrive;

public final class Payload {
    @SuppressWarnings("unused")
    public static class GameState{
        public enum SignalState{ LEFT, MIDDLE, RIGHT }
        public enum StartSlot{
            CLOSER(12.00), FARTHER(-36.00);

            public final double posX;

            StartSlot(double posX){
                this.posX = posX;
            }
        }
        public enum TeamColor{
            BLUE(1), RED(-1);

            public final double flipY;

            TeamColor(double flipY){
                this.flipY = flipY;
            }
        }

        public SignalState signalState = SignalState.LEFT;
        public StartSlot startSlot = StartSlot.CLOSER;
        public TeamColor teamColor = TeamColor.BLUE;
    }

    public GameState gameState = new GameState();

    public Camera camera;
    public PixelArm pixelArm;
    public HighLevel highLevel;

    final MecanumDrive drive;

    public Payload(HardwareMap hardwareMap, MecanumDrive drive, boolean disable) {
        this.drive = drive;
        if(disable)return;
        camera = new Camera(hardwareMap);
        pixelArm = new PixelArm(hardwareMap);
        highLevel = new HighLevel(this);
    }
}
