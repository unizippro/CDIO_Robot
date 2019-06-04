package RoadPlanner;

import RoadPlanner.ball.Ball;
import RoadPlanner.board.Board;
import RoadPlanner.Robot;
import RoadPlanner.Instruction;
import RoadPlanner.Planner;
import RoadPlanner.board.Cross;
import RoadPlanner.board.Quadrant;
import math.Calculator;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains needed info about the robot, board and balls' positions.
 * The class is passed as a reference when creating new planner.
 *
 */
public class RoadController {
    private List<Ball> balls = new ArrayList<>();
    private Board board = new Board();
    private Robot robot;
    private Planner planner = new Planner(this);
    private Cross cross;
    /**
     * Index
     * 0 = Left, 1 = Up, 2 = Right, 3 = Left
     */
    private List<Quadrant> quadrants = new ArrayList<>();

    public List<Quadrant> getQuadrants() {
        return quadrants;
    }

    public void setQuadrants(List<Quadrant> quadrants) {
        this.quadrants = quadrants;
    }

    public RoadController() {
    }

    public void initializeBoard(List<Point> points) {
        this.board.update(points);
    }

    public void initializeCross(List<Point> crossPoints) {
        this.cross = new Cross(crossPoints);
    }

    public void initializeBalls(List<Point> balls) {
        List<Ball> newBallList = new ArrayList<>();
        for (Point point : balls) {
            newBallList.add(new Ball(point));
        }
        this.balls.addAll(newBallList);
    }

    public void initializeRobot(Point pointFront, Point pointBack) {
        this.robot = new Robot(pointFront, pointBack);
        this.calcCompas();
    }

    public void initializeQuadrants() {
        try {
            this.quadrants = this.board.calculateQuadrant(this.board, this.cross);
        } catch (NullPointerException e) {
            System.out.println("Board eller cross eksisterer ikke");
        }
    }

    public List<Ball> getBalls() {
        return balls;
    }

    public void setBalls(List<Ball> balls) {
        this.balls = balls;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Robot getRobot() {
        return robot;
    }

    public void updateRobot(Point pointFront, Point pointBack) {
        this.robot.update(pointFront, pointBack);
        this.calcCompas();
    }

    /**
     * This is the facade to execute instructions in Planner
     * @return instruction to be executed by MovementController
     */
    public Instruction getNextInstruction() {
        Instruction inst = this.planner.nextInstructionv3();
        System.out.println(inst);
        return inst;
    }

    private void calcCompas() {
        double temp = Calculator.CALCULATE_ANGLE(this.robot.getVector(), this.board.getxAxis());
        System.out.println("The robot's direction is " + temp);

        if (temp <= 45 && temp >= -45) {
            this.robot.setCompas(Robot.Compas.RIGHT);
        } else if (temp < 135 && temp > 45) {
            this.robot.setCompas(Robot.Compas.UP);
        } else if (temp <= -135 || temp >= 135) {
            this.robot.setCompas(Robot.Compas.LEFT);
        } else if (temp < -45 && temp > -135) {
            this.robot.setCompas(Robot.Compas.DOWN);
        }
    }
}
