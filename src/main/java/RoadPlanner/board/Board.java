package RoadPlanner.board;

import RoadPlanner.Vector;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Board {
    /**
     * List consisting of corners of data type point
     * 0 = upperleft
     * 1 = upperright
     * 2 = lowerleft
     * 3 = lowerright
     */
    private List<Point> corners = new ArrayList<>();
    private Vector xAxis;

    public Board(List<Point> boardList) {
        this.update(boardList);
    }

    public Board() {
        //For planner constructor
        xAxis = new Vector(0, 0);
    }

    /**
     * assigns new points and updates xAxis' vectors
     * @param boardList
     */
    public void update(List<Point> boardList) {
        this.assignPoints(boardList);
        this.xAxis.update(this.corners.get(2), this.corners.get(3));
    }
    private void assignPoints(List<Point> boardList) {
        this.corners.add(boardList.get(0));
        this.corners.add(boardList.get(0));
        this.corners.add(boardList.get(0));
        this.corners.add(boardList.get(0));
    }

    public Point getLowerLeft() {
        return this.corners.get(2);
    }

    public Point getLowerRight() {
        return this.corners.get(3);
    }

    public Point getUpperLeft() {
        return this.corners.get(0);
    }

    public Point getUpperRight() {
        return this.corners.get(1);
    }

    public Vector getxAxis() {
        return this.xAxis;
    }
}
