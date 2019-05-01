import RoadPlanner.Coordinate;
import RoadPlanner.Instruction;
import RoadPlanner.Planner;
import movement_queue.Directions;
import movement_queue.MovementController;

import java.util.ArrayList;
import java.util.List;

//TODO: When the robot have completed an instruction, the virual world should be updated accordingly. To test the GUI an instruction queue

public class Program {
    private MovementController movementController = new MovementController();

    public static void main(String[] args) {
        new Program().doAction();
    }

    private void doAction() {

        List<Coordinate> coorList = new ArrayList<>();

        coorList.add(new Coordinate(7,8));
        //Robot back
        coorList.add(new Coordinate(9,8));
        //Cornors
        coorList.add(new Coordinate(0,0));
        coorList.add(new Coordinate(100,0));
        coorList.add(new Coordinate(100,100));
        coorList.add(new Coordinate(0,100));
        //balls
        coorList.add(new Coordinate(7,5));
        coorList.add(new Coordinate(6,4));
        coorList.add(new Coordinate(8,2));

        Planner t = new Planner(coorList);
        Instruction instruction = t.nextInstructionv2();
        System.out.println(instruction);

        this.movementController.addMovement(instruction);
        //movementController.run();

    }

    static void  testInstructionv3() {
        System.out.println("\nSTARTING TEST OF \"instructionv3\"");

        List<Coordinate> coorList = new ArrayList<>();
        //Hardcoded positions
        //Robot front
        coorList.add(new Coordinate(8,9));
        //Robot back
        coorList.add(new Coordinate(8,7));
        //Cornors
        coorList.add(new Coordinate(0,0));
        coorList.add(new Coordinate(100,0));
        coorList.add(new Coordinate(100,100));
        coorList.add(new Coordinate(0,100));
        //balls
        coorList.add(new Coordinate(6,6));
        coorList.add(new Coordinate(4,4));
        coorList.add(new Coordinate(3,13));
        coorList.add(new Coordinate(21,4));

        Planner t = new Planner(coorList);
        Instruction ins = t.nextInstructionv3();
        System.out.println(ins + "\n");

        //After 0
        coorList.set(0,new Coordinate(8,7));
        coorList.set(1,new Coordinate(8,9));
        t.updatePlanner(coorList);
        ins = t.nextInstructionv3();
        System.out.println(ins + "\n");

        //After 1
        coorList.set(0,new Coordinate(8,5));
        coorList.set(1,new Coordinate(8,7));
        t.updatePlanner(coorList);
        ins = t.nextInstructionv3();
        System.out.println(ins + "\n");

        //After 2
        coorList.set(0,new Coordinate(5,6));
        coorList.set(1,new Coordinate(7,6));
        coorList.remove(6);
        t.updatePlanner(coorList);
        ins = t.nextInstructionv3();
        System.out.println(ins + "\n");

        //After 3
        coorList.set(0,new Coordinate(3,6));
        coorList.set(1,new Coordinate(5,6));
        t.updatePlanner(coorList);
        ins = t.nextInstructionv3();
        System.out.println(ins + "\n");

        //After 4
        coorList.set(0,new Coordinate(4,5));
        coorList.set(1,new Coordinate(4,7));
        t.updatePlanner(coorList);
        ins = t.nextInstructionv3();
        System.out.println(ins + "\n");

        //After 5
        coorList.set(0,new Coordinate(4,3));
        coorList.set(1,new Coordinate(4,5));
        coorList.remove(6);
        t.updatePlanner(coorList);
        ins = t.nextInstructionv3();
        System.out.println(ins + "\n");

        //After 6
        coorList.set(0,new Coordinate(4,5));
        coorList.set(1,new Coordinate(4,3));
        t.updatePlanner(coorList);
        ins = t.nextInstructionv3();
        System.out.println(ins + "\n");

        //After 7
        coorList.set(0,new Coordinate(4,14));
        coorList.set(1,new Coordinate(4,12));
        t.updatePlanner(coorList);
        ins = t.nextInstructionv3();
        System.out.println(ins + "\n");


        //After 8
        coorList.set(0,new Coordinate(2,13));
        coorList.set(1,new Coordinate(4,13));
        coorList.remove(6);
        t.updatePlanner(coorList);
        ins = t.nextInstructionv3();
        System.out.println(ins + "\n");

        //After 9
        coorList.set(0,new Coordinate(4,13));
        coorList.set(1,new Coordinate(2,13));
        t.updatePlanner(coorList);
        ins = t.nextInstructionv3();
        System.out.println(ins + "\n");

        //After 10
        coorList.set(0,new Coordinate(22,13));
        coorList.set(1,new Coordinate(20,13));
        t.updatePlanner(coorList);
        ins = t.nextInstructionv3();
        System.out.println(ins + "\n");
//After 10
        coorList.set(0,new Coordinate(21,3));
        coorList.set(1,new Coordinate(21,5));
        coorList.remove(6);
        t.updatePlanner(coorList);
        ins = t.nextInstructionv3();
        System.out.println(ins + "\n");

    }
}