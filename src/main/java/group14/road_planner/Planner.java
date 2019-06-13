package group14.road_planner;

import group14.road_planner.ball.Ball;
import group14.road_planner.board.Quadrant;
import group14.road_planner.board.SafePointTravel;
import group14.robot.data.Instruction;
import group14.road_planner.deposit.DepositPlanner;

import java.awt.*;


public class Planner {
    private RoadController roadController;
    private boolean travelToNextQuadrant = false;
    private PlannerHelper plannerHelper = new PlannerHelper();
    private Point depositPoint;

    public Planner(RoadController roadController) {
        this.roadController = roadController;
    }

    private Ball getClosestBall() {

        var balls = this.roadController.getBallsWithinArea();
        Ball max = balls.get(0);
        double minLength = Integer.MAX_VALUE;
        for (int i = 0; i < balls.size(); i++) {
            double temp = this.plannerHelper.getDistance(this.roadController.getRobot().getFront(), balls.get(i).getPos());
            if (temp < minLength) {
                max = balls.get(i);
                minLength = temp;
            }
        }
        return max;
//        System.out.println("Current closest ball: " + closestBall.getPos());
    }

    //TODO
    public Instruction nextInstruction() {
//        var robot = this.roadController.getRobot();

        //ballsleft?
        //ballswithinarea?

        if (this.roadController.getBalls().size() == 0) {
            depositPoint = new DepositPlanner().getSmallGoal(this.roadController.getBoard());
        }
        if (this.roadController.getBallsWithinArea().size() > 0) {
            return this.travelToPoint(this.getClosestBall().getPos());
        } else {
            return this.travelToNextSafePoint();
        }
    }

    /**
     * calculates angle and distance to closest ball and maps data to instruction
     *
     * @return Instruction
     */
    private Instruction travelToPoint(Point travelToPoint) {
        double angle = this.plannerHelper.getAngle(this.roadController.getRobot(), travelToPoint);
        double distance = this.plannerHelper.getDistanceProjected(this.roadController.getRobot(), travelToPoint);
        return new Instruction(angle, distance);
    }

    /**
     * Checks if the robot is within acceptable distance to the exit safe point, if so the robot will travel
     * to next quadrant's entry safe point
     *
     * @return Instruction
     */
    private Instruction travelToNextSafePoint() {
        var exitSafePoint = this.roadController.getCurrentQuadrant().getExitSafePoint();
//        if (this.plannerHelper.isNearPoint(this.roadController.getRobot(), exitSafePoint)) {
////            this.travelToNextQuadrant = false;
//            return this.travelToPoint(this.roadController.getNextQuadrant().getEntrySafePoint());
//        }
//        return this.travelOwnExitSafePoint();
        return this.travelToPoint(this.roadController.getNextQuadrant().getEntrySafePoint());
    }

    /**
     * Checks if the robot is within acceptable distance to the entry safe point, if so the robot will travel
     * to previous quadrant's exit safe point
     *
     * @return Instruction
     */
    private Instruction travelToPreviousSafePoint() {
        var entrySafePoint = this.roadController.getCurrentQuadrant().getEntrySafePoint();
        if (this.plannerHelper.isNearPoint(this.roadController.getRobot(), entrySafePoint)) {
//            this.travelToNextQuadrant = false;
            return this.travelToPoint(this.roadController.getPreviousQuadrant().getExitSafePoint());
        }
        return this.travelOwnEntrySafePoint();
    }

    private Instruction travelOwnExitSafePoint() {
//        this.travelToNextQuadrant = true;
        return this.travelToPoint(this.roadController.getCurrentQuadrant().getExitSafePoint());
    }

    private Instruction travelOwnEntrySafePoint() {
        this.travelToNextQuadrant = true;
        return this.travelToPoint(this.roadController.getCurrentQuadrant().getEntrySafePoint());
    }

    private Instruction travelToQuadrant(Quadrant quadrant) {
        if (this.roadController.getCurrentQuadrant() != quadrant) {
            //Check quadrant jumps from robot.pos
            switch(this.plannerHelper.goBackOrForward(this.roadController, quadrant)) {
                case 0: return this.travelToPreviousSafePoint();
                case 1: return this.travelToNextSafePoint();
            }
            return travelToNextSafePoint();
        }
        return null;
    }

}
