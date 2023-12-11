package org.firstinspires.ftc.teamcode.Payload;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;

public final class Intake {
    private final CRServo servo;

    Intake(HardwareMap hardwareMap){
        servo = hardwareMap.crservo.get("intake_servo");
    }

    public void open(){
        servo.setPower(.5);
    }

    public void close(){
        servo.setPower(-.5);
    }
}
