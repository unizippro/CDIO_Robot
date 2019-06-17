package group14.opencv;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;


public class CalibratedCamera extends Camera {

    private int numberOfCalibrations = 10;
    private int chessboardCornersHorizontal;
    private int chessboardCornersVertical;

    private int numberOfSuccess;
    private boolean isCalibrated = false;
    private volatile boolean canCalibrate = false;

    private Size boardSize;

    private final MatOfPoint3f objects = new MatOfPoint3f();
    private final AtomicReference<MatOfPoint2f> imageCorners = new AtomicReference<>(new MatOfPoint2f());

    private final ArrayList<Mat> imagePoints = new ArrayList<>();
    private final ArrayList<Mat> objectPoints = new ArrayList<>();

    // Information which can be used for undistortion, point projection etc
    private final Mat intrinsic = new Mat(3, 3, CvType.CV_32FC1);
    private final Mat distCoeffs = new Mat();
    private final ArrayList<Mat> rvecs = new ArrayList<>();
    private final ArrayList<Mat> tvecs = new ArrayList<>();

    private CalibrationPossibleListener calibrationPossibleListener;

    public interface CalibrationPossibleListener {
        void calibrationChanged(boolean canCalibrate);
    }


    public CalibratedCamera(int cameraIndex, int chessboardCornersHorizontal, int chessboardCornersVertical) {
        super(cameraIndex);

        this.chessboardCornersHorizontal = chessboardCornersHorizontal;
        this.chessboardCornersVertical = chessboardCornersVertical;

        this.resetCalibration();
    }

    public CalibratedCamera(int cameraIndex, int numberOfCalibrations, int chessboardCornersHorizontal, int chessboardCornersVertical) {
        this(cameraIndex, chessboardCornersHorizontal, chessboardCornersVertical);

        this.numberOfCalibrations = numberOfCalibrations;
    }

    @Override
    public void start(FrameUpdatedHandler updatedHandler) {
        super.start((Mat frame) -> {
            var outFrame = new Mat();

            if (this.isCalibrated) {
                Calib3d.undistort(frame, outFrame, this.intrinsic, this.distCoeffs);
                //Homeography here.
            } else {
                this.drawCalibration(frame, outFrame);
            }

            updatedHandler.frameUpdated(outFrame);
        });
    }

    public synchronized void saveCalibrationData() {
        if (this.isCalibrated) {
            return;
        }

        if (this.numberOfSuccess < this.numberOfCalibrations) {
            var imageCorners = this.imageCorners.getAndSet(new MatOfPoint2f());

            this.imagePoints.add(imageCorners);
            this.objectPoints.add(this.objects);

            this.numberOfSuccess++;
        } else {
            this.intrinsic.put(0, 0, 1);
            this.intrinsic.put(1, 1, 1);

            Calib3d.calibrateCamera(this.objectPoints, this.imagePoints, FRAME_SIZE, this.intrinsic, this.distCoeffs, this.rvecs, this.tvecs);
            this.isCalibrated = true;
        }
    }

    public void setCalibrationPossibleListener(CalibrationPossibleListener listener) {
        this.calibrationPossibleListener = listener;
    }

    private void drawCalibration(Mat frame, Mat outFrame) {
        frame.copyTo(outFrame);

        var grayImage = new Mat();
        Imgproc.cvtColor(frame, grayImage, Imgproc.COLOR_BGR2GRAY);

        var imageCorners = this.imageCorners.get();

        var canCalibrate = Calib3d.findChessboardCorners(grayImage, this.boardSize, imageCorners, Calib3d.CALIB_CB_ADAPTIVE_THRESH + Calib3d.CALIB_CB_NORMALIZE_IMAGE + Calib3d.CALIB_CB_FAST_CHECK);
        if (canCalibrate) {
            var term = new TermCriteria(TermCriteria.EPS | TermCriteria.MAX_ITER, 30, 0.1);
            Imgproc.cornerSubPix(grayImage, imageCorners, new Size(11, 11), new Size(-1, -1), term);

            Calib3d.drawChessboardCorners(outFrame, this.boardSize, imageCorners, true);
        }

        if (canCalibrate != this.canCalibrate && this.calibrationPossibleListener != null) {
            this.canCalibrate = canCalibrate;
            this.calibrationPossibleListener.calibrationChanged(canCalibrate);
        }
    }

    private void resetCalibration() {
        this.isCalibrated = false;
        this.numberOfSuccess = 0;
        this.imagePoints.clear();
        this.objectPoints.clear();

        this.boardSize = new Size(this.chessboardCornersHorizontal, this.chessboardCornersVertical);

        for (int i = 0; i < (this.chessboardCornersHorizontal * this.chessboardCornersVertical); i++) {
            this.objects.push_back(new MatOfPoint3f(new Point3(i / this.chessboardCornersHorizontal, i % this.chessboardCornersVertical, 0.0f)));
        }
    }

}
