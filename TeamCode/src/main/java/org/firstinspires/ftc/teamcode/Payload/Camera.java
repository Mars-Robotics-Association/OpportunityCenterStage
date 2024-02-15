
package org.firstinspires.ftc.teamcode.Payload;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.checkerframework.checker.nullness.qual.Nullable;
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
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.List;

import javax.lang.model.type.NullType;

@Config
public class Camera {
    static Scalar BLUE_MIN = new Scalar(152, 133, 160);
    static Scalar BLUE_MAX = new Scalar(177, 255, 254);
    static Scalar RED_MIN  = new Scalar(0, 155, 144);
    static Scalar RED_MAX  = new Scalar(8, 225, 240);

    public enum SearchRegion{
        LEFT(75, 180, 200, 80),
        MIDDLE(350, 100, 200, 150),
        RIGHT(1000, 200, 300, 250);

        public double coverage = -1;
        final Point point;
        final Size size;

        SearchRegion(int x, int y, int w, int h){
            point = new Point(x, y);
            size = new Size(w, h);
        }
    }

    private static final class PropDetector implements VisionProcessor{
        @Override
        public void init(int width, int height, CameraCalibration calibration) {}

        public GameState gameState;

        Bitmap stagingBitmap = null;

        private final Mat rgb = new Mat();
        private final Mat hsv = new Mat();
        private final Mat blue = new Mat();
        private final Mat red = new Mat();
        private final Mat both = new Mat();
        private final Mat patch = new Mat();

        @Override
        public NullType processFrame(Mat input, long captureTimeNanos) {
            Core.rotate(input, input, Core.ROTATE_180);

            // strip the alpha channel, and why does it give us that??
            Imgproc.cvtColor(input, rgb, Imgproc.COLOR_RGBA2RGB);

            sendImage(rgb);

            // use the same trick as last year ig
            Imgproc.cvtColor(rgb, hsv, Imgproc.COLOR_RGB2HSV_FULL);

            Core.inRange(hsv, BLUE_MIN, BLUE_MAX, blue);
            Core.inRange(hsv, RED_MIN, RED_MAX, red);
            Core.add(blue, red, both);

            for (SearchRegion region : SearchRegion.values()) {
                Size size = region.size;

                Imgproc.getRectSubPix(both, size, region.point, patch);

                region.coverage = (double) Core.countNonZero(patch) /
                        size.height;
            }

            return null;
        }

        private void sendImage(Mat image){
            // there are much faster ways to do this, but FTCDashboard won't let us

            if (stagingBitmap == null)stagingBitmap = Bitmap.createBitmap(
                    rgb.width(), rgb.height(),
                    Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(image, stagingBitmap);
            FtcDashboard.getInstance().sendImage(stagingBitmap);
        }

        @Override
        public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {}
    }

    private final AprilTagProcessor aprilTag;
    private final PropDetector propDetector;

    public Camera(HardwareMap hardwareMap, GameState gameState) {
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
        propDetector.gameState = gameState;

        new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .enableLiveView(true)
                .setStreamFormat(VisionPortal.StreamFormat.YUY2)
                .addProcessor(aprilTag)
                .addProcessor(propDetector)
                .build();
    }

    public @Nullable Pose2d findTagWithID(int desiredTag){
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