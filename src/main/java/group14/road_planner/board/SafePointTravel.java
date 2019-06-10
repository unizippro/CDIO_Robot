package group14.road_planner.board;

import group14.road_planner.Robot;

import java.awt.*;
import java.util.List;

public class SafePointTravel {

    public Point getClosestSafePoint(Board board, Robot robot, boolean travelToQuadrantExitSafepoint) {
        int currentQuadrant = board.getRobotQuadrantPlacement(robot);
        return getClosest(board.getQuadrants().get(currentQuadrant).getSafePoints(), robot.getFrontOpenCVPoint(), travelToQuadrantExitSafepoint);
    }

    /**
     * Returns point of closest safepoint in robot's quadrant + 1
     * @param robot
     * @return
     */
    public Point getNextSafePoint(Board board, Robot robot) {
        int currentQuadrant = board.getRobotQuadrantPlacement(robot);
        //If currentquadrant is 3, thus in the last quadrant, we wish to travel to quadrant 0 instead of a not-existing quadrant 4
        if (currentQuadrant == 3) {
            currentQuadrant = 0;
        }else{
            currentQuadrant++;
        }
        return getClosest(board.getQuadrants().get(currentQuadrant).getSafePoints(), robot.getFrontOpenCVPoint(), false);
    }


    private Point getClosest(List<Point> points, Point pointRobot, boolean travelToQuadrantExitSafepoint) {
        if (points.size() == 1) {
            return points.get(0);
        } else if (points.size() == 2) {
//            double distance1 = Point2D.distance(points.get(0).getX(), points.get(0).getY(), pointRobot.getX(), pointRobot.getY());
//            double distance2 = Point2D.distance(points.get(1).getX(), points.get(1).getY(), pointRobot.getX(), pointRobot.getY());
//            if (distance1 >= distance2) {
//                return points.get(1);
//            } else {
//                return points.get(0);
//            }

            //Is it the "exit" safe point of the quadrant or the "entrance" safe point?
            if(travelToQuadrantExitSafepoint){
                return points.get(1);
            }else{
                return points.get(0);
            }

        }
        //hopefully not gonna happen.
        return null;
    }

}
