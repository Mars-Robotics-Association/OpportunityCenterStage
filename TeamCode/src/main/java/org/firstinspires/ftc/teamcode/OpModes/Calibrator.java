package org.firstinspires.ftc.teamcode.OpModes;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Payload.Payload;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

@TeleOp
public class Calibrator extends OpMode {

    private ColorSensor colorSensor;
    private DcMotor liftMotor;

    private double lastSelectionTime;
    private double lastRuntime;

    @Override
    public void init() {
        Iterator<ColorSensor> colorIterator = hardwareMap.colorSensor.iterator();
        if(colorIterator.hasNext()){
            colorSensor = colorIterator.next();
            Category.TEAM_SENSOR.available = true;
        }

        if(hardwareMap.dcMotor.contains("lift_motor")) {
            liftMotor = hardwareMap.dcMotor.get("lift_motor");
            liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            Category.LIFT_MOTOR.available = true;
        }

        Category.initialize(hardwareMap);

        lastSelectionTime = lastRuntime = getRuntime();
    }

    private enum Category{
        TEAM_SENSOR,
        LIFT_MOTOR,
        GRIPPER_WRIST("wrist_servo"),
        GRIPPER_LEFT("left_gripper"),
        GRIPPER_RIGHT("right_gripper");

        static final Category[] ALL = Category.values();

        @Nullable public final String servoName;
        @Nullable public Servo servo;

        public boolean available = true;

        public Category prev() {
            if(this == TEAM_SENSOR)return GRIPPER_RIGHT;
            return ALL[ordinal() - 1];
        }

        public Category next() {
            if(this == GRIPPER_RIGHT)return TEAM_SENSOR;
            return ALL[ordinal() + 1];
        }

        static void initialize(HardwareMap hardwareMap){
            for (Category category : Category.values()) {
                if (category.servoName == null)continue;
                category.available = true;
                category.servo = hardwareMap.servo.get(category.servoName);
            }
        }

        Category(){
            servoName = null;
        }

        Category(@Nullable String servo){
            servoName = servo;
        }
    }

    private Category category = Category.LIFT_MOTOR;

    @SuppressLint("DefaultLocale")
    private void logFeedbackData(){
        Payload.GameState state = Payload.GameState.getSlotAndTeamFromSensor(colorSensor);

        Payload.GameState.ColorSensorChoice closestColor = state.colorChoice;
        assert closestColor != null;

        Telemetry t = telemetry;
        if(!category.available){
            t.addLine("This device is currently unavailable");
            return;
        }

        if (category == Category.TEAM_SENSOR) {
            double[] values = Payload.GameState.debugRGBReadings;
            t.addData("Raw Color Values", String.format("r:%.3f g:%.3f b:%.3f", values[0], values[0], values[0]));
            t.addLine("Processing Results");
            t.addData("Closest Color", String.format("%s (r:%.3f, g:%.3f, b:%.3f)",
                    closestColor.name(), closestColor.r1, closestColor.g1, closestColor.b1));
            t.addData("Team Color", state.teamColor.name());
            t.addData("Starting Slot", state.startSlot.name());
        } else if (category == Category.LIFT_MOTOR) {
            t.addData("Is the lift motor locked?", liftMotor.getZeroPowerBehavior() == DcMotor.ZeroPowerBehavior.BRAKE ? "Yes" : "No");
            t.addLine(String.format("Lift Motor Current Power: %.3f", liftMotor.getPower()));
            t.addData("Lift Motor Encoder Position", liftMotor.getCurrentPosition());
        } else {
            assert category.servo != null;
            telemetry.addData("Position", category.servo.getPosition());
        }
    }

    @SuppressLint("DefaultLocale")
    private void gamepadControl() {
        if (getRuntime() - lastSelectionTime < 1.0) {
            int selector = 0;
            if (gamepad1.dpad_up) selector--;
            if (gamepad1.dpad_down) selector++;

            switch (selector) {
                case -1:
                    category = category.prev();
                    lastSelectionTime = getRuntime();
                    break;
                case +1:
                    category = category.next();
                    lastSelectionTime = getRuntime();
                    break;
            }
        }

        // don't let it run on its own
        if(category != Category.LIFT_MOTOR && Category.LIFT_MOTOR.available){
            liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            liftMotor.setPower(0);
        }

        Servo servo = category.servo;

        if(!category.available)return;

        if(category == Category.LIFT_MOTOR) {
            int shouldLock = (gamepad1.a ? 0x10 : 0x00) | (gamepad1.x ? 0x01 : 0x00);

            if (shouldLock == 0x10)
                liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            if (shouldLock == 0x01)
                liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            double power = gamepad1.left_stick_y;
            if (Math.abs(power) > .1) {
                liftMotor.setPower(power * .5);
            } else liftMotor.setPower(0);
        } else if (servo != null) {
            double lastPosition = servo.getPosition();

            double deltaTime = getRuntime() - lastRuntime;
            lastRuntime = getRuntime();

            if (Math.abs(gamepad1.left_stick_y) > .1) {
                double change = deltaTime * gamepad1.left_stick_y * .1;

                servo.setPosition(lastPosition + change);
            }
        }
    }

    @Override
    public void loop() {
        gamepadControl();
        telemetry.addData("Currently testing system", category.name());
        logFeedbackData();
        telemetry.update();
    }
}
