package RoadPlanner;

public class Instruction {
    double angle;
    double distance;

    public Instruction(double angle, double distance) {
        this.angle = angle;
        this.distance = distance;
    }

    public String toString() {
        return "Instruction:  \n\tTurn: " + String.format("%.2f", angle) + " deg.\n\tDrive: " + String.format("%.2f", distance) + " units.";
    }
}
