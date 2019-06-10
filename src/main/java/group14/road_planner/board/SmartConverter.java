package group14.road_planner.board;

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

        pixelsPerCm = (calcXPixelLength(upperLeft, upperRight)+calcXPixelLength(lowerLeft, lowerRight)+calcYPixelLength(upperLeft, lowerLeft) + calcYPixelLength(upperRight,lowerRight)) / 4;
    }

    private double calcXPixelLength(Point p1, Point p2) {
        System.out.println(Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2)) / longBoard);
        return Math.sqrt(Math.pow(p2.x - p1.x,2)+Math.pow(p2.y - p1.y,2)) / longBoard;
    }

    private double calcYPixelLength(Point p1, Point p2) {
        return (p2.y - p1.y) / shortBoard;
    }

}
