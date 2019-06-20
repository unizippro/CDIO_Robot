package group14.navigator;

import group14.navigator.data.Point2D;
import group14.robot.data.Instruction;

import java.util.*;

public class InstructionSet extends LinkedList<Instruction> {

    private Point2D from;
    private Point2D to;
    private String message;

    public interface InstructionRunner {
        void run(Instruction instruction);
    }

    public void run(InstructionRunner runner) {
        while (! this.isEmpty()) {
            runner.run(this.poll());
        }
    }

    void setData(Point2D from, Point2D to, String message) {
        this.from = from;
        this.to = to;
        this.message = message;
    }


    @Override
    public String toString() {
        return "InstructionSet" + super.toString() + " - " + this.from + " ---> " + this.to + " " + this.message;
    }
}
