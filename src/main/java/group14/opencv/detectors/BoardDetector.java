package group14.opencv.detectors;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

public class BoardDetector {

    int minRed= 160;
    int minGreen = 0;
    int minBlue = 0;
    int maxRed = 255;
    int maxGreen= 120;
    int maxBlue = 120;

    public Pair<Mat, List<Point>> run(Mat src) {
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
        Point p;
        List<Point> pointList = new ArrayList<>();
        for (int i = 0; i < destNorm.rows(); i++) {
            for (int j = 0; j < destNorm.cols(); j++) {
                if ((int) destNormData[i * destNorm.cols() + j] > threshold) {
                    Imgproc.circle(destNormScaled, p = new Point(j, i), 5, new Scalar(0), 2, 8, 0);

                    pointlist.add(p);
                    System.out.println(p);
                }
            }
        }
        List<Point> finalPointList = this.sortPoints(pointlist);
        System.out.println(finalPointList);
        for (Point point : finalPointList) {
            Imgproc.circle(src, p = new Point(point.x, point.y), 5, new Scalar(0), 2, 8, 0);
        }

        List<Point> possibleCrossPointList = new ArrayList<>();
        for (Point point : pointlist) {
            if (point.x > 100 && point.y > 100 && point.x < 500 && point.y < 400) {
                possibleCrossPointList.add(point);
            }
        }
        System.out.println(possibleCrossPointList);

        List<Point> finalCrossPointList = this.calculatePoints(possibleCrossPointList);
        System.out.println(finalCrossPointList);
        for(Point point : finalCrossPointList) {
            Imgproc.circle(src, p = new Point(point.x, point.y), 5, new Scalar(0), 2, 8, 0);
        }

        return new Pair<>(src, finalPointList);
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


    private List<Point> sortPoints(List<Point> points) {
        Point upperLeftPoint = new Point();
        Point upperRightPoint = new Point();
        Point lowerRightPoint = new Point(1000, 1000);
        Point lowerLeftPoint = new Point();

        for (Point point : points) {
            if (point.x < 100 && point.y < 100) {
                if (point.x > upperLeftPoint.x || point.y > upperLeftPoint.y) {
                    upperLeftPoint = point;
                }
            } else if (500 < point.x && point.x < 700 && 0 < point.y && point.y < 100) {
                if (point.x < upperRightPoint.x || point.y > upperRightPoint.y) {
                    upperRightPoint = point;
                }
            } else if (500 < point.x && point.x < 700 && 400 < point.y && point.y < 600) {
                if (point.x < lowerRightPoint.x || point.y < lowerRightPoint.y) {
                    lowerRightPoint = point;
                }
            } else if (0 < point.x && point.x < 100 && 400 < point.y && point.y < 600) {
                if (point.x > lowerLeftPoint.x || point.y < lowerLeftPoint.y) {
                    lowerLeftPoint = point;
                }
            }
        }

        List<Point> correctPoints = new ArrayList<>();
        correctPoints.add(upperLeftPoint);
        correctPoints.add(upperRightPoint);
        correctPoints.add(lowerRightPoint);
        correctPoints.add(lowerLeftPoint);

        return correctPoints;
    }

}