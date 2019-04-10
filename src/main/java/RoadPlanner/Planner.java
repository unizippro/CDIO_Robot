package RoadPlanner;

        import java.util.ArrayList;
        import java.util.List;

        import static java.lang.Math.*;

public class Planner {
    public Robot robot;
    Board board;
    ArrayList<Ball> balls;
    Ball closetBall;
    Vector currentClosetBall;
    int deltaAngle = 2;

    public Planner(List<Coordinate> coorList) {
        int i = 0;
        this.robot = new Robot();
        this.board = new Board();
        this.balls = new ArrayList<Ball>();
        this.currentClosetBall = new Vector();

        updatePlanner(coorList);

        System.out.println("Robot in pos: " + robot.mid);


        //TODO align robot first!

        //findClosetBall();
        //Vector toBall = calcVector(robot.mid,closetBall.pos);
        //Vector robotv = calcVector(robot.mid,robot.front);
        //System.out.println("toBall: " + toBall);
        //System.out.println("Robot: " + robotv);
        //System.out.println("AngleToBall = " + calcAngle(robotv,toBall));
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
        return new Instruction( calcAngle(robot.vector, currentClosetBall), currentClosetBall.lenght);
    }

    public Instruction nextInstructionv2() {
        findClosetBall();

        //1 degree delta, the robot should turn 90 and drive forward
        if(abs((abs(calcAngle(robot.vector, currentClosetBall))-90))<= 1 && (abs(calcAngle(robot.vector, currentClosetBall))-90) < 0 ){
            ////If the robot should do a 90 turn to run in the negativ x-axis
            return new Instruction( calcAngle(robot.vector, currentClosetBall), currentClosetBall.lenght);
        }else if((abs(calcAngle(robot.vector, currentClosetBall))-90) > 0 ){
            //If the robot should do a 180 turn to run in the positiv x-axis
            return new Instruction( 180, 0);
        }else{
            System.out.println("**********"+(abs(calcAngle(robot.vector, currentClosetBall))-90));
            // If not, we should do the tour in two parts.
            System.out.println("x:"+currentClosetBall.x+" y:"+currentClosetBall.y);
            return new Instruction( 0, abs(currentClosetBall.x));
        }

    }
    public Instruction nextInstructionv3() {
        findClosetBall();
        double angleToBall = calcAngle(robot.vector, currentClosetBall);
        try {
            System.out.println("Robot compas: " + robot.compas.toString());
            System.out.println("AngleToBall : " + angleToBall);
            switch (robot.compas) {
                case UP:
                    // Tests if the ball is +- 90 degrees from the robot.
                    if( (abs(angleToBall) < 90 + deltaAngle) || (abs(angleToBall) > 270 - deltaAngle )) {
                        // The ball is in front of us
                        if(abs((abs(angleToBall)-90))<= deltaAngle ){ // Test if the route is in one part or two parts
                            //System.out.println("The ball is in a 90 deg direction.");
                            return new Instruction( angleToBall, currentClosetBall.lenght);
                        } else { // The route is spitted up in 2 parts.
                            //System.out.println("The route is spitted up");
                            return new Instruction( 0, abs(currentClosetBall.y));
                        }
                    } else {
                        // The ball is behind us
                        return new Instruction(180 , 0 );  // turn 180 deg.
                    }
                case DOWN:
                    // Tests if the ball is +- 90 degrees from the robot.
                    if( (abs(angleToBall) < 90 + deltaAngle) || (abs(angleToBall) > 270 - deltaAngle )) {
                        // The ball is in front of us
                        if(abs((abs(angleToBall)-90))<= deltaAngle ){ // Test if the route is in one part or two parts
                            //System.out.println("The ball is in a 90 deg direction.");
                            return new Instruction( angleToBall, currentClosetBall.lenght);
                        } else { // The route is spitted up in 2 parts.
                            //System.out.println("The route is spitted up");
                            return new Instruction( 0, abs(currentClosetBall.y));
                        }
                    } else {
                        // The ball is behind us
                        return new Instruction(180 , 0 );  // turn 180 deg.
                    }
                case LEFT:
                    // Tests if the ball is +- 90 degrees from the robot.
                    if( (abs(angleToBall) < 90 + deltaAngle) || (abs(angleToBall) > 270 - deltaAngle )) {
                        // The ball is in front of us
                        if(abs((abs(angleToBall)-90))<= deltaAngle ){ // Test if the route is in one part or two parts
                            //System.out.println("The ball is in a 90 deg direction.");
                            return new Instruction( angleToBall, currentClosetBall.lenght);
                        } else { // The route is spitted up in 2 parts.
                            //System.out.println("The route is spitted up");
                            return new Instruction( 0, abs(currentClosetBall.x));
                        }
                    } else {
                        // The ball is behind us
                        return new Instruction(180 , 0 );  // turn 180 deg.
                    }
                case RIGHT:
                    // Tests if the ball is +- 90 degrees from the robot.
                    if( ((abs(angleToBall) < 90 + deltaAngle) || (abs(angleToBall) > 270 - deltaAngle ))) {// &&( angleToBall != -180)) {
                        // The ball is in front of us
                        if(abs((abs(angleToBall)-90))<= deltaAngle ){ // Test if the route is in one part or two parts
                            //System.out.println("The ball is in a 90 deg direction.");
                            return new Instruction( angleToBall, currentClosetBall.lenght);
                        } else { // The route is spitted up in 2 parts.
                            //System.out.println("The route is spitted up");
                            return new Instruction( 0, abs(currentClosetBall.x));
                        }
                    } else {
                        // The ball is behind us
                        return new Instruction(180, 0);  // turn 180 deg.
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.err.println("ERROR: COMPAS WAS NOT SET??");
        return new Instruction( 1337, 1337);
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
        //System.out.println("Before manipulation: " + angle);
        if(angle < (-180)) {
            //System.out.println(angle + 360);
            return angle + 360;
        } else if( angle > 180) {
            //System.out.println(angle - 360);
            return angle - 360;
        } else {
            //System.out.println(angle);
            return angle;
        }
    }

    public void updatePlanner(List<Coordinate> coorList) {
        int i = 0;
        this.robot.update(coorList.get(i++),coorList.get(i++));
        this.board.update(coorList.get(i++),coorList.get(i++),coorList.get(i++),coorList.get(i++));
        this.balls = new ArrayList<Ball>();

        for (int j = i; j < coorList.size(); j++) {
            this.balls.add(new Ball(coorList.get(j)));
        }

        this.currentClosetBall = new Vector();

        double temp = calcAngle(robot.vector , board.xAxis);
        System.out.println("The robot's direction is " + temp);

        if(temp <= 45 && temp >= -45){
            this.robot.compas = Robot.Compas.RIGHT;
        }else if(temp < 135 && temp > 45){
            this.robot.compas = Robot.Compas.UP;
        }else if(temp <= -135 || temp >= 135){
            this.robot.compas = Robot.Compas.LEFT;
        }else if(temp < -45 && temp > -135) {
            this.robot.compas = Robot.Compas.DOWN;
        } else {
            System.err.println("ERROR robot's direction is is not coverd: " + temp);
        }
/*

        if(temp <= 45 && temp >= 315){
            this.robot.compas = Robot.Compas.RIGHT;
        }else if(temp < 135 && temp > 45){
            this.robot.compas = Robot.Compas.UP;
        }else if(temp <= 225 && temp >= 135){
            this.robot.compas = Robot.Compas.LEFT;
        }else if(temp < 315 && temp > 225){
            this.robot.compas = Robot.Compas.DOWN;
        } else {
            System.err.println("ERROR COMPASS WAS NOT SET!!");
        }
        // Robot, board and balls are updated now!
        */
    }
}
