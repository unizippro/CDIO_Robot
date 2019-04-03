package RoadPlanner;

public class Board {
    Coordinate lowerLeft;
    Coordinate lowerRight;
    Coordinate upperRight;
    Coordinate upperLeft;

    Vector xAxis;

    public Board(Coordinate lowerLeft, Coordinate lowerRight, Coordinate upperRight, Coordinate upperLeft) {
        this.lowerLeft = lowerLeft;
        this.lowerRight = lowerRight;
        this.upperRight = upperRight;
        this.upperLeft = upperLeft;
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
}
