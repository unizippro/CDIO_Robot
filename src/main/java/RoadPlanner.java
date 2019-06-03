import RoadPlanner.ball.Ball;
import RoadPlanner.board.Board;
import RoadPlanner.Robot;
import RoadPlanner.Instruction;
import RoadPlanner.Planner;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RoadPlanner {
    private List<Ball> balls = new ArrayList<>();
    private Board board = new Board();
    private Robot robot = new Robot();
    private Planner planner = new Planner();

    public RoadPlanner() {
    }

    public void initalizeBoard(List<Point> points) {
        this.board.update(points);
    }

    public void intializeBalls(List<Point> balls) {
        List<Ball> newBallList = new ArrayList<>();
        for (Point point : balls) {
            newBallList.add(new Ball(point));
        }
        this.balls.addAll(newBallList);
    }

    public void initializeRobot(Point pointFront, Point pointBack) {
        this.robot.update(pointFront, pointBack);
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

    public void setRobot(Robot robot) {
        this.robot = robot;
    }

    public Instruction getNextInstruction() {
        return this.planner.nextInstruction();
    }
}
