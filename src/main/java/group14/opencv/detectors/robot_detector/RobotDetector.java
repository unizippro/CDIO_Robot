package group14.opencv.detectors.robot_detector;

import group14.opencv.detectors.Detector;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RobotDetector extends Detector<RobotDetectorResult, RobotDetector.Config> {

    private double pixelsPrCm = 9;

    public static class Config {
        // HSV Blue
        public AtomicInteger blueMinH = new AtomicInteger(100);
        public AtomicInteger blueMaxH = new AtomicInteger(140);
        public AtomicInteger blueMinS = new AtomicInteger(160);
        public AtomicInteger blueMaxS = new AtomicInteger(255);
        public AtomicInteger blueMinV = new AtomicInteger(90);
        public AtomicInteger blueMaxV = new AtomicInteger(255);

        // HSV Green
        public AtomicInteger greenMinH = new AtomicInteger(20);
        public AtomicInteger greenMaxH = new AtomicInteger(95);
        public AtomicInteger greenMinS = new AtomicInteger(40);
        public AtomicInteger greenMaxS = new AtomicInteger(255);
        public AtomicInteger greenMinV = new AtomicInteger(80);
        public AtomicInteger greenMaxV = new AtomicInteger(255);

    }

    double camHeight = 169.5;
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


        // Color 1 - blue
        var frontPoints = this.getPointsWithColor(blueMat, out);

        // Color 2 - green
        var backPoints = this.getPointsWithColor(greenMat, out);

        //front is index 0

        Point front = null;
        Point back = null;
        if (! frontPoints.isEmpty() && ! backPoints.isEmpty()) {
            front = frontPoints.get(0);
            back = backPoints.get(0);
        }

        return new RobotDetectorResult(out, blueMat, greenMat, front, back);
    }

    public void setPixelsPrCm(double pixelsPrCm) {
        this.pixelsPrCm = pixelsPrCm;
    }

    @Override
    protected Config createConfig() {
        return new Config();
    }

    private List<Point> getPointsWithColor(Mat frame, Mat out) {
        //! [houghcircles]
        Mat circlesFrame = new Mat();
        Imgproc.HoughCircles(frame, circlesFrame, Imgproc.HOUGH_GRADIENT, 7, 1000000000, 80, 20, 15, 22);

        Point imgCenter = new Point(frame.width() / 2, frame.height() / 2);

        var points = new ArrayList<Point>();
        //! [draw] Color 1
        for (int x = 0; x < circlesFrame.cols(); x++) {
            double[] c = circlesFrame.get(0, x);
            Point center = new Point(Math.round(c[0]), Math.round(c[1]));
            Point finalCenter = projectPoint(camHeight, robotFrontHeight, imgCenter, center);

            Imgproc.circle(out, center, 1, new Scalar(0, 100, 100), 3, 8, 0);
            int radius = (int) Math.round(c[2]);
            Imgproc.circle(out, center, radius, new Scalar(255, 0, 255), 3, 8, 0);

            points.add(finalCenter);
        }

        return points;
    }

    private Point projectPoint(double camHeight, double objectHeight, Point centerPoint, Point objectPoint) {

        // Beregn hypotynusen
        var b = camHeight * this.pixelsPrCm;
        //var b = 165;
        //objectPoint = new Point(30, 0);
        //centerPoint = new Point(0,0);
        var tx = objectPoint.x - centerPoint.x;
        var ty = objectPoint.y - centerPoint.y;
        var a = Math.sqrt(tx*tx+ty*ty);  // Lenght from center to
        //System.out.println("a = " + a);
        var c = Math.sqrt(a*a + b*b);
        //System.out.println("c = " + c);
        var A = Math.toDegrees(Math.acos((b*b + c*c - a*a)/(2*b*c)));

        //System.out.println("angle A = " + A + "Should be 10.3");

        var B = 180 - A - 90;
        var realDistToPoint = (Math.sin(Math.toRadians(A)) * (b - objectHeight * this.pixelsPrCm)) / Math.sin(Math.toRadians(B));
        //System.out.println("Dist = "  +realDistToPoint);
        var movingDist = a - realDistToPoint;
        var angle = getAngleBetweenPoint(centerPoint, objectPoint);
        return getVectorEndPoint(objectPoint, getOppositeAngle(angle) , movingDist);
    }

    public static double getAngleBetweenPoint(Point point1, Point point2) {
        var dx = point2.x - point1.x;
        var dy = -(point1.y - point2.y); // Reverse math sign due to y positive from to bottom

        var result = Math.toDegrees(Math.atan2(dy, dx));

        return result < 0 ? (360d + result) : result;
    }

    public static Point getVectorEndPoint(Point startPoint, double angle, double magnitude) {
        var dx = magnitude * Math.cos(Math.toRadians(angle));
        var dy = magnitude * Math.sin(Math.toRadians(angle));

        return new Point(startPoint.x + dx, startPoint.y + dy);
    }

    public static double getOppositeAngle(double angle) {
        return (angle + 180) % 360;
    }

    private Mat threshold(Mat src, Scalar lower, Scalar upper) {
        Mat out = new Mat();
        Core.inRange(src, lower, upper, out);

        return out;
    }
}
