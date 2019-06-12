package group14.opencv.detectors.robot_detector;

import group14.opencv.detectors.Detector;
import group14.opencv.utils.ImageProcessUtils;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class RobotDetector extends Detector<RobotDetectorResult, RobotDetector.Config> {

    static class Config {

    }

    // Color 1 - Blue
    //BGR: 199, 41, 29
    int minRed_Color1 = 0;
    int minGreen_Color1 = 0;
    int minBlue_Color1 = 160;
    int maxRed_Color1 = 130;
    int maxGreen_Color1 = 130;
    int maxBlue_Color1 = 255;

    // Color 2 - Green
    // BGR: 49, 134, 25
    int minRed_Color2 = 70;
    int minGreen_Color2 = 150;
    int minBlue_Color2 = 70;
    int maxRed_Color2 = 150;
    int maxGreen_Color2 = 255;
    int maxBlue_Color2 = 150;

    double camHeight = 165;
    double robotFrontHeight = 28;
    double robotBackHeight = 27;

    @Override
    public RobotDetectorResult run(Mat src) {
        var out = new Mat();
        src.copyTo(out);

        //Blur mat to better define objects
        var blurred = ImageProcessUtils.blur(src, 5);

        // Color 1 - blue
        var frontPoints = this.getPointsWithColor(blurred, out, new Scalar(minBlue_Color1, minGreen_Color1, minRed_Color1), new Scalar(maxBlue_Color1, maxGreen_Color1, maxRed_Color1));

        // Color 2 - green
        var backPoints = this.getPointsWithColor(blurred, out, new Scalar(minBlue_Color2, minGreen_Color2, minRed_Color2), new Scalar(maxBlue_Color2, maxGreen_Color2, maxRed_Color2));

        return new RobotDetectorResult(out, frontPoints, backPoints);
    }

    @Override
    protected Config createConfig() {
        return new Config();
    }

    private List<Point> getPointsWithColor(Mat frame, Mat out, Scalar lower, Scalar upper) {
        Mat backgroundThresholdFrame = new Mat();
        Core.inRange(frame, new Scalar(minBlue_Color1, minGreen_Color1, minRed_Color1), new Scalar(maxBlue_Color1, maxGreen_Color1, maxRed_Color1), backgroundThresholdFrame);

        //! [houghcircles]
        Mat circlesFrame = new Mat();
        Imgproc.HoughCircles(backgroundThresholdFrame, circlesFrame, Imgproc.HOUGH_GRADIENT, 3, 0.5, 50, 25, 0, 15);

        Point imgCenter = new Point(frame.width() / 2, frame.height() / 2);

        var points = new ArrayList<Point>();
        //! [draw] Color 1
        for (int x = 0; x < circlesFrame.cols(); x++) {
            double[] c = circlesFrame.get(0, x);
            Point center = new Point(Math.round(c[0]), Math.round(c[1]));
            Point finalCenter = projectPoint(camHeight, robotFrontHeight, imgCenter, center);
            // circle center
            Imgproc.circle(out, finalCenter, 1, new Scalar(0, 100, 100), 3, 8, 0);
            // circle outline
            int radius = (int) Math.round(c[2]);
            Imgproc.circle(out, finalCenter, radius, new Scalar(255, 0, 255), 3, 8, 0);

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
}
