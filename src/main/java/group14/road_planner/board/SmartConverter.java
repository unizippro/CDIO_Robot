package group14.road_planner.board;

import java.awt.*;
import java.util.List;

public class SmartConverter {
    private double longBoard = 167;
    private double shortBoard = 122;
    private static double pixelsPerCm = 0;

    public static double getPixelsPerCm() {
        return pixelsPerCm;
    }

    public void calculateBoard(List<Point> boardList) {
        var upperLeft = boardList.get(0);
        var upperRight = boardList.get(1);
        var lowerLeft = boardList.get(2);
        var lowerRight = boardList.get(3);

        pixelsPerCm = (calcXPixelLength(upperLeft, upperRight)+calcXPixelLength(lowerLeft, lowerRight)+calcYPixelLength(upperLeft, lowerLeft) + calcYPixelLength(upperRight,lowerRight)) / 4;
    }

    private double calcXPixelLength(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p2.x - p1.x,2)+Math.pow(p2.y - p1.y,2)) / longBoard;
    }

    private double calcYPixelLength(Point p1, Point p2) {
        return (p2.y - p1.y) / shortBoard;
    }

}
