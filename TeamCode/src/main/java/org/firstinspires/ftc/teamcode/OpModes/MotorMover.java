package org.firstinspires.ftc.teamcode.OpModes;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.Map.Entry;

@TeleOp(group = "Utility")
public class MotorMover extends OpMode {
    Entry<String, DcMotor>[] motors = null;

    @Override @SuppressWarnings("all")
    public void init() {
        motors = (Entry<String, DcMotor>[]) hardwareMap.dcMotor.entrySet().toArray();
    }

    int selectedMotor = 0;

    private double lastChangeTimestamp;

    @SuppressLint("DefaultLocale")
    @Override
    public void loop() {
        changeMotor: if (getRuntime() - lastChangeTimestamp > 0.5) {
            int selector = (gamepad1.dpad_up ? 0x10 : 0x00) | (gamepad1.dpad_down ? 0x01 : 0x00);

            switch(selector){
                case 0x10:
                    if (selectedMotor == 0)
                        selectedMotor = motors.length;
                    selectedMotor--;
                    break changeMotor;
                case 0x01:
                    selectedMotor++;
                    if (selectedMotor == motors.length)
                        selectedMotor = 0;
                    break changeMotor;
            }
            lastChangeTimestamp = getRuntime();
        }

        motors[selectedMotor].getValue().setPower(gamepad1.left_stick_x);

        telemetry.addLine("Motor Ports:");
        for (int i = 0; i < motors.length; i++) {
            String cursor = (i == selectedMotor) ? "-->" : "";
            DcMotor motor = motors[i].getValue();
            telemetry.addLine(String.format("%s %s", cursor, motor.getConnectionInfo()));
        }
    }
}
