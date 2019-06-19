package group14.navigator;

import lejos.robotics.geometry.Point2D;
import lejos.robotics.geometry.Rectangle2D;
import org.opencv.core.Point;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {

    public enum Split {
        VERTICAL, HORIZONTAL
    }

    public static Rectangle2D.Double rectangleWithCenter(Point2D.Double point, double margin) {
        return new Rectangle2D.Double(Math.max(point.x - margin, 0), Math.max(point.y - margin, 0), margin * 2, margin * 2);
    }

    public static Rectangle2D.Double rectangleWithExpandedMargin(Rectangle2D.Double rectangle, double margin) {
        return new Rectangle2D.Double(Math.max(rectangle.x - margin, 0), Math.max(rectangle.y - margin, 0), rectangle.width + (margin * 2), rectangle.height + (margin * 2));
    }

    public static List<Rectangle2D.Double> rectangleSplitAt(Rectangle2D.Double rectangle, Point2D.Double point, Split direction) {
        switch (direction) {
            case VERTICAL:
                var leftWidth = point.x - rectangle.x;

                var left = new Rectangle2D.Double(rectangle.x, rectangle.y, leftWidth, rectangle.height);
                var right = new Rectangle2D.Double(point.x, rectangle.y, rectangle.width - leftWidth, rectangle.height);

                return Arrays.asList(left, right);

            case HORIZONTAL:
                var topHeight = point.y - rectangle.y;

                var top = new Rectangle2D.Double(rectangle.x, rectangle.y, rectangle.width, topHeight);
                var bottom = new Rectangle2D.Double(rectangle.x, point.y, rectangle.width, rectangle.height - topHeight);

                return Arrays.asList(top, bottom);
        }

        throw new RuntimeException("Not valid split direction: " + direction);
    }

    public static Point2D.Double rectangleGetCenter(Rectangle2D.Double rectangle) {
        return new Point2D.Double(rectangle.getCenterX(), rectangle.getCenterY());
    }

    public static Point2D.Double toNavigatorPoint(Point point, double ratio) {
        return new Point2D.Double(point.x / ratio, point.y / ratio);
    }

    public static List<Point2D.Double> toNavigatorPoints(List<Point> points, double ratio) {
        return points.stream()
                .map((Point point) -> toNavigatorPoint(point, ratio))
                .collect(Collectors.toList());
    }

    public static Rectangle2D.Double createRectangleFromPoints(List<Point2D.Double> points) {
        if (points.size() != 2) {
            throw new RuntimeException("Cannot create rectangle with " + points.size() + " points");
        }

        var topLeft = points.get(0);
        var bottomRight = points.get(1);

        return new Rectangle2D.Double(topLeft.x, topLeft.y, bottomRight.x - topLeft.x, bottomRight.y - topLeft.y);
    }
}
