package group14.opencv.detectors.board_detector;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.List;

public class Corners {
    private final double marginWidth;
    private final double marginHeight;
    private Size imageSize;

    private Point upperLeftPoint;
    private Point upperRightPoint;
    private Point lowerRightPoint;
    private Point lowerLeftPoint;

    Corners(Size imageSize, double margin) {
        this.imageSize = imageSize;

        margin = Math.max(0, Math.min(margin, 50));

        this.marginWidth = imageSize.width * (margin / 100.0);
        this.marginHeight = imageSize.height * (margin / 100.0);
    }

    public Point getUpperLeftPoint() {
        return upperLeftPoint;
    }

    public Point getUpperRightPoint() {
        return upperRightPoint;
    }

    public Point getLowerRightPoint() {
        return lowerRightPoint;
    }

    public Point getLowerLeftPoint() {
        return lowerLeftPoint;
    }

    public void calculatePoints(List<Point> points) {
        for (Point point : points) {
            if (this.isWithin(point, Corners.ImageLocation.TOP_LEFT)) {
                if (upperLeftPoint == null || (point.x > upperLeftPoint.x || point.y > upperLeftPoint.y)) {
                    upperLeftPoint = point;
                }
            } else if (this.isWithin(point, Corners.ImageLocation.TOP_RIGHT)) {
                if (upperRightPoint == null || (point.x < upperRightPoint.x || point.y > upperRightPoint.y)) {
                    upperRightPoint = point;
                }
            } else if (this.isWithin(point, Corners.ImageLocation.BOTTOM_RIGHT)) {
                if (lowerRightPoint == null || (point.x < lowerRightPoint.x || point.y < lowerRightPoint.y)) {
                    lowerRightPoint = point;
                }
            } else if (this.isWithin(point, Corners.ImageLocation.BOTTOM_LEFT)) {
                if (lowerLeftPoint == null || (point.x > lowerLeftPoint.x || point.y < lowerLeftPoint.y)) {
                    lowerLeftPoint = point;
                }
            }
        }
    }

    protected void draw(Mat image) {
        if (this.upperLeftPoint != null) {
            Imgproc.circle(image, this.upperLeftPoint, 5, new Scalar(255, 255, 255), 2, 8, 0);
        }
        if (this.upperRightPoint != null) {
            Imgproc.circle(image, this.upperRightPoint, 5, new Scalar(255, 255, 255), 2, 8, 0);
        }
        if (this.lowerRightPoint != null) {
            Imgproc.circle(image, this.lowerRightPoint, 5, new Scalar(255, 255, 255), 2, 8, 0);
        }
        if (this.lowerLeftPoint != null) {
            Imgproc.circle(image, this.lowerLeftPoint, 5, new Scalar(255, 255, 255), 2, 8, 0);
        }

        Imgproc.rectangle(image, new Rect(0, 0, (int) this.marginWidth, (int) this.marginHeight), new Scalar(0), 3);
        Imgproc.rectangle(image, new Rect((int) (this.imageSize.width - this.marginWidth), 0, (int) this.marginWidth, (int) this.marginHeight), new Scalar(0), 3);
        Imgproc.rectangle(image, new Rect(0, (int) (this.imageSize.height - this.marginHeight), (int) this.marginWidth, (int) this.marginHeight), new Scalar(0), 3);
        Imgproc.rectangle(image, new Rect((int) (this.imageSize.width - this.marginWidth), (int) (this.imageSize.height - this.marginHeight), (int) this.marginWidth, (int) this.marginHeight), new Scalar(0), 3);

        if (this.upperLeftPoint != null && this.upperRightPoint != null && this.lowerRightPoint != null && this.lowerLeftPoint != null) {
            Imgproc.line(image, this.upperLeftPoint, this.upperRightPoint, new Scalar(0, 255, 255), 3);
            Imgproc.line(image, this.upperRightPoint, this.lowerRightPoint, new Scalar(0, 255, 255), 3);
            Imgproc.line(image, this.lowerRightPoint, this.lowerLeftPoint, new Scalar(0, 255, 255), 3);
            Imgproc.line(image, this.lowerLeftPoint, this.upperLeftPoint, new Scalar(0, 255, 255), 3);
        }
    }

    private boolean isWithin(Point point, ImageLocation corner) {
        switch (corner) {
            case TOP_LEFT:
                return point.x < this.marginWidth && point.y < this.marginHeight;
            case TOP_RIGHT:
                return point.x > (this.imageSize.width - this.marginWidth) && point.y < this.marginHeight;
            case BOTTOM_LEFT:
                return point.x < this.marginWidth && point.y > (this.imageSize.height - this.marginHeight);
            case BOTTOM_RIGHT:
                return point.x > (this.imageSize.width - this.marginWidth) && point.y > (this.imageSize.height - this.marginHeight);
            default:
                return false;
        }
    }

    @Override
    public String toString() {
        return "[" + this.upperLeftPoint + ", " + this.upperRightPoint + ", " + this.lowerRightPoint + ", " + this.lowerLeftPoint + "]";
    }


    enum ImageLocation {
        TOP_LEFT, TOP_RIGHT,
        BOTTOM_RIGHT, BOTTOM_LEFT
    }
}
