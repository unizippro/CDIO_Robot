package group14.opencv.detectors.robot_detector;

import org.opencv.core.Mat;
import org.opencv.core.Point;

import java.util.List;

public class RobotDetectorResult {
    private Mat output;
    private List<Point> pointsFront;
    private List<Point> pointsBack;

    RobotDetectorResult(Mat output, List<Point> pointsFront, List<Point> pointsBack) {
        this.output = output;
        this.pointsFront = pointsFront;
        this.pointsBack = pointsBack;
    }

    public Mat getOutput() {
        return this.output;
    }

    public List<Point> getPointsFront() {
        return this.pointsFront;
    }

    public List<Point> getPointsBack() {
        return this.pointsBack;
    }
}
