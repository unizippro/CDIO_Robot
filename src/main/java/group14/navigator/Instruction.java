package group14.navigator;

public class Instruction {

    private final InstructionType type;
    private final double amount;

    public enum InstructionType {
        TURN,
        FORWARD,
        BACKWARD,
        DANCE
    }

    Instruction(InstructionType type, double amount) {
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
        return "Instruction(" + this.type + ", " + this.amount + ")";
    }
}
