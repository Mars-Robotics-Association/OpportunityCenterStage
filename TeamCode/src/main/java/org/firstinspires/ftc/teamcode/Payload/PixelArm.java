package org.firstinspires.ftc.teamcode.Payload;

import android.annotation.SuppressLint;

import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public final class PixelArm {
    public final Lift lift;
    public final Gripper gripperA;
    public final Gripper gripperB;

    public static class Lift{
        Lift(HardwareMap hardwareMap){
            motor = hardwareMap.dcMotor.get("lift_motor");
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

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
        private static final double MAX_SAFE_INCHES = 5;

        /**
         * Raises the lift to a desired height.
         * @param inches Height measured from the ground in inches.
         * @throws OutOfRange Thrown when {@code inches} exceeds {@link #MAX_SAFE_INCHES} or less than 0.
         */
        void gotoHeightInches(double inches) throws OutOfRange{
            if(inches < 0 || MAX_SAFE_INCHES < inches)throw new OutOfRange(inches);
            if(motor.getMode() != DcMotor.RunMode.RUN_TO_POSITION)
                motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motor.setTargetPosition((int) (inches * TICKS_PER_INCH));
        }

        /**
         * This function will enter an implicit "Override Mode." To exit, call gotoHeightInches or freeze
         * @param power equivalent to {@link DcMotor#setPower(double power)}
         */
        public void override(double power){
            if((double)(motor.getCurrentPosition()) / TICKS_PER_INCH > MAX_SAFE_INCHES - 2.0){
                gotoHeightInches(MAX_SAFE_INCHES - 4.0);
                return;
            }

            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motor.setPower(power);
        }

        boolean isBusy(){
            boolean busy = motor.isBusy();
            if(busy)motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            return busy;
        }
    }

    public static class Gripper{

        public enum Side{
            A(false, "left_gripper", .8, .5),
            B(false, "right_gripper", .48, .34);

            public final Servo.Direction direction;
            public final String name;

            public final double closePos;
            public final double openPos;

            Side(boolean inverted, String name, double closePos, double openPos) {
                this.direction = inverted ? Servo.Direction.REVERSE : Servo.Direction.FORWARD;
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

    public PixelArm(HardwareMap hardwareMap){
        lift = new Lift(hardwareMap);
        gripperA = new Gripper(hardwareMap, Gripper.Side.A);
        gripperB = new Gripper(hardwareMap, Gripper.Side.B);
    }

    public Action moveLift(double inches){
        lift.gotoHeightInches(inches);

        return telemetryPacket -> !lift.isBusy();
    }

    public Action grab(){
        if(!gripperA.closed)gripperA.close();
        if(!gripperB.closed)gripperB.close();

        return telemetryPacket -> false;
    }

    public Action placeOnBoard(){
        gripperA.open();
        gripperB.open();

        return telemetryPacket -> false;
    }
}