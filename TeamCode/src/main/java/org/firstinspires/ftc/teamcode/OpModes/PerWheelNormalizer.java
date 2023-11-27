package org.firstinspires.ftc.teamcode.OpModes;

import android.annotation.SuppressLint;

import com.acmerobotics.roadrunner.ftc.Encoder;
import com.acmerobotics.roadrunner.ftc.OverflowEncoder;
import com.acmerobotics.roadrunner.ftc.RawEncoder;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@SuppressWarnings("unused")
public class PerWheelNormalizer extends LinearOpMode {
    static double ENCODER_RESOLUTION = 10;

    static double LEARNING_RATE = 0.0001;

    private class MotorWrapper{
        public final String name;

        MotorWrapper(String name){
            this.name = name;
            motor = (DcMotorEx) hardwareMap.dcMotor.get(name);

            encoder = new OverflowEncoder(new RawEncoder(motor));
        }

        public double error = 0;

        private final DcMotorEx motor;

        private final Encoder encoder;

        double getPower(){return motor.getPower();}
        void setPower(double power){motor.setPower(power);}

        int getSpeed(){
            return encoder.getPositionAndVelocity().velocity;
        }
    }
    MotorWrapper[] allMotors = null;
    MotorWrapper slowestMotor = null;

    static double toRPM(int ticksPerSecond){
        return (ticksPerSecond * 60) / ENCODER_RESOLUTION;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void runOpMode() {
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
            int speed = Math.abs(motor.getSpeed());

            if (speed < slowestSpeed) {
                slowestSpeed = speed;
                slowestMotor = motor;
            }
        }

        telemetry.update();

        while(opModeIsActive()){
            if(gamepad1.x || gamepad2.x)break;

            telemetry.update();

            telemetry.addLine(String.format("Slowest motor is %s at %.3f RPM",
                    slowestMotor.name, toRPM(slowestMotor.getSpeed())));

            telemetry.addLine("Press [X] to stop training");
            telemetry.addLine("| Name | Power | Error | RPM");

            double errorSum = 0;

            for (MotorWrapper motor : allMotors) {
                if (motor == slowestMotor)continue;

                int speed = motor.getSpeed();

                double error = motor.error = (speed - slowestSpeed);
                errorSum += error;

                double power = motor.getPower();

                motor.setPower(power - error * LEARNING_RATE);

                telemetry.addLine(String.format(
                        "|  %s  | %.5f | %.5f | %.5f",
                        motor.name, power, error, toRPM(speed)));
            }

            telemetry.addData("Average Error", errorSum / allMotors.length);
        }

        while(opModeIsActive())sleep(40);
    }
}
