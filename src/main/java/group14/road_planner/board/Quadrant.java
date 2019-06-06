package group14.road_planner.board;

import group14.road_planner.ball.Ball;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Quadrant {
    private Point upperLeft;
    private Point upperRight;
    private Point lowerLeft;
    private Point lowerRight;
    private List<Point> safeArea;
    private int SAFETY_MARGIN = 10;


    public Point getUpperLeft() {
        return upperLeft;
    }

    public void setUpperLeft(Point upperLeft) {
        this.upperLeft = upperLeft;
    }

    public Point getUpperRight() {
        return upperRight;
    }

    public void setUpperRight(Point upperRight) {
        this.upperRight = upperRight;
    }

    public Point getLowerLeft() {
        return lowerLeft;
    }

    public void setLowerLeft(Point lowerLeft) {
        this.lowerLeft = lowerLeft;
    }

    public Point getLowerRight() {
        return lowerRight;
    }

    public void setLowerRight(Point lowerRight) {
        this.lowerRight = lowerRight;
    }

    public void createSafetyArea() {
        this.safeArea = new ArrayList<>();
        this.safeArea.add(new Point(this.upperLeft.x + SAFETY_MARGIN, this.upperLeft.y + SAFETY_MARGIN));
        this.safeArea.add(new Point(this.upperRight.x - SAFETY_MARGIN, this.upperRight.y + SAFETY_MARGIN));
        this.safeArea.add(new Point(this.lowerLeft.x + SAFETY_MARGIN, this.lowerLeft.y - SAFETY_MARGIN));
        this.safeArea.add(new Point(this.lowerRight.x - SAFETY_MARGIN, this.lowerRight.y - SAFETY_MARGIN));
        System.out.println(this.safeArea);
    }

    /**
     * Checks if ball's position is withing x-axis of safearea and y-axis of safearea
     * @param ball
     * @return
     */
    public boolean isWithingSafetyArea(Ball ball) {
        if (ball.getPos().x >= this.safeArea.get(0).x && ball.getPos().x <= this.safeArea.get(1).x) {
            return ball.getPos().y >= this.safeArea.get(0).y && ball.getPos().y <= this.safeArea.get(3).y;
        }
        return false;
    }
}
