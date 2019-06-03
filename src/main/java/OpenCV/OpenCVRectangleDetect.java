package OpenCV;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
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

public class OpenCVRectangleDetect {

    int minRed= 140;
    int minGreen = 0;
    int minBlue = 0;
    int maxRed = 255;
    int maxGreen= 120;
    int maxBlue = 120;

    public void run(String[] args) {

		/*VideoCapture cap = new VideoCapture(1);
		if(!cap.isOpened()) {
			System.out.println("Error");
		}
		else {
			Mat videoMat = new Mat();
			cap.read(videoMat);
			while(true) {
				if(cap.read(videoMat)) {
					System.out.println("Frame obtained");
					Imgcodecs.imwrite("C://capture/camera.jpg", videoMat);
					break;
				}
			}
		}*/
        String default_file = "C:\\Users\\Sebastian\\Downloads\\IMG_4838.jpg";
        String filename = ((args.length > 0) ? args[0] : default_file);


        // Load an image
        Mat src = Imgcodecs.imread(filename, Imgcodecs.IMREAD_COLOR);

        // Check if image is loaded fine
        if (src.empty()) {
            System.out.println("Error opening image!");
            System.out.println("Program Arguments: [image_name -- default " + default_file + "] \n");
            System.exit(-1);
        }
        Mat hsv = new Mat();
        Imgproc.cvtColor(src, hsv, Imgproc.COLOR_BGR2HSV);





        //Bgrthresh overvejes hvis hsv ikke er tilstrækkeligt
        Mat bgrThresh = new Mat();
        Core.inRange(src, new Scalar(minBlue, minGreen, minRed), new Scalar(maxBlue, maxGreen, maxRed), bgrThresh);

        Mat thresh1 = new Mat();
        // Til at finde banen
        Core.inRange(hsv, new Scalar(0, 100, 200), new Scalar(255, 255, 255), thresh1);



        Mat dest = Mat.zeros(bgrThresh.size(), CvType.CV_8UC3);
        Mat destNorm = new Mat();
        Mat destNormScaled = new Mat();

        Imgproc.cornerHarris(thresh1, dest, 9, 5, 0.1);
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

        Mat reziseImg = new Mat();
        Size scaleSize = new Size(1000, 680);
        Imgproc.resize(src, reziseImg, scaleSize, 0, 0, Imgproc.INTER_AREA);

        // ! [display]
        HighGui.imshow("detected circles", reziseImg);
        HighGui.waitKey();
        // ! [display]

        System.exit(0);

    }

    public List<Point> SortPoints(List<Point> p) {
        Point firstQuarterPoint = new Point();
        Point secondQuarterPoint = new Point();
        Point thirdQuarterPoint = new Point(4000, 4000);
        Point fourthQuarterPoint = new Point();
        List<Point> correctPoints = new ArrayList<Point>();

        for (Point point : p) {
            System.out.println(point);
            if (0 < point.x && point.x < 400 && 0 < point.y && point.y < 800) {
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
            if (3000 < point.x && point.x < 3800 && 0 < point.y && point.y < 800) {
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
            if (3000 < point.x && point.x < 4000 && 2500 < point.y && point.y < 3500) {
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
            if (0 < point.x && point.x < 600 && 2500 < point.y && point.y < 3500) {
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