package group14.opencv.detectors.ball_detector;

import org.opencv.core.Mat;
import org.opencv.core.Point;

import java.util.List;

public class BallDetectorResult {
    private Mat output;
    private List<Point> balls;

    BallDetectorResult(Mat output, List<Point> balls) {
        this.output = output;
        this.balls = balls;
    }

    public Mat getOutput() {
        return this.output;
    }

    public List<Point> getBalls() {
        return this.balls;
    }
}
