package OpenCV;

import group14.Resources;
import group14.opencv.detectors.BoardDetector;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

public class RectanglesDetectRun {
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

        Mat src = Imgcodecs.imread(Resources.TestImages.board7, Imgcodecs.IMREAD_COLOR);

        var result = new BoardDetector().run(src);

        System.out.println(result.getValue());

        HighGui.imshow("Result", result.getKey());
        HighGui.resizeWindow("Result", src.width() / 2, src.height() / 2);
        HighGui.waitKey();

        System.exit(0);
    }
}