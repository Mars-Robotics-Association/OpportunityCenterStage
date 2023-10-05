package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@SuppressWarnings("unused")
@Autonomous
public class BasicParkRed extends LinearOpMode {
    public DcMotor motorFL;
    public DcMotor motorFR;
    public DcMotor motorBL;
    public DcMotor motorBR;

    public final double movePower = 0.4;
    public final int moveDuration = 4000;

    public LinearOpMode opMode;

    public void initHardware(LinearOpMode opMode) {
        motorFL = opMode.hardwareMap.dcMotor.get("FL");
        motorFR = opMode.hardwareMap.dcMotor.get("FR");
        motorBL = opMode.hardwareMap.dcMotor.get("BL");
        motorBR = opMode.hardwareMap.dcMotor.get("BR");

        motorFL.setDirection(DcMotor.Direction.REVERSE);
        motorFR.setDirection(DcMotor.Direction.FORWARD);
        motorBL.setDirection(DcMotor.Direction.REVERSE);
        motorBR.setDirection(DcMotor.Direction.FORWARD);
        /*
        All motors should move robot forward with positive motor power.
        Do not change direction or the hardware map for the drive motors.
         */
    }

    public void runOpMode() {
        initHardware(this);

        waitForStart();

        double power = movePower;

        motorFL.setPower(power);
        motorFR.setPower(-power);
        motorBL.setPower(-power);
        motorBR.setPower(power);

        sleep(moveDuration);

        motorFL.setPower(0);
        motorFR.setPower(0);
        motorBL.setPower(0);
        motorBR.setPower(0);
    }
}