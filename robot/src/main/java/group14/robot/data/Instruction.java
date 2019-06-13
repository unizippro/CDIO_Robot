package group14.robot.data;

import java.io.Serializable;

public class Instruction implements Serializable {
    private double angle;
    private double distance;

    public Instruction(double angle, double distance) {
        this.angle = angle;
        this.distance = distance;
    }

    public double getAngle() {
        return this.angle;
    }

    public double getDistance() {
        return this.distance;
    }

    public String toString() {
        return "Instruction:  \n\tTurn: " + String.format("%.2f", angle) + " deg.\n\tDrive: " + String.format("%.2f", distance) + " units.";
    }
}
