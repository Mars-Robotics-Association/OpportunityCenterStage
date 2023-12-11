package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public final class utils {

    public static double boolsToDir(boolean positive, boolean negative, double multiplier){
        double value = 0;

        if(positive)value += multiplier;
        if(negative)value -= multiplier;

        return value;
    }

    public static final class Debouncer {
        private final OpMode opMode;
        private final double duration;
        private double lastTimestamp;

        public Debouncer(OpMode opMode, double duration){
            this.opMode = opMode;
            this.duration = duration;

            this.lastTimestamp = Double.NEGATIVE_INFINITY;
        }

        public boolean poll(){
            if(opMode.getRuntime() - lastTimestamp <= duration)return false;

            lastTimestamp = opMode.getRuntime();
            return true;
        }

        public boolean poll(boolean stack){
            if(stack)return poll();
            return false;
        }
    }
}
