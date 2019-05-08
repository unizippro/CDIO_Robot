package RoadPlanner.board;

import RoadPlanner.Vector;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private Point lowerLeft;
    private Point lowerRight;
    private Point upperRight;
    private Point upperLeft;

    Vector xAxis;

    public Board(List<Integer> boardList) {
        this.assignPoints(boardList);
        xAxis = new Vector(lowerLeft, lowerRight);
    }

    public Board() {
        //For planner constructor
        xAxis = new Vector(0, 0);
    }

    public void update(Point lowerLeft, Point lowerRight, Point upperRight, Point upperLeft) {
        this.lowerLeft = lowerLeft;
        this.lowerRight = lowerRight;
        this.upperRight = upperRight;
        this.upperLeft = upperLeft;
        this.xAxis.update(lowerLeft, lowerRight);
    }
    private void assignPoints(List<Integer> boardList) {
        this.upperLeft = new Point(boardList.get(0), boardList.get(1));
        this.upperRight = new Point(boardList.get(2), boardList.get(3));
        this.lowerLeft = new Point(boardList.get(4), boardList.get(5));
        this.lowerRight = new Point(boardList.get(6), boardList.get(7));
    }

    public Point getLowerLeft() {
        return lowerLeft;
    }

    public Point getLowerRight() {
        return lowerRight;
    }

    public Point getUpperLeft() {
        return upperLeft;
    }

    public Point getUpperRight() {
        return upperRight;
    }
}
