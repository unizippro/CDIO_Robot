package group14.opencv.detectors;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

public class RobotDetector {

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
    int minRed_Color2 = 70;
    int minGreen_Color2 = 150;
    int minBlue_Color2 = 70;
    int maxRed_Color2 = 150;
    int maxGreen_Color2 = 255;
    int maxBlue_Color2 = 150;

    double camHeight = 165;
    double robotFrontHeight = 28;
    double robotBackHeight = 27;

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

        Point imgCenter = new Point(src.width()/2, src.height()/2);
        //! [draw] Color 1
        for (int x = 0; x < circles_Color1.cols(); x++) {
            double[] c = circles_Color1.get(0, x);
            Point center = new Point(Math.round(c[0]), Math.round(c[1]));
            Point finalCenter = projectPoint(camHeight, robotFrontHeight, imgCenter, center);
            // circle center
            Imgproc.circle(src, finalCenter, 1, new Scalar(0,100,100), 3, 8, 0 );
            // circle outline
            int radius = (int) Math.round(c[2]);
            Imgproc.circle(src, finalCenter, radius, new Scalar(255,0,255), 3, 8, 0 );

            System.out.println(finalCenter);
        }

        //! [draw] Color 2
        for (int x = 0; x < circles_Color2.cols(); x++) {
            double[] c = circles_Color2.get(0, x);
            Point center = new Point(Math.round(c[0]), Math.round(c[1]));
            Point finalCenter = projectPoint(camHeight, robotBackHeight, imgCenter, center);
            // circle center
            Imgproc.circle(src, finalCenter, 1, new Scalar(0,100,100), 3, 8, 0 );
            // circle outline
            int radius = (int) Math.round(c[2]);
            Imgproc.circle(src, finalCenter, radius, new Scalar(255,0,255), 3, 8, 0 );

            System.out.println(finalCenter);
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

    public Point projectPoint(double camHeight, double objectHeight, Point centerPoint, Point projectPoint) {
        //Camheight og objectHeight er angviet i pixel så de konverteres
        camHeight = camHeight*2.8;
        objectHeight = objectHeight*2.8;

        double grundLinje = Math.sqrt(Math.pow(centerPoint.x-projectPoint.x, 2)+ Math.pow(centerPoint.y-projectPoint.y, 2));
        double vinkelProjectPoint = Math.toDegrees(Math.asin(camHeight/(Math.sqrt(Math.pow(camHeight, 2)+Math.pow(grundLinje, 2)))));

        double robotTopVinkel = 90-vinkelProjectPoint;
        double projectLength = (objectHeight*robotTopVinkel)/vinkelProjectPoint;
        double grundLinje2 = grundLinje-projectLength;
        double strengthFactor = grundLinje2/grundLinje;
        double xChange = centerPoint.x-projectPoint.x;
        double yChange = centerPoint.y - projectPoint.y;
        Point newPoint = new Point(centerPoint.x-xChange*strengthFactor,centerPoint.y-yChange*strengthFactor);
        return newPoint;
    }
}
