package org.firstinspires.ftc.teamcode.OpModes;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Payload.Camera;
import org.firstinspires.ftc.teamcode.Payload.Payload;

@TeleOp(group = "Utility")
public class CameraTester extends OpMode {
    private Camera camera;

    @Override
    public void init() {
        camera = new Payload(this, null).camera;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void loop() {
        for (Camera.SearchRegion region :
                Camera.SearchRegion.values()) {
            telemetry.addLine(String.format("Region %s with coverage %.3f", region.name(), region.coverage));
        }
    }
}
