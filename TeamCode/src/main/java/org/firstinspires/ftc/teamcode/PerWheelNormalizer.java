package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.ftc.Encoder;
import com.acmerobotics.roadrunner.ftc.OverflowEncoder;
import com.acmerobotics.roadrunner.ftc.RawEncoder;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.VoltageSensor;

public class PerWheelNormalizer extends LinearOpMode {
    static double LEARNING_RATE = 0.01;

    private class MotorWrapper{
        MotorWrapper(String name){
            motor = (DcMotorEx) hardwareMap.dcMotor.get(name);

            encoder = new OverflowEncoder(new RawEncoder(motor));
        }

        private final DcMotorEx motor;

        private final Encoder encoder;

        void setPower(double power){
            motor.setPower(power);
        }

        double getSpeed(){
            return (double)encoder.getPositionAndVelocity().velocity / voltageSensor.getVoltage();
        }
    }

    VoltageSensor voltageSensor = null;
    MotorWrapper[] allMotors = null;
    MotorWrapper fastestMotor = null;

    double getVoltage(){
        return voltageSensor.getVoltage();
    }

    @Override
    public void runOpMode() {
        voltageSensor = hardwareMap.voltageSensor.iterator().next();

        allMotors = new MotorWrapper[]{
                new MotorWrapper("TL"),
                new MotorWrapper("TR"),
                new MotorWrapper("BL"),
                new MotorWrapper("BR"),
        };

        for (MotorWrapper motor : allMotors) {
            motor.setPower(1);
        }

        telemetry.addLine("Determining slowest motor");

        sleep(500);

        double slowestSpeed = Double.POSITIVE_INFINITY;

        for (MotorWrapper motor : allMotors) {
            double speed = Math.abs(motor.getSpeed());

            if (speed < slowestSpeed) {
                slowestSpeed = speed;
                fastestMotor = motor;
            }
        }

        telemetry.update();

        while(opModeIsActive()){
            telemetry.addLine("Determining fastest motor");
        }
    }
}
