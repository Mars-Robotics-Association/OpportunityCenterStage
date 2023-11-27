package org.firstinspires.ftc.teamcode.Payload;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public final class PixelArm {
    public final Lift lift;
    public final Wrist wrist;
    public final Gripper gripperA;
    public final Gripper gripperB;

    public static class Lift{
        Lift(HardwareMap hardwareMap){
            motor = hardwareMap.dcMotor.get("lift_motor");
            motor.setDirection(DcMotorSimple.Direction.REVERSE);

            // this will get changed often to prevent constant power draw
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        }

        static class OutOfRange extends RuntimeException{
            @SuppressLint("DefaultLocale")
            OutOfRange(double attemptedHeight){
                super(String.format("Lift cannot safely achieve height of %f inches", attemptedHeight));
            }
        }

        private final DcMotor motor;

        // Derive these value from Calibration Mode
        // Inches to Encoder Ticks conversion
        private static final double TICKS_PER_INCH = 100;
        // Maximum height allowed
        private static final double MAX_SAFE_INCHES = 12;

        /**
         * Raises the lift to a desired height.
         * @param inches Height measured from the ground in inches.
         * @throws OutOfRange Thrown when {@code inches} exceeds {@link #MAX_SAFE_INCHES} or less than 0.
         */
        public void gotoHeight(double inches) throws OutOfRange{
            if(inches < 0 || MAX_SAFE_INCHES < inches)throw new OutOfRange(inches);
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motor.setTargetPosition((int) (inches * TICKS_PER_INCH));
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

        /**
         * This function will enter an implicit "Override Mode." To exit, call gotoHeightInches or freeze
         * @param power equivalent to {@link DcMotor#setPower(double power)}
         */
        public void override(double power){
            if((double)(motor.getCurrentPosition()) / TICKS_PER_INCH > MAX_SAFE_INCHES - 2.0){
                gotoHeight(MAX_SAFE_INCHES - 4.0);
                return;
            }

            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motor.setPower(power);
        }

        public boolean isBusy(){
            boolean busy = motor.isBusy();
            if(busy)motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            return busy;
        }
    }

    public static class Gripper{

        public enum Side{
            A("left_gripper", .8, .5),
            B("right_gripper", .48, .34);

            public final String name;

            public final double closePos;
            public final double openPos;

            Side(String name, double closePos, double openPos) {
                this.name = name;
                this.closePos = closePos;
                this.openPos = openPos;
            }
        }

        private final Servo servo;

        Gripper(HardwareMap hardwareMap, Side side){
            this.servo = hardwareMap.servo.get(side.name);
            this.side = side;
        }

        public final Side side;

        private boolean closed = false;

        public void toggle(){
            if(closed)open();
            else close();
        }

        public void open(){
            servo.setPosition(side.openPos);
            closed = false;
        }

        public void close(){
            servo.setPosition(side.closePos);
            closed = true;
        }
    }

    public static class Wrist{
        private static final double GROUND_ANGLE = 0.3;
        private static final double BOARD_ANGLE = 0.6;

        private final Servo servo;

        Wrist(HardwareMap hardwareMap){
            this.servo = hardwareMap.servo.get("wrist_servo");
        }

        public void toGroundAngle(){
            servo.setPosition(GROUND_ANGLE);
        }

        public void toBoardAngle(){
            servo.setPosition(BOARD_ANGLE);
        }
    }

    public PixelArm(HardwareMap hardwareMap){
        lift = new Lift(hardwareMap);
        wrist = new Wrist(hardwareMap);
        gripperA = new Gripper(hardwareMap, Gripper.Side.A);
        gripperB = new Gripper(hardwareMap, Gripper.Side.B);
    }
}