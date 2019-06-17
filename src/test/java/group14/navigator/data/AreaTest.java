package group14.navigator.data;

import lejos.robotics.geometry.Point2D;
import lejos.robotics.geometry.Rectangle2D;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AreaTest {

    private Area area;

    @Before
    public void setUp() throws Exception {
        this.area = new Area(new Rectangle2D.Double(0, 0, 100, 100), 10);
    }

    @Test
    public void isWithinSafetyArea() {
        assertTrue(this.area.isWithinSafetyArea(new Point2D.Double(30, 50)));
        assertFalse(this.area.isWithinSafetyArea(new Point2D.Double(5, 5)));
        assertFalse(this.area.isWithinSafetyArea(new Point2D.Double(95, 95)));
    }

    @Test
    public void getDangerousAreaDirection() throws Exception {
        assertEquals(Area.DangerousAreaDirection.TOP, this.area.getDangerousAreaDirection(new Point2D.Double(50, 5)));
        assertEquals(Area.DangerousAreaDirection.BOTTOM, this.area.getDangerousAreaDirection(new Point2D.Double(50, 95)));
        assertEquals(Area.DangerousAreaDirection.LEFT, this.area.getDangerousAreaDirection(new Point2D.Double(5, 50)));
        assertEquals(Area.DangerousAreaDirection.RIGHT, this.area.getDangerousAreaDirection(new Point2D.Double(95, 50)));
    }

    @Test
    public void getProjectedPoint() throws Exception {
        var pointTop = this.area.getProjectedPoint(new Point2D.Double(50, 5), Area.DangerousAreaDirection.TOP);
        assertEquals(50, pointTop.x, 0);
        assertEquals(10, pointTop.y, 0);

        var pointBottom = this.area.getProjectedPoint(new Point2D.Double(50, 95), Area.DangerousAreaDirection.BOTTOM);
        assertEquals(50, pointBottom.x, 0);
        assertEquals(90, pointBottom.y, 0);

        var pointLeft = this.area.getProjectedPoint(new Point2D.Double(5, 50), Area.DangerousAreaDirection.LEFT);
        assertEquals(10, pointLeft.x, 0);
        assertEquals(50, pointLeft.y, 0);

        var pointRight = this.area.getProjectedPoint(new Point2D.Double(95, 50), Area.DangerousAreaDirection.RIGHT);
        assertEquals(90, pointRight.x, 0);
        assertEquals(50, pointRight.y, 0);
    }

    @Test
    public void getNearestSafePoint() {
        var safePoint1 = this.area.getNearestSafePoint(new Point2D.Double(20, 20));
        assertEquals(50, safePoint1.x, 0);
        assertEquals(25, safePoint1.y, 0);

        var safePoint2 = this.area.getNearestSafePoint(new Point2D.Double(90, 90));
        assertEquals(50, safePoint2.x, 0);
        assertEquals(75, safePoint2.y, 0);
    }

}