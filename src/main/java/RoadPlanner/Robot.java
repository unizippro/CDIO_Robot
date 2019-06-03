package RoadPlanner;

import java.awt.*;

public class Robot {


    enum Sector{
        UPPER,
        LOWER,
        RIGHT,
        LEFT
    }

    enum Compas{
        UP,
        DOWN,
        RIGHT,
        LEFT
    }

    Point front;
    Point rear;
    Point mid;
    Vector vector;
    Sector sector;
    Compas compas;

    public Robot() {
        //For Planner constructor
    }

    public Robot(Point front, Point rear) {
        this.front = front;
        this.rear = rear;
        sector = Sector.UPPER;
        calcMid();
        calcVector();

    }

    public void setFront(Point c) {
        this.front = c;
    }
    public void setRear(Point c) {
        this.rear = c;
    }

    /**
     * @param front
     * @param back
     */
    public void update(Point front, Point back) {
        setFront(front);
        setRear(back);
        calcMid();
        calcVector();

    }

    private void calcMid() {
        this.mid = new Point((front.x + rear.x)/2,(front.y + rear.y)/2);
    }

    /**
     *  Calculate the vector from 2 Points c1 to c2.
     */
    private void calcVector() {
        vector = new Vector(front.x - mid.x, front.y - mid.y);
    }


}
