package RoadPlanner;

        import java.util.ArrayList;
        import static java.lang.Math.*;

public class Planner {
    Robot robot;
    Board board;
    ArrayList<Ball> balls;
    Ball closetBall;
    Vector currentClosetBall;

    public Planner() {
        this.robot = new Robot(new Coordinate(7,8),new Coordinate(9,8));
        this.board = new Board(new Coordinate(0,0),new Coordinate(20,0),new Coordinate(20,10),new Coordinate(0,10));
        this.balls = new ArrayList<Ball>();
        this.currentClosetBall = new Vector();
        System.out.println("Robot in pos: " + robot.mid);
        for(int i = 0; i < 10; i++) {
            this.balls.add(new Ball(new Coordinate(6 + i, 10)));
            System.out.println("Ball: " + i + " with pos: " + balls.get(i).pos);
        }

        //TODO align robot first!

        findClosetBall();
        Vector toBall = calcVector(robot.mid,closetBall.pos);
        Vector robotv = calcVector(robot.mid,robot.front);
        System.out.println("toBall: " + toBall);
        System.out.println("Robot: " + robotv);

        System.out.println("Angle = " + calcAngle(robotv,toBall));
    }

    public void findClosetBall(){
        if(balls.size() > 0) {
            currentClosetBall = calcVector(robot.mid, balls.get(0).pos);
            closetBall = balls.get(0);
        } else {
            throw new IllegalArgumentException("There where no Balls in the balls array.");
        }
        for(int i = 1; i < balls.size(); i++) {
            Vector tempVector = calcVector(robot.mid, balls.get(i).pos);
            if(tempVector.lenght < currentClosetBall.lenght) {
                currentClosetBall = tempVector;
                closetBall = balls.get(i);
            }
        }
        System.out.println("Current closest ball: " + closetBall.pos);
    }



    public Instruction nextInstruction() {
        findClosetBall();
        return new Instruction( calcAngle(robot.direction, currentClosetBall), currentClosetBall.lenght);
    }

    /**
     *  Calculate the vector from 2 Coordinates c1 to c2.
     * @param c1
     * @param c2
     * @return
     */
    public Vector calcVector(Coordinate c1, Coordinate c2) {
        return new Vector(c2.x - c1.x, c2.y - c1.y);
    }


    /**
     * Calculate the delta angle from v1 to v2
     * @param v1
     * @param v2
     * @return value between -180 to 180.
     */
    public double calcAngle(Vector v1, Vector v2) {
        // http://www.euclideanspace.com/maths/algebra/vectors/angleBetween/
        double angle = toDegrees(atan2(v1.y,v1.x) - atan2(v2.y,v2.x));
        System.out.println("Before manipulation: " + angle);
        if(angle < (-180)) {
            System.out.println(angle + 360);
            return angle + 360;
        } else if( angle > 180) {
            System.out.println(angle - 360);
            return angle - 360;
        } else {
            System.out.println(angle);
            return angle;
        }
    }

}
