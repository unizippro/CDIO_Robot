package RoadPlanner;

import RoadPlanner.ball.Ball;
import RoadPlanner.board.Board;
import RoadPlanner.Robot;
import RoadPlanner.Instruction;
import RoadPlanner.Planner;
import RoadPlanner.board.Cross;
import RoadPlanner.board.Quadrant;

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
     * 0 = upperleft, 1 = upperright, 2 = lowerleft, 3 = lowerright
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
    }

    public void initializeQuadrants() {
        Quadrant quadrant = new Quadrant();
        quadrant.setUpperLeft(this.board.getUpperLeft());
        quadrant.setLowerLeft(this.board.getLowerLeft());
        Point point = new Point(this.cross.getLeft().x, this.board.getUpperLeft().y);
        quadrant.setUpperRight(point);

        Point point1 = new Point(this.cross.getLeft().x, this.board.getLowerRight().y);
        quadrant.setLowerRight(point1);
        this.quadrants.add(quadrant);
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
    }

    /**
     * This is the facade to execute instructions in Planner
     * @return instruction to be executed by MovementController
     */
    public Instruction getNextInstruction() {
        return this.planner.nextInstructionv3();
    }
}
