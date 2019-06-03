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

    private Point front;
    private Point rear;
    private Point mid;
    private Vector vector;
    private Sector sector;
    private Compas compas;

    public Robot(Point front, Point rear) {
        this.front = front;
        this.rear = rear;
        sector = Sector.UPPER;
        calcMid();
        calcVector();

    }

    public Compas getCompas() {
        return compas;
    }

    public Sector getSector() {
        return sector;
    }

    public Vector getVector() {
        return vector;
    }

    public Point getFront() {
        return front;
    }

    public Point getRear() {
        return rear;
    }

    public Point getMid() {
        return mid;
    }

    public void setFront(Point c) {
        this.front = c;
    }
    public void setRear(Point c) {
        this.rear = c;
    }
    public void setCompas(Compas compas) {
        this.compas = compas;
    }

    public void setMid(Point mid) {
        this.mid = mid;
    }

    private void setVector(Vector vector) {
        this.vector = vector;
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
        this.setMid(new Point((front.x + rear.x)/2,(front.y + rear.y)/2));
    }

    /**
     *  Calculate the vector from 2 Points c1 to c2.
     */
    private void calcVector() {
        this.setVector(new Vector(front.x - mid.x, front.y - mid.y));
    }


}
