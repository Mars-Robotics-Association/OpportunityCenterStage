package org.firstinspires.ftc.teamcode.Payload;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public final class Intake {
    private final Servo servo;

    Intake(HardwareMap hardwareMap){
        servo = hardwareMap.servo.get("intake_servo");
    }

    public void intake(){servo.setPosition(0);}

    public void stop(){servo.setPosition(.5);}

    public void outtake(){
        servo.setPosition(1);
    }
}
