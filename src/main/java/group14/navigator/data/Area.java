package group14.navigator.data;

import group14.navigator.Calculator;
import group14.navigator.Utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Area {

    private final Rectangle2D boundingRect;
    private final double safetyMargin;
    private SafePointLocation safePointLocation;

    private final Rectangle2D safetyArea;

    private final Point2D safePointTop;
    private final Point2D safePointBottom;

    public enum DangerousAreaDirection {
        TOP_LEFT, TOP_RIGHT, DOWN_LEFT, DOWN_RIGHT,
        TOP, BOTTOM, LEFT, RIGHT;

        public static boolean isCorner(DangerousAreaDirection direction) {
            return direction == Area.DangerousAreaDirection.TOP_LEFT || direction == Area.DangerousAreaDirection.TOP_RIGHT || direction == Area.DangerousAreaDirection.DOWN_LEFT || direction == Area.DangerousAreaDirection.DOWN_RIGHT;
        }
    }

    public enum SafePointLocation {
        ALL, TOP, DOWN;

        public boolean isTop() {
            return this == TOP;
        }

        public boolean isDown() {
            return this == DOWN;
        }
    }

    Area(Rectangle2D boundingRect, double safetyMargin, SafePointLocation safePointLocation) {
        this.boundingRect = boundingRect;
        this.safetyMargin = safetyMargin;
        this.safePointLocation = safePointLocation;

        this.safetyArea = Utils.rectangleWithExpandedMargin(boundingRect, -safetyMargin);

        var horizontalSplit = Utils.rectangleSplitAt(this.boundingRect, Utils.rectangleGetCenter(this.boundingRect), Utils.Split.HORIZONTAL);

        this.safePointTop = Utils.rectangleGetCenter(horizontalSplit.get(0));
        this.safePointBottom = Utils.rectangleGetCenter(horizontalSplit.get(1));
    }

    public Rectangle2D getBoundingRect() {
        return this.boundingRect;
    }

    public double getX() {
        return this.boundingRect.getX();
    }

    public double getY() {
        return this.boundingRect.getY();
    }

    public double getWidth() {
        return this.boundingRect.getWidth();
    }

    public double getHeight() {
        return this.boundingRect.getHeight();
    }

    public boolean contains(Point2D point) {
        return this.boundingRect.contains(point);
    }

    public boolean isWithinSafetyArea(Point2D point) {
        return this.safetyArea.contains(point);
    }

    public DangerousAreaDirection getDangerousAreaDirection(Point2D point) throws Exception {
        var extraRect = Utils.rectangleWithExpandedMargin(this.boundingRect, 2);

        var isTop = extraRect.getMinY() <= point.y && point.y <= this.safetyArea.getMinY();
        var isBottom = extraRect.getMaxY() >= point.y && point.y >= this.safetyArea.getMaxY();
        var isLeft = extraRect.getMinX() <= point.x && point.x <= this.safetyArea.getMinX();
        var isRight = extraRect.getMaxX() >= point.x && point.x >= this.safetyArea.getMaxX();

        if (isTop) {
            if (isLeft) {
                return DangerousAreaDirection.TOP_LEFT;
            } else if (isRight) {
                return DangerousAreaDirection.TOP_RIGHT;
            } else {
                return DangerousAreaDirection.TOP;
            }
        } else if (isBottom) {
            if (isLeft) {
                return DangerousAreaDirection.DOWN_LEFT;
            } else if (isRight) {
                return DangerousAreaDirection.DOWN_RIGHT;
            } else {
                return DangerousAreaDirection.BOTTOM;
            }
        } else if (isLeft) {
            return DangerousAreaDirection.LEFT;
        } else if (isRight) {
            return DangerousAreaDirection.RIGHT;
        }

        throw new Exception(point + " is not in a dangerous area - this should be checked first");
    }

    public Point2D getProjectedPoint(Point2D point, DangerousAreaDirection direction) throws Exception {
        var safetyDistance = this.safetyMargin * 1.5;

        switch (direction) {
            case TOP:
                var startPointTop = new Point2D(point.x, this.boundingRect.getMinY());

                return Calculator.getVectorEndPoint(startPointTop, 90, safetyDistance);

            case BOTTOM:
                var startPointBottom = new Point2D(point.x, this.boundingRect.getMaxY());

                return Calculator.getVectorEndPoint(startPointBottom, 270, safetyDistance);

            case LEFT:
                var startPointLeft = new Point2D(this.boundingRect.getMinX(), point.y);

                return Calculator.getVectorEndPoint(startPointLeft, 0, safetyDistance);

            case RIGHT:
                var startPointRight = new Point2D(this.boundingRect.getMaxX(), point.y);

                return Calculator.getVectorEndPoint(startPointRight, 180, safetyDistance);

            case TOP_LEFT:
                var startPointTopLeft = new Point2D(this.boundingRect.getMinX(), this.boundingRect.getMinY());

                return Calculator.getVectorEndPoint(startPointTopLeft, 45, safetyDistance * 1.25);

            case TOP_RIGHT:
                var startPointTopRight = new Point2D(this.boundingRect.getMaxX(), this.boundingRect.getMinY());

                return Calculator.getVectorEndPoint(startPointTopRight, 135, safetyDistance * 1.25);

            case DOWN_LEFT:
                var startPointDownLeft = new Point2D(this.boundingRect.getMinX(), this.boundingRect.getMaxY());

                return Calculator.getVectorEndPoint(startPointDownLeft, 315, safetyDistance * 1.25);

            case DOWN_RIGHT:
                var startPointDownRight = new Point2D(this.boundingRect.getMaxX(), this.boundingRect.getMaxY());

                return Calculator.getVectorEndPoint(startPointDownRight, 225, safetyDistance * 1.25);
        }

        throw new Exception("Direction cannot be null");
    }

    public Point2D getNearestSafePoint(Point2D point) {
        if (this.safePointLocation.isTop() || point.distance(this.safePointTop) < point.distance(this.safePointBottom)) {
            return this.safePointTop;
        } else {
            return this.safePointBottom;
        }
    }

    Point2D getSafePointTop() {
        return this.safePointTop;
    }

    Point2D getSafePointBottom() {
        return this.safePointBottom;
    }

    void setSafePointLocation(SafePointLocation safePointLocation) {
        this.safePointLocation = safePointLocation;
    }

    public Rectangle2D getSafetyArea() {
        return this.safetyArea;
    }

    public List<Point2D> getSafePoints() {
        if (this.safePointLocation.isTop()) {
            return Collections.singletonList(this.safePointTop);
        } else if (this.safePointLocation.isDown()) {
            return Collections.singletonList(this.safePointBottom);
        } else {
            return Arrays.asList(this.safePointTop, this.safePointBottom);
        }
    }

    @Override
    public String toString() {
        return "Area(\n\t" + this.boundingRect + "\n\t" + this.safetyArea + "\n)";
    }
}
