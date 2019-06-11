package group14.road_planner;

import group14.road_planner.ball.Ball;
import group14.math.Calculator;
import group14.road_planner.board.Board;
import group14.road_planner.board.Quadrant;
import group14.road_planner.board.SafePointTravel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.*;


public class Planner {
    private Ball closestBall;
    private Vector currentClosetBall;
    private int deltaAngle = 2;
    private RoadController roadController;
    private List<Ball> this_quadrant_balls;
    private Vector destinationVector;
    private boolean travelToNextQuadrant = false;
    private int phaseOneStep = 0;

    public Planner(RoadController roadController) {
        this.roadController = roadController;
        this.currentClosetBall = new Vector();
//        updatePlanner(coorList);


        //TODO align robot first!

        //findClosestBall();
        //Vector toBall = calcVector(robot.mid,closestBall.pos);
        //Vector robotv = calcVector(robot.mid,robot.front);
        //System.out.println("toBall: " + toBall);
        //System.out.println("Robot: " + robotv);
        //System.out.println("AngleToBall = " + calcAngle(robotv,toBall));
    }

    private void findClosestBall() {
        if (this_quadrant_balls.size() > 0) {
            currentClosetBall = Calculator.CALCULATE_VECTOR(this.roadController.getRobot().getFront(), this_quadrant_balls.get(0).getPos());
            closestBall = this_quadrant_balls.get(0);
        } else {
            throw new IllegalArgumentException("There where no Balls in the balls array.");
        }
        for (int i = 1; i < this_quadrant_balls.size(); i++) {
            Vector tempVector = Calculator.CALCULATE_VECTOR(this.roadController.getRobot().getFront(), this_quadrant_balls.get(i).getPos());
            if (tempVector.length < currentClosetBall.length) {
                currentClosetBall = tempVector;
                closestBall = this_quadrant_balls.get(i);
            }
        }
        System.out.println("Current closest ball: " + closestBall.getPos());
    }

