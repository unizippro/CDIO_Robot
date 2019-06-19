package group14.navigator;

import group14.navigator.data.Point2D;
import group14.navigator.data.Rectangle2D;
import org.junit.Test;
import org.opencv.core.Point;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

public class UtilsTest {

    @Test
    public void rectangleWithCenter() {
        var rect = Utils.rectangleWithCenter(new Point2D(5, 5), 5);

        assertEquals(0, rect.x, 0);
        assertEquals(0, rect.y, 0);
        assertEquals(10, rect.width, 0);
        assertEquals(10, rect.height, 0);
    }

    @Test
    public void rectangleWithExpandedMargin() {
        var rect = Utils.rectangleWithExpandedMargin(new Rectangle2D(5, 5, 10, 10), 5);

        assertEquals(0, rect.x, 0);
        assertEquals(0, rect.y, 0);
        assertEquals(20, rect.width, 0);
        assertEquals(20, rect.height, 0);
    }

    @Test
    public void rectangleSplitAt() {
        var rectList1 = Utils.rectangleSplitAt(new Rectangle2D(0, 0, 100, 100), new Point2D(60, 60), Utils.Split.VERTICAL);

        assertEquals(0, rectList1.get(0).x, 0);
        assertEquals(0, rectList1.get(0).y, 0);
        assertEquals(60, rectList1.get(0).width, 0);
        assertEquals(100, rectList1.get(0).height, 0);

        assertEquals(60, rectList1.get(1).x, 0);
        assertEquals(0, rectList1.get(1).y, 0);
        assertEquals(40, rectList1.get(1).width, 0);
        assertEquals(100, rectList1.get(1).height, 0);


        var rectList2 = Utils.rectangleSplitAt(new Rectangle2D(0, 0, 100, 100), new Point2D(60, 60), Utils.Split.HORIZONTAL);

        assertEquals(0, rectList2.get(0).x, 0);
        assertEquals(0, rectList2.get(0).y, 0);
        assertEquals(100, rectList2.get(0).width, 0);
        assertEquals(60, rectList2.get(0).height, 0);

        assertEquals(0, rectList2.get(1).x, 0);
        assertEquals(60, rectList2.get(1).y, 0);
        assertEquals(100, rectList2.get(1).width, 0);
        assertEquals(40, rectList2.get(1).height, 0);
    }

    @Test
    public void rectangleGetCenter() {
        var center = Utils.rectangleGetCenter(new Rectangle2D(0, 0, 10, 10));

        assertEquals(5, center.x, 0);
        assertEquals(5, center.y, 0);
    }

    @Test
    public void toNavigatorPoint() {
        assertTrue(Utils.toNavigatorPoint(new Point(), 0) instanceof Point2D);
    }

    @Test
    public void toNavigatorPoints() {
        var listOfPoints = Arrays.asList(new Point(), new Point());

        assertEquals(2, Utils.toNavigatorPoints(listOfPoints, 0).size(), 0);
    }

    @Test()
    public void createRectangleFromPoints() {
        var rect = Utils.createRectangleFromPoints(Arrays.asList(new Point2D(0, 0), new Point2D(10, 10)));

        assertEquals(0, rect.x, 0);
        assertEquals(0, rect.y, 0);
        assertEquals(10, rect.getMaxX(), 0);
        assertEquals(10, rect.getMaxY(), 0);
        assertEquals(10, rect.width, 0);
        assertEquals(10, rect.height, 0);


        var rect2 = Utils.createRectangleFromPoints(Arrays.asList(new Point2D(10, 10), new Point2D(20, 20)));

        assertEquals(10, rect2.x, 0);
        assertEquals(10, rect2.y, 0);
        assertEquals(20, rect2.getMaxX(), 0);
        assertEquals(20, rect2.getMaxY(), 0);
        assertEquals(10, rect2.width, 0);
        assertEquals(10, rect2.height, 0);
    }

    @Test(expected = RuntimeException.class)
    public void createRectangleFromPointsExceptions() {
        Utils.createRectangleFromPoints(Collections.emptyList());
        Utils.createRectangleFromPoints(Collections.singletonList(new Point2D()));
        Utils.createRectangleFromPoints(Arrays.asList(new Point2D(), new Point2D(), new Point2D()));
    }
}