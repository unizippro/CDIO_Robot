package RoadPlanner;

import java.util.ArrayList;
import java.util.List;

public class Ball {
    List<Coordinate> ballList = new ArrayList<>();

    public Ball() {
    }

    public void setPos(Coordinate pos, int index) {
        this.ballList.set(index, pos);
    }

    public void addBall(Coordinate coordinates) {
        this.ballList.add(coordinates);
    }

    public void removeBall(int index) {
        this.ballList.remove(index);
    }

}
