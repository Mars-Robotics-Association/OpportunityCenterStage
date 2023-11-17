package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;


@TeleOp(name="GripperTestV2", group="Robot")


public class GripperTestV2 extends OpMode {

    //Declare OpMode members.
    public Servo leftGripperServo    = null;
    public Servo rightGripperServo   = null;
    public Servo    wristServo   = null;

    public static final double MID_SERVO   =  0.4;
    public static final double GRIPPER_SPEED  = 0.02 ;        // sets rate to move servo
    public static final double ARM_UP_POWER    =  0.50 ;   // Run arm motor up at 50% power
    public static final double ARM_DOWN_POWER  = -0.25 ;   // Run arm motor down at -25% power
    private boolean rightBumperState = true;
    private boolean leftBumperState = true;
    private boolean wristServoState = true;
    public double leftGripperOpen = .3;//position at which left gripper is open
    public double leftGripperClosed = .5; //position at which left gripper is closed
    public double rightGripperOpen = -.2;//position at which right gripper is open
    public double rightGripperClosed = -.5;//position at which right gripper is closed

    public double wristServoUp = .1;//position at which the wrist action is up
    public double wristServoDown = .8;//position at which the wrist action is down

    private boolean leftGripperPos = false; //closed
    private boolean rightGripperPos = false; //closed
    private boolean wristServoPos = false; //down
    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        telemetry.setAutoClear(false);
        // Define and initialize ALL installed servos.
        leftGripperServo  = hardwareMap.get(Servo.class, "leftGripperServo");
        rightGripperServo = hardwareMap.get(Servo.class, "rightGripperServo");
        wristServo = hardwareMap.get(Servo.class,"wristServo");
        leftGripperServo.setPosition(leftGripperClosed);
        rightGripperServo.setPosition(rightGripperClosed);
        wristServo.setPosition(wristServoDown);

        // Send telemetry message to signify robot waiting;
        telemetry.addData(">", "Robot Ready.  Press Play.");
        telemetry.addData("initial left Gripper Position",  leftGripperServo.getPosition());
        telemetry.addData("initial Right Gripper Position",  rightGripperServo.getPosition());
        telemetry.addData("initial Wrist Servo Position", wristServo.getPosition());

    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {


        // Use gamepad left & right Bumpers to open and close the gripper
        if (gamepad1.right_bumper) {
            if (rightBumperState) {
                rightGripperServo.setPosition(rightGripperClosed);
            }
            else {
                rightGripperServo.setPosition(rightGripperOpen);
            }
            rightBumperState = !rightBumperState;
        }

        if (gamepad1.left_bumper) {
            if (leftBumperState) {
                leftGripperServo.setPosition(leftGripperClosed);
            }
            else {
                leftGripperServo.setPosition(leftGripperOpen);
            }
            leftBumperState = !leftBumperState;
        }

        // Use gamepad buttons to move the arm up (Y) and down (A)
        //  if (gamepad1.y)
        //       liftMotor.setPower(ARM_UP_POWER);
        //    else if (gamepad1.a)
        //       liftMotor.setPower(ARM_DOWN_POWER);
        //    else
        //       liftMotor.setPower(0.0);*/

        // Send telemetry message to signify robot running;
        telemetry.addData("left Gripper Position",  leftGripperServo.getPosition());
        telemetry.addData("Right Gripper Position",  rightGripperServo.getPosition());
        telemetry.addData("Wrist Servo Position", wristServo.getPosition());

        //updateTelemetry();
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }
}
