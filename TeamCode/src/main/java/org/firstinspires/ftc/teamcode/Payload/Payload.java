package org.firstinspires.ftc.teamcode.Payload;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.RoadRunner.MecanumDrive;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public final class Payload {

    public final GameState gameState = new GameState();
    public final MecanumDrive drive;
    public final Camera camera;
    public final PixelArm pixelArm;
    public final Intake intake;
    public final SkyHook skyHook;
    public final Winch winch;
    public final LightBeams lightBeams;
    public final CollisionAvoidance collisionAvoidance;

    public final HardwareMap hardwareMap;
    public final OpMode opMode;

    public Payload(OpMode opMode, MecanumDrive drive) {
        this.opMode = opMode;
        hardwareMap = opMode.hardwareMap;

        this.drive = drive;
        camera = new Camera(this);
        pixelArm = new PixelArm(this);
        intake = new Intake(this);
        skyHook = new SkyHook(this);
        winch = new Winch(this);
        lightBeams = new LightBeams(this);
        collisionAvoidance = new CollisionAvoidance(this);
    }

    public void update() {
        lightBeams.update();
    }
}
