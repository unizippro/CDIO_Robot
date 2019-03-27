package RoadPlanner;

import static java.lang.Math.*;

public class Robot {
    Coordinate front;
    Coordinate rear;
    Coordinate mid;
    Vector direction;


    public Robot(Coordinate front, Coordinate rear) {
        this.front = front;
        this.rear = rear;
        calcMid();
        calcVector();

    }

    public void setFront(Coordinate c) {
        this.front = c;
    }
    public void setRear(Coordinate c) {
        this.rear = c;
    }

    public void update(Coordinate c1, Coordinate c2) {
        setFront(c1);
        setRear(c2);
        calcMid();

    }

    public void calcMid() {
        this.mid = new Coordinate((front.x + rear.x)/2,(front.y + rear.y)/2);
    }

    /**
     *  Calculate the vector from 2 Coordinates c1 to c2.
     * @param c1
     * @param c2
     * @return
     */
    public void calcVector() {
        direction = new Vector(front.x - mid.x, front.y - mid.y);
    }


}
