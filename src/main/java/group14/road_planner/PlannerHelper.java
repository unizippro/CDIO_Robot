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

        double angle = ballRot - robotRot;
        if (Math.abs(angle) > 180)
            if (angle > 0) {
                angle = 360 - angle;
            } else {
                angle = 360 + angle;
            }
        return angle;
//        if (robotRot > 0) {
//            return ballRot-robotRot;
//        } else {
//            return ballRot+robotRot;
//        }

    }

    public double getDistanceProjected(Robot robot, Point ballPoint) {
        double angle = Math.atan2(ballPoint.getY() - robot.getRotationalPoint().getY(), ballPoint.getX() - robot.getRotationalPoint().getX());
        double xPoint = robot.getRotationalPoint().x + robot.getDistanceRotationalToFront() * Math.cos(angle);
        double yPoint = robot.getRotationalPoint().y + robot.getDistanceRotationalToFront() * Math.sin(angle);

        return this.getDistance(new Point((int) xPoint, (int) yPoint), ballPoint);
    }
}
