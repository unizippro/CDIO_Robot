package OpenCV;

import org.opencv.core.Core;

public class RectanglesOpenCV {
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new OpenCVRectangleDetect().run(args);
    }
}