package OpenCV;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

public class RobotDetect {

    // Color 1 - Blue
    //BGR: 199, 41, 29
    int minRed_Color1 = 0;
    int minGreen_Color1 = 0;
    int minBlue_Color1 = 160;
    int maxRed_Color1 = 130;
    int maxGreen_Color1 = 130;
    int maxBlue_Color1 = 255;

    // Color 2 - Green
    // BGR: 49, 134, 25
    int minRed_Color2 = 30;
    int minGreen_Color2 = 160;
    int minBlue_Color2 = 30;
    int maxRed_Color2 = 140;
    int maxGreen_Color2 = 255;
    int maxBlue_Color2 = 140;

    public void run(String[] args) {
        Mat src = new Mat();
        VideoCapture cap = new VideoCapture(1);
        if (!cap.isOpened()) {
            System.out.println("Error");
        } else {
            cap.read(src);
        }

        //Blur mat to better define objects
        Imgproc.medianBlur(src, src, 5);

        //BgrThresh - Color 1 - blue
        Mat bgrThresh_Color1 = new Mat();
        Core.inRange(src, new Scalar(minBlue_Color1, minGreen_Color1, minRed_Color1), new Scalar(maxBlue_Color1, maxGreen_Color1, maxRed_Color1), bgrThresh_Color1);

        //BgrThresh - Color 2 - yellow
        Mat bgrThresh_Color2 = new Mat();
        Core.inRange(src, new Scalar(minBlue_Color2, minGreen_Color2, minRed_Color2), new Scalar(maxBlue_Color2, maxGreen_Color2, maxRed_Color2), bgrThresh_Color2);

        //! [houghcircles]
        Mat circles_Color1 = new Mat();
        Imgproc.HoughCircles(bgrThresh_Color1, circles_Color1, Imgproc.HOUGH_GRADIENT, 3, 0.5, 50, 25, 0, 15);

        Mat circles_Color2 = new Mat();
        Imgproc.HoughCircles(bgrThresh_Color2, circles_Color2, Imgproc.HOUGH_GRADIENT, 3, 0.5, 50, 25, 0, 15);

        //! [draw] Color 1
        for (int x = 0; x < circles_Color1.cols(); x++) {
            double[] c = circles_Color1.get(0, x);
            Point center = new Point(Math.round(c[0]), Math.round(c[1]));
            // circle center
            Imgproc.circle(src, center, 1, new Scalar(0,100,100), 3, 8, 0 );
            // circle outline
            int radius = (int) Math.round(c[2]);
            Imgproc.circle(src, center, radius, new Scalar(255,0,255), 3, 8, 0 );

            System.out.println(center);
        }

        //! [draw] Color 2
        for (int x = 0; x < circles_Color2.cols(); x++) {
            double[] c = circles_Color2.get(0, x);
            Point center = new Point(Math.round(c[0]), Math.round(c[1]));
            // circle center
            Imgproc.circle(src, center, 1, new Scalar(0,100,100), 3, 8, 0 );
            // circle outline
            int radius = (int) Math.round(c[2]);
            Imgproc.circle(src, center, radius, new Scalar(255,0,255), 3, 8, 0 );

            System.out.println(center);
        }

        Mat reziseImg = new Mat();
        Mat reziseColor1Img = new Mat();
        Mat reziseColor2Img = new Mat();
        Size scaleSize = new Size(1000, 680);
        Imgproc.resize(src, reziseImg, scaleSize, 0, 0, Imgproc.INTER_AREA);
        Imgproc.resize(bgrThresh_Color1, reziseColor1Img, scaleSize, 0, 0, Imgproc.INTER_AREA);
        Imgproc.resize(bgrThresh_Color2, reziseColor2Img, scaleSize, 0, 0, Imgproc.INTER_AREA);


        // ! [display]
        HighGui.imshow("detected robot", reziseImg);
        //HighGui.imshow("detected robot", reziseColor1Img);
        //HighGui.imshow("detected robot", reziseColor2Img);
        HighGui.waitKey();

        System.exit(0);
    }
}
