import RoadPlanner.Ball;
import RoadPlanner.Coordinate;
import RoadPlanner.Instruction;
import RoadPlanner.Planner;

import java.util.ArrayList;
import java.util.List;

public class Program {
    public static void main(String[] args) throws Exception {
        System.out.println("Starting program");
        List<Coordinate> coorList = new ArrayList<>();

        //Hardcoded positions
            //Robot front
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
        Instruction ins = t.nextInstructionv2();
        System.out.println(ins);

        //Update after first instuction
        coorList.set(0,new Coordinate(6,8));
        coorList.set(1,new Coordinate(8,8));
        t.updatePlanner(coorList);
        ins = t.nextInstructionv2();
        System.out.println(ins);

        //Update after second instuction
        coorList.set(0,new Coordinate(7,4));
        coorList.set(1,new Coordinate(7,6));
        coorList.remove(6);
        t.updatePlanner(coorList);
        ins = t.nextInstructionv2();
        System.out.println(ins);

        //Update after third instuction
        coorList.set(0,new Coordinate(7,3));
        coorList.set(1,new Coordinate(7,5));
        t.updatePlanner(coorList);
        ins = t.nextInstructionv2();
        System.out.println(ins);

        //Update after fourth instuction
        coorList.set(0,new Coordinate(5,4));
        coorList.set(1,new Coordinate(7,4));
        coorList.remove(6);
        t.updatePlanner(coorList);
        ins = t.nextInstructionv2();
        System.out.println(ins);
    }
}