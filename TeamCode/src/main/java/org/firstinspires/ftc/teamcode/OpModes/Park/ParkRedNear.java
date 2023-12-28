package org.firstinspires.ftc.teamcode.OpModes.Park;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Payload.Payload;

@SuppressWarnings("unused")
@Autonomous
public class ParkRedNear extends LinearOpMode {
    static double POWER = 0.6;

    DcMotorEx leftFront;
    DcMotorEx leftBack;
    DcMotorEx rightBack;
    DcMotorEx rightFront;

    @Override
    public void runOpMode() throws InterruptedException {
        {
            leftFront = hardwareMap.get(DcMotorEx.class, "FL");
            leftBack = hardwareMap.get(DcMotorEx.class, "BL");
            rightBack = hardwareMap.get(DcMotorEx.class, "BR");
            rightFront = hardwareMap.get(DcMotorEx.class, "FR");

            leftFront.setDirection(DcMotor.Direction.REVERSE);
            leftBack.setDirection(DcMotor.Direction.REVERSE);
            rightBack.setDirection(DcMotor.Direction.FORWARD);
            rightFront.setDirection(DcMotor.Direction.FORWARD);

            leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }

        Payload payload = new Payload(hardwareMap, null);
        payload.pixelArm.wrist.toStorageAngle();

        waitForStart();

        leftFront .setPower(-POWER);
        leftBack  .setPower(+POWER);
        rightBack .setPower(-POWER);
        rightFront.setPower(+POWER);

        sleep(200);

        leftFront .setPower(+POWER);
        leftBack  .setPower(+POWER);
        rightBack .setPower(+POWER);
        rightFront.setPower(+POWER);

        sleep(900);

        leftFront .setPower(0);
        leftBack  .setPower(0);
        rightBack .setPower(0);
        rightFront.setPower(0);

        payload.pixelArm.wrist.toStorageAngle();
    }
}
