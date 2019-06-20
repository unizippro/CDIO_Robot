package group14.navigator;

import group14.robot.data.Instruction;
import lejos.robotics.geometry.Point2D;

import java.util.*;

public class InstructionSet extends LinkedList<Instruction> {

    private Point2D.Double destination;

    public interface InstructionRunner {
        void run(Instruction instruction);
    }

    public void run(InstructionRunner runner) {
        while (! this.isEmpty()) {
            runner.run(this.poll());
        }
    }

    public Point2D.Double getDestination() {
        return this.destination;
    }

    void setDestination(Point2D.Double destination) {
        this.destination = destination;
    }

    @Override
    public String toString() {
        return "InstructionSet(dest: " + this.destination + ") " + super.toString();
    }
}
