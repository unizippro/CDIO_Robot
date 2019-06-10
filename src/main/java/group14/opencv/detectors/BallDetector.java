package group14.opencv.detectors;

import com.google.common.util.concurrent.AtomicDouble;
import javafx.util.Pair;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class BallDetector {

    private Config config = new Config();

    public static class Config {
        public AtomicInteger blurSize = new AtomicInteger(5);
        public AtomicDouble lowerThreshold = new AtomicDouble(185);

        private AtomicDouble houghDp = new AtomicDouble(3);
        private AtomicDouble houghMinDist = new AtomicDouble(0.5);
        public AtomicDouble houghParam1 = new AtomicDouble(50);
        public AtomicDouble houghParam2 = new AtomicDouble(25);
        private AtomicInteger houghMinRadius = new AtomicInteger(0);
        private AtomicInteger houghMaxRadius = new AtomicInteger(30);
    }

    public BallDetector() { }

    public Config getConfig() {
        return this.config;
    }

    public Pair<Mat, List<Point>> run(Mat src) {
        var points = new ArrayList<Point>();
        var out = new Mat();
        src.copyTo(out);


        //Blur mat to better define objects
        var blurred = this.blur(src, this.config.blurSize.get());

        //Set threshold
        var thresh = this.threshold(blurred, this.config.lowerThreshold.get(), 255);

        //! [houghcircles]
        Mat circles = new Mat();
        Imgproc.HoughCircles(thresh, circles, Imgproc.HOUGH_GRADIENT, this.config.houghDp.get(), this.config.houghMinDist.get(), this.config.houghParam1.get(), this.config.houghParam2.get(), this.config.houghMinRadius.get(), this.config.houghMaxRadius.get());

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

        return new Pair<>(out, points);
    }


    private Mat blur(Mat src, int ksize) {
        Mat out = new Mat();
        Imgproc.medianBlur(src, out, ksize);

        return out;
    }


    private Mat threshold(Mat src, double lower, double upper) {
        Mat out = new Mat();
        Core.inRange(src, new Scalar(lower, lower, lower), new Scalar(upper, upper, upper), out);

        return out;
    }
}