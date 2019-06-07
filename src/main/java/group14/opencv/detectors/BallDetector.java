package group14.opencv.detectors;

import javafx.util.Pair;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;


public class BallDetector {

    private ArrayList<Point> points = new ArrayList<>();
    private Mat src;
    private Mat out;


    public BallDetector(Mat src) {
        this.src = src;
        this.out = src;
    }


    public void run() {
        //Blur mat to better define objects
        var blurred = this.blur(this.src, 5);

        //Set threshold
        var thresh = this.threshold(blurred, 185, 255);

        //! [houghcircles]
        Mat circles = new Mat();
        Imgproc.HoughCircles(thresh, circles, Imgproc.HOUGH_GRADIENT, 3, 0.5, 50, 25, 0, 30);

        //! [draw]
        for (int x = 0; x < circles.cols(); x++) {
            double[] c = circles.get(0, x);
            Point center = new Point(Math.round(c[0]), Math.round(c[1]));
            this.points.add(center);

            // circle center
            Imgproc.circle(this.out, center, 1, new Scalar(0, 100, 100), 3, 8, 0);

            // circle outline
            int radius = (int) Math.round(c[2]);
            Imgproc.circle(this.out, center, radius, new Scalar(255, 0, 255), 3, 8, 0);
        }
        //! [draw]
    }


    public Pair<Mat, List<Point>> getResult() {
        return new Pair<>(this.out, this.points);
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