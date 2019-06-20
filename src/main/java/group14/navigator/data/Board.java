package group14.navigator.data;

import group14.navigator.Calculator;
import group14.navigator.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static group14.navigator.data.Area.SafePointLocation.*;

public class Board {

    private Rectangle2D boundingRect;
    private final double extraMargin;
    private final double safetyMargin;

    private Point2D depositPoint;

    private final List<Area> areas = new ArrayList<>();
    private Rectangle2D cross;

    public Board(Rectangle2D boundingRect) {
        this(boundingRect, 0, null, 0);
    }

    public Board(Rectangle2D boundingRect, double extraMargin, Rectangle2D cross, double safetyMargin) {
        this.extraMargin = extraMargin;
        this.safetyMargin = safetyMargin;

        this.updateBoard(boundingRect, cross);
    }

    public List<Area> getAreas() {
        return this.areas;
    }

    public Point2D getDepositPoint() {
        return this.depositPoint;
    }

    public void updateBoard(Rectangle2D boundingRect, Rectangle2D cross) {
        if (this.canUpdateToNewRect(boundingRect)) {
            this.boundingRect = Utils.rectangleWithExpandedMargin(boundingRect, this.extraMargin);
        }

        this.cross = cross;

        this.depositPoint = new Point2D(boundingRect.getMinX() + this.safetyMargin * 1.15, boundingRect.y + (boundingRect.getHeight() / 2));

        var crossPoint = cross != null ? cross : new Rectangle2D((this.boundingRect.width / 2) - 100, (this.boundingRect.height / 2) - 100, 200, 200);
        this.setCrossPosition(crossPoint);
    }

    public boolean contains(Point2D point) {
        return this.boundingRect.contains(point);
    }

    public boolean isInsideCross(Point2D point) {
        return this.cross.contains(point);
    }

    public Point2D getProjectedPoint(Point2D point) {
        var crossCenter = this.cross.getCenter();
        var angle = Calculator.getAngleBetweenPoint(crossCenter, point);

        return Calculator.getVectorEndPoint(crossCenter, angle, this.safetyMargin * 1.30);
    }

    public Area getAreaForPoint(Point2D point) {
        for (var area : this.areas) {
            if (area.contains(point)) {
                return area;
            }
        }

        return null;
    }

    public Area getAreaAfter(Area areaOld) {
        var getNext = false;
        for (var area : this.areas) {
            if (getNext) {
                return area;
            }

            getNext = area == areaOld;
        }

        return this.areas.get(0);
    }

    private void setCrossPosition(Rectangle2D cross) {
        if (! this.boundingRect.contains(cross)) {
            return;
        }

        var crossCenter = Utils.rectangleGetCenter(cross);
        var verticalSplit = Utils.rectangleSplitAt(this.boundingRect, crossCenter, Utils.Split.VERTICAL);

        var areaLeft = new Area(verticalSplit.get(0), this.safetyMargin, ALL);
        var areaRight = new Area(verticalSplit.get(1), this.safetyMargin, ALL);

        double safePointTopWidth = areaRight.getSafePointTop().x - areaLeft.getSafePointTop().x;
        double safePointDownWidth = areaRight.getSafePointBottom().x - areaLeft.getSafePointBottom().x;

        var safePointTopCenter = new Point2D(safePointTopWidth, areaRight.getSafePointTop().y);
        var safePointDownCenter = new Point2D(safePointDownWidth, areaRight.getSafePointBottom().y);

        var safePointTopRect = Utils.rectangleWithCenter(safePointTopCenter, safePointTopWidth, 20);
        var safePointDownRect = Utils.rectangleWithCenter(safePointDownCenter, safePointDownWidth, 20);

        if (safePointTopRect.intersects(cross)) {
            areaLeft.setSafePointLocation(DOWN);
            areaRight.setSafePointLocation(DOWN);
        } else if (safePointDownRect.intersects(cross)) {
            areaLeft.setSafePointLocation(TOP);
            areaRight.setSafePointLocation(TOP);
        }

        this.areas.clear();
        this.areas.addAll(Arrays.asList(areaLeft, areaRight));
    }

    private boolean canUpdateToNewRect(Rectangle2D newRect) {
        if (this.boundingRect == null) {
            return true;
        }

        return Utils.rectangleWithExpandedMargin(this.boundingRect, 3).contains(newRect);
    }

    @Override
    public String toString() {
        return "Board(\n\t" + this.areas + "\n)";
    }
}
