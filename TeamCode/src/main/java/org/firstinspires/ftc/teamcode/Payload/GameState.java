package org.firstinspires.ftc.teamcode.Payload;

public final class GameState {
    public SignalState signalState;
    public TeamColor teamColor;
    public ParkSpot parkSpot;
    //can we add a separate variable for park location vs start location?

    public enum SignalState {LEFT, MIDDLE, RIGHT}

    public enum TeamColor {BLUE, RED}

    public enum ParkSpot {NEAR, FAR}
}