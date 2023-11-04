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
        motors.get(selectedMotor).setPower(gamepad1.left_stick_x);

        int changeIndexTo = selectedMotor;

        if(gamepad1.dpad_up)changeIndexTo--;
        if(gamepad1.dpad_down)changeIndexTo++;

        if(canSwitchMotor){
            canSwitchMotor = changeIndexTo == selectedMotor;

            if(changeIndexTo == motors.size()) selectedMotor = 0;
            else if (changeIndexTo < 0) selectedMotor = motors.size() - 1;
            else selectedMotor = changeIndexTo;

        }else canSwitchMotor = true;


        telemetry.addLine("Motor Ports:");
        for (int i = 0; i < motors.size(); i++) {
            DcMotor motor = motors.get(i);
            String cursor = (i == selectedMotor) ? "-->" : "";
            telemetry.addLine(String.format("%s %d", cursor, motor.getPortNumber()));
        }
    }
}
