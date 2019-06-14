package group14.opencv.detectors.board_detector;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.util.concurrent.AtomicDouble;
import group14.opencv.detectors.Detector;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

public class BoardDetector extends Detector<BoardDetectorResult, BoardDetector.Config> {

    public static class Config {
        public AtomicDouble cornerMarginPercentage = new AtomicDouble(30);
        public AtomicInteger minRed = new AtomicInteger(160);
        public AtomicInteger maxRed = new AtomicInteger(255);
        public AtomicInteger minGreen = new AtomicInteger(0);
        public AtomicInteger maxGreen = new AtomicInteger(120);
        public AtomicInteger minBlue = new AtomicInteger(0);
        public AtomicInteger maxBlue = new AtomicInteger(120);
    }

    //int minRed = 160;
    //int minGreen = 0;
    //int minBlue = 0;
    //int maxRed = 255;
    //int maxGreen = 120;
    //int maxBlue = 120;

    public BoardDetectorResult run(Mat src) {
        var out = new Mat();
        src.copyTo(out);

        var config = this.getConfig();

        var bgrThresh = this.threshold(src, new Scalar(config.minBlue.get(), config.minGreen.get(), config.minRed.get()), new Scalar(config.maxBlue.get(), config.maxGreen.get(), config.maxRed.get()));

        Mat dest = Mat.zeros(bgrThresh.size(), CvType.CV_8UC3);
        Mat destNorm = new Mat();
        Mat destNormScaled = new Mat();

        Imgproc.cornerHarris(bgrThresh, dest, 9, 5, 0.1);
        Core.normalize(dest, destNorm, 0, 255, Core.NORM_MINMAX);
        Core.convertScaleAbs(destNorm, destNormScaled);

        float[] destNormData = new float[(int) (destNorm.total() * destNorm.channels())];
        destNorm.get(0, 0, destNormData);

        int threshold = 200;
        List<Point> pointList = new ArrayList<>();
        for (int i = 0; i < destNorm.rows(); i++) {
            for (int j = 0; j < destNorm.cols(); j++) {
                if ((int) destNormData[i * destNorm.cols() + j] > threshold) {
                    pointList.add(new Point(j, i));
                }
            }
        }

        var cornerPoints = new Corners(src.size(), this.getConfig().cornerMarginPercentage.get());
        cornerPoints.calculatePoints(pointList);
        cornerPoints.draw(out);

        // Calculate cross position
        var center = new Point(src.width() / 2, src.height() / 2);
        var marginWidth = center.x * (this.getConfig().cornerMarginPercentage.get() / 100.0);
        var marginHeight = center.y * (this.getConfig().cornerMarginPercentage.get() / 100.0);
        var centerRect = new Rect(new Point(center.x - marginWidth, center.y - marginHeight), new Point(center.x + marginWidth, center.y + marginHeight));
        List<Point> possibleCrossPointList = new ArrayList<>();
        for (Point point : pointList) {
            if (centerRect.contains(point)) {
                possibleCrossPointList.add(point);
            }
        }

        List<Point> finalCrossPointList = this.calculatePoints(possibleCrossPointList);
        for (Point point : finalCrossPointList) {
            Imgproc.circle(out, point, 5, new Scalar(0), 2, 8, 0);
        }
        Imgproc.rectangle(out, centerRect, new Scalar(255, 255, 0), 3);

        return new BoardDetectorResult(out, bgrThresh, cornerPoints, finalCrossPointList);
    }

    @Override
    protected Config createConfig() {
        return new Config();
    }


    private List<Point> calculatePoints(List<Point> pointList) {
        if (pointList.isEmpty()) {
            return new ArrayList<>();
        }

        List<Point> xList = this.sortX(pointList);
        List<Point> yList = this.sortY(pointList);

        Point left = xList.get(0);
        Point right = xList.get(xList.size() - 1);
        Point up = yList.get(0);
        Point down = yList.get(yList.size() - 1);

        List<Point> crossPoints = new ArrayList<>();
        crossPoints.add(left);
        crossPoints.add(right);
        crossPoints.add(up);
        crossPoints.add(down);

        return crossPoints;
    }


    private List<Point> sortX(List<Point> pointList) {
        List<Point> xSortedList = new ArrayList<>(pointList);
        xSortedList.sort(Comparator.comparingInt(o -> (int) o.x));

        return xSortedList;
    }

    private List<Point> sortY(List<Point> pointList) {
        List<Point> ySortedList = new ArrayList<>(pointList);
        ySortedList.sort(Comparator.comparingInt(o -> (int) o.y));

        return ySortedList;
    }

    private Mat threshold(Mat src, Scalar lower, Scalar upper) {
        Mat out = new Mat();
        Core.inRange(src, lower, upper, out);

        return out;
    }

}