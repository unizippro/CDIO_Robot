package group14.robot.data;

import java.io.Serializable;

public class Instruction implements Serializable {

    private final InstructionType type;
    private final double amount;

    public enum InstructionType implements Serializable {
        TURN,
        FORWARD,
        BACKWARD,
        DEPOSIT,
        WAIT,
        DANCE,
        SHORT_DANCE
    }

    public static Instruction forward(double amount) {
        return new Instruction(InstructionType.FORWARD, amount);
    }

    public static Instruction turn(double amount) {
        return new Instruction(InstructionType.TURN, amount);
    }

    public static Instruction backward(double amount) {
        return new Instruction(InstructionType.BACKWARD, amount);
    }

    public static Instruction sleep(double amount) {
        return new Instruction(InstructionType.WAIT, amount);
    }

    public static Instruction deposit() {
        return new Instruction(InstructionType.DEPOSIT);
    }

    public static Instruction dance(boolean shortDance) {
        if (shortDance) {
            return new Instruction(InstructionType.SHORT_DANCE);
        }

        return new Instruction(InstructionType.DANCE);
    }

    public Instruction(InstructionType type) {
        this.type = type;
        this.amount = 0;
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
