package group14.road_planner;

import java.awt.*;
import java.awt.geom.Point2D;

public class PlannerHelper {
    public double getDistance(Point robotPos, Point ballPoint) {
        return Point2D.distance(robotPos.getX(), robotPos.getY(), ballPoint.getX(), ballPoint.getY());
    }

    public double getAngle(Point robotPos, Point ballPoint) {
        return Math.toDegrees(Math.atan2(ballPoint.getY() - robotPos.getY(), ballPoint.getX() - robotPos.getX()));
    }
}
