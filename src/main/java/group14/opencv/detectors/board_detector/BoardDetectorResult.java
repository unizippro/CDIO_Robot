package group14.opencv.detectors.board_detector;

import org.opencv.core.Mat;
import org.opencv.core.Point;

import java.util.List;

public class BoardDetectorResult {
    private Mat output;
    private Corners corners;
    private List<Point> cross;

    BoardDetectorResult(Mat output, Corners corners, List<Point> cross) {
        this.output = output;
        this.corners = corners;
        this.cross = cross;
    }

    public Mat getOutput() {
        return this.output;
    }

    public Corners getCorners() {
        return this.corners;
    }

    public List<Point> getCross() {
        return this.cross;
    }
}
