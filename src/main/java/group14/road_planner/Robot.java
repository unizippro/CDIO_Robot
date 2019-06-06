package group14.road_planner;

import group14.math.Calculator;
import group14.road_planner.board.Board;
import group14.road_planner.board.Quadrant;

import java.awt.*;
import java.util.List;

public class Robot {

    public Quadrant getCurrentQuadrant() {
        return this.currentQuadrant;
    }

    public void setCurrentQuadrant(Quadrant currentQuadrant) {
        this.currentQuadrant = currentQuadrant;
    }

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
    private Quadrant currentQuadrant;

    /**
     * index 0 = front, index 1 = back
     * @param robotPoints
     */
    public Robot(List<Point> robotPoints) {
        this.front = robotPoints.get(0);
        this.rear = robotPoints.get(1);
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

    public void calcCompas(Board board) {
            double temp = Calculator.CALCULATE_ANGLE(this.getVector(), board.getxAxis());
            System.out.println("The robot's direction is " + temp);

            if (temp <= 45 && temp >= -45) {
                this.setCompas(Robot.Compas.RIGHT);
            } else if (temp < 135 && temp > 45) {
                this.setCompas(Robot.Compas.UP);
            } else if (temp <= -135 || temp >= 135) {
                this.setCompas(Robot.Compas.LEFT);
            } else if (temp < -45 && temp > -135) {
                this.setCompas(Robot.Compas.DOWN);
            }
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
