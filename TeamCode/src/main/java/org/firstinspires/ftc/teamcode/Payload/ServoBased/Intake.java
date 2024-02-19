package org.firstinspires.ftc.teamcode.Payload.ServoBased;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Payload.Payload;

public final class Intake {
    private final Servo servo;

    public Intake(Payload payload){
        HardwareMap hardwareMap = payload.hardwareMap;
        servo = hardwareMap.servo.get("intake_servo");
    }

    public void intake(){servo.setPosition(0);}

    public void stop(){servo.setPosition(.5);}

    public void outtake(){
        servo.setPosition(1);
    }
}
