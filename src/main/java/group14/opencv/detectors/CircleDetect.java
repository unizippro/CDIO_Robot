package group14.opencv.detectors;

import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import static org.opencv.imgcodecs.Imgcodecs.imread;

public class CircleDetect {

    public void run(String[] args) {
        Mat src = new Mat();
        VideoCapture cap = new VideoCapture(1);
        if(!cap.isOpened()) {
            System.out.println("Error");
        }
        else {

            cap.read(src);
        }

        //Blur mat to better define objects
        Imgproc.medianBlur(src, src, 5);
        //Set threshold
        Mat thresh = new Mat();
        Core.inRange(src, new Scalar(185, 185, 185), new Scalar(255, 255, 255), thresh);

        //! [houghcircles]
        Mat circles = new Mat();
        Imgproc.HoughCircles(thresh, circles, Imgproc.HOUGH_GRADIENT, 3, 0.5, 50, 25, 0, 30);

        //! [draw]
        for (int x = 0; x < circles.cols(); x++) {
            double[] c = circles.get(0, x);
            Point center = new Point(Math.round(c[0]), Math.round(c[1]));
            // circle center
            Imgproc.circle(src, center, 1, new Scalar(0,100,100), 3, 8, 0 );
            // circle outline
            int radius = (int) Math.round(c[2]);
            Imgproc.circle(src, center, radius, new Scalar(255,0,255), 3, 8, 0 );

            System.out.println(center);
        }
        //! [draw]

        //rezise img
        Mat reziseImg = new Mat();
        Size scaleSize = new Size(1000,680);
        Imgproc.resize(src, reziseImg, scaleSize, 0, 0, Imgproc.INTER_AREA);

        //! [display]
        HighGui.imshow("detected circles", reziseImg);
        HighGui.waitKey();
        //! [display]

        System.exit(0);
    }
}