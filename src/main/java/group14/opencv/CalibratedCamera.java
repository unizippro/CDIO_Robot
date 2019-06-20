package group14.opencv;

import com.google.gson.*;
import group14.opencv.utils.ImageConverter;
import group14.opencv.detectors.board_detector.BoardDetector;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


public class CalibratedCamera extends Camera {

    private BoardDetector boardDetector;

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
    private Mat intrinsic = new Mat(3, 3, CvType.CV_32FC1);
    private Mat distCoeffs = new Mat();
    private List<Mat> rvecs = new ArrayList<>();
    private List<Mat> tvecs = new ArrayList<>();

    private CalibrationPossibleListener calibrationPossibleListener;
    private CalibrationCustomHandler calibrationCustomHandler;

    public interface CalibrationPossibleListener {
        void calibrationChanged(boolean canCalibrate);
    }

    public interface CalibrationCustomHandler {
        void handleCustomCalibration(Mat frame, Mat outFrame);
    }


    public CalibratedCamera(int cameraIndex, int chessboardCornersHorizontal, int chessboardCornersVertical, BoardDetector.Config boardDetectorConfig) {
        super(cameraIndex);

        this.chessboardCornersHorizontal = chessboardCornersHorizontal;
        this.chessboardCornersVertical = chessboardCornersVertical;

        this.boardDetector = new BoardDetector(boardDetectorConfig);

        this.resetCalibration();

        if (Files.exists(Path.of("calibration_data.json"))) {
            System.out.println("Calibration data exists");
            var calibData = this.readCalibrationDataFromFile();
            if (calibData == null) {
                return;
            }

            this.intrinsic = calibData.intrinsic;
            this.distCoeffs = calibData.distCoeffs;
            this.rvecs = calibData.rvecs;
            this.tvecs = calibData.tvecs;

            this.isCalibrated = true;
            System.out.println("Calibrated from file");
        }
    }

    public CalibratedCamera(int cameraIndex, int numberOfCalibrations, int chessboardCornersHorizontal, int chessboardCornersVertical, BoardDetector.Config boardDetectorConfig) {
        this(cameraIndex, chessboardCornersHorizontal, chessboardCornersVertical, boardDetectorConfig);

        this.numberOfCalibrations = numberOfCalibrations;
    }

    @Override
    public void start(FrameUpdatedHandler updatedHandler) {
        super.start((Mat frame) -> {
            var outFrame = new Mat();

            if (this.isCalibrated) {
                Calib3d.undistort(frame, outFrame, this.intrinsic, this.distCoeffs);
            } else {
                this.drawCalibration(frame, outFrame);
            }

            Mat updatedFrame = new Mat();

            if (this.calibrationCustomHandler != null) {
                this.calibrationCustomHandler.handleCustomCalibration(outFrame, updatedFrame);
            } else {
                outFrame.copyTo(updatedFrame);
            }

            frame.release();
            outFrame.release();

            updatedHandler.frameUpdated(updatedFrame);
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

            this.saveCalibrationDataToFile();
        }
    }

    private CalibrationData readCalibrationDataFromFile() {
        var gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Mat.class, (JsonSerializer<Mat>) (src, typeOfSrc, context) -> ImageConverter.matToJson(src))
                .registerTypeAdapter(Mat.class, (JsonDeserializer<Mat>) (src, typeOfT, context) -> ImageConverter.matFromJson(src))
                .create();

        try {
            var fr = new FileReader("calibration_data.json");
            var calibrationData = gson.fromJson(fr, CalibrationData.class);
            fr.close();

            return calibrationData;
        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }
    }

    public void saveCalibrationDataToFile() {
        var gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Mat.class, (JsonSerializer<Mat>) (src, typeOfSrc, context) -> ImageConverter.matToJson(src))
                .registerTypeAdapter(Mat.class, (JsonDeserializer<Mat>) (src, typeOfT, context) -> ImageConverter.matFromJson(src))
                .create();

        var json = gson.toJson(new CalibrationData(intrinsic, distCoeffs, rvecs, tvecs));
        try (var fw = new FileWriter("calibration_data.json")) {
            fw.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isCalibrated() {
        return this.isCalibrated;
    }

    public void setCalibrationPossibleListener(CalibrationPossibleListener listener) {
        this.calibrationPossibleListener = listener;
    }

    public void setCalibrationCustomHandler(CalibrationCustomHandler calibrationCustomHandler) {
        this.calibrationCustomHandler = calibrationCustomHandler;
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


    private class CalibrationData {

        public Mat intrinsic;
        public Mat distCoeffs;
        public List<Mat> rvecs;
        public List<Mat> tvecs;

        public CalibrationData(Mat intrinsic, Mat distCoeffs, List<Mat> rvecs, List<Mat> tvecs) {
            this.intrinsic = intrinsic;
            this.distCoeffs = distCoeffs;
            this.rvecs = rvecs;
            this.tvecs = tvecs;
        }

    }

}
