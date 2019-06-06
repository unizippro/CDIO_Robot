package group14.road_planner.ball;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BallList {
    private List<Ball> ballList = new ArrayList<>();

    public void addBall(Point points) {
        this.ballList.add(new Ball(points));
    }

    public List<Ball> getBallList() {
        return ballList;
    }
}
