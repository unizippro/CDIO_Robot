package group14.road_planner.board;

import java.awt.*;
import java.util.List;

public class SafetyArea {
    private int safetyMargin = 10;
    private Point upperLeft;
    private Point upperRight;
    private Point lowerLeft;
    private Point lowerRight;
    private Quadrant quadrant;

    public int getSafetyMargin() {
        return safetyMargin;
    }

    public Point getUpperLeft() {
        return upperLeft;
    }

    public Point getUpperRight() {
        return upperRight;
    }

    public Point getLowerLeft() {
        return lowerLeft;
    }

    public Point getLowerRight() {
        return lowerRight;
    }


    public SafetyArea(Quadrant quadrant) {
        this.quadrant = quadrant;
        this.initialize();
    }

    private void initialize() {
        this.upperLeft = new Point(this.quadrant.getUpperLeft().x + this.safetyMargin, this.quadrant.getUpperRight().y + this.safetyMargin);
        this.upperRight = new Point(this.quadrant.getUpperRight().x - this.safetyMargin, this.quadrant.getUpperRight().y + this.safetyMargin);
        this.lowerLeft = new Point(this.quadrant.getLowerLeft().x + this.safetyMargin, this.quadrant.getLowerLeft().y - this.safetyMargin);
        this.lowerRight = new Point(this.quadrant.getLowerRight().x - this.safetyMargin, this.quadrant.getLowerRight().y - this.safetyMargin);
    }

    public void updateSafetyMargins(int margin) {
        this.safetyMargin = margin;
        this.initialize();
    }

}
