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
}
