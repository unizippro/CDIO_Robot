package group14.robot;

import lejos.robotics.geometry.Point2D;

public class Robot extends group14.navigator.data.Robot {
    public Robot(Point2D.Double frontPoint, Point2D.Double rearPoint) {
        super(frontPoint, rearPoint);
    }

    @Override
    protected double getDistanceRearPointToFront() {
        return 29.5;
    }

    @Override
    protected double getDistanceRearPointToRotating() {
        return 8.5;
    }
}
