package group14.road_planner;

import group14.road_planner.board.Board;
import group14.road_planner.board.Quadrant;
import group14.road_planner.board.SmartConverter;

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

    /**
     * Checks if the distance from rotational point to wall is less than the distance from back to rotational.
     * The robot will and should not turn if the distance from rotational point to back is more, as it will touch
     * the wall
     * @param robot
     * @param board
     * @return
     */
    public boolean safeToTurn(Robot robot, Board board) {
        var distanceRotationalToBack = Point2D.distance(robot.getRotationalPoint().x, robot.getRotationalPoint().y, robot.getBack().x, robot.getBack().y)*25;
        var distanceToBoardSouth = Point2D.distance(robot.getRotationalPoint().x, robot.getRotationalPoint().y, robot.getRotationalPoint().x, board.getLowerLeft().y)*3;
        var distanceToBoardNorth = Point2D.distance(robot.getRotationalPoint().x, robot.getRotationalPoint().y, robot.getRotationalPoint().x, board.getUpperLeft().y)*3;
        var distanceToBoardEast = Point2D.distance(robot.getRotationalPoint().x, robot.getRotationalPoint().y, board.getLowerRight().x, robot.getRotationalPoint().y)*3;
        var distanceToBoardWest = Point2D.distance(robot.getRotationalPoint().x, robot.getRotationalPoint().y, board.getLowerLeft().x, robot.getRotationalPoint().y)*3;
        return !(distanceRotationalToBack >= distanceToBoardEast) ||
                !(distanceRotationalToBack>= distanceToBoardNorth) ||
                !(distanceRotationalToBack>= distanceToBoardSouth) ||
                !(distanceRotationalToBack>= distanceToBoardWest);
    }

    public boolean robotWithinGoal(Point robotFront, Point goalPoint) {
        if (robotFront.y > (goalPoint.y - 5* SmartConverter.getPixelsPerCm()) && robotFront.y < (goalPoint.y + 5*SmartConverter.getPixelsPerCm())) {
            return robotFront.x > goalPoint.x && robotFront.x < (goalPoint.x + 25 * SmartConverter.getPixelsPerCm());
        }
        return false;
    }

    /**
     * 0: left, 1: upper, 2: right, 3: lower
     */
    public int safetyAreaViolation(Robot robot, Point point, Quadrant quadrant) {
        int safeMargin = 10;
        if (point.x >= quadrant.getLowerLeft().x && point.x <= quadrant.getSafetyArea().getLowerLeft().x) {
            if (robot.getRotationalPoint().y > point.y - safeMargin*SmartConverter.getPixelsPerCm() && robot.getRotationalPoint().y < point.y + safeMargin*SmartConverter.getPixelsPerCm()) {
                return 4;
            }
            return 0;
        }
        if (point.y >= quadrant.getUpperLeft().y && point.y <= quadrant.getSafetyArea().getUpperLeft().y) {
            if (robot.getRotationalPoint().x > point.x - safeMargin*SmartConverter.getPixelsPerCm() && robot.getRotationalPoint().x < point.x + safeMargin*SmartConverter.getPixelsPerCm()) {
                return 4;
            }
            return 1;
        }
        if (point.x <= quadrant.getLowerRight().x && point.x >= quadrant.getSafetyArea().getLowerRight().x) {
            if (robot.getRotationalPoint().y > point.y - safeMargin*SmartConverter.getPixelsPerCm() && robot.getRotationalPoint().y < point.y + safeMargin*SmartConverter.getPixelsPerCm()) {
                return 4;
            }
            return 2;
        }
        if (point.y <= quadrant.getLowerLeft().y && point.y >= quadrant.getSafetyArea().getLowerLeft().y) {
            if (robot.getRotationalPoint().x > point.x - safeMargin*SmartConverter.getPixelsPerCm() && robot.getRotationalPoint().x < point.x + safeMargin*SmartConverter.getPixelsPerCm()) {
                return 4;
            }
            return 3;
        }
        return 4;
    }
}
