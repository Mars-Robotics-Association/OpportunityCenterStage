package org.firstinspires.ftc.teamcode.Payload;

import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Payload {
    public final Camera camera;
    public final PixelArm pixelArm;

    public Payload(HardwareMap hardwareMap){
        camera = new Camera(hardwareMap);
        pixelArm = new PixelArm(hardwareMap);
    }
}
