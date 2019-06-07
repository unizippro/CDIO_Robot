package group14.opencv.detectors;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;
import org.opencv.videoio.VideoCapture;

public class BoardDetector {

    int minRed= 160;
    int minGreen = 0;
    int minBlue = 0;
    int maxRed = 255;
    int maxGreen= 120;
    int maxBlue = 120;

    public void run(String[] args) {
        Mat src = new Mat();
        VideoCapture cap = new VideoCapture(1);
        if(!cap.isOpened()) {
            System.out.println("Error");
        }
        else {
            cap.read(src);
        }

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
        List<Point> pointlist = new ArrayList<Point>();
        for (int i = 0; i < destNorm.rows(); i++) {
            for (int j = 0; j < destNorm.cols(); j++) {
                if ((int) destNormData[i * destNorm.cols() + j] > threshold) {
                    Imgproc.circle(destNormScaled, p = new Point(j, i), 5, new Scalar(0), 2, 8, 0);

                    pointlist.add(p);
                    System.out.println(p);

                }
            }
        }
        List<Point> finalPointList = new ArrayList<Point>();
        finalPointList = this.SortPoints(pointlist);
        System.out.println(finalPointList);
        for (Point point : finalPointList) {
            Imgproc.circle(src, p = new Point(point.x, point.y), 5, new Scalar(0), 2, 8, 0);
        }

        List<Point> possibleCrossPointList = new ArrayList<Point>();
        for (Point point : pointlist) {
            if(point.x>100 && point.y>100 && point.x<500 && point.y<400) {
                possibleCrossPointList.add(point);
            }
        }
        System.out.println(possibleCrossPointList);

        List<Point> finalCrossPointList = new ArrayList<Point>();
        finalCrossPointList = this.calculatePoints(possibleCrossPointList);
        System.out.println(finalCrossPointList);
        for(Point point: finalCrossPointList) {
            Imgproc.circle(src, p = new Point(point.x, point.y), 5, new Scalar(0), 2, 8, 0);
        }

        Mat reziseImg = new Mat();
        Size scaleSize = new Size(1000, 680);
        Imgproc.resize(src, reziseImg, scaleSize, 0, 0, Imgproc.INTER_AREA);

        // ! [display]
        HighGui.imshow("detected corners", reziseImg);
        HighGui.waitKey();
        // ! [display]

        System.exit(0);

    }


    private List<Point> calculatePoints(List<Point> pointList) {
        List<Point> crossPoints = new ArrayList<Point>();

        List<Point> xList = this.sortX(pointList);
        List<Point> yList = this.sortY(pointList);

        Point left = new Point();
        Point right = new Point();
        Point up = new Point();
        Point down = new Point();

        left = xList.get(0);
        right = xList.get(xList.size()-1);

        up = yList.get(0);
        down = yList.get(yList.size()-1);
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


    public List<Point> SortPoints(List<Point> p) {
        Point firstQuarterPoint = new Point();
        Point secondQuarterPoint = new Point();
        Point thirdQuarterPoint = new Point(1000, 1000);
        Point fourthQuarterPoint = new Point();
        List<Point> correctPoints = new ArrayList<Point>();

        for (Point point : p) {
            System.out.println(point);
            if (0 < point.x && point.x < 100 && 0 < point.y && point.y < 100) {
                if (firstQuarterPoint == null) {
                    firstQuarterPoint = point;
                } else {
                    if (point.x > firstQuarterPoint.x) {
                        firstQuarterPoint = point;
                    }
                    if (point.y > firstQuarterPoint.y) {
                        firstQuarterPoint = point;
                    }
                }
            }
            if (500 < point.x && point.x < 700 && 0 < point.y && point.y < 100) {
                if (secondQuarterPoint == null) {
                    secondQuarterPoint = point;
                } else {
                    if (point.x < secondQuarterPoint.x) {
                        secondQuarterPoint = point;
                    }
                    if (point.y > secondQuarterPoint.y) {
                        secondQuarterPoint = point;
                    }
                }
            }
            if (500 < point.x && point.x < 700 && 400 < point.y && point.y < 600) {
                if (thirdQuarterPoint == null) {
                    thirdQuarterPoint = point;
                } else {
                    if (point.x < thirdQuarterPoint.x) {
                        thirdQuarterPoint = point;
                    }
                    if (point.y < thirdQuarterPoint.y) {
                        thirdQuarterPoint = point;
                    }
                }
            }
            if (0 < point.x && point.x < 100 && 400 < point.y && point.y < 600) {
                if (fourthQuarterPoint == null) {
                    fourthQuarterPoint = point;
                } else {
                    if (point.x > fourthQuarterPoint.x) {
                        fourthQuarterPoint = point;
                    }
                    if (point.y < fourthQuarterPoint.y) {
                        fourthQuarterPoint = point;
                    }
                }
            }

        }

        correctPoints.add(firstQuarterPoint);
        correctPoints.add(secondQuarterPoint);
        correctPoints.add(thirdQuarterPoint);
        correctPoints.add(fourthQuarterPoint);
        return correctPoints;
    }


}