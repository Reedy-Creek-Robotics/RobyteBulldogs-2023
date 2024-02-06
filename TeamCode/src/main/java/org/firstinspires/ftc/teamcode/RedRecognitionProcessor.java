package org.firstinspires.ftc.teamcode;

import android.graphics.Canvas;
import android.util.Log;

import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class RedRecognitionProcessor implements VisionProcessor {

    static final Scalar BLUE = new Scalar(0, 0, 255);
    static final Scalar RED = new Scalar(255, 0, 0);
    static final Scalar PURPLE = new Scalar(255, 0, 255);
    static final Scalar GREEN = new Scalar(0, 255, 0);

    // 3, 168, 107
    static final Scalar LOW_RED1 = new Scalar(160, 130, 20);
    static final Scalar HIGH_RED1 = new Scalar(180, 255, 255);
    static final Scalar LOW_RED2 = new Scalar(0, 130, 20);
    static final Scalar HIGH_RED2 = new Scalar(20, 255, 255);

    public String runAuto = "none";

    /*
     * The core values which define the location and size of the sample regions
     */
    public static Point REGION1_TOPLEFT_ANCHOR_POINT = new Point(15,100);
    public static Point REGION2_TOPLEFT_ANCHOR_POINT = new Point(130, 50);
    public static Point REGION3_TOPLEFT_ANCHOR_POINT = new Point(240,100);

    static int REGION_WIDTH = 120;
    static int REGION_HEIGHT = 100;


    /*
     * Points which actually define the sample region rectangles, derived from above values
     *
     * Example of how points A and B work to define a rectangle
     *
     *   ------------------------------------
     *   | (0,0) Point A                    |
     *   |                                  |
     *   |                                  |
     *   |                                  |
     *   |                                  |
     *   |                                  |
     *   |                                  |
     *   |               Point B (640, 480) |
     *   ------------------------------------
     *
     */
    Point region1_pointA = new Point(
            REGION1_TOPLEFT_ANCHOR_POINT.x,
            REGION1_TOPLEFT_ANCHOR_POINT.y);
    Point region1_pointB = new Point(
            REGION1_TOPLEFT_ANCHOR_POINT.x + REGION_WIDTH,
            REGION1_TOPLEFT_ANCHOR_POINT.y + REGION_HEIGHT);

    Point region2_pointA = new Point(
            REGION2_TOPLEFT_ANCHOR_POINT.x + REGION_WIDTH + 10,
            REGION2_TOPLEFT_ANCHOR_POINT.y);
    Point region2_pointB = new Point(
            REGION2_TOPLEFT_ANCHOR_POINT.x + (2 * REGION_WIDTH) + 10,
            REGION2_TOPLEFT_ANCHOR_POINT.y + REGION_HEIGHT);

    Point region3_pointA = new Point(
            REGION3_TOPLEFT_ANCHOR_POINT.x + (2 * REGION_WIDTH) + 20,
            REGION3_TOPLEFT_ANCHOR_POINT.y);
    Point region3_pointB = new Point(
            REGION3_TOPLEFT_ANCHOR_POINT.x + (3 * REGION_WIDTH) + 20,
            REGION3_TOPLEFT_ANCHOR_POINT.y + REGION_HEIGHT);

    /*
     * Working variables
     */
    Mat region1;
    Mat region2;
    Mat region3;
    Mat hsvMat = new Mat();
    int nonZero1 = 0;
    int nonZero2 = 0;
    int nonZero3 = 0;

    private boolean initialized = false;

    public boolean isInitialized() {
        return initialized;
    }

    public int getNonZero1() {
        return nonZero1;
    }

    @Override
    public void init(int width, int height, CameraCalibration calibration) {

    }

    @Override
    public Object processFrame(Mat frame, long captureTimeNanos) {
        Imgproc.cvtColor(frame, hsvMat, Imgproc.COLOR_RGB2HSV);
        if( !initialized ) {
            Log.d("OPENCV_BOUNDS", hsvMat.width() + ", " + hsvMat.height());
            region1 = hsvMat.submat(new Rect(region1_pointA, region1_pointB));
            region2 = hsvMat.submat(new Rect(region2_pointA, region2_pointB));
            region3 = hsvMat.submat(new Rect(region3_pointA, region3_pointB));

            this.initialized = true;
            Log.d("OPENCV", "INITIALIZED");
        }
        Mat inRangeMat1 = new Mat();
        Mat inRangeMat2 = new Mat();
        Mat inRangeMat3 = new Mat();
        Mat inRangeMat4 = new Mat();
        Mat inRangeMat5 = new Mat();
        Mat inRangeMat6 = new Mat();
        Core.inRange(region1, LOW_RED1, HIGH_RED1, inRangeMat1);
        Core.inRange(region2, LOW_RED1, HIGH_RED1, inRangeMat2);
        Core.inRange(region3, LOW_RED1, HIGH_RED1, inRangeMat3);
        Core.inRange(region1, LOW_RED2, HIGH_RED2, inRangeMat4);
        Core.inRange(region2, LOW_RED2, HIGH_RED2, inRangeMat5);
        Core.inRange(region3, LOW_RED2, HIGH_RED2, inRangeMat6);
        nonZero1 = (int) Core.countNonZero(inRangeMat1) + Core.countNonZero(inRangeMat4);
        nonZero2 = (int) Core.countNonZero(inRangeMat2) + Core.countNonZero(inRangeMat5);
        nonZero3 = (int) Core.countNonZero(inRangeMat3) + Core.countNonZero(inRangeMat6);

        int sensitivity = 2000;

        Imgproc.rectangle(
                frame,
                region1_pointA,
                region1_pointB,
                (nonZero1 > sensitivity) ? GREEN : BLUE,
                2
        );

        Imgproc.rectangle(
                frame,
                region2_pointA,
                region2_pointB,
                (nonZero2 > sensitivity) ? GREEN : BLUE,
                2
        );

        Imgproc.rectangle(
                frame,
                region3_pointA,
                region3_pointB,
                (nonZero3 > sensitivity) ? GREEN : BLUE,
                2
        );

        /*
         * Now that we found the max, we actually need to go and
         * figure out which sample region that value was from
         */

        if(nonZero1 > sensitivity) // Was it from region 1?
        {
            Log.d("OPENCV", "LEFT-TOP");
            runAuto = "left";
            Imgproc.rectangle(
                    frame,
                    region1_pointA,
                    region1_pointB,
                    GREEN,
                    3
            );
        }
        else if(nonZero2 > sensitivity) // Was it from region 2?
        {
            Log.d("OPENCV", "TOP-CENTER");
            runAuto = "center";
            Imgproc.rectangle(
                    frame,
                    region2_pointA,
                    region2_pointB,
                    GREEN,
                    3
            );
        }
        else if(nonZero3 > sensitivity) // Was it from region 3?
        {
            Log.d("OPENCV", "TOP-RIGHT");
            runAuto = "right";
            Imgproc.rectangle(
                    frame,
                    region3_pointA,
                    region3_pointB,
                    GREEN,
                    3
            );
        }
        else{
            Log.d("OPENCV", "UNKNOWN");
        }
        return frame;
    }

    @Override
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {

    }
}