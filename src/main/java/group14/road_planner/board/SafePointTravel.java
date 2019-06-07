package group14.road_planner.board;

import group14.road_planner.Robot;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SafePointTravel {

    public Point getClosestSafePoint(LinkedList<Point> linkedList, List<Quadrant> quadrants, Robot robot) {
        int currentQuadrant = calculateCurrentQuadrant(quadrants, robot);
        return getClosest(this.getSafePointsQuadrant(linkedList, currentQuadrant), robot.getMid());
    }

    /**
     * Returns point of closest safepoint in robot's quadrant + 1
     * @param linkedList
     * @param quadrants
     * @param robot
     * @return
     */
    public Point getNextSafePoint(LinkedList<Point> linkedList, List<Quadrant> quadrants, Robot robot) {
        int currentQuadrant = calculateCurrentQuadrant(quadrants, robot);
        //If currentquadrant is 3, thus in the last quadrant, we wish to travel to quadrant 0 instead of a not-existing quadrant 4
        if (currentQuadrant == 3) {
            currentQuadrant = 0;
        }
        return getClosest(this.getSafePointsQuadrant(linkedList, currentQuadrant), robot.getMid());
    }

    private int calculateCurrentQuadrant(List<Quadrant> quadrants, Robot robot) {
        for (int i = 0; i < quadrants.size(); i++) {
            if (quadrants.get(i) == robot.getCurrentQuadrant()) {
                return i;
            }
        }
        //Hopefully not gonna happen.
        return -1;
    }

    private Point getClosest(List<Point> points, Point pointRobot) {
        if (points.size() == 1) {
            return points.get(0);
        } else if (points.size() == 2) {
            double distance1 = Point2D.distance(points.get(0).getX(), points.get(0).getY(), pointRobot.getX(), pointRobot.getY());
            double distance2 = Point2D.distance(points.get(1).getX(), points.get(1).getY(), pointRobot.getX(), pointRobot.getY());
            if (distance1 >= distance2) {
                return points.get(1);
            } else {
                return points.get(0);
            }
        }
        //hopefully not gonna happen.
        return null;
    }

    private List<Point> getSafePointsQuadrant(LinkedList<Point> linkedList, int quadrant) {
        List<Point> pointsArray = new ArrayList<>();

        switch (quadrant) {
            case 0:
                pointsArray.add(linkedList.get(0));
                pointsArray.add(linkedList.get(1));
                break;
            case 1:
                pointsArray.add(linkedList.get(2));
                break;
            case 2:
                pointsArray.add(linkedList.get(3));
                pointsArray.add(linkedList.get(4));
                break;
            case 3:
                pointsArray.add(linkedList.get(5));
                break;
        }
        return pointsArray;
    }

}
