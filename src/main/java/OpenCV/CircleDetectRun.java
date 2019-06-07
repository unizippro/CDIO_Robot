package OpenCV;

import group14.opencv.detectors.BallDetector;
import org.opencv.core.Core;

public class CircleDetectRun {
    public static void main(String[] args) {
        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new BallDetector().run(args);
    }
}