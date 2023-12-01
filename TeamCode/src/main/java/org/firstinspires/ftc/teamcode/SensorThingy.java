package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Payload.Payload;

@SuppressWarnings("unused")
public final class SensorThingy extends OpMode {
    private MultipleTelemetry mTelemetry;

    public enum ColorSensorChoice{
        YELLOW(1,1,0, Payload.GameState.TeamColor.BLUE, Payload.GameState.StartSlot.CLOSER ),
        GREEN (0,1,1, Payload.GameState.TeamColor.BLUE, Payload.GameState.StartSlot.FARTHER),
        BLACK (0,0,0, Payload.GameState.TeamColor.RED , Payload.GameState.StartSlot.CLOSER ),
        WHITE (1,1,1, Payload.GameState.TeamColor.RED , Payload.GameState.StartSlot.FARTHER);

        public final double r1, g1, b1;

        public double lastDistance = 0;

        public final Payload.GameState.TeamColor teamColor;
        public final Payload.GameState.StartSlot startSlot;

        void updateDistance(double r2, double g2, double b2){
            double r3 = (r1 - r2);
            double g3 = (g1 - g2);
            double b3 = (b1 - b2);

            lastDistance = Math.sqrt(r3 * r3 + g3 * g3 + b3 * b3);
        }

        ColorSensorChoice(double r, double g, double b,
                          Payload.GameState.TeamColor teamColor,
                          Payload.GameState.StartSlot startSlot){
            this.r1 = r;
            this.g1 = g;
            this.b1 = b;
            this.teamColor = teamColor;
            this.startSlot = startSlot;
        }
    }



    @Override
    public void init() {
        mTelemetry = new MultipleTelemetry(super.telemetry, FtcDashboard.getInstance().getTelemetry());

    }

    @Override
    public void loop() {

    }
}
