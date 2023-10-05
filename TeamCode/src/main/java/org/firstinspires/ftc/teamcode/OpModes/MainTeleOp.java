package org.firstinspires.ftc.teamcode.OpModes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Roadrunner.drive.OppyMecanumDrive;

@TeleOp
public class MainTeleOp extends OpMode {

    private OppyMecanumDrive drive;

    /**
     * User-defined init method
     * <p>
     * This method will be called once, when the INIT button is pressed.
     */
    @Override
    public void init() {
        Telemetry telemetry = new MultipleTelemetry(this.telemetry, FtcDashboard.getInstance().getTelemetry());

        drive = new OppyMecanumDrive(hardwareMap);
    }

    /**
     * User-defined loop method
     * <p>
     * This method will be called repeatedly during the period between when
     * the play button is pressed and when the OpMode is stopped.
     */
    @Override
    public void loop() {
        Pose2d gamepadPose = new Pose2d(
                gamepad1.left_stick_x,
                gamepad1.left_stick_y,
                gamepad1.right_stick_x);

        drive.setDrivePower(gamepadPose);

        telemetry.addLine("Chassis Debug Info")
                .addData("LastError", drive.getLastError())
                .addData("PoseEstimate", drive.getPoseEstimate())
                .addData("WheelVelocities", drive.getWheelVelocities());

        drive.update();
    }
}
