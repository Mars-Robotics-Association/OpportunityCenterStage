package org.firstinspires.ftc.teamcode.OpModes;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.ArrayList;

@TeleOp
public class MotorMover extends OpMode {
    ArrayList<DcMotor> motors = new ArrayList<>();

    @Override
    public void init() {
        hardwareMap.dcMotor.iterator()
                .forEachRemaining(dcMotor -> motors.add(dcMotor));
    }

    int selectedMotor = 0;
    boolean canSwitchMotor = true;

    @SuppressLint("DefaultLocale")
    @Override
    public void loop() {
        int changeMotorTo = selectedMotor;

        if(gamepad1.dpad_up)changeMotorTo--;
        if(gamepad1.dpad_down)changeMotorTo++;

        if(canSwitchMotor){
            canSwitchMotor = changeMotorTo == selectedMotor;

            if(changeMotorTo == motors.size()) changeMotorTo = 0; // jump backward
            if(changeMotorTo == -1) changeMotorTo = motors.size() - 1; // wrap forward

            selectedMotor = changeMotorTo;
        }else canSwitchMotor = true;

        motors.get(selectedMotor).setPower(gamepad1.left_stick_x);

        telemetry.addLine("Motor Ports:");
        for (int i = 0; i < motors.size(); i++) {
            DcMotor motor = motors.get(i);
            String cursor = (i == selectedMotor) ? "-->" : "";
            telemetry.addLine(String.format("%s %d", cursor, motor.getPortNumber()));
        }
    }
}
