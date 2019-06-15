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
    public Point safetyAreaViolation(Robot robot, Point point, Quadrant quadrant) {
        int safeMargin = 10;
        if (withinUpperLeftCorner(point, quadrant)) {
            Point p = new Point(point.x + 15 * (int) SmartConverter.getPixelsPerCm(), point.y + 15 * (int) SmartConverter.getPixelsPerCm());
        }
        if (withinUpperRightCorner(point, quadrant)) {
            Point p = new Point(point.x - 15 * (int) SmartConverter.getPixelsPerCm(), point.y + 15 * (int) SmartConverter.getPixelsPerCm());
        }
        if (withinLowerRightCorner(point, quadrant)) {
            Point p = new Point(point.x - 15 * (int) SmartConverter.getPixelsPerCm(), point.y - 15 * (int) SmartConverter.getPixelsPerCm());
        }
        if (withinLowerLeftCorner(point, quadrant)) {
            Point p = new Point(point.x + 15 * (int) SmartConverter.getPixelsPerCm(), point.y - 15 * (int) SmartConverter.getPixelsPerCm());
        }
        if (withinLeftMargin(point, quadrant)) {
            if (robot.getRotationalPoint().y > point.y - safeMargin*SmartConverter.getPixelsPerCm() && robot.getRotationalPoint().y < point.y + safeMargin*SmartConverter.getPixelsPerCm()) {
                return point;
            }
            return new Point(point.x + 20 * (int)SmartConverter.getPixelsPerCm(), point.y);
        }
        if (withingUpMargin(point, quadrant)) {
            if (robot.getRotationalPoint().x > point.x - safeMargin*SmartConverter.getPixelsPerCm() && robot.getRotationalPoint().x < point.x + safeMargin*SmartConverter.getPixelsPerCm()) {
                return point;
            }
            return new Point(point.x, point.y + 20 * (int)SmartConverter.getPixelsPerCm());
        }
        if (withinRightMargin(point, quadrant)) {
            if (robot.getRotationalPoint().y > point.y - safeMargin*SmartConverter.getPixelsPerCm() && robot.getRotationalPoint().y < point.y + safeMargin*SmartConverter.getPixelsPerCm()) {
                return point;
            }
            return new Point(point.x - 20 * (int)SmartConverter.getPixelsPerCm(), point.y);
        }
        if (withinDownMargin(point, quadrant)) {
            if (robot.getRotationalPoint().x > point.x - safeMargin*SmartConverter.getPixelsPerCm() && robot.getRotationalPoint().x < point.x + safeMargin*SmartConverter.getPixelsPerCm()) {
                return point;
            }
            return new Point(point.x, point.y - 20 * (int)SmartConverter.getPixelsPerCm());
        }
        return point;
    }

    private boolean withinLeftMargin(Point point, Quadrant quadrant) {
        return point.x >= quadrant.getLowerLeft().x && point.x <= quadrant.getSafetyArea().getLowerLeft().x;

    }

    private boolean withingUpMargin(Point point, Quadrant quadrant) {
        return point.y >= quadrant.getUpperLeft().y && point.y <= quadrant.getSafetyArea().getUpperLeft().y;
    }

    private boolean withinRightMargin(Point point, Quadrant quadrant) {
        return point.x <= quadrant.getLowerRight().x && point.x >= quadrant.getSafetyArea().getLowerRight().x;
    }

    private boolean withinDownMargin(Point point, Quadrant quadrant) {
        return point.y <= quadrant.getLowerLeft().y && point.y >= quadrant.getSafetyArea().getLowerLeft().y;
    }

    private boolean withinUpperLeftCorner(Point point, Quadrant quadrant) {
        return this.withinLeftMargin(point, quadrant) && this.withingUpMargin(point,quadrant);
    }

    private boolean withinUpperRightCorner(Point point, Quadrant quadrant) {
        return this.withingUpMargin(point, quadrant) && this.withinRightMargin(point, quadrant);
    }

    private boolean withinLowerRightCorner(Point point, Quadrant quadrant) {
        return this.withinRightMargin(point, quadrant) && this.withinDownMargin(point, quadrant);
    }

    private boolean withinLowerLeftCorner(Point point, Quadrant quadrant) {
        return this.withinDownMargin(point, quadrant) && this.withinLeftMargin(point, quadrant);
    }
}
