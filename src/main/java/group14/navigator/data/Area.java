package group14.navigator.data;

import group14.navigator.Calculator;
import lejos.robotics.geometry.Point2D;
import lejos.robotics.geometry.Rectangle2D;

public class Area {

    private final Rectangle2D.Double boundingRect;
    private final double safetyMargin;

    private final Rectangle2D.Double safetyArea;

    private final Point2D.Double safePointTop;
    private final Point2D.Double safePointBottom;

    public enum DangerousAreaDirection {
        TOP, BOTTOM, LEFT, RIGHT
    }

    Area(Rectangle2D.Double boundingRect, double safetyMargin) {
        this.boundingRect = boundingRect;
        this.safetyMargin = safetyMargin;

        this.safetyArea = new Rectangle2D.Double(boundingRect.x + safetyMargin, boundingRect.y + safetyMargin, boundingRect.width - (safetyMargin * 2), boundingRect.height - (safetyMargin * 2));

        var top = new Rectangle2D.Double(this.boundingRect.x, this.boundingRect.y, this.boundingRect.width, this.boundingRect.height / 2);
        var bottom = new Rectangle2D.Double(this.boundingRect.x, this.boundingRect.getCenterY(), this.boundingRect.width, this.boundingRect.height / 2);

        this.safePointTop = new Point2D.Double(top.getCenterX(), top.getCenterY());
        this.safePointBottom = new Point2D.Double(bottom.getCenterX(), bottom.getCenterY());
    }

    public Rectangle2D.Double getBoundingRect() {
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

    public boolean contains(Point2D.Double point) {
        return this.boundingRect.contains(point);
    }

    public boolean isWithinSafetyArea(Point2D.Double point) {
        return this.safetyArea.contains(point);
    }

    public DangerousAreaDirection getDangerousAreaDirection(Point2D.Double point) throws Exception {
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

    public Point2D.Double getProjectedPoint(Point2D.Double point, DangerousAreaDirection direction) throws Exception {
        switch (direction) {
            case TOP:
                var startPointTop = new Point2D.Double(point.x, this.boundingRect.getMinY());

                return Calculator.getVectorEndPoint(startPointTop, 90, this.safetyMargin);

            case BOTTOM:
                var startPointBottom = new Point2D.Double(point.x, this.boundingRect.getMaxY());

                return Calculator.getVectorEndPoint(startPointBottom, 270, this.safetyMargin);

            case LEFT:
                var startPointLeft = new Point2D.Double(this.boundingRect.getMinX(), point.y);

                return Calculator.getVectorEndPoint(startPointLeft, 0, this.safetyMargin);

            case RIGHT:
                var startPointRight = new Point2D.Double(this.boundingRect.getMaxX(), point.y);

                return Calculator.getVectorEndPoint(startPointRight, 180, this.safetyMargin);
        }

        throw new Exception("Direction cannot be null");
    }

    public Point2D.Double getNearestSafePoint(Point2D.Double point) {
        if (point.distance(this.safePointTop) < point.distance(this.safePointBottom)) {
            return this.safePointTop;
        } else {
            return this.safePointBottom;
        }
    }

}
