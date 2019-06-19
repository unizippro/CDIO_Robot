package group14.navigator;

import group14.navigator.data.Point2D;
import org.junit.Test;

import static org.junit.Assert.*;

public class CalculatorTest {

    @Test
    public void getAngleBetweenPoint() {
        var point1 = new Point2D(1, 1);
        var point2 = new Point2D(2, 2);

        assertEquals(45, Calculator.getAngleBetweenPoint(point1, point2), 0);
        assertEquals(225, Calculator.getAngleBetweenPoint(point2, point1), 0);
        assertEquals(90, Calculator.getAngleBetweenPoint(point1, new Point2D(1, 3)), 0);
        assertEquals(0, Calculator.getAngleBetweenPoint(point1, new Point2D(2, 1)), 0);
    }

    @Test
    public void getVectorEndPoint() {
        var endPoint = Calculator.getVectorEndPoint(new Point2D(1, 1), 0, 5);
        assertEquals(6, endPoint.x, 0);
        assertEquals(1, endPoint.y, 0);

        var endPoint2 = Calculator.getVectorEndPoint(new Point2D(1, 1), 90, 5);
        assertEquals(1, endPoint2.x, 0.001);
        assertEquals(6, endPoint2.y, 0.001);

        var endPoint3 = Calculator.getVectorEndPoint(new Point2D(1, 1), 45, 5);
        assertEquals(4.5, endPoint3.x, 0.04);
        assertEquals(4.5, endPoint3.y, 0.04);
    }

    @Test
    public void getOppositeAngle() {
        assertEquals(0, Calculator.getOppositeAngle(180), 0);
        assertEquals(270, Calculator.getOppositeAngle(90), 0);
    }

    @Test
    public void getTurnAngle() {
        assertEquals(45, Calculator.getTurnAngle(45, 90), 0);
        assertEquals(0, Calculator.getTurnAngle(45, 45), 0);
        assertEquals(-45, Calculator.getTurnAngle(45, 0), 0);
        assertEquals(-135, Calculator.getTurnAngle(0, 225), 0);
        assertEquals(60, Calculator.getTurnAngle(350, 50), 0);
        assertEquals(-60, Calculator.getTurnAngle(350, 290), 0);
    }
}