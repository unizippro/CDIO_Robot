package OpenCV;

import group14.opencv.detectors.BoardDetector;
import org.opencv.core.Core;

public class RectanglesDetectRun {
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new BoardDetector().run(args);
    }
}