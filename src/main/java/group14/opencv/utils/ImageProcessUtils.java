package group14.opencv.utils;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class ImageProcessUtils {

    public static Mat blur(Mat src, int ksize) {
        Mat out = new Mat();
        Imgproc.medianBlur(src, out, ksize);

        return out;
    }

}
