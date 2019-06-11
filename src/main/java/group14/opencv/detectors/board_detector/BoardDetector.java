package group14.opencv.detectors.board_detector;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.google.common.util.concurrent.AtomicDouble;
import group14.opencv.detectors.Detector;
import javafx.util.Pair;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

public class BoardDetector extends Detector<BoardDetectorResult, BoardDetector.Config> {

    public static class Config {
        public AtomicDouble cornerMarginPercentage = new AtomicDouble(30);
    }

    int minRed= 160;
    int minGreen = 0;
    int minBlue = 0;
    int maxRed = 255;
    int maxGreen= 120;
    int maxBlue = 120;

    public BoardDetectorResult run(Mat src) {
        var out = new Mat();
        src.copyTo(out);

        //Bgrthresh overvejes hvis hsv ikke er tilstrækkeligt
        Mat bgrThresh = new Mat();
        Core.inRange(src, new Scalar(minBlue, minGreen, minRed), new Scalar(maxBlue, maxGreen, maxRed), bgrThresh);

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

        return new BoardDetectorResult(out, cornerPoints);
    }

    @Override
    protected Config createConfig() {
        return new Config();
    }

    public Pair<Mat, List<Point>> runCross(Mat src, List<Point> pointList) {
        List<Point> possibleCrossPointList = new ArrayList<>();
        for (Point point : pointList) {
            if (point.x > 100 && point.y > 100 && point.x < 500 && point.y < 400) {
                possibleCrossPointList.add(point);
            }
        }
        System.out.println(possibleCrossPointList);

        List<Point> finalCrossPointList = this.calculatePoints(possibleCrossPointList);
        System.out.println(finalCrossPointList);
        for(Point point : finalCrossPointList) {
            Imgproc.circle(src, point, 5, new Scalar(0), 2, 8, 0);
        }

        return new Pair<>(src, finalCrossPointList);
    }


    private List<Point> calculatePoints(List<Point> pointList) {
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

}