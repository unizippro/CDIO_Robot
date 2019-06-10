package group14.road_planner;

import group14.math.Calculator;
import group14.road_planner.board.Board;
import group14.road_planner.board.Quadrant;

import java.awt.*;
import java.util.List;

public class Robot {

    //TODO: This variable should be reset, when changing from phase 1->2
    private int quadrantsVisited = 0;

    public int getQuadrantsVisited() {
        return quadrantsVisited;
    }

    public void incrementQuadrantsVisited() {
        this.quadrantsVisited++ ;
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

    private Point frontOpenCVPoint;
    private Point rearOpenCVPoint;
    private Point midOpenCVPoint;
    private Vector vector;
    private Sector sector;
    private Compas compas;
    private Quadrant currentQuadrant;
    private Point rear;
    private Point mid;
    private Point front;

    /**
     * index 0 = frontOpenCVPoint, index 1 = back
     * @param robotPoints
     */
    public Robot(List<Point> robotPoints) {
        this.frontOpenCVPoint = robotPoints.get(0);
        this.rearOpenCVPoint = robotPoints.get(1);
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

    public Point getFrontOpenCVPoint() {
        return frontOpenCVPoint;
    }

    public Point getRearOpenCVPoint() {
        return rearOpenCVPoint;
    }

    public Point getMidOpenCVPoint() {
        return midOpenCVPoint;
    }

    public void setFrontOpenCVPoint(Point c) {
        this.frontOpenCVPoint = c;
    }
    public void setRearOpenCVPoint(Point c) {
        this.rearOpenCVPoint = c;
    }
    public void setCompas(Compas compas) {
        this.compas = compas;
    }

    public void setMidOpenCVPoint(Point midOpenCVPoint) {
        this.midOpenCVPoint = midOpenCVPoint;
    }

    private void setVector(Vector vector) {
        this.vector = vector;
    }
    /**
     * @param front
     * @param back
     */
    public void update(Point front, Point back) {
        setFrontOpenCVPoint(front);
        setRearOpenCVPoint(back);
        calcMid();
        calcVector();
     }

    public void calcCompas(Board board) {
            double temp = Calculator.CALCULATE_ANGLE(this.getVector(), board.getxAxis());
            System.out.println("The robot's direction is " + temp+
                    "\n-------------------------------------");

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
        this.setMidOpenCVPoint(new Point((frontOpenCVPoint.x + rearOpenCVPoint.x)/2,(frontOpenCVPoint.y + rearOpenCVPoint.y)/2));
    }

    /**
     *  Calculate the vector from 2 Points c1 to c2.
     */
    private void calcVector() {
        this.setVector(new Vector(frontOpenCVPoint.x - midOpenCVPoint.x, frontOpenCVPoint.y - midOpenCVPoint.y));
    }

    private void calcRobotPositions() {
    }



}
