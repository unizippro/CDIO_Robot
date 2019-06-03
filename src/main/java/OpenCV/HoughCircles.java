package OpenCV;

import org.opencv.core.Core;

public class HoughCircles {
    public static void main(String[] args) {
        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new openCVCircleDetect().run(args);
    }
}