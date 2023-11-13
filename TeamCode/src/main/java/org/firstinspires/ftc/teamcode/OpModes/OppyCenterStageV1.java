package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;


@TeleOp(name="OppyCenterStageV1", group="Robot")

public class OppyCenterStageV1 extends OpMode {

    /* Declare OpMode members. */
    public DcMotor leftFront   = null;
    public DcMotor  rightFront  = null;
    public DcMotor  leftRear   = null;
    public DcMotor  rightRear     = null;
    public DcMotor  liftMotor     = null;
    public Servo leftGripperServo    = null;
    public Servo    rightGripperServo   = null;
    public Servo    wristServo   = null;



    double clawOffset = 0;

    public static final double MID_SERVO   =  0.5 ;
    public static final double CLAW_SPEED  = 0.02 ;        // sets rate to move servo
    public static final double ARM_UP_POWER    =  0.50 ;   // Run arm motor up at 50% power
    public static final double ARM_DOWN_POWER  = -0.25 ;   // Run arm motor down at -25% power
    private boolean rightBumperState = false;
    private boolean leftBumperState = false;
    public double leftGripperOpen = .5;
    public double leftGripperClosed = .8;
    public double rightGripperOpen = .5;
    public double rightGripperClosed = .8;
    private boolean leftGripperPos = false; //closed
    private boolean rightGripperPos = false; //closed

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        // Define and Initialize Motors
        leftFront  = hardwareMap.get(DcMotor.class, "FL");
        rightFront = hardwareMap.get(DcMotor.class, "FR");
        leftRear  = hardwareMap.get(DcMotor.class, "BL");
        rightRear = hardwareMap.get(DcMotor.class, "BR");
        //liftMotor  = hardwareMap.get(DcMotor.class, "liftMotor");

        // To drive forward, most robots need the motor on one side to be reversed, because the axles point in opposite directions.
        // Pushing the left and right sticks forward MUST make robot go forward. So adjust these two lines based on your first test drive.
        // Note: The settings here assume direct drive on left and right wheels.  Gear Reduction or 90 Deg drives may require direction flips
        leftFront.setDirection(DcMotor.Direction.REVERSE);
        rightFront.setDirection(DcMotor.Direction.FORWARD);
        leftRear.setDirection(DcMotor.Direction.REVERSE);
        rightRear.setDirection(DcMotor.Direction.FORWARD);

        // If there are encoders connected, switch to RUN_USING_ENCODER mode for greater accuracy
        // leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        // rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Define and initialize ALL installed servos.
            /*leftGripperServo  = hardwareMap.get(Servo.class, "leftGripperServo");
            rightGripperServo = hardwareMap.get(Servo.class, "rightGripperServo");
            leftGripperServo.setPosition(MID_SERVO);
            rightGripperServo.setPosition(MID_SERVO);*/

        // Send telemetry message to signify robot waiting;
        telemetry.addData(">", "Robot Ready.  Press Play.");    //
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
        double y = -gamepad1.left_stick_y; // Remember, this is reversed!
        double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
        double rx = gamepad1.right_stick_x;

            /* Denominator is the largest motor power (absolute value) or 1
            This ensures all the powers maintain the same ratio */
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = (y + x + rx) / denominator;
        double backLeftPower = (y - x + rx) / denominator;
        double frontRightPower = (y - x - rx) / denominator;
        double backRightPower = (y + x - rx) / denominator;

        leftFront.setPower(frontLeftPower);
        leftRear.setPower(backLeftPower);
        rightFront.setPower(frontRightPower);
        rightRear.setPower(backRightPower);


        // Use gamepad left & right Bumpers to open and close the claw
            /*if (gamepad1.right_bumper) {
                if (rightBumperState) {
                    clawOffset -= CLAW_SPEED;  //set positions instead
                }
                else {
                    clawOffset += CLAW_SPEED;
                }

                rightBumperState = !rightBumperState;
            }

            if (gamepad1.left_bumper) {
                if (leftBumperState) {
                    clawOffset -= CLAW_SPEED;
                }
                else {
                    clawOffset += CLAW_SPEED;
                }
                leftBumperState = !leftBumperState;
            }

            // Move both servos to new position.  Assume servos are mirror image of each other.
            clawOffset = Range.clip(clawOffset, -0.5, 0.5);
            leftGripperServo.setPosition(MID_SERVO + clawOffset);
            rightGripperServo.setPosition(MID_SERVO - clawOffset);

            // Use gamepad buttons to move the arm up (Y) and down (A)
            if (gamepad1.y)
                liftMotor.setPower(ARM_UP_POWER);
            else if (gamepad1.a)
                liftMotor.setPower(ARM_DOWN_POWER);
            else
                liftMotor.setPower(0.0);*/

        // Send telemetry message to signify robot running;
        //telemetry.addData("gripper",  "Offset = %.2f", clawOffset);
        //telemetry.addData("left",  "%.2f", left);
        //telemetry.addData("right", "%.2f", right);
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }
}
