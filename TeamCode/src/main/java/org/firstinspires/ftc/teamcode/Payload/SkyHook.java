package org.firstinspires.ftc.teamcode.Payload;


import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * The servo thingy that controls the spring-loaded stick thingy
 */
public class SkyHook {
    static double STOW_POSITION = 0.0;
    static double ENGAGE_POSITION = 0.0;
    private final Servo servo;

    SkyHook(Payload payload){
        servo = payload.hardwareMap.tryGet(Servo.class, "skyhook");
        stow();
    }

    public void stow(){
        if (servo == null)return;
        servo.setPosition(STOW_POSITION);
    }

    public void engage(){
        if (servo == null)return;
        servo.setPosition(ENGAGE_POSITION);
    }
}
