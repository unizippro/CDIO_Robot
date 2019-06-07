package OpenCV;

import group14.opencv.detectors.RobotDetector;
import org.opencv.core.Core;

public class RobotDetectRun {
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new RobotDetector().run(args);
    }
}
