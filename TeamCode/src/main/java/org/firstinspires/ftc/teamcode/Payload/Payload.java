package org.firstinspires.ftc.teamcode.Payload;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.RoadRunner.MecanumDrive;

public final class Payload {
    public final GameState gameState;
    public final MecanumDrive drive;
    public final Camera camera;
    public final PixelArm pixelArm;
    public final Intake intake;
    public final Winch winch;
    public Payload(HardwareMap hardwareMap, MecanumDrive drive) {
        gameState = new GameState();

        this.drive = drive;
        camera = new Camera(hardwareMap, gameState);
        pixelArm = new PixelArm(hardwareMap);
        intake = new Intake(hardwareMap);
        winch = new Winch(hardwareMap);
    }
}
