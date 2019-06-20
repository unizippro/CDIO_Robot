package group14.navigator;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class NavigatorDrawing {

    private Navigator navigator;
    private final double pixelsPrCm;
    private List<Point> robotPositions = new ArrayList<>();

    public NavigatorDrawing(Navigator navigator, double pixelsPrCm) {
        this.navigator = navigator;
        this.pixelsPrCm = pixelsPrCm;
    }

    public Mat drawOn(Mat frame) {
        var out = new Mat();
        frame.copyTo(out);

        var areas = this.navigator.getBoard().getAreas();

        areas.forEach(area -> {
            var rect = new Rect((int) (area.getX() * this.pixelsPrCm), (int) (area.getY() * this.pixelsPrCm), (int) (area.getWidth() * this.pixelsPrCm), (int) (area.getHeight() * this.pixelsPrCm));
            Imgproc.rectangle(out, rect, new Scalar(0), 3);

            var safetyArea = area.getSafetyArea();
            var safetyRect = new Rect((int) (safetyArea.getX() * this.pixelsPrCm), (int) (safetyArea.getY() * this.pixelsPrCm), (int) (safetyArea.getWidth() * this.pixelsPrCm), (int) (safetyArea.getHeight() * this.pixelsPrCm));

            Imgproc.rectangle(out, safetyRect, new Scalar(0, 0, 255), 3);

            area.getSafePoints().forEach(p -> {
                var point = new Point(p.x * this.pixelsPrCm, p.y * this.pixelsPrCm);
                Imgproc.circle(out, point, 8, new Scalar(160, 0, 0), 3);
            });
        });

        var robotPosition = this.navigator.getRobotPosition();
        var point = new Point(robotPosition.x * this.pixelsPrCm, robotPosition.y * this.pixelsPrCm);
        Imgproc.circle(out, point, 8, new Scalar(0, 255, 255), 3);

//        this.robotPositions.add(point);
//
//        if (this.robotPositions.size() > 1) {
//            for (int i = 1; i < this.robotPositions.size(); i++) {
//                var pointPrev = this.robotPositions.get(i - 1);
//                var pointNow = this.robotPositions.get(i);
//
//                var pointPrevOpen = new Point(pointPrev.x * this.pixelsPrCm, pointPrev.y * this.pixelsPrCm);
//                var pointNowOpen = new Point(pointNow.x * this.pixelsPrCm, pointNow.y * this.pixelsPrCm);
//
//                Imgproc.arrowedLine(out, pointPrevOpen, pointNowOpen, new Scalar(0, 255, 255), 3);
//            }
//        }

        var balls = this.navigator.getBallPositions();
        balls.forEach(p -> {
            var pointBall = new Point(p.x * this.pixelsPrCm, p.y * this.pixelsPrCm);
            Imgproc.circle(out, pointBall, 8, new Scalar(255, 0, 255), 3);
        });

        return out;
    }
}
