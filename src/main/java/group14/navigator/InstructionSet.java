package group14.navigator;

import group14.navigator.data.Point2D;
import group14.robot.data.Instruction;

import java.util.*;

public class InstructionSet extends LinkedList<Instruction> {

    private Point2D destination;
    private String message;

    public interface InstructionRunner {
        void run(Instruction instruction);
    }

    public void run(InstructionRunner runner) {
        while (! this.isEmpty()) {
            runner.run(this.poll());
        }
    }

    void setDestination(Point2D destination, String message) {
        this.destination = destination;
        this.message = message;
    }


    @Override
    public String toString() {
        return "InstructionSet" + super.toString() + " - " + this.destination + " " + this.message;
    }
}
