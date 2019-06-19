package group14.navigator.data;

import group14.navigator.Utils;
import lejos.robotics.geometry.Point2D;
import lejos.robotics.geometry.Rectangle2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {

    private Rectangle2D.Double boundingRect;
    private final double safetyMargin;

    private final List<Area> areas = new ArrayList<>();

    public Board(Rectangle2D.Double boundingRect) {
        this(boundingRect, 0, null, 0);
    }

    public Board(Rectangle2D.Double boundingRect, double extraMargin, Point2D.Double splitAt, double safetyMargin) {
        this.boundingRect = Utils.rectangleWithExpandedMargin(boundingRect, extraMargin);
        this.safetyMargin = safetyMargin;

        var splitPoint = splitAt != null ? splitAt : new Point2D.Double(this.boundingRect.width / 2, this.boundingRect.height / 2);

        this.splitAt(splitPoint);
    }

    public List<Area> getAreas() {
        return this.areas;
    }

    public boolean contains(Point2D.Double point) {
        return this.boundingRect.contains(point);
    }

    public Area getAreaForPoint(Point2D.Double point) {
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

    private void splitAt(Point2D.Double point) {
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

}
