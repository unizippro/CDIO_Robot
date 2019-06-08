package group14;

import group14.road_planner.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

//TODO: When the robot have completed an instruction, the virual world should be updated accordingly. To test the GUI an instruction queue

public class Program {
//    private MovementController movementController = new MovementController();

    public static void main(String[] args) {
        new Program().doAction();
    }

    private void doAction() {

//        /**
//         * Boardet udregnes ud fra pixels. Bliver pt ikke anvendt
//         */
//        Point p1 = new Point();
//        p1.setLocation(136,356);
//
//        Point p2 = new Point();
//        p2.setLocation(3676, 443);
//
//        Point p3 = new Point();
//        p3.setLocation(366,2886);
//
//        Point p4 = new Point();
//        p4.setLocation(3570,2838);
//
//        SmartConverter sc = new SmartConverter();
//        sc.calculateBoard(p1,p2,p3,p4);
//        this.testInstructionv4();


        //balls
        ArrayList<Point> ballList = new ArrayList<>();
        ballList.add(new Point(60, 60));
        ballList.add(new Point(15, 15));
        ballList.add(new Point(200, 40));
        ballList.add(new Point(280,80));

        //Board
        List<Point> boardList = new ArrayList<>();
        boardList.add(new Point(0,0));
        boardList.add(new Point(300,0));
        boardList.add(new Point(0,200));
        boardList.add(new Point(300,200));

        //Robot
        List<Point> robotList = new ArrayList<>();
        robotList.add(new Point(2,0));
        robotList.add(new Point(0,0));

        //Cross
        List<Point> crossList = new ArrayList<>();
        crossList.add(new Point(150,100));
        crossList.add(new Point(200,50));
        crossList.add(new Point(250,100));
        crossList.add(new Point(200,150));

        //Hardcoded positions
        RoadController roadController = new RoadController();
        roadController.initialize(boardList, ballList, crossList, robotList);

        System.out.println(roadController.getQuadrants());

        roadController.getNextInstruction();

        roadController.updateRobot(new Point(16,0), new Point(14,0));

        roadController.getNextInstruction();

        roadController.updateRobot(new Point(15,16), new Point(15,14));
        //Remove ball
        roadController.removeBall(1);

        roadController.getNextInstruction();

        roadController.updateRobot(new Point(15,61), new Point(15,59));

        roadController.getNextInstruction();

        roadController.updateRobot(new Point(61,60), new Point(59,60));

        //Remove ball
        roadController.removeBall(0);

        //Drive to safe point
        roadController.getNextInstruction();

        roadController.updateRobot(new Point(76,55), new Point(74,55));

        //Drive to next quadrant safe point
        roadController.getNextInstruction();

        //The robot is roughly this way, and now pointing to the right
        roadController.updateRobot(new Point(201,25), new Point(199,25));

        //By safepoint in 2 quadrant and should now go to ball
        roadController.getNextInstruction();

        //The robot just picked up ball 3 and is now pointing up
        roadController.updateRobot(new Point(200,41), new Point(200,39));
        roadController.removeBall(0);


        //No more balls in quadrant, should travel back to safe point (200,25)
        roadController.getNextInstruction();

        //The robot is pointing down and ready to travel to next quadrant
        roadController.updateRobot(new Point(200,24), new Point(200,26));
        //Drive to next quadrant safe point
        roadController.getNextInstruction();

        //The robot is roughly this way, and now pointing to the right
        roadController.updateRobot(new Point(274,55), new Point(276,55));

        System.out.println("--------------------------------\nNewest Command:");
        //By safepoint 3, should travel to ball 4 (280,80)
        roadController.getNextInstruction();


        /*
        //15 units frem
        roadController.updateRobot(new Point(76,60), new Point(74,60));

        roadController.getNextInstruction();

        //90 med uret og 5 units frem
        roadController.updateRobot(new Point(75,61-5), new Point(75,59-5));

        //Vi er ved safepoint
        roadController.getNextInstruction();
        */





    }

//    void testInstructionv4() {
//        System.out.println("\nSTARTING TEST OF \"instructionv3\"");
//
//
//        ArrayList<Point> boardList = new ArrayList<>();
//        ArrayList<Point> ballList = new ArrayList<>();
//        //TODO: points should be added as pixels. The roadplanner can utilize group14.road_planner.board.smartconverts pixeltocm to convert
////Cornors
//        boardList.add(new Point(0, 0));
//        boardList.add(new Point(100, 0));
//        boardList.add(new Point(100, 100));
//        boardList.add(new Point(0, 100));
//
//        //balls
//        ballList.add(new Point(60, 60));
//        ballList.add(new Point(200, 0));
//
//        //Hardcoded positions
//        RoadController roadController = new RoadController();
//        roadController.initializeRobot(new Point(2, 0), new Point(0, 0));
//        roadController.initializeBoard(boardList);
//        roadController.initializeBalls(ballList);
//
//        this.movementController.addMovement(roadController.getNextInstruction());
//        this.movementController.run();
//
//        /**
//         * Hvis der skal testes, skal der PT bruges this.movementcontroller.setrobot og angive nye positioner til robotten.
//         * Derefter skal this.movementcontroller.addmovement(roadcontroller.getnextinstruction()) kaldes
//          */
//
//
//    }
}