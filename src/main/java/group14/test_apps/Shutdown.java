package group14.test_apps;

import lejos.hardware.BrickFinder;
import lejos.hardware.BrickInfo;
import group14.robot.interfaces.IRobot;

import java.rmi.Naming;

public class Shutdown {
    public static void main(String[] args) throws Exception {
        BrickInfo[] bricks = BrickFinder.discover();
        if (bricks.length == 0) {
            throw new RuntimeException("No bricks on group14.network");
        }

        IRobot robot = (IRobot) Naming.lookup("rmi://" + bricks[0].getIPAddress() + ":1199/robot");

        robot.shutdown();
    }
}
