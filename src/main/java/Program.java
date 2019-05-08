import RoadPlanner.Instruction;
import RoadPlanner.Planner;
import RoadPlanner.board.SmartConverter;
import movement_queue.MovementController;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

//TODO: When the robot have completed an instruction, the virual world should be updated accordingly. To test the GUI an instruction queue

public class Program {
    private MovementController movementController = new MovementController();

    public static void main(String[] args) {
        new Program().doAction();
    }

    private void doAction() {
        Point p1 = new Point();
        p1.setLocation(136,356);

        Point p2 = new Point();
        p2.setLocation(3676, 443);

        Point p3 = new Point();
        p3.setLocation(366,2886);

        Point p4 = new Point();
        p4.setLocation(3570,2838);

        SmartConverter sc = new SmartConverter();
        sc.calculateBoard(p1,p2,p3,p4);
        System.out.println(sc.getLength());
    }

//    void testInstructionv4() {
//        System.out.println("\nSTARTING TEST OF \"instructionv3\"");
//
//
//        List<Point> coorList = new ArrayList<>();
//        //Hardcoded positions
//        //Robot front
//        coorList.add(new Point(2, 0));
//        //Robot back
//        coorList.add(new Point(0, 0));
//        //Cornors
//        coorList.add(new Point(0, 0));
//        coorList.add(new Point(100, 0));
//        coorList.add(new Point(100, 100));
//        coorList.add(new Point(0, 100));
//        //balls
//        coorList.add(new Point(60, 60));
//        coorList.add(new Point(200, 0));
//        Planner t = new Planner(coorList);
//        Instruction ins = t.nextInstructionv3();
//        this.movementController.addMovement(ins);
//        movementController.run();
//        System.out.println(ins + "\n");
//
//        //After 0
//        coorList.set(0,new Point(61,0));
//        coorList.set(1,new Point(59,0));
//        t.updatePlanner(coorList);
//        ins = t.nextInstructionv3();
//        this.movementController.addMovement(ins);
//        movementController.run();
//        System.out.println(ins + "\n");
//
//
//        //After 1
//        coorList.set(0,new Point(60,61));
//        coorList.set(1,new Point(60,59));
//        coorList.remove(6);
//        t.updatePlanner(coorList);
//        ins = t.nextInstructionv3();
//        this.movementController.addMovement(ins);
//        movementController.run();
//        System.out.println(ins + "\n");
//
//        coorList.set(0,new Point(60,59));
//        coorList.set(1,new Point(60,61));
//        t.updatePlanner(coorList);
//        ins = t.nextInstructionv3();
//        this.movementController.addMovement(ins);
//        movementController.run();
//        System.out.println(ins + "\n");
//
//        //After 3
// coorList.set(0,new Point(60,-1));
//        coorList.set(1,new Point(60,1));
//        t.updatePlanner(coorList);
//        ins = t.nextInstructionv3();
//        this.movementController.addMovement(ins);
//        movementController.run();
//        System.out.println(ins + "\n");
//
//
//    }
//
//    void  testInstructionv3() {
//        System.out.println("\nSTARTING TEST OF \"instructionv3\"");
//
//        List<Point> coorList = new ArrayList<>();
//        //Hardcoded positions
//        //Robot front
//        coorList.add(new Point(2,0));
//        //Robot back
//        coorList.add(new Point(0,0));
//        //Cornors
//        coorList.add(new Point(0,0));
//        coorList.add(new Point(100,0));
//        coorList.add(new Point(100,100));
//        coorList.add(new Point(0,100));
//        //balls
//        coorList.add(new Point(0,100));
//        Planner t = new Planner(coorList);
//        Instruction ins = t.nextInstructionv3();
//        this.movementController.addMovement(ins);
//        movementController.run();
//        System.out.println(ins + "\n");
//
//        //After 0
//        coorList.set(0,new Point(8,7));
//        coorList.set(1,new Point(8,9));
//        t.updatePlanner(coorList);
//        ins = t.nextInstructionv3();
//        this.movementController.addMovement(ins);
//        movementController.run();
//        System.out.println(ins + "\n");
//
//        //After 1
//        coorList.set(0,new Point(8,5));
//        coorList.set(1,new Point(8,7));
//        t.updatePlanner(coorList);
//        ins = t.nextInstructionv3();
//        this.movementController.addMovement(ins);
//        System.out.println(ins + "\n");
//
//        //After 2
//        coorList.set(0,new Point(5,6));
//        coorList.set(1,new Point(7,6));
//        coorList.remove(6);
//        t.updatePlanner(coorList);
//        ins = t.nextInstructionv3();
//        this.movementController.addMovement(ins);
//        System.out.println(ins + "\n");
//
//        //After 3
//        coorList.set(0,new Point(3,6));
//        coorList.set(1,new Point(5,6));
//        t.updatePlanner(coorList);
//        ins = t.nextInstructionv3();
//        this.movementController.addMovement(ins);
//        System.out.println(ins + "\n");
//
//        //After 4
//        coorList.set(0,new Point(4,5));
//        coorList.set(1,new Point(4,7));
//        t.updatePlanner(coorList);
//        ins = t.nextInstructionv3();
//        this.movementController.addMovement(ins);
//        System.out.println(ins + "\n");
//
//        //After 5
//        coorList.set(0,new Point(4,3));
//        coorList.set(1,new Point(4,5));
//        coorList.remove(6);
//        t.updatePlanner(coorList);
//        ins = t.nextInstructionv3();
//        this.movementController.addMovement(ins);
//        System.out.println(ins + "\n");
//
//        //After 6
//        coorList.set(0,new Point(4,5));
//        coorList.set(1,new Point(4,3));
//        t.updatePlanner(coorList);
//        ins = t.nextInstructionv3();
//        this.movementController.addMovement(ins);
//        System.out.println(ins + "\n");
//
//        //After 7
//        coorList.set(0,new Point(4,14));
//        coorList.set(1,new Point(4,12));
//        t.updatePlanner(coorList);
//        ins = t.nextInstructionv3();
//        this.movementController.addMovement(ins);
//        System.out.println(ins + "\n");
//
//
//        //After 8
//        coorList.set(0,new Point(2,13));
//        coorList.set(1,new Point(4,13));
//        coorList.remove(6);
//        t.updatePlanner(coorList);
//        ins = t.nextInstructionv3();
//        this.movementController.addMovement(ins);
//        System.out.println(ins + "\n");
//
//        //After 9
//        coorList.set(0,new Point(4,13));
//        coorList.set(1,new Point(2,13));
//        t.updatePlanner(coorList);
//        ins = t.nextInstructionv3();
//        this.movementController.addMovement(ins);
//        System.out.println(ins + "\n");
//
//        //After 10
//        coorList.set(0,new Point(22,13));
//        coorList.set(1,new Point(20,13));
//        t.updatePlanner(coorList);
//        ins = t.nextInstructionv3();
//        this.movementController.addMovement(ins);
//        System.out.println(ins + "\n");
//////After 10
////        coorList.set(0,new Point(21,3));
////        coorList.set(1,new Point(21,5));
////        coorList.remove(6);
////        t.updatePlanner(coorList);
////        ins = t.nextInstructionv3();
////        this.movementController.addMovement(ins);
////        System.out.println(ins + "\n");

//    }
}