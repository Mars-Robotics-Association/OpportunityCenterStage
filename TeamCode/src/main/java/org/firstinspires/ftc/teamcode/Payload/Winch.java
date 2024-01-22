package org.firstinspires.ftc.teamcode.Payload;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Winch {
    private final DcMotor motor;

    Winch(HardwareMap hardwareMap){
        motor = hardwareMap.dcMotor.get("winch");
    }

    public void reelIn(){
        motor.setPower(.8);
    }
    public void release() {motor.setPower(-.8); }
}
