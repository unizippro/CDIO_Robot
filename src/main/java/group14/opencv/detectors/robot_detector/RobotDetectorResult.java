package group14.opencv.detectors.robot_detector;

import org.opencv.core.Mat;

public class RobotDetectorResult {
    private Mat output;

    RobotDetectorResult(Mat output) {
        this.output = output;
    }

    public Mat getOutput() {
        return output;
    }
}
