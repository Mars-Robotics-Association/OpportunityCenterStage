package org.firstinspires.ftc.teamcode.Payload;

public final class GameState {
    public SignalState signalState;
    public TeamColor teamColor;

    public enum SignalState {LEFT, MIDDLE, RIGHT}

    public enum TeamColor {
        BLUE(1), RED(-1);

        public final double flipY;

        TeamColor(double flipY) {
            this.flipY = flipY;
        }
    }
}
