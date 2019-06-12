package group14.road_planner.deposit;

import group14.road_planner.board.Board;
import group14.road_planner.board.Quadrant;
import group14.road_planner.board.goal.Goal;
import group14.road_planner.board.goal.GoalType;

import java.awt.*;

public class DepositPlanner {

    /**
     * Finds the middle point of the small goal
     * @param board
     * @return
     */
    public Point getSmallGoal(Board board) {
        var goals = board.getGoals();
        for (Goal goal : goals) {
            if (goal.getGoalType() == GoalType.SMALL) {
                return goal.getPos();
            }
        }
        return null;
    }

}
