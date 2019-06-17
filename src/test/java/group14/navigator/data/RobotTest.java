package group14.navigator.data;

import lejos.robotics.geometry.Point2D;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RobotTest {

    private Robot robot;

    @Before
    public void setUp() throws Exception {
        this.robot = new Robot(new Point2D.Double(0, 0), new Point2D.Double(1, 1));
    }

    @Test
    public void getFrontPosition() {
        var robot = new Robot(new Point2D.Double(50, 8), new Point2D.Double(50, 10));
        var position = robot.getFrontPosition();

        assertEquals(50, position.x, 0);
        assertEquals(0, position.y, 0);
    }

    @Test
    public void getDirectionAngle() {
        assertEquals(225, this.robot.getDirectionAngle(), 0);

        var robot = new Robot(new Point2D.Double(1, 1), new Point2D.Double(1, 2));
        assertEquals(270, robot.getDirectionAngle(), 0);

        var robot2 = new Robot(new Point2D.Double(1, 1), new Point2D.Double(0, 1));
        assertEquals(0, robot2.getDirectionAngle(), 0);
    }

    @Test
    public void getRotatingPoint() {
        var robot = new Robot(new Point2D.Double(50, 8), new Point2D.Double(50, 10));
        var rotatingPoint = robot.getRotatingPoint();

        assertEquals(50, rotatingPoint.x, 0.1);
        assertEquals(5, rotatingPoint.y, 0.1);


        var robot1 = new Robot(new Point2D.Double(2, 0), new Point2D.Double(0, 0));
        var rotatingPoint1 = robot1.getRotatingPoint();

        assertEquals(5, rotatingPoint1.x, 0.1);
        assertEquals(0, rotatingPoint1.y, 0.1);
    }

    @Test
    public void getDistanceTo() {
        var robot = new Robot(new Point2D.Double(50, 18), new Point2D.Double(50, 20));
        assertEquals(10, robot.getDistanceTo(new Point2D.Double(50, 0)), 0);
    }
}