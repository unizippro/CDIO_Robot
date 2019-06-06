package group14.road_planner;

import group14.road_planner.ball.Ball;
import group14.road_planner.board.Board;
import group14.road_planner.board.Cross;
import group14.road_planner.board.Quadrant;
import group14.math.Calculator;

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

    /**
     *
     * @param boardPoints
     * @param ballPoints
     * @param crossPoints
     * @param robotPoints
     */
    public void initialize(List<Point> boardPoints,
                           List<Point> ballPoints,
                           List<Point> crossPoints,
                           List<Point> robotPoints) {
        this.initializeBoard(boardPoints);
        this.initializeBalls(ballPoints);
        this.initializeCross(crossPoints);
        this.initializeRobot(robotPoints);
        this.initializeQuadrants();

    }

    private void initializeBoard(List<Point> points) {
        this.board.update(points);
    }

    private void initializeCross(List<Point> crossPoints) {
        this.cross = new Cross(crossPoints);
    }

    private  void initializeBalls(List<Point> balls) {
        List<Ball> newBallList = new ArrayList<>();
        for (Point point : balls) {
            newBallList.add(new Ball(point));
        }
        this.balls.addAll(newBallList);
    }

    private void initializeRobot(List<Point> robotPoints) {
        this.robot = new Robot(robotPoints);
        this.calcCompas();
    }

    private void initializeQuadrants() {
        try {
            this.quadrants = this.board.calculateQuadrant(this.board, this.cross);
            this.board.createSafePoints(this.getQuadrants());
        } catch (NullPointerException e) {
            System.out.println("Board eller cross eksisterer ikke");
        }
    }

    public List<Ball> getBalls() {
        return balls;
    }
    public void setSafetyMargins(int margin) {
        for (Quadrant quadrant : this.getQuadrants()) {
            quadrant.setSafetyMargin(margin);
        }
    }

    public void setBalls(List<Ball> balls) {
        this.balls = balls;
    }

    public void removeBall(int index) {
        this.balls.remove(index);
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
        Instruction inst = this.planner.nextInstruction(this.getRobot());
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

    /**
     * uses the planner to check if the quadrant on which the robot is at contains any balls
     * @return list of balls within safetyarea
     */
    public List<Ball> getBallsWithinArea() {

        return this.planner.ballsWithinSafeArea(this.getQuadrants().get(this.getRobot().getCurrentQuadrant()), this.getBalls());
    }
}
