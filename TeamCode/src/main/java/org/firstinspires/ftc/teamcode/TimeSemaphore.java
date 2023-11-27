package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public final class TimeSemaphore {
    private final OpMode opMode;
    private final double duration;
    private double lastTimestamp;

    public TimeSemaphore(OpMode opMode, double duration){
        this.opMode = opMode;
        this.duration = duration;

        this.lastTimestamp = Double.NEGATIVE_INFINITY;
    }

    public boolean poll(){
        if(opMode.getRuntime() - lastTimestamp <= duration)return false;

        lastTimestamp = opMode.getRuntime();
        return true;
    }
}
