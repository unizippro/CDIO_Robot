package OpenCV;

import group14.opencv.detectors.CircleDetect;
import org.opencv.core.Core;

public class CircleDetectRun {
    public static void main(String[] args) {
        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new CircleDetect().run(args);
    }
}