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
    private SafetyArea safetyArea;



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
        this.safetyArea = new SafetyArea(this);
    }

    /**
     * Checks if ball's position is withing x-axis of safearea and y-axis of safearea
     * @param ball
     * @return
     */
    public boolean isWithingSafetyArea(Ball ball) {
        if (ball.getPos().x >= this.safetyArea.getUpperLeft().x && ball.getPos().x <= this.safetyArea.getUpperRight().x) {
            return ball.getPos().y >= this.safetyArea.getUpperLeft().y && ball.getPos().y <= this.safetyArea.getLowerRight().y;
        }
        return false;
    }

    public void setSafetyMargin(int margin) {
        this.safetyArea.updateSafetyMargins(margin);
    }

    public SafetyArea getSafetyArea() {
        return this.safetyArea;
    }
}
