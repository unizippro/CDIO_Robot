package group14.test_apps.opencv;

import group14.Resources;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class CalibrationTestRun {

    static {
        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        AtomicBoolean isCalibrated = new AtomicBoolean(false);
        var boardNumbers = 50;
        AtomicInteger successes = new AtomicInteger();

        var numberCornersHorizontal = 7;
        var numberCornersVertical = 6;


        var files = Resources.OpenCVSamples.getAllChessboardFiles();

        var imagePoints = new ArrayList<Mat>();
        var objectPoints = new ArrayList<Mat>();

        var obj = new MatOfPoint3f();
        var ref = new Object() {
            MatOfPoint2f imageCorners = new MatOfPoint2f();
        };
        var intrinsic = new Mat(3, 3, CvType.CV_32FC1);
        var distCoeffs = new Mat();

        for (int i = 0; i < (numberCornersHorizontal * numberCornersVertical); i++) {
            obj.push_back(new MatOfPoint3f(new Point3(i / numberCornersHorizontal, i % numberCornersVertical, 0.0f)));
        }

        while (true) {
            files.forEach(file -> {
                var frame = Imgcodecs.imread(file, Imgcodecs.IMREAD_COLOR);

                if (!frame.empty()) {
                    var grayImage = new Mat();

                    if (successes.get() < boardNumbers) {
                        Imgproc.cvtColor(frame, grayImage, Imgproc.COLOR_BGR2GRAY);

                        var boardSize = new Size(numberCornersHorizontal, numberCornersVertical);

                        var found = Calib3d.findChessboardCorners(grayImage, boardSize, ref.imageCorners, Calib3d.CALIB_CB_ADAPTIVE_THRESH + Calib3d.CALIB_CB_NORMALIZE_IMAGE + Calib3d.CALIB_CB_FAST_CHECK);
                        if (found) {
                            var term = new TermCriteria(TermCriteria.EPS | TermCriteria.MAX_ITER, 30, 0.1);
                            Imgproc.cornerSubPix(grayImage, ref.imageCorners, new Size(11, 11), new Size(-1, -1), term);

                            Calib3d.drawChessboardCorners(frame, boardSize, ref.imageCorners, true);

                            imagePoints.add(ref.imageCorners);
                            ref.imageCorners = new MatOfPoint2f();
                            objectPoints.add(obj);
                            successes.getAndIncrement();
                        }
                    } else {
                        var rvecs = new ArrayList<Mat>();
                        var tvecs = new ArrayList<Mat>();

                        intrinsic.put(0, 0, 1);
                        intrinsic.put(1, 1, 1);

                        Calib3d.calibrateCamera(objectPoints, imagePoints, frame.size(), intrinsic, distCoeffs, rvecs, tvecs);
                        isCalibrated.set(true);
                    }

                    if (isCalibrated.get()) {
                        var undistorted = new Mat();

                        Calib3d.undistort(frame, undistorted, intrinsic, distCoeffs);

                        HighGui.imshow("Result", undistorted);
                    } else {
                        HighGui.imshow("Result", frame);
                    }

                    HighGui.waitKey();
                }
            });
        }
    }

}
