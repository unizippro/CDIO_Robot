package group14.opencv.detectors.ball_detector;

import com.google.common.util.concurrent.AtomicDouble;
import group14.opencv.detectors.Detector;
import group14.opencv.utils.ImageProcessUtils;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;


public class BallDetector extends Detector<BallDetectorResult, BallDetector.Config> {

    public static class Config {
        public AtomicInteger blurSize = new AtomicInteger(5);
        public AtomicDouble lowerThreshold = new AtomicDouble(200);

        private AtomicDouble houghDp = new AtomicDouble(2);
        private AtomicDouble houghMinDist = new AtomicDouble(10);
        public AtomicDouble houghParam1 = new AtomicDouble(80);
        public AtomicDouble houghParam2 = new AtomicDouble(28);
        private AtomicInteger houghMinRadius = new AtomicInteger(10);
        private AtomicInteger houghMaxRadius = new AtomicInteger(20);
    }

    public BallDetectorResult run(Mat src) {
        var points = new ArrayList<Point>();
        var out = new Mat();
        src.copyTo(out);

        var config = this.getConfig();


        //Blur mat to better define objects
        var blurred = ImageProcessUtils.blur(src, config.blurSize.get());

        //Set threshold
        var thresh = this.threshold(blurred, config.lowerThreshold.get(), 255);

        //! [houghcircles]
        Mat circles = new Mat();
        Imgproc.HoughCircles(thresh, circles, Imgproc.HOUGH_GRADIENT, config.houghDp.get(), config.houghMinDist.get(), config.houghParam1.get(), config.houghParam2.get(), config.houghMinRadius.get(), config.houghMaxRadius.get());

        //! [draw]
        for (int x = 0; x < circles.cols(); x++) {
            double[] c = circles.get(0, x);
            Point center = new Point(Math.round(c[0]), Math.round(c[1]));
            points.add(center);

            // circle center
            Imgproc.circle(out, center, 1, new Scalar(0, 100, 100), 3, 8, 0);

            // circle outline
            int radius = (int) Math.round(c[2]);
            Imgproc.circle(out, center, radius, new Scalar(255, 0, 255), 3, 8, 0);
        }
        //! [draw]

        return new BallDetectorResult(out, points);
    }

    @Override
    protected Config createConfig() {
        return new Config();
    }

    private Mat threshold(Mat src, double lower, double upper) {
        Mat out = new Mat();
        Core.inRange(src, new Scalar(lower, lower, lower), new Scalar(upper, upper, upper), out);

        return out;
    }
}