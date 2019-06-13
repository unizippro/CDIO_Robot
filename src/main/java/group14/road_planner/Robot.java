package group14.road_planner;

import group14.math.Calculator;
import group14.road_planner.board.Board;
import group14.road_planner.board.SmartConverter;

import java.awt.*;
import java.util.List;

public class Robot {

    public Point getRotationalPoint() {
        return rotationalPoint;
    }

    enum Compas {
        UP,
        DOWN,
        RIGHT,
        LEFT
    }

    private Point frontOpenCVPoint;
    private Point rearOpenCVPoint;
    private Vector vector;
    private Compas compas;
    private Point front;
    private Point back;
    private Point rotationalPoint;
    private double distanceFromBackPointToRear = 5 * SmartConverter.getPixelsPerCm();
    private double distanceFromBackToFront = 29.5 * SmartConverter.getPixelsPerCm();
    private double distanceFromBackToRotational = 11 * SmartConverter.getPixelsPerCm();

    /**
     * index 0 = frontOpenCVPoint, index 1 = back
     *
     * @param robotPoints
     */
    public Robot(List<Point> robotPoints) {
        this.frontOpenCVPoint = robotPoints.get(0);
        this.rearOpenCVPoint = robotPoints.get(1);double angle = Math.atan2(this.frontOpenCVPoint.y - this.rearOpenCVPoint.y,
                this.frontOpenCVPoint.x - this.rearOpenCVPoint.x);
        double xPoint = this.rearOpenCVPoint.x + this.distanceFromBackToFront * Math.cos(angle);
        double yPoint = this.rearOpenCVPoint.y + this.distanceFromBackToFront * Math.sin(angle);
        this.front = new Point((int) xPoint, (int) yPoint);
//        calcMid();
        calcVector();
        calcRobotPositions();
        calcRotationalPoint();
    }

    public Compas getCompas() {
        return compas;
    }

    public Vector getVector() {
        return vector;
    }

    public Point getFront() {
        return this.front;
    }

    public Point getRearOpenCVPoint() {
        return this.rearOpenCVPoint;
    }

    private void setCompas(Compas compas) {
        this.compas = compas;
    }

    /**
     * @param front
     * @param back
     */
    public void update(Point front, Point back) {
        this.frontOpenCVPoint = front;
        this.rearOpenCVPoint = back;
        calcVector();
        calcRobotPositions();
        calcRotationalPoint();
    }

    public void calcCompas(Board board) {
        double temp = Calculator.CALCULATE_ANGLE(this.getVector(), board.getxAxis());
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

//    private void calcMid() {
//        this.mid =  new Point((this.front.x + this.rear.x)/2,(this.front.y + this.rear.y)/2);
//    }

    /**
     * Calculate the vector from 2 Points c1 to c2.
     */
    private void calcVector() {
        this.vector = new Vector(frontOpenCVPoint.x - rearOpenCVPoint.x, frontOpenCVPoint.y - rearOpenCVPoint.y);
    }

    /**
     * Uses the back and front points' x and y coordinates to calculate angle in radians
     * Calculates the front using simple trigonometry from rear point, angle and distance
     */
    private void calcRobotPositions() {
        double angle = Math.atan2(this.frontOpenCVPoint.y - this.rearOpenCVPoint.y,
                this.frontOpenCVPoint.x - this.rearOpenCVPoint.x);
        double xPoint = this.rearOpenCVPoint.x + this.distanceFromBackToFront * Math.cos(angle);
        double yPoint = this.rearOpenCVPoint.y + this.distanceFromBackToFront * Math.sin(angle);
        this.front = new Point((int) xPoint, (int) yPoint);
    }

    private void calcRotationalPoint() {
        double angle = Math.atan2(this.frontOpenCVPoint.y - this.rearOpenCVPoint.y,
                this.frontOpenCVPoint.x - this.rearOpenCVPoint.x);
        double xPoint = this.rearOpenCVPoint.x + this.distanceFromBackToRotational * Math.cos(angle);
        double yPoint = this.rearOpenCVPoint.y + this.distanceFromBackToRotational * Math.sin(angle);
        this.rotationalPoint = new Point((int) xPoint, (int) yPoint);
    }

    //TODO: This variable should be reset, when changing from phase 1->2
    private int quadrantsVisited = 0;

    public int getQuadrantsVisited() {
        return quadrantsVisited;
    }

    public void incrementQuadrantsVisited() {
        this.quadrantsVisited++;
    }

    public double getDistanceRotationalToFront() {
        return this.distanceFromBackToFront - this.distanceFromBackToRotational;
    }

    private void calcBackPoint() {
        double angle = Math.atan2(this.rearOpenCVPoint.y - this.frontOpenCVPoint.y,
                this.rearOpenCVPoint.x - this.frontOpenCVPoint.x);
        double xPoint = this.rearOpenCVPoint.x + this.distanceFromBackPointToRear * Math.cos(angle);
        double yPoint = this.rearOpenCVPoint.y + this.distanceFromBackPointToRear * Math.sin(angle);
        this.back = new Point((int) xPoint, (int) yPoint);
    }

    public Point getBack() {
        return this.back;
    }

}
