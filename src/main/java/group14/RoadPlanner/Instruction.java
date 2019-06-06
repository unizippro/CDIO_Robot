package group14.RoadPlanner;

public class Instruction {
    public double angle;
    public double distance;

    public Instruction(double angle, double distance) {
        this.angle = angle;
        this.distance = distance;
    }

    public String toString() {
        return "Instruction:  \n\tTurn: " + String.format("%.2f", angle) + " deg.\n\tDrive: " + String.format("%.2f", distance) + " units.";
    }
}
