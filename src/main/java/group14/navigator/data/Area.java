package group14.navigator.data;

import group14.navigator.Calculator;
import group14.navigator.Utils;

import java.util.Arrays;
import java.util.List;

public class Area {

    private final Rectangle2D boundingRect;
    private final double safetyMargin;

    private final Rectangle2D safetyArea;

    private final Point2D safePointTop;
    private final Point2D safePointBottom;

    public enum DangerousAreaDirection {
        TOP, BOTTOM, LEFT, RIGHT
    }

    Area(Rectangle2D boundingRect, double safetyMargin) {
        this.boundingRect = boundingRect;
        this.safetyMargin = safetyMargin;

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
        // todo: add checks for corners
        if (this.boundingRect.getMinY() <= point.y && point.y <= this.safetyArea.getMinY()) {
            return DangerousAreaDirection.TOP;
        } else if (this.boundingRect.getMaxY() >= point.y && point.y >= this.safetyArea.getMaxY()) {
            return DangerousAreaDirection.BOTTOM;
        } else if (this.boundingRect.getMinX() <= point.x && point.x <= this.safetyArea.getMinX()) {
            return DangerousAreaDirection.LEFT;
        } else if (this.boundingRect.getMaxX() >= point.x && point.x >= this.safetyArea.getMaxX()) {
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
        }

        throw new Exception("Direction cannot be null");
    }

    public Point2D getNearestSafePoint(Point2D point) {
        if (point.distance(this.safePointTop) < point.distance(this.safePointBottom)) {
            return this.safePointTop;
        } else {
            return this.safePointBottom;
        }
    }

    public Rectangle2D getSafetyArea() {
        return this.safetyArea;
    }

    public List<Point2D> getSafePoints() {
        return Arrays.asList(this.safePointTop, this.safePointBottom);
    }

    @Override
    public String toString() {
        return "Area(\n\t" + this.boundingRect + "\n\t" + this.safetyArea + "\n)";
    }
}
