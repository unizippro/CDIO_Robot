package group14.opencv.detectors.ball_detector;

import com.google.common.util.concurrent.AtomicDouble;
import group14.opencv.detectors.Detector;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;


public class BallDetector extends Detector<BallDetectorResult, BallDetector.Config> {

    public static class Config {

        public AtomicInteger ballDECircle = new AtomicInteger(25);
        public AtomicInteger ballThreshold2 = new AtomicInteger(200);
        public AtomicInteger ballGausBlurSize = new AtomicInteger(5);
        public AtomicDouble lowerThreshold = new AtomicDouble(200);

        private AtomicDouble houghDp = new AtomicDouble(2);
        private AtomicDouble houghMinDist = new AtomicDouble(10);
        public AtomicDouble houghParam1 = new AtomicDouble(80);
        public AtomicDouble houghParam2 = new AtomicDouble(28);
        private AtomicInteger houghMinRadius = new AtomicInteger(10);
        private AtomicInteger houghMaxRadius = new AtomicInteger(20);

        public AtomicInteger ballMinH = new AtomicInteger(1);
        public AtomicInteger ballMaxH = new AtomicInteger(140);
        public AtomicInteger ballMinS = new AtomicInteger(1);
        public AtomicInteger ballMaxS = new AtomicInteger(60);
        public AtomicInteger ballMinV = new AtomicInteger(200);
        public AtomicInteger ballMaxV = new AtomicInteger(235);

    }

    public BallDetectorResult run(Mat src) {
        var config = this.getConfig();
        var points = new ArrayList<Point>();
        Mat out = new Mat();
        src.copyTo(out);
        Mat work = new Mat();
        src.copyTo(work);

        // Used to Dilate and Erode
        int diaCircle = config.ballDECircle.get();
        int eroCircle = config.ballDECircle.get()+3;

        Mat diaMat = Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(diaCircle, diaCircle), new Point(diaCircle/2, diaCircle/2));
        Mat eroMat = Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(eroCircle, eroCircle), new Point(eroCircle/2, eroCircle/2));


        Imgproc.cvtColor(work, work, Imgproc.COLOR_BGR2HSV);


        Core.inRange(work
                , new Scalar(config.ballMinH.get(), config.ballMinS.get(), config.ballMinV.get())
                , new Scalar(config.ballMaxH.get(), config.ballMaxS.get(), config.ballMaxV.get())
                , work);

        Imgproc.dilate(work , work, diaMat);
        Imgproc.erode(work , work, eroMat);
        Imgproc.dilate(work , work, diaMat);
        Imgproc.erode(work , work, diaMat);
        Imgproc.GaussianBlur(work, work, new Size(config.ballGausBlurSize.get(),config.ballGausBlurSize.get()),0);


        //int circle = 22;
//
//        var points = new ArrayList<Point>();
//        var out = new Mat();
//        Mat work = new Mat();
//        src.copyTo(work);
//        src.copyTo(out);
//        Mat element = Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(circle, circle),
//                new Point(circle/2, circle/2));
//        var config = this.getConfig();
//
//        //var otsu = Imgproc.T
//
//        //Imgproc.Canny(srcBlur, detectedEdges, lowThresh, lowThresh * RATIO, KERNEL_SIZE, false);
//
//        Imgproc.threshold(work, work, config.ballDECircle.get(), 255, Imgproc.ADAPTIVE_THRESH_MEAN_C);
//        //Imgproc.threshold(src, src, 200, 255, 0);
//        //Imgproc.dilate(work, work, element);
//        Imgproc.cvtColor(work, work, Imgproc.COLOR_BGR2GRAY );
//        //Imgproc.cvtColor(src, src, Imgproc.COLOR_BGR2HSV );
//        Imgproc.GaussianBlur(work, work, new Size( config.ballGausBlurSize.get(), config.ballGausBlurSize.get()), 0);
//        Imgproc.threshold(work, work, config.ballDECircle.get(), 255, Imgproc.THRESH_BINARY);
//        //Imgproc.Canny(src,src,300,300);
//        //Imgproc.dilate(work, work, element);
//        //Imgproc.erode(work, work, element);
//
//        //List<MatOfPoint> contours = new ArrayList<>();
//        //Imgproc.findContours(work, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
//
//        //System.out.println(contours.get(0).toString());

        /*
         Tegner Kanter
        Mat drawing = Mat.zeros(src.size(), CvType.CV_8UC3);
        for (int i = 0; i < contours.size(); i++) {
            Scalar color = new Scalar(150,150,150);
            Imgproc.drawContours(drawing, contours, i, color, 2, Imgproc.LINE_8, out, 0, new Point());
        }
        */



        Mat circles = new Mat();
        Imgproc.HoughCircles(work, circles, Imgproc.HOUGH_GRADIENT, config.houghDp.get(), config.houghMinDist.get(), config.houghParam1.get(), config.houghParam2.get(), config.houghMinRadius.get(), config.houghMaxRadius.get());

        //! [draw]
//        System.out.println("Fandt " + circles.cols() + " Bolde");
        for (int x = 0; x < circles.cols(); x++) {
            double[] c = circles.get(0, x);
            Point center = new Point(Math.round(c[0]), Math.round(c[1]));
            points.add(center);

            // circle center
            Imgproc.circle(out, center, 1, new Scalar(0, 100, 100), 3, 8, 0);

            // circle outline
            int radius = (int) Math.round(c[2]);
            Imgproc.circle(out, center, radius, new Scalar(255, 0, 255), 3, 8, 0);
        }


        //! [draw]


        return new BallDetectorResult(out, work, points);
    }

    @Override
    protected Config createConfig() {
        return new Config();
    }

    private Mat threshold(Mat src, double lower, double upper) {
        Mat out = new Mat();
        Core.inRange(src, new Scalar(lower, lower, lower), new Scalar(upper, upper, upper), out);

        return out;
    }
}