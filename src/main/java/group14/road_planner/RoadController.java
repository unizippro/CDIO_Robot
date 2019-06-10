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
        this.initializeQuadrants();
        this.initializeRobot(robotPoints);

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
        this.robot.calcCompas(this.getBoard());
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

    public void removeBall(int index) {
        this.balls.remove(index);
    }

    public Board getBoard() {
        return board;
    }

    public Robot getRobot() {
        return robot;
    }

    public void updateRobot(Point pointFront, Point pointBack) {
        this.robot.update(pointFront, pointBack);
        //TODO update quadrant!
        this.robot.calcCompas(this.getBoard());
    }

    /**
     * This is the facade to execute instructions in Planner
     * @return instruction to be executed by MovementController
     */
    public Instruction getNextInstruction() {
        Instruction inst = null;
        try {
            inst = this.planner.nextInstruction(this.getRobot());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(inst);
        return inst;
    }

    /**
     * uses the qudrant to check if the quadrant on which the robot is at contains any balls
     * @return list of balls within safetyarea
     */
    public List<Ball> getBallsWithinArea() {
        return this.getCurrentQuadrant().ballsWithinArea(this.getBalls());
    }

    public Quadrant getCurrentQuadrant() {
        return this.board.getQuadrants().get(this.board.getRobotQuadrantPlacement(this.robot));
    }
}
