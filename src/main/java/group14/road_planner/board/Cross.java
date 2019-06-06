package group14.road_planner.board;


import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Cross {
    private int LENGTH_CM = 20;
    private Point left;
    private Point up;
    private Point down;
    private Point right;

    public Cross(List<Point> pointList) {
        this.calculatePoints(pointList);
    }

    private void calculatePoints(List<Point> pointList) {
        List<Point> xList = this.sortX(pointList);
        List<Point> yList = this.sortY(pointList);

        this.left = xList.get(0);
        this.right = xList.get(xList.size()-1);

        this.up = yList.get(0);
        this.down = yList.get(yList.size()-1);
    }

    private List<Point> sortX(List<Point> pointList) {
        List<Point> xSortedList = new ArrayList<>(pointList);
        xSortedList.sort(Comparator.comparingInt(o -> o.x));
        return xSortedList;
    }

    private List<Point> sortY(List<Point> pointList) {
        List<Point> ySortedList = new ArrayList<>(pointList);
        ySortedList.sort(Comparator.comparingInt(o -> o.y));
        return ySortedList;
    }

    public void printPoints() {
        System.out.println("left" + this.left);
        System.out.println("right" + this.right);
        System.out.println("up" + this.up);
        System.out.println("down" + this.down);
    }

    public Point getLeft() {
        return this.left;
    }

    public Point getUp() {
        return this.up;
    }

    public Point getDown() {
        return this.down;
    }

    public Point getRight() {
        return this.right;
    }
}
