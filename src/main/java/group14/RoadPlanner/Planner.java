package group14.RoadPlanner;

import group14.RoadPlanner.ball.Ball;
import group14.math.Calculator;

import static java.lang.Math.*;


public class Planner {
    private Ball closestBall;
    private Vector currentClosetBall;
    private int deltaAngle = 2;
    private RoadController roadController;

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
        if (this.roadController.getBalls().size() > 0) {
            currentClosetBall = Calculator.CALCULATE_VECTOR(this.roadController.getRobot().getMid(), this.roadController.getBalls().get(0).getPos());
            closestBall = this.roadController.getBalls().get(0);
        } else {
            throw new IllegalArgumentException("There where no Balls in the balls array.");
        }
        for (int i = 1; i < this.roadController.getBalls().size(); i++) {
            Vector tempVector = Calculator.CALCULATE_VECTOR(this.roadController.getRobot().getMid(), this.roadController.getBalls().get(i).getPos());
            if (tempVector.length < currentClosetBall.length) {
                currentClosetBall = tempVector;
                closestBall = this.roadController.getBalls().get(i);
            }
        }
        System.out.println("Current closest ball: " + closestBall.getPos());
    }

    //TODO
    public Instruction nextInstruction() {
        findClosestBall();
        return new Instruction(Calculator.CALCULATE_ANGLE(this.roadController.getRobot().getVector(), currentClosetBall), currentClosetBall.length);
    }

    public Instruction nextInstructionv2() {
        findClosestBall();

        //1 degree delta, the robot should turn 90 and drive forward
        if (abs((abs(Calculator.CALCULATE_ANGLE(this.roadController.getRobot().getVector(), currentClosetBall)) - 90)) <= 1 && (abs(Calculator.CALCULATE_ANGLE(this.roadController.getRobot().getVector(), currentClosetBall)) - 90) < 0) {
            ////If the robot should do a 90 turn to run in the negativ x-axis
            return new Instruction(Calculator.CALCULATE_ANGLE(this.roadController.getRobot().getVector(), currentClosetBall), currentClosetBall.length);
        } else if ((abs(Calculator.CALCULATE_ANGLE(this.roadController.getRobot().getVector(), currentClosetBall)) - 90) > 0) {
            //If the robot should do a 180 turn to run in the positiv x-axis
            return new Instruction(180, 0);
        } else {
            System.out.println("**********" + (abs(Calculator.CALCULATE_ANGLE(this.roadController.getRobot().getVector(), currentClosetBall)) - 90));
            // If not, we should do the tour in two parts.
            System.out.println("x:" + currentClosetBall.getX() + " y:" + currentClosetBall.getY());
            return new Instruction(0, abs(currentClosetBall.getX()));
        }

    }

    public Instruction nextInstructionv3() {
        findClosestBall();
        double angleToBall = Calculator.CALCULATE_ANGLE(this.roadController.getRobot().getVector(), currentClosetBall);
        try {
            System.out.println("Robot compas: " + this.roadController.getRobot().getCompas().toString());
            System.out.println("AngleToBall : " + angleToBall);
            switch (this.roadController.getRobot().getCompas()) {
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
                            return new Instruction(angleToBall, currentClosetBall.length);
                        } else { // The route is spitted up in 2 parts.
                            //System.out.println("The route is spitted up");
                            return new Instruction(0, abs(currentClosetBall.getX()));
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

    private Instruction getInstruction(double angleToBall) {
        if ((abs(angleToBall) < 90 + deltaAngle) || (abs(angleToBall) > 270 - deltaAngle)) {
            // The ball is in front of us
            if (abs((abs(angleToBall) - 90)) <= deltaAngle) { // Test if the route is in one part or two parts
                //System.out.println("The ball is in a 90 deg direction.");
                return new Instruction(angleToBall, currentClosetBall.length);
            } else { // The route is spitted up in 2 parts.
                //System.out.println("The route is spitted up");
                return new Instruction(0, abs(currentClosetBall.getY()));
            }
        } else {
            // The ball is behind us
            return new Instruction(180, 0);  // turn 180 deg.
        }
    }


}
