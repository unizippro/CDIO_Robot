package group14.opencv.detectors.robot_detector;

import org.opencv.core.Mat;
import org.opencv.core.Point;

import java.util.List;

public class RobotDetectorResult {
    private Mat output;
    private Mat outputThreshBlue;
    private Mat outputThreshGreen;

    private Point pointFront;
    private Point pointBack;

    RobotDetectorResult(Mat output, Mat outputThreshBlue, Mat outputThreshGreen, Point pointFront, Point pointBack) {
        this.output = output;
        this.outputThreshBlue = outputThreshBlue;
        this.outputThreshGreen = outputThreshGreen;
        this.pointFront = pointFront;
        this.pointBack = pointBack;
    }

    public Mat getOutput() {
        return this.output;
    }

    public Point getPointFront() {
        return this.pointFront;
    }

    public Point getPointBack() {
        return this.pointBack;
    }

    public Mat getOutputThreshBlue() {
        return this.outputThreshBlue;
    }

    public Mat getOutputThreshGreen() {
        return this.outputThreshGreen;
    }
}
