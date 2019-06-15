package group14.opencv.detectors.robot_detector;

import com.google.common.util.concurrent.AtomicDouble;
import group14.opencv.detectors.Detector;
import group14.opencv.utils.ImageProcessUtils;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RobotDetector extends Detector<RobotDetectorResult, RobotDetector.Config> {

    public static class Config {
        // Color 2 - Light green
        public AtomicInteger minRedColorGreen = new AtomicInteger(90);
        public AtomicInteger maxRedColorGreen = new AtomicInteger(190);

        public AtomicInteger minGreenColorGreen = new AtomicInteger(205);
        public AtomicInteger maxGreenColorGreen = new AtomicInteger(255);

        public AtomicInteger minBlueColorGreen = new AtomicInteger(165);
        public AtomicInteger maxBlueColorGreen = new AtomicInteger(255);

        // Color 1 - Light blue
        public AtomicInteger minRedColorBlue = new AtomicInteger(0);
        public AtomicInteger maxRedColorBlue = new AtomicInteger(63);
        public AtomicInteger minGreenColorBlue = new AtomicInteger(94);
        public AtomicInteger maxGreenColorBlue = new AtomicInteger(255);
        public AtomicInteger minBlueColorBlue = new AtomicInteger(157);
        public AtomicInteger maxBlueColorBlue = new AtomicInteger(255);
    }

    double camHeight = 165;
    double robotFrontHeight = 28;
    double robotBackHeight = 27;

    /*
    // Color 2 - Green
    // BGR: 49, 134, 25
    int minRed_Color2 = 70;
    int maxRed_Color2 = 150;

    int minGreen_Color2 = 150;
    int maxGreen_Color2 = 255;

    int minBlue_Color2 = 70;
    int maxBlue_Color2 = 150;

    // Color 1 - Blue
    //BGR: 199, 41, 29
    int minRed_Color1 = 0;
    int maxRed_Color1 = 130;

    int minGreen_Color1 = 0;
    int maxGreen_Color1 = 130;

    int minBlue_Color1 = 160;
    int maxBlue_Color1 = 255;*/

    @Override
    public RobotDetectorResult run(Mat src) {
        var out = new Mat();
        src.copyTo(out);

        //Blur mat to better define objects
        var blurred = ImageProcessUtils.blur(src, 5);

        var config = this.getConfig();

        var threshBlue = this.threshold(blurred, new Scalar(config.minBlueColorBlue.get(), config.minGreenColorBlue.get(), config.minRedColorBlue.get()), new Scalar(config.maxBlueColorBlue.get(), config.maxGreenColorBlue.get(), config.maxRedColorBlue.get()));

        var threshGreen = this.threshold(blurred, new Scalar(config.minBlueColorGreen.get(), config.minGreenColorGreen.get(), config.minRedColorGreen.get()), new Scalar(config.maxBlueColorGreen.get(), config.maxGreenColorGreen.get(), config.maxRedColorGreen.get()));


        // Color 1 - blue
        var frontPoints = this.getPointsWithColor(threshBlue);

        // Color 2 - green
        var backPoints = this.getPointsWithColor(threshGreen);

        //front is index 0

        Point front = null;
        Point back = null;
        if (! frontPoints.isEmpty() && ! backPoints.isEmpty()) {
            front = frontPoints.get(0);
            back = backPoints.get(0);

            Imgproc.circle(out, front, 1, new Scalar(0, 100, 100), 3, 8, 0);
            Imgproc.circle(out, front, 5, new Scalar(255, 0, 255), 3, 8, 0);

            Imgproc.circle(out, back, 1, new Scalar(0, 100, 100), 3, 8, 0);
            Imgproc.circle(out, back, 5, new Scalar(255, 0, 255), 3, 8, 0);
        }

        return new RobotDetectorResult(out, threshBlue, threshGreen, front, back);
    }

    @Override
    protected Config createConfig() {
        return new Config();
    }

    private List<Point> getPointsWithColor(Mat frame) {
        //! [houghcircles]
        Mat circlesFrame = new Mat();
        Imgproc.HoughCircles(frame, circlesFrame, Imgproc.HOUGH_GRADIENT, 2, 10, 80, 34, 10, 40);

        Point imgCenter = new Point(frame.width() / 2, frame.height() / 2);

        var points = new ArrayList<Point>();
        //! [draw] Color 1
        for (int x = 0; x < circlesFrame.cols(); x++) {
            double[] c = circlesFrame.get(0, x);
            Point center = new Point(Math.round(c[0]), Math.round(c[1]));
            Point finalCenter = projectPoint(camHeight, robotFrontHeight, imgCenter, center);

            points.add(finalCenter);
        }

        return points;
    }

    private Point projectPoint(double camHeight, double objectHeight, Point centerPoint, Point projectPoint) {
        //Camheight og objectHeight er angviet i pixel så de konverteres
        camHeight = camHeight*2.8;
        objectHeight = objectHeight*2.8;

        double grundLinje = Math.sqrt(Math.pow(centerPoint.x-projectPoint.x, 2)+ Math.pow(centerPoint.y-projectPoint.y, 2));
        double vinkelProjectPoint = Math.toDegrees(Math.asin(camHeight/(Math.sqrt(Math.pow(camHeight, 2)+Math.pow(grundLinje, 2)))));

        double robotTopVinkel = 90-vinkelProjectPoint;
        double projectLength = (objectHeight*robotTopVinkel)/vinkelProjectPoint;
        double grundLinje2 = grundLinje-projectLength;
        double strengthFactor = grundLinje2/grundLinje;
        double xChange = centerPoint.x-projectPoint.x;
        double yChange = centerPoint.y - projectPoint.y;
        Point newPoint = new Point(centerPoint.x-xChange*strengthFactor,centerPoint.y-yChange*strengthFactor);
        return newPoint;
    }

    private Mat threshold(Mat src, Scalar lower, Scalar upper) {
        Mat out = new Mat();
        Core.inRange(src, lower, upper, out);

        return out;
    }
}
