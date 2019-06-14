package group14.road_planner;

import group14.road_planner.ball.Ball;
import group14.road_planner.board.Quadrant;
import group14.road_planner.board.SafePointTravel;
import group14.road_planner.board.SmartConverter;
import group14.robot.data.Instruction;
import group14.road_planner.deposit.DepositPlanner;

import java.awt.*;


public class Planner {
    private RoadController roadController;
    private PlannerHelper plannerHelper = new PlannerHelper();
    private Point depositPoint;
    private boolean travelledToDepositPoint = false;
    private boolean readyToDeposit = false;

    public Planner(RoadController roadController) {
        this.roadController = roadController;
    }

    private Ball getClosestBall() {

//        var balls = this.roadController.getBallsWithinArea();
        var balls = this.roadController.getBalls();
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
        var robot = this.roadController.getRobot();

        //ballsleft?
        //ballswithinarea?
        System.out.println("Robotpos: " + robot.getFront());
        System.out.println("Robot quadrant: " + this.roadController.getQuadrants().indexOf(this.roadController.getCurrentQuadrant()));

        if (this.roadController.getBalls().size() == 0 || this.roadController.getBalls() == null) {
            System.out.println("Der er ingen bolde!");
            if (this.roadController.getBoard().isWithinBoardSafeArea(robot.getFront())) {
                return this.travelToGoal();
            }
            return this.backOff();
//            depositPoint = new DepositPlanner().getSmallGoal(this.roadController.getBoard());

        }
        if (this.roadController.getBalls().size() > 0) {
//          if (this.roadController.getBalls().size()>0) {
            System.out.println("Der er bolde i området!");
            if (this.roadController.getBoard().isWithinBoardSafeArea(robot.getFront())) {
                System.out.println("Det er sikkert at dreje");
                return this.travelToPoint(this.getClosestBall().getPos());
            } else {
                return this.backOff();
            }
        } else {
            if (this.roadController.getBoard().isWithinBoardSafeArea(robot.getFront())) {
                System.out.println("Det er sikkert at dreje til kvadrant");
                return this.travelToNextSafePoint();
            } else {
                return this.backOff();
            }
        }
        //If the robot made it all the way though the safe points
        // it should now go drop off the balls
//        if(robot.getQuadrantsVisited() >= 4){
//
//            double angleToDestinationPoint;
//            switch (phaseOneStep){
//                case 0:
//                    phaseOneStep++;
//                    System.out.println("The robot is now ready to deliver all the balls it has collected from phase 1.\n" +
//                            "Step 0 - Going to drop-off point");
//                    //Go to drop-off point at hardcoded (20,100)
//
//                    destinationVector = Calculator.CALCULATE_VECTOR(robot.getFront(),
//                            new Point(20,100));
//
//                    angleToDestinationPoint= Calculator.CALCULATE_ANGLE(robot.getVector(), destinationVector);
//                    return new Instruction(angleToDestinationPoint, destinationVector.length);
//                case 1:
//                    phaseOneStep++;
//                    System.out.println("Step 1 - The robot is driving 1 unit towards the cross, to align it self" +
//                            " for reversing");
//
//                    //Go to drop-off point at hardcoded (21,100)
//                    destinationVector = Calculator.CALCULATE_VECTOR(robot.getFront(),
//                            new Point(21,100));
//
//                    angleToDestinationPoint= Calculator.CALCULATE_ANGLE(robot.getVector(), destinationVector);
//                    return new Instruction(angleToDestinationPoint, destinationVector.length);
//                case 2:
//                    phaseOneStep++;
//                    System.out.println("Step 2 - The robot will now reverse hardcoded amount and drop off balls");
//                    return new Instruction(0,-10);
//                default:
//                    throw new Exception("I should now start on phase 2");
//                    //TODO: This variable should be reset, when changing from phase 1->2
//                    //QuadrantsVisited = 0;
//            }
//        }
//
//
//        //Make a new list with the balls in the current quadrant
//        this_quadrant_balls = ballsWithinSafeArea(this.roadController.getCurrentQuadrant(), this.roadController.getBalls());
//
//        //Check if there is any balls in the quadrant, if none we should go to the next quadrant
//        if(this_quadrant_balls.size() == 0){
//
//
//
//            //Travel to own point directly
//            //TODO: There should be a check if the "direct" path is
//            // intersecting with the cross and if so, the robot should do the "two part" tour instead
//            destinationVector = Calculator.CALCULATE_VECTOR(robot.getFront(),
//                    travelBetweenSafePoints(robot,this.roadController.getBoard(), true));
//
//            if(this.travelToNextQuadrant){
//                System.out.println("Driving to safe point:");
//            }else{
//                System.out.println("Driving to safe point in next quadrant:");
//                //TODO Here the next quadrant should be picked? But I'm not sure how
//                // so for now it is just hardcoded
//                List<Quadrant> quadrants_temp = this.roadController.getQuadrants();
//                if (quadrants_temp.get(0).equals(this.roadController.getCurrentQuadrant())) {
//                }else if (quadrants_temp.get(1).equals(this.roadController.getCurrentQuadrant())) {
//                }else if (quadrants_temp.get(2).equals(this.roadController.getCurrentQuadrant())) {
//                }else if (quadrants_temp.get(3).equals(this.roadController.getCurrentQuadrant())) {
//                }
//
//                robot.incrementQuadrantsVisited();
//
//
//            }
//
//            double angleToDestinationPoint= Calculator.CALCULATE_ANGLE(robot.getVector(), destinationVector);
//            return new Instruction(angleToDestinationPoint, destinationVector.length);
//
//        }else{
//            //else we should go find the next ball
//            findClosestBall();
//            destinationVector = currentClosetBall;
//        }
//
//
//        //TODO: There should be a check if the first instruction of the "two part" tour path is
//        // intersecting with the cross and if so, the robot should start with the second instruction
//        // and if that doesnt intersect the robot should take the tour in the opposite order.
//
//        double angleToBall = Calculator.CALCULATE_ANGLE(robot.getVector(), destinationVector);
//        try {
//            System.out.println("Robot compas: " + robot.getCompas().toString());
//            System.out.println("AngleToBall : " + angleToBall);
//            switch (robot.getCompas()) {
//                case UP:
//                case DOWN:
//                    // Tests if the ball is +- 90 degrees from the robot.
//                    return getInstruction(angleToBall);
//                case LEFT:
//                case RIGHT:
//                    // Tests if the ball is +- 90 degrees from the robot.
//                    if ((abs(angleToBall) < 90 + deltaAngle) || (abs(angleToBall) > 270 - deltaAngle)) {
//                        // The ball is in front of us
//                        if (abs((abs(angleToBall) - 90)) <= deltaAngle) { // Test if the route is in one part or two parts
//                            //System.out.println("The ball is in a 90 deg direction.");
//                            return new Instruction(angleToBall, destinationVector.length);
//                        } else { // The route is spitted up in 2 parts.
//                            //System.out.println("The route is spitted up");
//                            return new Instruction(0, abs(destinationVector.getX()));
//                        }
//                    } else {
//                        // The ball is behind us
//                        return new Instruction(180, 0);  // turn 180 deg.
//                    }// &&( angleToBall != -180)) {
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.err.println("ERROR: COMPAS WAS NOT SET??");
//        return new Instruction(1337, 1337);
//    }
//
//    private Instruction getInstruction(double angletoPoint) {
//        if ((abs(angletoPoint) < 90 + deltaAngle) || (abs(angletoPoint) > 270 - deltaAngle)) {
//            // The ball is in front of us
//            if (abs((abs(angletoPoint) - 90)) <= deltaAngle) { // Test if the route is in one part or two parts
//                //System.out.println("The ball is in a 90 deg direction.");
//                return new Instruction(angletoPoint, destinationVector.length);
//            } else { // The route is spitted up in 2 parts.
//                //System.out.println("The route is spitted up");
//                return new Instruction(0, abs(destinationVector.getY()));
//            }
//        } else {
//            // The ball is behind us
//            return new Instruction(180, 0);  // turn 180 deg.
//        }
    }

