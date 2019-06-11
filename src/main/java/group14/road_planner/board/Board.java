package group14.road_planner.board;

import group14.road_planner.Robot;
import group14.road_planner.Vector;
import group14.road_planner.board.goal.Goal;
import group14.road_planner.board.goal.GoalType;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Board {
    /**
     * List consisting of corners of data type point
     * 0 = upperleft
     * 1 = upperright
     * 2 = lowerleft
     * 3 = lowerright
     */
    private List<Point> corners = new ArrayList<>();
    private Vector xAxis;
    private List<Quadrant> quadrants = new ArrayList<>();
    private BoardHelper boardHelper = new BoardHelper();
    private int robotQuadrantPlacement;
    private Goal smallGoal;
    private Goal largeGoal;

    public Board(List<Point> boardList) {
        this.update(boardList);
    }

    public Board() {
        //For planner constructor
        xAxis = new Vector(0, 0);
    }



    /**
     * assigns new points and updates xAxis' vectors
     *
     * @param boardList
     */
    public void update(List<Point> boardList) {
        this.assignPoints(boardList);
        this.xAxis.update(this.corners.get(2), this.corners.get(3));
    }

    private void assignPoints(List<Point> boardList) {
        this.corners.add(boardList.get(0));
        this.corners.add(boardList.get(1));
        this.corners.add(boardList.get(2));
        this.corners.add(boardList.get(3));

        this.smallGoal = new Goal(GoalType.SMALL, this);
        this.largeGoal = new Goal(GoalType.LARGE, this);
    }

    public void createSafePoints(List<Quadrant> quadrants) {
        this.boardHelper.createPoints(quadrants);
    }

    public Point getLowerLeft() {
        return this.corners.get(2);
    }

    public Point getLowerRight() {
        return this.corners.get(3);
    }

    public Point getUpperLeft() {
        return this.corners.get(0);
    }

    public Point getUpperRight() {
        return this.corners.get(1);
    }

    public Vector getxAxis() {
        return this.xAxis;
    }

    /**
     * Facade for creating quadrants
     *
     * @param board
     * @param cross
     * @return list of type quadrant
     */
    public List<Quadrant> calculateQuadrant(Board board, Cross cross) {
        this.quadrants.add(this.calculateQuadrantLeft(board, cross));
        this.quadrants.add(this.calculateQuadrantUp(board, cross));
        this.quadrants.add(this.calculateQuadrantRight(board, cross));
        this.quadrants.add(this.calculateQuadrantDown(board, cross));
        for (Quadrant quadrant : this.quadrants) {
            quadrant.createSafetyArea();
        }
        return this.quadrants;
    }

    private Quadrant calculateQuadrantLeft(Board board, Cross cross) {
        Quadrant quadrant = new Quadrant();
        quadrant.setUpperLeft(board.getUpperLeft());
        quadrant.setLowerLeft(board.getLowerLeft());
        quadrant.setUpperRight(new Point(cross.getLeft().x, board.getUpperLeft().y));
        quadrant.setLowerRight(new Point(cross.getLeft().x, board.getLowerRight().y));
        return quadrant;
    }


    private Quadrant calculateQuadrantUp(Board board, Cross cross) {
        Quadrant quadrant = new Quadrant();
        quadrant.setUpperLeft(new Point(cross.getLeft().x, board.getUpperLeft().y));
        quadrant.setLowerLeft(new Point(cross.getLeft().x, cross.getUp().y));
        quadrant.setUpperRight(new Point(cross.getRight().x, board.getUpperLeft().y));
        quadrant.setLowerRight(new Point(cross.getRight().x, cross.getUp().y));

        return quadrant;
    }

    private Quadrant calculateQuadrantRight(Board board, Cross cross) {
        Quadrant quadrant = new Quadrant();
        quadrant.setUpperLeft(new Point(cross.getRight().x, board.getUpperLeft().y));
        quadrant.setLowerLeft(new Point(cross.getRight().x, board.getLowerRight().y));
        quadrant.setUpperRight(board.getUpperRight());
        quadrant.setLowerRight(board.getLowerRight());

        return quadrant;
    }

    private Quadrant calculateQuadrantDown(Board board, Cross cross) {
        Quadrant quadrant = new Quadrant();
        quadrant.setUpperLeft(new Point(cross.getLeft().x, cross.getDown().y));
        quadrant.setLowerLeft(new Point(cross.getLeft().x, board.getLowerRight().y));
        quadrant.setUpperRight(new Point(cross.getRight().x, cross.getDown().y));
        quadrant.setLowerRight(new Point(cross.getRight().x, board.getLowerRight().y));

        return quadrant;
    }

    public int getRobotQuadrantPlacement(Robot robot) {
        return this.boardHelper.getRobotPlacement(robot.getFront(), this);
    }

    public List<Quadrant> getQuadrants() {
        return this.quadrants;
    }
}
