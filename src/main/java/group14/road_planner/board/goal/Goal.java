package group14.road_planner.board.goal;

import group14.road_planner.board.Board;

import java.awt.*;

public class Goal  {
    private Point goalPoint;
    private GoalType goalType;

    public Goal(GoalType goalType, Board board) {
        this.goalType = goalType;
        this.calculateGoalPoint(board);
    }

    public Point getGoalPoint() {
        return this.goalPoint;
    }

    public GoalType getGoalType() {
        return this.goalType;
    }

    private void calculateGoalPoint(Board board) {
        this.goalPoint = new Point();
        this.goalPoint.x = this.goalType == GoalType.SMALL ? board.getLowerLeft().x : board.getLowerRight().x;
        this.goalPoint.y = board.getUpperLeft().y + (board.getLowerLeft().y - board.getUpperLeft().y) / 2;
    }

    public void swapGoalType() {
        if (getGoalType() == GoalType.SMALL) {
            this.goalType = GoalType.LARGE;
        } else {
            this.goalType = GoalType.SMALL;
        }
    }
}
