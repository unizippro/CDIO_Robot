package RoadPlanner.board;

import java.awt.*;

public class SmartConverter {
    private double longBoard = 167;
    private double shortBoard = 122;
    private double pixelToLength = 0;

    public double getLength() {
        return pixelToLength;
    }

    public void calculateBoard(Point lowerLeft,
                               Point lowerRight,
                               Point upperLeft,
                               Point upperRight) {
        pixelToLength = 0;
        for (int i = 0; i < 4; i++) {
            double toAdd = (i % 2 == 0) ? calcXPixelLength(lowerLeft, lowerRight) : calcYPixelLength(upperLeft, upperRight);
            pixelToLength = +toAdd;
        }
        pixelToLength = pixelToLength / 4;
    }

    private double calcXPixelLength(Point p1, Point p2) {
        return (p2.x - p1.x) / longBoard;
    }

    private double calcYPixelLength(Point p1, Point p2) {
        return (p2.y - p1.y) / shortBoard;
    }

}
