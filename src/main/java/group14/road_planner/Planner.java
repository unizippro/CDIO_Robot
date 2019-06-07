package group14.road_planner;

import group14.road_planner.ball.Ball;
import group14.math.Calculator;
import group14.road_planner.board.Board;
import group14.road_planner.board.Quadrant;
import group14.road_planner.board.SafePoint;

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
            currentClosetBall = Calculator.CALCULATE_VECTOR(this.roadController.getRobot().getMid(), this_quadrant_balls.get(0).getPos());
            closestBall = this_quadrant_balls.get(0);
        } else {
            throw new IllegalArgumentException("There where no Balls in the balls array.");
        }
        for (int i = 1; i < this_quadrant_balls.size(); i++) {
            Vector tempVector = Calculator.CALCULATE_VECTOR(this.roadController.getRobot().getMid(), this_quadrant_balls.get(i).getPos());
            if (tempVector.length < currentClosetBall.length) {
                currentClosetBall = tempVector;
                closestBall = this_quadrant_balls.get(i);
            }
        }
        System.out.println("Current closest ball: " + closestBall.getPos());
    }

    //TODO
    public Instruction nextInstruction(Robot robot) {

        //Make a new list with the balls in the current quadrant
        this_quadrant_balls = ballsWithinSafeArea(this.roadController.getRobot().getCurrentQuadrant(), this.roadController.getBalls());

        //Check if there is any balls in the quadrant, if none we should go to the next quadrant
        if(this_quadrant_balls.size() == 0){

            //Travel to own
            System.out.println("Driving to safe point: " + this.roadController.getBoard().getSafePointLinkedList().get(0).toString());
            destinationVector = Calculator.CALCULATE_VECTOR(this.roadController.getRobot().getMid(), this.roadController.getBoard().getSafePointLinkedList().get(0));

            //TODO travel to next quadrant

        }else{
            //else we should go find the next ball
            findClosestBall();
            destinationVector = currentClosetBall;
        }


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
     * @return
     */
    private Point travelBetweenSafePoints(Robot robot, Board board) {
        //this.travelOwnSafePoint(robot, board);
        if (this.travelToNextQuadrant) {
            this.travelToNextQuadrant = false;
            return new SafePoint().getNextSafePoint(board.getSafePointLinkedList(), this.roadController.getQuadrants(), robot);
        }

        return this.travelOwnSafePoint(robot,board);
    }

    private Point travelOwnSafePoint(Robot robot, Board board) {
        this.travelToNextQuadrant = true;
        return new SafePoint().getClosestSafePoint(board.getSafePointLinkedList(), this.roadController.getQuadrants(), robot);
    }

}
