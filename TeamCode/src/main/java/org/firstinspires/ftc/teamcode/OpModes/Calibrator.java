package org.firstinspires.ftc.teamcode.OpModes;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.utils;
import org.jetbrains.annotations.Nullable;

@TeleOp
public class Calibrator extends OpMode {

    private DcMotor liftMotor;
    private double lastRuntime;

    private final utils.Debouncer selector = new utils.Debouncer(this, 0.5);

    @Override
    public void init() {
        if(hardwareMap.dcMotor.contains("lift_motor")) {
            liftMotor = hardwareMap.dcMotor.get("lift_motor");
            liftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
            liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            Category.LIFT_MOTOR.available = true;
        }

        Category.initialize(hardwareMap);

    }

    private enum Category{
        LIFT_MOTOR,
        STIG("stick_servo"),
        GRIPPER_WRIST("wrist_servo"),
        GRIPPER_LEFT("left_gripper"),
        GRIPPER_RIGHT("right_gripper");
        static final Category[] ALL = Category.values();

        @Nullable public final String servoName;
        @Nullable public Servo servo;

        public boolean available = true;

        public Category prev() {
            if(this == LIFT_MOTOR)return GRIPPER_RIGHT;
            return ALL[ordinal() - 1];
        }

        public Category next() {
            if(this == GRIPPER_RIGHT)return LIFT_MOTOR;
            return ALL[ordinal() + 1];
        }

        static void initialize(HardwareMap hardwareMap){
            for (Category category : Category.values()) {
                if (category.servoName == null)continue;
                category.servo = hardwareMap.tryGet(Servo.class, category.servoName);
                category.available = category.servo != null;
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
        Telemetry t = telemetry;

        if (category == Category.LIFT_MOTOR) {
            t.addData("Is the lift motor locked?", liftMotor.getZeroPowerBehavior() == DcMotor.ZeroPowerBehavior.BRAKE ? "Yes" : "No");
            t.addLine(String.format("Lift Motor Current Power: %.3f", liftMotor.getPower()));
            t.addData("Lift Motor Encoder Position", liftMotor.getCurrentPosition());
        } else {
            Servo servo = category.servo;
            assert servo != null;
            telemetry.addData("Position", servo.getPosition());
        }
    }

    @SuppressLint("DefaultLocale")
    private void gamepadControl() {
        // don't let it run on its own
        if(category != Category.LIFT_MOTOR && Category.LIFT_MOTOR.available)
            liftMotor.setPower(0);

        Servo servo = category.servo;

        if(!category.available)return;

        if(category == Category.LIFT_MOTOR) {
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
        if(selector.poll(gamepad1.dpad_up || gamepad1.dpad_down)) {
            if(gamepad1.dpad_up)
                category = category.prev();
            else if (gamepad1.dpad_down)
                category = category.next();
        }

        telemetry.addData("Currently testing system", category.name());

        if(category.available){
            gamepadControl();
            logFeedbackData();
        }else
            telemetry.addLine("This device is currently unavailable");

        telemetry.update();
    }
}
