package org.firstinspires.ftc.teamcode.Payload;

import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * The servo thingy that controls the spring-loaded stick thingy
 */
public class TheServo {
    TheServo(HardwareMap hardwareMap){
        hardwareMap.servo.get("stick_servo");
    }
}
