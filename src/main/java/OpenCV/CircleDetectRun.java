package OpenCV;

import group14.Resources;
import group14.opencv.detectors.BallDetector;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;

public class CircleDetectRun {
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

        result.getValue().forEach(System.out::println);

        HighGui.imshow("Result", result.getKey());
        HighGui.resizeWindow("Result", 680, 400);
        HighGui.waitKey();

        System.exit(0);
    }
}