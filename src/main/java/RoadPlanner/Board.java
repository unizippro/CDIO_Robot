package RoadPlanner;

import java.util.ArrayList;
import java.util.List;

public class Board {
    Coordinate lowerLeft;
    Coordinate lowerRight;
    Coordinate upperRight;
    Coordinate upperLeft;

    Vector xAxis;

    public Board(List<Integer> boardList) {
        this.assignCoordinates(boardList);
        xAxis = new Vector(lowerLeft, lowerRight);
    }

    public Board() {
        //For planner constructor
        xAxis = new Vector(0, 0);
    }

    public void update(Coordinate lowerLeft, Coordinate lowerRight, Coordinate upperRight, Coordinate upperLeft) {
        this.lowerLeft = lowerLeft;
        this.lowerRight = lowerRight;
        this.upperRight = upperRight;
        this.upperLeft = upperLeft;
        this.xAxis.update(lowerLeft, lowerRight);
    }
    private void assignCoordinates(List<Integer> boardList) {
        this.upperLeft = new Coordinate(boardList.get(0), boardList.get(1));
        this.upperRight = new Coordinate(boardList.get(2), boardList.get(3));
        this.lowerLeft = new Coordinate(boardList.get(4), boardList.get(5));
        this.lowerRight = new Coordinate(boardList.get(6), boardList.get(7));
    }
}
