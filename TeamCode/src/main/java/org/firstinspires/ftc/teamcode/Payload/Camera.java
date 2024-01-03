package org.firstinspires.ftc.teamcode.Payload;

import android.graphics.Canvas;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase;
import org.firstinspires.ftc.vision.apriltag.AprilTagPoseFtc;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.List;

@Config
public class Camera {
    static class HSVColor{
        HSVColor(double h, double s, double v){
            this.h = h;
            this.s = s;
            this.v = v;
        }

        double h;
        double s;
        double v;

        Scalar toScalar(){
            return new Scalar(h, s, v);
        }
    }

    @Config
    public static class ColorSettings{
        static HSVColor BLUE_MIN = new HSVColor(0, 0, 0);
        static HSVColor BLUE_MAX = new HSVColor(0, 0, 0);
        static HSVColor RED_MIN  = new HSVColor(0, 0, 0);
        static HSVColor RED_MAX  = new HSVColor(0, 0, 0);
    }

    static class SearchRegion{
        SearchRegion(Point point, Size size, double threshold){
            this.point = point;
            this.size = size;
            this.threshold = threshold;
        }

        Point point;
        Size size;
        double threshold;

        boolean foundProp;
    }

    @Config
    public static class SearchRegions{
        public static SearchRegion[] getAllRegions(){
             return new SearchRegion[]{LEFT, MIDDLE, RIGHT};
        }

        public static SearchRegion LEFT = new SearchRegion(
                new Point(), new Size(), .6);
        public static SearchRegion MIDDLE = new SearchRegion(
                new Point(), new Size(), .6);
        public static SearchRegion RIGHT = new SearchRegion(
                new Point(), new Size(), .6);
    }

    public Action waitForNextScan(){
        return propDetector.waitForNextScan();
    }

    private static final class PropDetector implements VisionProcessor{
        private final Mat rgb = new Mat();
        private final Mat hsv = new Mat();
        private final Mat patch = new Mat();
        private final Mat inRange = new Mat();

        @Override
        public void init(int width, int height, CameraCalibration calibration) {

        }

        public GameState.TeamColor teamColor = GameState.TeamColor.BLUE;

        public Action waitForNextScan(){
            hasScanned = false;
            return _$ -> !hasScanned;
        }

        boolean hasScanned = false;

        // Returns a threshold
        private void testRegion(SearchRegion region){
            Size size = region.size;

            // cut the desired region ✂️
            Imgproc.getRectSubPix(patch, size, region.point, hsv);

            // use our color ranges (team-dependent) to create a binary image 🔴🟢🔵 -> ✔️✖️
            // inRange(x, y) <- TEAM_COLOR_MIN <= patch(x, y) <= TEAM_COLOR_MAX
            if(teamColor == GameState.TeamColor.BLUE)
                Core.inRange(patch, ColorSettings.BLUE_MIN.toScalar(), ColorSettings.BLUE_MAX.toScalar(), inRange);
            else
                Core.inRange(patch, ColorSettings.RED_MIN.toScalar(), ColorSettings.RED_MAX.toScalar(), inRange);

            double totalPixels = size.width * size.height;

            // Count the pixels from before but much faster than the Orion-way
            double confidence = (double) Core.countNonZero(inRange) / totalPixels;

            region.foundProp = confidence >= region.threshold;
        }

        @Override
        public Object processFrame(Mat input, long captureTimeNanos) {
            // strip the alpha channel, and why does it give us this??
            Imgproc.cvtColor(input, rgb, Imgproc.COLOR_RGBA2RGB);

            // use the same trick as last year ig
            Imgproc.cvtColor(rgb, hsv, Imgproc.COLOR_RGB2HSV);

            testRegion(SearchRegions.LEFT);
            testRegion(SearchRegions.MIDDLE);
            testRegion(SearchRegions.RIGHT);

            return null;
        }

        @Override
        public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {

        }
    }

    private final AprilTagProcessor aprilTag;
    private final PropDetector propDetector;

    public Camera(HardwareMap hardwareMap, GameState.TeamColor teamColor) {
        aprilTag = new AprilTagProcessor.Builder()
                .setDrawAxes(true)
                .setDrawCubeProjection(true)
                .setDrawTagOutline(true)
                .setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
                .setTagLibrary(AprilTagGameDatabase.getCenterStageTagLibrary())
                .setOutputUnits(DistanceUnit.INCH, AngleUnit.RADIANS)
                .build();

        aprilTag.setDecimation(3);

        propDetector = new PropDetector();
        propDetector.teamColor = teamColor;

        new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .enableLiveView(true)
                .setStreamFormat(VisionPortal.StreamFormat.YUY2)
                .addProcessor(aprilTag)
                .addProcessor(propDetector)
                .build();
    }

    public Pose2d findTagWithID(int desiredTag){
        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        for (AprilTagDetection detection : currentDetections)
            if ((detection.ftcPose != null) && (detection.id == desiredTag)){
                AprilTagPoseFtc ftcPose = detection.ftcPose;

                                    // can you stop?
                return new Pose2d(ftcPose.y, ftcPose.x, ftcPose.bearing);
            }
        return null;
    }
}
