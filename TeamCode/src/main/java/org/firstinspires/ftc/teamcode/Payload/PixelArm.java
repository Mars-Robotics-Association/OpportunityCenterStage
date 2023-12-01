package org.firstinspires.ftc.teamcode.Payload;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.hardware.DcMotor;
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
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }

        static class OutOfRange extends RuntimeException{
            @SuppressLint("DefaultLocale")
            OutOfRange(double attemptedHeight){
                super(String.format("Lift cannot safely achieve height of %f inches", attemptedHeight));
            }
        }

        public final DcMotor motor;

        // Derive these value from Calibration Mode
        // Inches to Encoder Ticks conversion
        private static final double TICKS_PER_INCH = 37.192307692307692307692307692308;
        // Maximum height allowed
        private static final double MAX_SAFE_INCHES = 20;

        /**
         * Raises the lift to a desired height.
         * @param inches Height measured from the ground in inches.
         * @throws OutOfRange Thrown when {@code inches} exceeds {@link #MAX_SAFE_INCHES} or less than 0.
         */
        public void setHeight(double inches) throws OutOfRange{
            if(inches < 0 || MAX_SAFE_INCHES < inches)throw new OutOfRange(inches);
            motor.setTargetPosition((int) (inches * TICKS_PER_INCH));
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

        public double getHeight(){
            return (double)(motor.getCurrentPosition()) / TICKS_PER_INCH;
        }

        public boolean isBusy(){
            return motor.isBusy();
        }
    }

    public static class Gripper{

        public enum Side{
            A("left_gripper", .4688, .4077),
            B("right_gripper", .0350, .1127);

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
        private static final double GROUND_POSITION = 0.0888;
        private static final double BOARD_POSITION = 0.2211;
        private static final double STORAGE_POSITION = 0.4422;
        private final Servo servo;

        Wrist(HardwareMap hardwareMap){
            this.servo = hardwareMap.servo.get("wrist_servo");
        }

        public void toGroundAngle(){
            servo.setPosition(GROUND_POSITION);
        }

        public void toBoardAngle(){
            servo.setPosition(BOARD_POSITION);
        }
        public void toStorageAngle(){
            servo.setPosition(STORAGE_POSITION);
        }
    }

    public PixelArm(HardwareMap hardwareMap){
        lift = new Lift(hardwareMap);
        wrist = new Wrist(hardwareMap);
        gripperA = new Gripper(hardwareMap, Gripper.Side.A);
        gripperB = new Gripper(hardwareMap, Gripper.Side.B);
    }
}