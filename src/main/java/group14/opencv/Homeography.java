package group14.opencv;

import group14.opencv.detectors.board_detector.BoardDetector;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class Homeography implements CalibratedCamera.CalibrationCustomHandler {

    private static final double LONG_LENGTH = 166.8;

    private final BoardDetector boardDetector;
    private boolean isHomo = false;
    private Mat homeographyMat;
    private Size homeoSize;
    private double pixelsPrCm;


    public Homeography(BoardDetector boardDetector) {
        this.boardDetector = boardDetector;
    }

    public double getPixelsPrCm() {
        return this.pixelsPrCm;
    }

    @Override
    public void handleCustomCalibration(Mat frame, Mat outFrame) {
        if (this.isHomo) {
            Imgproc.warpPerspective(frame, outFrame, this.homeographyMat, this.homeoSize);
        } else {
            var boardDetectorResult = this.boardDetector.run(frame);

            var corners = boardDetectorResult.getCorners();
            if (corners.size() == 4) {
                var margin = 100;

                var width = (Math.max(corners.get(1).x, corners.get(3).x) + margin) - (Math.min(corners.get(0).x, corners.get(2).x) - margin);
                var height = (Math.max(corners.get(2).y, corners.get(3).y) + margin) - (Math.min(corners.get(0).y, corners.get(1).y) - margin);

                var aspectRatio = width / height;
                this.homeoSize = new Size(width * aspectRatio, height);

                var srcMat = new MatOfPoint2f(
                        new Point(corners.get(0).x - margin, corners.get(0).y - margin),
                        new Point(corners.get(1).x + margin, corners.get(1).y - margin),
                        new Point(corners.get(2).x - margin, corners.get(2).y + margin),
                        new Point(corners.get(3).x + margin, corners.get(3).y + margin)
                );
                var dst = new MatOfPoint2f(
                        new Point(0, 0),
                        new Point(this.homeoSize.width, 0),
                        new Point(0, this.homeoSize.height),
                        new Point(this.homeoSize.width, this.homeoSize.height)
                );

                // Calculate Homo
                this.homeographyMat = Imgproc.getPerspectiveTransform(srcMat, dst);

                Imgproc.warpPerspective(frame, outFrame, this.homeographyMat, this.homeoSize);

                var result = this.boardDetector.run(outFrame);

                this.pixelsPrCm = (result.getCorners().get(1).x - result.getCorners().get(0).x) / LONG_LENGTH;
                System.out.println("Pixel ratio: " + this.pixelsPrCm);

                this.isHomo = true;
            } else {
                frame.copyTo(outFrame);
            }
        }
    }
}
