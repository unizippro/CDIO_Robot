package RoadPlanner;

import RoadPlanner.ball.Ball;
import RoadPlanner.ball.BallList;
import RoadPlanner.board.Board;

import java.awt.*;
import java.util.List;

import static java.lang.Math.*;

public class Planner {
    private Robot robot;
    private Board board;
    private List<Ball> balls;
    private Ball closestBall;
    private Vector currentClosetBall;
    private int deltaAngle = 2;

    public Planner(List<Point> coorList) {
        int i = 0;
        this.robot = new Robot();
//        this.board = new Board()
        this.balls = new BallList().getBallList();
        this.currentClosetBall = new Vector();

//        updatePlanner(coorList);

        System.out.println("Robot in pos: " + robot.mid);


        //TODO align robot first!

        //findClosestBall();
        //Vector toBall = calcVector(robot.mid,closestBall.pos);
        //Vector robotv = calcVector(robot.mid,robot.front);
        //System.out.println("toBall: " + toBall);
        //System.out.println("Robot: " + robotv);
        //System.out.println("AngleToBall = " + calcAngle(robotv,toBall));
    }

    private void findClosestBall() {
        if (balls.size() > 0) {
            currentClosetBall = calcVector(robot.mid, balls.get(0).getPos());
            closestBall = balls.get(0);
        } else {
            throw new IllegalArgumentException("There where no Balls in the balls array.");
        }
        for (int i = 1; i < balls.size(); i++) {
            Vector tempVector = calcVector(robot.mid, balls.get(i).getPos());
            if (tempVector.length < currentClosetBall.length) {
                currentClosetBall = tempVector;
                closestBall = balls.get(i);
            }
        }
        System.out.println("Current closest ball: " + closestBall.getPos());
    }


    public Instruction nextInstruction() {
        findClosestBall();
        return new Instruction(calcAngle(robot.vector, currentClosetBall), currentClosetBall.length);
    }

    public Instruction nextInstructionv2() {
        findClosestBall();

        //1 degree delta, the robot should turn 90 and drive forward
        if (abs((abs(calcAngle(robot.vector, currentClosetBall)) - 90)) <= 1 && (abs(calcAngle(robot.vector, currentClosetBall)) - 90) < 0) {
            ////If the robot should do a 90 turn to run in the negativ x-axis
            return new Instruction(calcAngle(robot.vector, currentClosetBall), currentClosetBall.length);
        } else if ((abs(calcAngle(robot.vector, currentClosetBall)) - 90) > 0) {
            //If the robot should do a 180 turn to run in the positiv x-axis
            return new Instruction(180, 0);
        } else {
            System.out.println("**********" + (abs(calcAngle(robot.vector, currentClosetBall)) - 90));
            // If not, we should do the tour in two parts.
            System.out.println("x:" + currentClosetBall.x + " y:" + currentClosetBall.y);
            return new Instruction(0, abs(currentClosetBall.x));
        }

    }

    public Instruction nextInstructionv3() {
        findClosestBall();
        double angleToBall = calcAngle(robot.vector, currentClosetBall);
        try {
            System.out.println("Robot compas: " + robot.compas.toString());
            System.out.println("AngleToBall : " + angleToBall);
            switch (robot.compas) {
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
                            return new Instruction(0, abs(currentClosetBall.x));
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
                return new Instruction(0, abs(currentClosetBall.y));
            }
        } else {
            // The ball is behind us
            return new Instruction(180, 0);  // turn 180 deg.
        }
    }

    /**
     * Calculate the vector from 2 Points c1 to c2.
     *
     * @param c1
     * @param c2
     * @return
     */
    public Vector calcVector(Point c1, Point c2) {
        return new Vector(c2.x - c1.x, c2.y - c1.y);
    }


    /**
     * Calculate the delta angle from v1 to v2
     *
     * @param v1
     * @param v2
     * @return value between -180 to 180.
     */
    public double calcAngle(Vector v1, Vector v2) {
        // http://www.euclideanspace.com/maths/algebra/vectors/angleBetween/
        double angle = toDegrees(atan2(v1.y, v1.x) - atan2(v2.y, v2.x));
        //System.out.println("Before manipulation: " + angle);
        if (angle < (-180)) {
            //System.out.println(angle + 360);
            return angle + 360;
        } else if (angle > 180) {
            //System.out.println(angle - 360);
            return angle - 360;
        } else {
            //System.out.println(angle);
            return angle;
        }
    }

    public Instruction getNextInstruction() {
        
    }

//    public void updatePlanner(List<Point> coorList) {
//        int i = 0;
//        this.robot.update(coorList.get(i++), coorList.get(i++));
////        this.board.update(coorList.get(i++), coorList.get(i++), coorList.get(i++), coorList.get(i++));
//        this.balls = new ArrayList<Ball>();
//
//        for (int j = i; j < coorList.size(); j++) {
//            this.balls.add(new Ball(coorList.get(j)));
//        }
//
//        this.currentClosetBall = new Vector();
//
//        double temp = calcAngle(robot.vector, board.xAxis);
//        System.out.println("The robot's direction is " + temp);
//
//        if (temp <= 45 && temp >= -45) {
//            this.robot.compas = Robot.Compas.RIGHT;
//        } else if (temp < 135 && temp > 45) {
//            this.robot.compas = Robot.Compas.UP;
//        } else if (temp <= -135 || temp >= 135) {
//            this.robot.compas = Robot.Compas.LEFT;
//        } else if (temp < -45 && temp > -135) {
//            this.robot.compas = Robot.Compas.DOWN;
//        } else {
//            System.err.println("ERROR robot's direction is is not coverd: " + temp);
//        }
//    }
}
