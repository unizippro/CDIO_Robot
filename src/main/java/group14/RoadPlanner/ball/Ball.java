package group14.RoadPlanner.ball;

import java.awt.*;

public class Ball {
    private Point pos;

    public Ball(Point pos) {
        this.pos = pos;
    }

    public void setPos(Point pos) {
        this.pos = pos;
    }

    public Point getPos() {
        return pos;
    }
}