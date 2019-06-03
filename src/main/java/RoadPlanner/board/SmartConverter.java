package RoadPlanner.board;

import java.awt.*;

public class SmartConverter {
    private double longBoard = 167;
    private double shortBoard = 122;
    private static double pixelsPerCm = 0;

    public static double getPixelsPerCm() {
        return pixelsPerCm;
    }

    public void calculateBoard(Point upperLeft,
                               Point upperRight,
                               Point lowerLeft,
                               Point lowerRight) {
        pixelsPerCm = 0;
        for (int i = 0; i < 4; i++) {
            double toAdd = (i % 2 == 0) ? calcXPixelLength(lowerLeft, lowerRight) : calcYPixelLength(upperLeft, lowerLeft);
            pixelsPerCm = pixelsPerCm + toAdd;
        }
        pixelsPerCm = pixelsPerCm / 4;
    }

    private double calcXPixelLength(Point p1, Point p2) {
        return (p2.x - p1.x) / longBoard;
    }

    private double calcYPixelLength(Point p1, Point p2) {
        return (p2.y - p1.y) / shortBoard;
    }

}
