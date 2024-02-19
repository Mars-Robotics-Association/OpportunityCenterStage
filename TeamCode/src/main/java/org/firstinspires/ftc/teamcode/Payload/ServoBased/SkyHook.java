package org.firstinspires.ftc.teamcode.Payload.ServoBased;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Payload.Payload;

/**
 * The servo thingy that controls the spring-loaded stick thingy
 */
public class SkyHook {
    static double STOW_POSITION = 0.6161;
    static double ENGAGE_POSITION = 0.2095;
    private final Servo servo;

    public SkyHook(Payload payload){
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

    public void toggle(){
        if (servo == null)return;
        if (servo.getPosition() == STOW_POSITION)
            engage();
        else
            stow();
    }
}
