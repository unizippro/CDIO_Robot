package OpenCV;

import group14.Resources;
import group14.opencv.detectors.ball_detector.BallDetector;
import group14.opencv.detectors.ball_detector.BallDetectorResult;
import group14.opencv.utils.ImageProcessUtils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

public class BallDetectRun {
    public static void main(String[] args) {
        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

//        VideoCapture cap = new VideoCapture(0);
//        if (! cap.isOpened()) {
//            System.out.println("Error");
//            System.exit(1);
//        }
//
//        Mat src = new Mat();
//        cap.read(src);

        Mat src = Imgcodecs.imread(Resources.TestImages.board4, Imgcodecs.IMREAD_COLOR);

        var result = new BallDetector().run(src);

        result.getBalls().forEach(System.out::println);

        HighGui.imshow("Result", result.getOutput());
        HighGui.resizeWindow("Result", 680, 400);
        HighGui.waitKey();

        System.exit(0);
    }

}