    private Instruction backOff() {
        System.out.println("It is not safe to turn, I am backing off! \n Planner.java");
        return new Instruction(0, -20);
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
//        this.travelToNextQuadrant = true;
        return this.travelToPoint(this.roadController.getCurrentQuadrant().getEntrySafePoint());
    }

    private Instruction travelToQuadrant(Quadrant quadrant) {
        if (this.roadController.getCurrentQuadrant() != quadrant) {
            //Check quadrant jumps from robot.pos
            switch (this.plannerHelper.goBackOrForward(this.roadController, quadrant)) {
                case 0:
                    return this.travelToPreviousSafePoint();
                case 1:
                    return this.travelToNextSafePoint();
            }
            return travelToNextSafePoint();
        }
        return null;
    }

    private Instruction travelToGoal() {
        var smallGoal = this.roadController.getBoard().getGoals().get(0);
        if (this.readyToDeposit) {
            this.roadController.readyToDeposit = true;
            this.readyToDeposit = false;
            return new Instruction(180, 0);
        }
        if (!this.travelledToDepositPoint) {
            Point p = new Point(smallGoal.getPos().x + 30 * (int) SmartConverter.getPixelsPerCm(), smallGoal.getPos().y);
            this.travelledToDepositPoint = true;
            return this.travelToPoint(p);
        } else {
            Point p = new Point(smallGoal.getPos().x + 25 * (int) SmartConverter.getPixelsPerCm(), smallGoal.getPos().y);
            this.travelledToDepositPoint = false;
            this.readyToDeposit = true;
            return this.travelToPoint(p);

        }
//        if (this.plannerHelper.robotWithinGoal(this.roadController.getRobot().getFront(), smallGoal.getGoalPoint())) {
//            Point p = new Point(smallGoal.getPos().x+100, smallGoal.getPos().y);
//            this.roadController.readyToDeposit = true;
//            return this.travelToPoint(p);
//
//        }
//        return this.travelToPoint(this.roadController.getBoard().getGoals().get(0).getGoalPoint());
    }

}
