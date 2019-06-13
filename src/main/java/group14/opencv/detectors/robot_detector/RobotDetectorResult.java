package group14.opencv.detectors.robot_detector;

import org.opencv.core.Mat;
import org.opencv.core.Point;

import java.util.List;

public class RobotDetectorResult {
    private Mat output;
    private Mat outputThreshBlue;
    private Mat outputThreshGreen;
    private List<Point> pointsFront;
    private List<Point> pointsBack;

    RobotDetectorResult(Mat output, Mat outputThreshBlue, Mat outputThreshGreen, List<Point> pointsFront, List<Point> pointsBack) {
        this.output = output;
        this.outputThreshBlue = outputThreshBlue;
        this.outputThreshGreen = outputThreshGreen;
        this.pointsFront = pointsFront;
        this.pointsBack = pointsBack;
    }

    public Mat getOutput() {
        return this.output;
    }

    public List<Point> getPointsFront() {
        return this.pointsFront;
    }

    public Mat getOutputThreshBlue() {
        return this.outputThreshBlue;
    }

    public Mat getOutputThreshGreen() {
        return this.outputThreshGreen;
    }

    public List<Point> getPointsBack() {
        return this.pointsBack;
    }
}
