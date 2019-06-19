package group14.robot.data;

import java.io.Serializable;

public class Instruction implements Serializable {

    private final InstructionType type;
    private final double amount;

    public enum InstructionType implements Serializable {
        TURN,
        FORWARD,
        BACKWARD,
        DANCE
    }

    public Instruction(InstructionType type, double amount) {
        this.type = type;
        this.amount = amount;
    }

    public InstructionType getType() {
        return this.type;
    }

    public double getAmount() {
        return this.amount;
    }

    @Override
    public String toString() {
        return "Instruction(" + this.type + ", " + String.format("%.2f", this.amount) + ")";
    }
}
