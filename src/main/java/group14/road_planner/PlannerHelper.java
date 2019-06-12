package group14.road_planner;

import group14.road_planner.board.Board;
import group14.road_planner.board.Quadrant;

import java.awt.*;
import java.awt.geom.Point2D;

public class PlannerHelper {
    private final int acceptedDistance = 5;

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
    }

    public double getDistanceProjected(Robot robot, Point ballPoint) {
        double angle = Math.atan2(ballPoint.getY() - robot.getRotationalPoint().getY(), ballPoint.getX() - robot.getRotationalPoint().getX());
        double xPoint = robot.getRotationalPoint().x + robot.getDistanceRotationalToFront() * Math.cos(angle);
        double yPoint = robot.getRotationalPoint().y + robot.getDistanceRotationalToFront() * Math.sin(angle);

        return this.getDistance(new Point((int) xPoint, (int) yPoint), ballPoint);
    }

    /**
     * checks if the robot's front is within acceptable distance to point
     * @param robot
     * @param boardPoint
     * @return
     */
    public boolean isNearPoint(Robot robot, Point boardPoint) {
        return !(this.getDistance(robot.getFront(), boardPoint) > this.acceptedDistance);
    }

    /**
     * TODO
     * Checks if it is faster to go to next or previous quadrant.
     * @return 1 = next, 0 = previous
     */
    public int goBackOrForward(RoadController roadController, Quadrant toQuadrant) {
        //checks if the distance from prev safepoint/next safepoint to quadrants different safepoints, approximate
        //estimation for best route
        var prev = roadController.getPreviousQuadrant();
        var next = roadController.getNextQuadrant();

        var distancePrev = this.getDistance(prev.getExitSafePoint(), toQuadrant.getExitSafePoint());
        var distanceNext = this.getDistance(next.getEntrySafePoint(), toQuadrant.getEntrySafePoint());

        if (distancePrev <= distanceNext) {
            return 0;
        } else {
            return 1;
        }
    }
}
