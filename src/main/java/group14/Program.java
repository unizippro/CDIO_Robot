package group14;

import group14.road_planner.*;
import group14.road_planner.board.SmartConverter;

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



        //balls
        ArrayList<Point> ballList = new ArrayList<>();
        ballList.add(new Point(296, 107));
        ballList.add(new Point(197, 383));
//        ballList.add(new Point(556, 86));
//        ballList.add(new Point(509, 434));
//        ballList.add(new Point(383, 422));
//        ballList.add(new Point(263, 125));
//        ballList.add(new Point(107, 266));
//        ballList.add(new Point(83, 407));
//        ballList.add(new Point(386, 233));
//        ballList.add(new Point(344, 242));

        //Board
        List<Point> boardList = new ArrayList<>();
        boardList.add(new Point(45, 48));
        boardList.add(new Point(594, 45));
        boardList.add(new Point(45, 444));
        boardList.add(new Point(601, 447));

        //Robot
        List<Point> robotList = new ArrayList<>();
        robotList.add(new Point(154, 231));
        robotList.add(new Point(131, 233));

        //Cross
        List<Point> crossList = new ArrayList<>();
        crossList.add(new Point(326, 256));
        crossList.add(new Point(388, 241));
        crossList.add(new Point(352, 217));
        crossList.add(new Point(362, 280));

        //Hardcoded positions
        RoadController roadController = new RoadController();
        roadController.initialize(boardList, ballList, crossList, robotList);

        System.out.println(roadController.getQuadrants());

        roadController.getNextInstruction();

//        roadController.updateRobot(new Point(16, 0), new Point(14, 0));
//
//        roadController.getNextInstruction();
//
//        roadController.updateRobot(new Point(15, 16), new Point(15, 14));
//        //Remove ball
//        roadController.removeBall(1);
//
//        roadController.getNextInstruction();
//
//        roadController.updateRobot(new Point(15, 61), new Point(15, 59));
//
//        roadController.getNextInstruction();
//
//        roadController.updateRobot(new Point(61, 60), new Point(59, 60));
//
//        //Remove ball
//        roadController.removeBall(0);
//
//        //Drive to safe point
//        roadController.getNextInstruction();
//
//        roadController.updateRobot(new Point(76, 55), new Point(74, 55));
//
//        //Drive to next quadrant safe point
//        roadController.getNextInstruction();
//
//        //The robot is roughly this way, and now pointing to the right
//        roadController.updateRobot(new Point(201, 25), new Point(199, 25));
//
//        //By safepoint in 2 quadrant and should now go to ball
//        roadController.getNextInstruction();
//
//        //The robot just picked up ball 3 and is now pointing up
//        roadController.updateRobot(new Point(200, 41), new Point(200, 39));
//        roadController.removeBall(0);
//
//
//        //No more balls in quadrant, should travel back to safe point (200,25)
//        roadController.getNextInstruction();
//
//        //The robot is pointing down and ready to travel to next quadrant
//        roadController.updateRobot(new Point(200, 24), new Point(200, 26));
//        //Drive to next quadrant safe point
//        roadController.getNextInstruction();
//
//        //The robot is roughly this way, and now pointing to the right
//        roadController.updateRobot(new Point(276, 55), new Point(274, 55));
//
//        //By safepoint 3, should travel to ball 4 (280,80)
//        roadController.getNextInstruction();
//
//        //run 5 units to the right
//        roadController.updateRobot(new Point(281, 55), new Point(279, 55));
//
//
//        roadController.getNextInstruction();
//
//        //Should no turn 90 to the left and drive 25 in the UP direction
//        roadController.updateRobot(new Point(280, 56 + 25), new Point(280, 54 + 25));
//
//
//        roadController.getNextInstruction();
//
//        //The robot just picked up ball 4 and is now pointing up
//        roadController.updateRobot(new Point(280, 56 + 25), new Point(280, 54 + 25));
//        roadController.removeBall(0);
//
//
//        //There is no more balls in quadrant 2, so the robot should go to safepoint 4 (275,145)
//        roadController.getNextInstruction();
//
//        //The robot is pointing up and ready to travel to next quadrant
//        roadController.updateRobot(new Point(275, 146), new Point(275, 144));
//
//
//        //By safepoint 4, drive to next quadrant safe point at (200,175)
//        roadController.getNextInstruction();
//
//
//        //The robot is pointing left at safe point 5
//        roadController.updateRobot(new Point(199, 175), new Point(201, 175));
//
//
//        //By safepoint 5, drive to ball 5 (175,175)
//        roadController.getNextInstruction();
//
//        //The robot just picked up ball 5 and is now pointing left
//        roadController.updateRobot(new Point(199 - 25, 175), new Point(201 - 25, 175));
//        roadController.removeBall(0);
//
//
//
//        //Turn 180 and drive 25 back to safepoint 5 at (200,175)
//        roadController.getNextInstruction();
//
//        //The robot is pointing right and is ready to travel to last safepoint
//        roadController.updateRobot(new Point(201, 175), new Point(199, 175));
//
//        //Turn 166.5 and drive 182.55 to safepoint 0 (last point) at (75,145)
//        roadController.getNextInstruction();
//
//        //The robot is pointing left and is at the last safepoint (0)
//        roadController.updateRobot(new Point(74, 145), new Point(76, 145));
//
//        //Phase 1 is now completed, so the next instruction should be
//        //going to the hardcoded drop-off point
//        roadController.getNextInstruction();
//
//        //The robot is now pointing left and is on the drop-off point
//        roadController.updateRobot(new Point(19, 100), new Point(21, 100));
//
//        //The next instruction is doing a 180 and driving 1 unit forward (towards the cross)
//        //and align it self for reversing
//        roadController.getNextInstruction();
//
//        //The robot is now pointing right and is ready for reversing
//        roadController.updateRobot(new Point(22, 100), new Point(20, 100));
//
//
//        //The robot should now do -10 units (reverse) and drop off the balls
//        roadController.getNextInstruction();
//
//        //The robot is now pointing right and is ready for reversing
//        roadController.updateRobot(new Point(22-10, 100), new Point(20-10, 100));
//
//        System.out.println("--------------------------------\nNewest Command:");
//        roadController.getNextInstruction();

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