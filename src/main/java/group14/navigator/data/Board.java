package group14.navigator.data;

import lejos.robotics.geometry.Point2D;
import lejos.robotics.geometry.Rectangle2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {

    private static final double SAFETY_MARGIN = 10;

    private Rectangle2D.Double boundingRect;

    private final List<Area> areas = new ArrayList<>();

    public Board(Rectangle2D.Double boundingRect) {
        this(boundingRect, new Point2D.Double(boundingRect.width / 2, 0));
    }

    public Board(Rectangle2D.Double boundingRect, Point2D.Double splitAt) {
        this.boundingRect = boundingRect;
        this.splitAt(splitAt);
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

        var left = new Rectangle2D.Double(this.boundingRect.x, this.boundingRect.y, this.boundingRect.x + point.x, this.boundingRect.height);
        var right = new Rectangle2D.Double(this.boundingRect.x + point.x, this.boundingRect.y, this.boundingRect.width - point.x, this.boundingRect.height);

        this.areas.clear();
        this.areas.addAll(Arrays.asList(new Area(left, SAFETY_MARGIN), new Area(right, SAFETY_MARGIN)));
    }

}
