package org.firstinspires.ftc.teamcode.Payload;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Winch {
    private final DcMotor motor;

    Winch(HardwareMap hardwareMap){
        motor = hardwareMap.dcMotor.get("lift");
    }

    public void reelIn(){
        motor.setPower(1.0);
    }
}
