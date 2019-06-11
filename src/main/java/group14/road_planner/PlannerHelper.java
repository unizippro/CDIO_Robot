package group14.road_planner;

import java.awt.*;
import java.awt.geom.Point2D;

public class PlannerHelper {
    public double getDistance(Point robotPos, Point ballPoint) {
        return Point2D.distance(robotPos.getX(), robotPos.getY(), ballPoint.getX(), ballPoint.getY());
    }

    public double getAngle(Robot robot, Point ballPoint) {
        double robotRot = Math.toDegrees(Math.atan2(robot.getFront().getY() - robot.getRearOpenCVPoint().getY(), robot.getFront().getX() - robot.getRearOpenCVPoint().getX()));
        double ballRot = Math.toDegrees(Math.atan2(ballPoint.getY() - robot.getRotationalPoint().getY(), ballPoint.getX() - robot.getRotationalPoint().getX()));
        return robotRot+ballRot;
    }
}
