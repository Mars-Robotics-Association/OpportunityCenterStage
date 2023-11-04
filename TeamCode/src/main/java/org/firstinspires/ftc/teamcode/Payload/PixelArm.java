package org.firstinspires.ftc.teamcode.Payload;

import android.annotation.SuppressLint;

import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public final class PixelArm {
    private final Lift lift;
    private final Gripper gripperA;
    private final Gripper gripperB;

    private static class Lift{
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
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motor.setTargetPosition((int) (inches * TICKS_PER_INCH));
        }

        boolean isBusy(){
            return motor.isBusy();
        }
    }

    public static class Gripper{

        private static final double OPEN_POSITION = 0.6;

        public enum Side{
            A(false, "left_gripper", -2.5),
            B(false, "right_gripper", 2.5);

            public final Servo.Direction direction;
            public final String name;
            public final double offset;

            Side(boolean inverted, String name, double offset) {
                this.direction = inverted ? Servo.Direction.REVERSE : Servo.Direction.FORWARD;
                this.name = name;
                this.offset = offset;
            }
        }

        private final Servo servo;

        Gripper(HardwareMap hardwareMap, Side side){
            this.servo = hardwareMap.servo.get(side.name);
            this.side = side;
        }

        public final Side side;

        private boolean closed = false;

        void open(){
            servo.setPosition(OPEN_POSITION);
            closed = false;
        }

        void close(){
            servo.setPosition(0.5);
            closed = true;
        }
    }

    public PixelArm(HardwareMap hardwareMap){
        lift = new Lift(hardwareMap);
        gripperA = new Gripper(hardwareMap, Gripper.Side.A);
        gripperB = new Gripper(hardwareMap, Gripper.Side.B);
    }

    public Action moveLift(double inches){
        return telemetryPacket -> {
            lift.gotoHeightInches(inches);

            return !lift.isBusy();
        };
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