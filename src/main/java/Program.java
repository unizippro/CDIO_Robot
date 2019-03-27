import lejos.hardware.BrickFinder;
import lejos.hardware.BrickInfo;
import robot.IMovement;

import java.rmi.Naming;

public class Program {
    public static void main(String[] args) throws Exception {
        BrickInfo[] bricks = BrickFinder.discover();
        if (bricks.length == 0) {
            throw new RuntimeException("No bricks on network");
        }

        IMovement movement = (IMovement) Naming.lookup("rmi://" + bricks[0].getIPAddress() + ":1199/movement");

        movement.setSpeedPercentage(25);
        movement.turn(-50);
    }
}