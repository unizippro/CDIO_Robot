package OpenCV;

import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import static org.opencv.imgcodecs.Imgcodecs.imread;

class openCVCircleDetect {

    public void run(String[] args) {
        String default_file = "C:\\Users\\Sebastian\\Downloads\\IMG_4838.jpg";
        String filename = ((args.length > 0) ? args[0] : default_file);

        // Load an image
        Mat src = Imgcodecs.imread(filename, Imgcodecs.IMREAD_COLOR);

        // Check if image is loaded fine
        if( src.empty() ) {
            System.out.println("Error opening image!");
            System.out.println("Program Arguments: [image_name -- default "
                    + default_file +"] \n");
            System.exit(-1);
        }

        //Blur mat to better define objects
        Imgproc.medianBlur(src, src, 5);
        //Set threshold
        Mat thresh = new Mat();
        Core.inRange(src, new Scalar(210, 210, 210), new Scalar(255, 255, 255), thresh);

        //! [houghcircles]
        Mat circles = new Mat();
        Imgproc.HoughCircles(thresh, circles, Imgproc.HOUGH_GRADIENT, 2, 40, 100, 46, 10, 200);

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