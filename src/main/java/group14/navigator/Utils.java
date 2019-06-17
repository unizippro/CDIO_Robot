package group14.navigator;

import lejos.robotics.geometry.Point2D;
import lejos.robotics.geometry.Rectangle2D;

import java.util.Arrays;
import java.util.List;

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
                var left = new Rectangle2D.Double(rectangle.x, rectangle.y, rectangle.x + point.x, rectangle.height);
                var right = new Rectangle2D.Double(rectangle.x + point.x, rectangle.y, rectangle.width - point.x, rectangle.height);

                return Arrays.asList(left, right);

            case HORIZONTAL:
                var top = new Rectangle2D.Double(rectangle.x, rectangle.y, rectangle.width, rectangle.y + point.y);
                var bottom = new Rectangle2D.Double(rectangle.x, rectangle.y + point.y, rectangle.width, rectangle.height - point.y);

                return Arrays.asList(top, bottom);
        }

        throw new RuntimeException("Not valid split direction: " + direction);
    }

    public static Point2D.Double rectangleGetCenter(Rectangle2D.Double rectangle) {
        return new Point2D.Double(rectangle.getCenterX(), rectangle.getCenterY());
    }
}
