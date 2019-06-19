package group14.opencv.detectors.robot_detector;

import group14.opencv.detectors.Detector;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RobotDetector extends Detector<RobotDetectorResult, RobotDetector.Config> {

    public static class Config {
        // HSV Blue
        public AtomicInteger blueMinH = new AtomicInteger(60);
        public AtomicInteger blueMaxH = new AtomicInteger(160);
        public AtomicInteger blueMinS = new AtomicInteger(200);
        public AtomicInteger blueMaxS = new AtomicInteger(255);
        public AtomicInteger blueMinV = new AtomicInteger(215);
        public AtomicInteger blueMaxV = new AtomicInteger(255);

        // HSV Green
        public AtomicInteger greenMinH = new AtomicInteger(20);
        public AtomicInteger greenMaxH = new AtomicInteger(100);
        public AtomicInteger greenMinS = new AtomicInteger(50);
        public AtomicInteger greenMaxS = new AtomicInteger(210);
        public AtomicInteger greenMinV = new AtomicInteger(0);
        public AtomicInteger greenMaxV = new AtomicInteger(255);

    }

    double camHeight = 165;
    double robotFrontHeight = 28;
    double robotBackHeight = 27;


    @Override
    public RobotDetectorResult run(Mat src) {
        Config config = this.getConfig();
        Mat out = new Mat();
        src.copyTo(out);
        Mat work = new Mat();
        src.copyTo(work);

        // Used to Dilate and Erode
        int circle = 25;
        Mat element = Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(circle, circle), new Point(circle/2, circle/2));


        Imgproc.cvtColor(work, work, Imgproc.COLOR_BGR2HSV);

        Mat blueMat = new Mat();
        Mat greenMat = new Mat();

        Core.inRange(work
                , new Scalar(config.blueMinH.get(), config.blueMinS.get(), config.blueMinV.get())
                , new Scalar(config.blueMaxH.get(), config.blueMaxS.get(), config.blueMaxV.get())
                , blueMat);
        Core.inRange(work
                , new Scalar(config.greenMinH.get(), config.greenMinS.get(), config.greenMinV.get())
                , new Scalar(config.greenMaxH.get(), config.greenMaxS.get(), config.greenMaxV.get())
                , greenMat);


        Imgproc.GaussianBlur(blueMat, blueMat, new Size(5,5),0);
        Imgproc.GaussianBlur(greenMat, greenMat, new Size(5,5),0);

        //Imgproc.dilate(work, work, element);
        Imgproc.erode(blueMat, blueMat, element);
        Imgproc.erode(greenMat, greenMat, element);

        Imgproc.dilate(blueMat, blueMat, element);
        Imgproc.dilate(greenMat, greenMat, element);

        var circle2 = 10;
        Mat element2 = Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(circle2, circle2), new Point(circle2/2, circle2/2));

        Imgproc.erode(blueMat, blueMat, element2);
        Imgproc.erode(greenMat, greenMat, element2);


        // Color 1 - blue
        var frontPoints = this.getPointsWithColor(blueMat);

        // Color 2 - green
        var backPoints = this.getPointsWithColor(greenMat);

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

        return new RobotDetectorResult(out, blueMat, greenMat, front, back);
    }

    @Override
    protected Config createConfig() {
        return new Config();
    }

    private List<Point> getPointsWithColor(Mat frame) {
        //! [houghcircles]
        Mat circlesFrame = new Mat();
        Imgproc.HoughCircles(frame, circlesFrame, Imgproc.HOUGH_GRADIENT, 7,10, 80, 34, 15, 20);

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
        camHeight = camHeight * 9.35;
        objectHeight = objectHeight * 9.35;

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
