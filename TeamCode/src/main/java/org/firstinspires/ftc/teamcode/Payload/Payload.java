package org.firstinspires.ftc.teamcode.Payload;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.RoadRunner.MecanumDrive;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public final class Payload {

    public final GameState gameState;
    public final MecanumDrive drive;
    public final Camera camera;
    public final PixelArm pixelArm;
    public final Intake intake;
    public final Winch winch;
    public final LightBeams lightBeams;
    public final CollisionAvoidance collisionAvoidance;

    public final HardwareMap hardwareMap;

    public Payload(HardwareMap hardwareMap, MecanumDrive drive) {
        gameState = new GameState();

        this.hardwareMap = hardwareMap;

        this.drive = drive;
        camera = new Camera(this);
        pixelArm = new PixelArm(this);
        intake = new Intake(this);
        winch = new Winch(this);
        lightBeams = new LightBeams(this);
        collisionAvoidance = new CollisionAvoidance(this);
    }
    public void update() {
        lightBeams.update();
    }
}
