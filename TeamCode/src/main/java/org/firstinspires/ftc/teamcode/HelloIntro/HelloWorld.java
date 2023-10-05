package org.firstinspires.ftc.teamcode.HelloIntro;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class HelloWorld extends OpMode {
    /**
     * User-defined init method
     * <p>
     * This method will be called once, when the INIT button is pressed.
     */
    @Override
    public void init() {

    }

    /**
     * User-defined loop method
     * <p>
     * This method will be called repeatedly during the period between when
     * the play button is pressed and when the OpMode is stopped.
     */
    @Override
    public void loop() {
        telemetry.addLine("Hi Opportunity!! 👋👋👋");
        telemetry.update();
    }
}
