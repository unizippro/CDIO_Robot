package group14.robot;

import lejos.hardware.BrickFinder;
import lejos.hardware.BrickInfo;
import robot.rmi_interfaces.IMovement;
import robot.rmi_interfaces.IRobot;
import robot.rmi_interfaces.ISensors;

import java.rmi.Naming;
import java.rmi.RemoteException;

public class RobotManager implements IRobotManager {
    private IRobot robot;
    private IMovement movement;
    private ISensors sensors;

    private BrickInfo brick;

    public RobotManager() {
        this.brick = this.getBricksOnNetwork()[0];

        try {
            this.robot = (IRobot) Naming.lookup("rmi://" + this.brick.getIPAddress() + ":1199/robot");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public BrickInfo[] getBricksOnNetwork() {
        BrickInfo[] bricks = BrickFinder.discover();
        if (bricks.length == 0) {
            throw new RuntimeException("No bricks on group14.network");
        }

        return bricks;
    }

    @Override
    public void setBrick(BrickInfo brick) {
        this.brick = brick;

        try {
            this.robot = (IRobot) Naming.lookup("rmi://" + this.brick.getIPAddress() + ":1199/robot");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() {
        try {
            this.robot.start();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        try {
            this.robot.stop();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void shutdown() {
        try {
            this.robot.shutdown();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public IMovement getMovement() {
        if (movement == null) {
            try {
                movement = (IMovement) Naming.lookup("rmi://" + this.brick.getIPAddress() + ":1199/movement");
            } catch (Exception e) {
                e.printStackTrace();

                return null;
            }
        }

        return movement;
    }

    @Override
    public ISensors getSensors() {
        if (sensors == null) {
            try {
                sensors = (ISensors) Naming.lookup("rmi://" + this.brick.getIPAddress() + ":1199/sensors");
            } catch (Exception e) {
                e.printStackTrace();

                return null;
            }
        }

        return sensors;
    }
}
