package group14.road_planner.board;

import group14.road_planner.Robot;

import java.awt.*;
import java.util.List;

public class SafePointTravel {

    public Point getClosestSafePoint(Board board, Robot robot) {
        int currentQuadrant = board.getRobotQuadrantPlacement(robot);
        return getEntrySafePoint(board.getQuadrants().get(currentQuadrant).getSafePoints());
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
        return getEntrySafePoint(board.getQuadrants().get(currentQuadrant).getSafePoints());
    }

    private Point getEntrySafePoint(List<Point> points) {
        if (points.size() == 1) {
            return points.get(0).getLocation();
        } else {
            return points.get(1).getLocation();
        }
    }

}
