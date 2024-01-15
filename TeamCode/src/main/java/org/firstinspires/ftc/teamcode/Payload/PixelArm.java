package org.firstinspires.ftc.teamcode.Payload;

import com.acmerobotics.roadrunner.Action;
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
            motor.setTargetPosition(0);
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }

        public final DcMotor motor;

        // Derive these value from Calibration Mode
        // Inches to Encoder Ticks conversion
        private static final double TICKS_PER_INCH = 37.192307692307692307692307692308;

        private static final double GAIN = 1;

        public Action setHeight(double inches){ //for teleop
            return telemetryPacket -> {
                double position = motor.getCurrentPosition() / TICKS_PER_INCH;

                double error = position - inches;

                if(Math.abs(error) > 1){
                    motor.setPower(-error * GAIN);
                    return true;
                }else{
                    motor.setPower(0);
                    return false;
                }
            };
        }

        public void setLiftHeight(double inches){ //for auto
            //motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motor.setPower(0.5);
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motor.setTargetPosition((int)(inches*TICKS_PER_INCH));
        }
    }

    public static class Gripper{

        public enum Side{
            A("left_gripper", .2444, .4172),
            B("right_gripper", .7450, .6056);
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
        private static final double GROUND_POSITION = 0.201666667;
        private static final double BOARD_POSITION = .3728;
        private static final double STORAGE_POSITION = BOARD_POSITION;
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