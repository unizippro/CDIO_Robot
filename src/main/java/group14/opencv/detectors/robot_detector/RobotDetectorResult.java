package group14.opencv.detectors.robot_detector;

import org.opencv.core.Mat;
import org.opencv.core.Point;

import java.util.ArrayList;
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

    public List<Point> getPoints() {
        if (!pointsBack.isEmpty() && !pointsFront.isEmpty()) {

            this.robotPoints = new ArrayList<>();
            robotPoints.add(pointsFront.get(0));
            robotPoints.add(pointsBack.get(0));
            return this.robotPoints;
        }
        return this.robotPoints;

    }

    public Mat getOutputThreshBlue() {
        return this.outputThreshBlue;
    }

    public Mat getOutputThreshGreen() {
        return this.outputThreshGreen;
    }
}
