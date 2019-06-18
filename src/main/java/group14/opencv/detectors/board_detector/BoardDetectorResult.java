package group14.opencv.detectors.board_detector;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;

import java.util.List;

public class BoardDetectorResult {
    private Mat output;
    private List<Point> corners;
    private Rect cross;
    private Mat hsvThresh;

    BoardDetectorResult(Mat output, Mat hsvThresh, List<Point> corners, Rect cross) {
        this.output = output;
        this.corners = corners;
        this.cross = cross;
        this.hsvThresh = hsvThresh;
    }

    public Mat getOutput() {
        return this.output;
    }

    public Mat getBgrThresh(){return this.hsvThresh;}

    // 3 - down left
    // 0 - up left
    // 2 - down right
    // 1 - up right
    public List<Point> getCorners() {
        return this.corners;
    }

    public Rect getCross() {
        return this.cross;
    }

}
