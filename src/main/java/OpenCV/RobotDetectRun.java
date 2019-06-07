package OpenCV;

import group14.opencv.detectors.RobotDetect;
import org.opencv.core.Core;

public class RobotDetectRun {
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new RobotDetect().run(args);
    }
}