    //TODO
    public Instruction nextInstruction() throws Exception {
        var robot = this.roadController.getRobot();
        //If the robot made it all the way though the safe points
        // it should now go drop off the balls
        if(robot.getQuadrantsVisited() >= 4){

            double angleToDestinationPoint;
            switch (phaseOneStep){
                case 0:
                    phaseOneStep++;
                    System.out.println("The robot is now ready to deliver all the balls it has collected from phase 1.\n" +
                            "Step 0 - Going to drop-off point");
                    //Go to drop-off point at hardcoded (20,100)

                    destinationVector = Calculator.CALCULATE_VECTOR(robot.getFront(),
                            new Point(20,100));

                    angleToDestinationPoint= Calculator.CALCULATE_ANGLE(robot.getVector(), destinationVector);
                    return new Instruction(angleToDestinationPoint, destinationVector.length);
                case 1:
                    phaseOneStep++;
                    System.out.println("Step 1 - The robot is driving 1 unit towards the cross, to align it self" +
                            " for reversing");

                    //Go to drop-off point at hardcoded (21,100)
                    destinationVector = Calculator.CALCULATE_VECTOR(robot.getFront(),
                            new Point(21,100));

                    angleToDestinationPoint= Calculator.CALCULATE_ANGLE(robot.getVector(), destinationVector);
                    return new Instruction(angleToDestinationPoint, destinationVector.length);
                case 2:
                    phaseOneStep++;
                    System.out.println("Step 2 - The robot will now reverse hardcoded amount and drop off balls");
                    return new Instruction(0,-10);
                default:
                    throw new Exception("I should now start on phase 2");
                    //TODO: This variable should be reset, when changing from phase 1->2
                    //QuadrantsVisited = 0;
            }
        }


        //Make a new list with the balls in the current quadrant
        this_quadrant_balls = ballsWithinSafeArea(this.roadController.getCurrentQuadrant(), this.roadController.getBalls());

        //Check if there is any balls in the quadrant, if none we should go to the next quadrant
        if(this_quadrant_balls.size() == 0){



            //Travel to own point directly
            //TODO: There should be a check if the "direct" path is
            // intersecting with the cross and if so, the robot should do the "two part" tour instead
            destinationVector = Calculator.CALCULATE_VECTOR(robot.getFront(),
                    travelBetweenSafePoints(robot,this.roadController.getBoard(), true));

            if(this.travelToNextQuadrant){
                System.out.println("Driving to safe point:");
            }else{
                System.out.println("Driving to safe point in next quadrant:");
                //TODO Here the next quadrant should be picked? But I'm not sure how
                // so for now it is just hardcoded
                List<Quadrant> quadrants_temp = this.roadController.getQuadrants();
                if (quadrants_temp.get(0).equals(this.roadController.getCurrentQuadrant())) {
                }else if (quadrants_temp.get(1).equals(this.roadController.getCurrentQuadrant())) {
                }else if (quadrants_temp.get(2).equals(this.roadController.getCurrentQuadrant())) {
                }else if (quadrants_temp.get(3).equals(this.roadController.getCurrentQuadrant())) {
                }

                robot.incrementQuadrantsVisited();


            }

            double angleToDestinationPoint= Calculator.CALCULATE_ANGLE(robot.getVector(), destinationVector);
            return new Instruction(angleToDestinationPoint, destinationVector.length);

        }else{
            //else we should go find the next ball
            findClosestBall();
            destinationVector = currentClosetBall;
        }


        //TODO: There should be a check if the first instruction of the "two part" tour path is
        // intersecting with the cross and if so, the robot should start with the second instruction
        // and if that doesnt intersect the robot should take the tour in the opposite order.

        double angleToBall = Calculator.CALCULATE_ANGLE(robot.getVector(), destinationVector);
        try {
            System.out.println("Robot compas: " + robot.getCompas().toString());
            System.out.println("AngleToBall : " + angleToBall);
            switch (robot.getCompas()) {
                case UP:
                case DOWN:
                    // Tests if the ball is +- 90 degrees from the robot.
                    return getInstruction(angleToBall);
                case LEFT:
                case RIGHT:
                    // Tests if the ball is +- 90 degrees from the robot.
                    if ((abs(angleToBall) < 90 + deltaAngle) || (abs(angleToBall) > 270 - deltaAngle)) {
                        // The ball is in front of us
                        if (abs((abs(angleToBall) - 90)) <= deltaAngle) { // Test if the route is in one part or two parts
                            //System.out.println("The ball is in a 90 deg direction.");
                            return new Instruction(angleToBall, destinationVector.length);
                        } else { // The route is spitted up in 2 parts.
                            //System.out.println("The route is spitted up");
                            return new Instruction(0, abs(destinationVector.getX()));
                        }
                    } else {
                        // The ball is behind us
                        return new Instruction(180, 0);  // turn 180 deg.
                    }// &&( angleToBall != -180)) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.err.println("ERROR: COMPAS WAS NOT SET??");
        return new Instruction(1337, 1337);
    }

    private Instruction getInstruction(double angletoPoint) {
        if ((abs(angletoPoint) < 90 + deltaAngle) || (abs(angletoPoint) > 270 - deltaAngle)) {
            // The ball is in front of us
            if (abs((abs(angletoPoint) - 90)) <= deltaAngle) { // Test if the route is in one part or two parts
                //System.out.println("The ball is in a 90 deg direction.");
                return new Instruction(angletoPoint, destinationVector.length);
            } else { // The route is spitted up in 2 parts.
                //System.out.println("The route is spitted up");
                return new Instruction(0, abs(destinationVector.getY()));
            }
        } else {
            // The ball is behind us
            return new Instruction(180, 0);  // turn 180 deg.
        }
    }

    /**
     *
     * @param quadrant
     * @param ballList
     * @return list of balls within safetyarea
     */
    public List<Ball> ballsWithinSafeArea(Quadrant quadrant, List<Ball> ballList) {
        List<Ball> newBallList = new ArrayList<>();
        for (Ball ball : ballList) {
            if (quadrant.isWithingSafetyArea(ball)) {
                newBallList.add(ball);
            }
        }
        return newBallList;
    }

    /**
     * Travel to next quadrant
     * @param robot
     * @param board
     * @param travelToQuadrantExitSafepoint
     * @return
     */
    private Point travelBetweenSafePoints(Robot robot, Board board, boolean travelToQuadrantExitSafepoint) {
        //this.travelOwnSafePoint(robot, board);
        if (this.travelToNextQuadrant) {
            System.out.println("Jeg tager nu til næste kvadrant!");
            this.travelToNextQuadrant = false;
            Point p = new SafePointTravel().getNextSafePoint(board, robot);
            System.out.println("Safepoint i næste kvadrant der køres til: "+p.toString());
            return p;
        }

        return this.travelOwnSafePoint(robot,board,travelToQuadrantExitSafepoint);
    }

    private Point travelOwnSafePoint(Robot robot, Board board, boolean travelToQuadrantExitSafepoint) {
        this.travelToNextQuadrant = true;
        Point p = new SafePointTravel().getClosestSafePoint(board, robot, travelToQuadrantExitSafepoint);
        System.out.println("Safepoint der køres til: "+p.toString());
        return p;
    }

}
