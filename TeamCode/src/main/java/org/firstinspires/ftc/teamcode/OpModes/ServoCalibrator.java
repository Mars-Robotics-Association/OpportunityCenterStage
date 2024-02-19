package org.firstinspires.ftc.teamcode.OpModes;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.firstinspires.ftc.teamcode.utils;

import java.util.ArrayList;

@TeleOp(group = "Utility")
public class ServoCalibrator extends OpMode {

    private double lastRuntime;
    private final utils.Debouncer selector = new utils.Debouncer(this, 0.5);
    private String[] names = {
            "skyhook",
            "launcher",
            "wrist_servo",
            "right_gripper",
            "left_gripper"
    };

    private Servo[] servos;
    private int servoIndex = 0;

    private void cycleForward(){
        servoIndex++;
        if (servoIndex == servos.length)servoIndex = 0;
    }

    private void cycleBackward(){
        if (servoIndex == 0)servoIndex = servos.length;
        servoIndex--;

    }

    @Override @SuppressWarnings("all")
    public void init() {
        servos = new Servo[names.length];

        for (int i = 0; i < names.length; i++)
            servos[i] = hardwareMap.tryGet(Servo.class, names[i]);
    }

    static String UNAVAILABLE = "🚫UNAVAILABLE🚫";

    @SuppressLint("DefaultLocale")
    private void printDeviceList(){
        for (int i = 0; i < names.length; i++) {
            String name = names[i];
            Servo servo = servos[i];
            String connectionInfo = servo != null ?
                    servo.getConnectionInfo() : UNAVAILABLE;
            telemetry.addData(name, connectionInfo);
        }

    }

    @SuppressLint("DefaultLocale")
    private void logFeedbackData(){
        Double position = servos[servoIndex].getPosition();
        String name = names[servoIndex];
        telemetry.addLine(String.format("Position of %s: %f", name, position));
    }

    @SuppressLint("DefaultLocale")
    private void gamepadControl() {
        Servo servo = servos[servoIndex];

        double lastPosition = servo.getPosition();

        double deltaTime = getRuntime() - lastRuntime;
        lastRuntime = getRuntime();

        if (Math.abs(gamepad1.left_stick_y) > .1) {
            double change = deltaTime * gamepad1.left_stick_y * .05;

            servo.setPosition(lastPosition + change);
        }
    }

    @Override
    public void loop() {
        if(selector.poll(gamepad1.dpad_up || gamepad1.dpad_down)) {
            if(gamepad1.dpad_up)
                cycleBackward();
            else if (gamepad1.dpad_down)
                cycleForward();
        }

        printDeviceList();
        telemetry.addLine("----------------------------");

        if(servos[servoIndex] != null){
            gamepadControl();
            logFeedbackData();
        }else
            telemetry.addLine("This device is currently unavailable");

        telemetry.update();
    }
}
