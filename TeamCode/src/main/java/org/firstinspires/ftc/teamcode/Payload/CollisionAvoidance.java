package org.firstinspires.ftc.teamcode.Payload;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class CollisionAvoidance {
    static double RAYMARCH_QUALITY = .25;

    private final Payload payload;
    Class<DistanceSensor> SENSOR = DistanceSensor.class;

    static class Rect{
        private final double x1;
        private final double y1;
        private final double x2;
        private final double y2;
        private final boolean invertArea;

        Rect(double x1, double y1, double x2, double y2, boolean invertArea){
            this.x1 = Math.min(x1, x2);
            this.y1 = Math.min(y1, y2);
            this.x2 = Math.max(x1, x2);
            this.y2 = Math.max(y1, y2);
            this.invertArea = invertArea;
        }

        boolean contains(Vector2d point){
            boolean inRect =
                    x1 <= point.x &&
                    x2 >= point.x &&
                    y1 <= point.y &&
                    y2 >= point.y;

            return inRect ^ invertArea;

        }
    }

    Rect[] rects = new Rect[]{
            new Rect(72,72,-72,-72, true),
            new Rect(60,48,72,24, false),
            new Rect(72,72,0,72, false),
    };
    
    static class Sensor{
        private final String name;
        private final Vector2d offset;

        Sensor(String name, double x, double y){
            this.name = name;
            this.offset = new Vector2d(x, y);
        }
        
        DistanceSensor device;
    }

    Sensor[] sensors = new Sensor[]{
            new Sensor("fl_sensor", -5, 7),
            new Sensor("fr_sensor", 5, 7)
    };

    CollisionAvoidance(Payload payload){
        this.payload = payload;

        HardwareMap hardwareMap = payload.hardwareMap;
        
        for (Sensor sensor : sensors)
            sensor.device = hardwareMap
                    .get(SENSOR, sensor.name);
    }

    private double simulateReading(Sensor sensor){
        Pose2d pose = payload.drive.pose;

        Vector2d ro = pose.times(sensor.offset);
        Vector2d rd = pose.heading.vec();

        double maxDistance = 72 * RAYMARCH_QUALITY;
        double stepSize = 1 / RAYMARCH_QUALITY;

        for (double d = 0; d < maxDistance; d += stepSize) {
            Vector2d point = ro.plus(rd.times(d));

            for (Rect rect : rects)
                if(rect.contains(point))return d;
        }

        return Double.POSITIVE_INFINITY;
    }

    public boolean shouldStop(double minimumDistance){
        if(payload.drive.pose.position.x < 0)
            return false;

        for (Sensor sensor : sensors) {
            double real = sensor.device.getDistance(DistanceUnit.INCH);

            double simulated = simulateReading(sensor);

            if(real < simulated && real <= minimumDistance)
                return true;
        }

        return false;
    }
}
