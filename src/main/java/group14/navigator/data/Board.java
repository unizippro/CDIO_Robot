package group14.navigator.data;

import group14.navigator.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {

    private Rectangle2D boundingRect;
    private final double safetyMargin;

    private final Point2D depositPointLeft;
    private final Point2D depositPointRight;

    private final List<Area> areas = new ArrayList<>();

    public Board(Rectangle2D boundingRect) {
        this(boundingRect, 0, null, 0);
    }

    public Board(Rectangle2D boundingRect, double extraMargin, Point2D splitAt, double safetyMargin) {
        this.boundingRect = Utils.rectangleWithExpandedMargin(boundingRect, extraMargin);
        this.safetyMargin = safetyMargin;
        this.depositPointLeft = new Point2D(boundingRect.getMinX() + safetyMargin * 1.25, boundingRect.y + (boundingRect.getHeight() / 2));
        this.depositPointRight = new Point2D(boundingRect.getMaxX() - safetyMargin * 1.25, boundingRect.y + (boundingRect.getHeight() / 2));

        System.out.println(this.depositPointLeft);
        System.out.println(this.depositPointRight);

        var splitPoint = splitAt != null ? splitAt : new Point2D(this.boundingRect.width / 2, this.boundingRect.height / 2);

        this.splitAt(splitPoint);
    }

    public List<Area> getAreas() {
        return this.areas;
    }

    public Point2D getDepositPointLeft() {
        return this.depositPointLeft;
    }

    public Point2D getDepositPointRight() {
        return this.depositPointRight;
    }

    public boolean contains(Point2D point) {
        return this.boundingRect.contains(point);
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

    private void splitAt(Point2D point) {
        if (! this.boundingRect.contains(point)) {
            return;
        }

        var verticalSplit = Utils.rectangleSplitAt(this.boundingRect, point, Utils.Split.VERTICAL);

        this.areas.clear();
        this.areas.addAll(Arrays.asList(
                new Area(verticalSplit.get(0), this.safetyMargin),
                new Area(verticalSplit.get(1), this.safetyMargin)
        ));
    }

    @Override
    public String toString() {
        return "Board(\n\t" + this.areas + "\n)";
    }
}
