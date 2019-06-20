package group14.opencv.detectors.board_detector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.util.concurrent.AtomicDouble;
import group14.opencv.detectors.Detector;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

public class BoardDetector extends Detector<BoardDetectorResult, BoardDetector.Config> {

    public BoardDetector(Config boardDetectorConfig) {
        this.setConfig(boardDetectorConfig);
    }

    public BoardDetector() {
        super();
    }

    public static class Config {
        public AtomicDouble cornerMarginPercentage = new AtomicDouble(30);
        public AtomicInteger blockSize = new AtomicInteger(11);
        public AtomicInteger kSize = new AtomicInteger(9);
        public AtomicInteger minHBoard = new AtomicInteger(0);
        public AtomicInteger maxHBoard = new AtomicInteger(120);
        public AtomicInteger minSBoard = new AtomicInteger(0);
        public AtomicInteger maxSBoard = new AtomicInteger(120);
        public AtomicInteger minVBoard = new AtomicInteger(180);
        public AtomicInteger maxVBoard = new AtomicInteger(255);
    }

    public BoardDetectorResult run(Mat src) {
        var out = new Mat();
        src.copyTo(out);

        var config = this.getConfig();

        Mat hsv = new Mat();
        Imgproc.cvtColor(src, hsv, Imgproc.COLOR_BGR2HSV);
        Mat hsvThresh  = this.threshold(src, new Scalar(config.minHBoard.get(), config.minSBoard.get(), config.minVBoard.get()), new Scalar(config.maxHBoard.get(), config.maxSBoard.get(), config.maxVBoard.get()));


        Mat dilateEle = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(4,4));
        Imgproc.dilate(hsvThresh, hsvThresh, dilateEle);
        Imgproc.dilate(hsvThresh, hsvThresh, dilateEle);
        Imgproc.dilate(hsvThresh, hsvThresh, dilateEle);

        Mat cannyOutput = new Mat();
        Imgproc.Canny(hsvThresh, cannyOutput, 200, 400);

        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(cannyOutput, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        MatOfPoint2f approxcurve = new MatOfPoint2f();

        List<MatOfPoint> cornerPoints = new ArrayList<>();
        List<Point> finalCornerPoints = new ArrayList<>();
        Rect cross = null;
        double area=20000000;

        for (int i = 0; i < contours.size(); i++) {
            //Imgproc.drawContours(drawing, contours, i, new Scalar(255));
            MatOfPoint2f contour2f = new MatOfPoint2f(contours.get(i).toArray());
            double approxDistance = Imgproc.arcLength(contour2f, true) * 0.02;
            if (approxDistance > 1) {
                Imgproc.approxPolyDP(contour2f, approxcurve, approxDistance, true);

                //Convert back to MatOfPoint
                MatOfPoint points = new MatOfPoint(approxcurve.toArray());

                if (points.total() == 12) {
                    cross = Imgproc.boundingRect(points);
                    //System.out.println(cross);
                    Imgproc.rectangle(out, cross, new Scalar(255));
                }

                if (points.total() == 4) {
                    cornerPoints.add(points);
                    //System.out.println(Imgproc.contourArea(points));
                }

            }
        }
        int boardArea = 900000;
        for (MatOfPoint matCornerPoints : cornerPoints) {
            if (Imgproc.contourArea(matCornerPoints) < area && Imgproc.contourArea(matCornerPoints) > boardArea) {
                if (finalCornerPoints.size() == 0) {
                    finalCornerPoints.clear();
                }
                finalCornerPoints = matCornerPoints.toList();
            }
        }
        List<Point> finalCornerPointsSorted = new ArrayList<>();
        for (Point point : finalCornerPoints) {
            //System.out.println(point);
            Imgproc.circle(out, point, 20, new Scalar(255), 3, 8, 0);
        }
        if (finalCornerPoints.size() != 0) {
            finalCornerPointsSorted = this.sortPoints(finalCornerPoints, src.size());
        }

        return new BoardDetectorResult(out, hsvThresh, finalCornerPointsSorted, cross);
    }

    @Override
    protected Config createConfig() {
        return new Config();
    }

    private List<Point> sortPoints(List<Point> points, Size frameSize) {
        Point center = new Point(frameSize.width / 2, frameSize.height / 2);

        Point upperLeft = null;
        Point upperRight = null;
        Point lowerRight = null;
        Point lowerLeft = null;

        for (Point point : points) {
            if (point.x < center.x && point.y < center.y) {
                upperLeft = point;
            } else if (point.x >= center.x && point.y < center.y) {
                upperRight = point;
            } else if (point.x < center.x && point.y >= center.y) {
                lowerLeft = point;
            } else if (point.x >= center.x && point.y >= center.y) {
                lowerRight = point;
            }
        }

        return Arrays.asList(upperLeft, upperRight, lowerLeft, lowerRight);
    }

    private Mat threshold(Mat src, Scalar lower, Scalar upper) {
        Mat out = new Mat();
        Core.inRange(src, lower, upper, out);

        return out;
    }

}