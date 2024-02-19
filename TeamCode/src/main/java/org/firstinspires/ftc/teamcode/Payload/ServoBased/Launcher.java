package org.firstinspires.ftc.teamcode.Payload.ServoBased;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Payload.Payload;

/**
 * The servo thingy that controls the spring-loaded stick thingy
 */
public class Launcher {
    static double ARM_POSITION = .9999;
    static double FIRE_POSITION = .6766;
    private final Servo servo;

    public Launcher(Payload payload){
        servo = payload.hardwareMap.tryGet(Servo.class, "launcher");
    }

    public void arm(){
        if (servo == null)return;
        servo.setPosition(ARM_POSITION);
    }

    public void fire(){
        if (servo == null)return;
        servo.setPosition(FIRE_POSITION);
    }
}
