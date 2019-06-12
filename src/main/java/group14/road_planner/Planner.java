package group14.road_planner;

import group14.road_planner.ball.Ball;
import group14.road_planner.board.Quadrant;
import group14.road_planner.board.SafePointTravel;
import group14.road_planner.deposit.DepositPlanner;
import jdk.jshell.spi.ExecutionControl;

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
    public Instruction nextInstruction(){
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
     * @return Instruction
     */
    private Instruction travelToPoint(Point travelToPoint) {
        double angle = this.plannerHelper.getAngle(this.roadController.getRobot(), travelToPoint);
        double distance = this.plannerHelper.getDistanceProjected(this.roadController.getRobot(), travelToPoint);
        return new Instruction(angle, distance);
    }

    private Instruction travelToNextSafePoint() {
        //this.travelOwnSafePoint(robot, board);
        if (this.travelToNextQuadrant) {
            System.out.println("Jeg tager nu til næste kvadrant!");
            this.travelToNextQuadrant = false;
            Point p = new SafePointTravel().getNextSafePoint(this.roadController.getBoard(), this.roadController.getRobot());
            System.out.println("Safepoint i næste kvadrant der køres til: "+p.toString());
            return this.travelToPoint(p);
        }

        return this.travelOwnSafePoint();
    }

    private Instruction travelToPreviousSafePoint() {
        if (this.travelToNextQuadrant) {
            this.travelToNextQuadrant = false;
            Point p = this.roadController.getPreviousQuadrant().getExitSafePoint().getLocation();
            return this.travelToPoint(p);
        }

        return this.travelOwnSafePoint();
    }

    private Instruction travelOwnSafePoint() {
        this.travelToNextQuadrant = true;
        Point p = this.roadController.getCurrentQuadrant().getExitSafePoint();
        System.out.println("Safepoint der køres til: "+p.toString());
        return this.travelToPoint(p);
    }

    private Instruction travelToQuadrant(Quadrant quadrant) {
        if (this.roadController.getCurrentQuadrant() != quadrant) {
            return travelToNextSafePoint();
        }
        return null;
    }

}
