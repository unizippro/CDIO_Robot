package group14.robot;

import group14.navigator.data.Point2D;

public class Robot extends group14.navigator.data.Robot {
    public Robot(Point2D frontPoint, Point2D rearPoint) {
        super(frontPoint, rearPoint);
    }

    @Override
    protected double getDistanceRearPointToFront() {
        return 22; // 27cm
    }

    @Override
    protected double getDistanceRearPointToRotating() {
        return 10; // 12.5cm
    }
}
