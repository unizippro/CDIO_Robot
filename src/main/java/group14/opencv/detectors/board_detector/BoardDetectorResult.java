package group14.opencv.detectors.board_detector;

import org.opencv.core.Mat;

public class BoardDetectorResult {
    private Mat output;
    private Corners corners;

    BoardDetectorResult(Mat output, Corners corners) {
        this.output = output;
        this.corners = corners;
    }

    public Mat getOutput() {
        return this.output;
    }

    public Corners getCorners() {
        return this.corners;
    }
}
