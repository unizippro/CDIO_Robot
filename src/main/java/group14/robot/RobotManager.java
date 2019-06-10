package group14.robot;

import lejos.hardware.BrickFinder;
import lejos.hardware.BrickInfo;
import group14.robot.interfaces.IController;
import group14.robot.interfaces.IMovement;
import group14.robot.interfaces.IRobot;
import group14.robot.interfaces.ISensors;

import java.rmi.Naming;
import java.rmi.RemoteException;

public class RobotManager implements IRobotManager {
    private IRobot robot;
    private IMovement movement;
    private ISensors sensors;
    private IController controller;

    private BrickInfo brick;

    @Override
    public BrickInfo[] getBricksOnNetwork() {
        return BrickFinder.discover();
    }

    @Override
    public void setBrick(BrickInfo brick) {
        try {
            this.robot = (IRobot) Naming.lookup("rmi://" + brick.getIPAddress() + ":1199/robot");
            this.brick = brick;
        } catch (Exception e) {
            this.brick = null;
            this.robot = null;

            e.printStackTrace();
        }
    }

    @Override
    public void start() {
        if (this.robot == null) {
            return;
        }

        try {
            this.robot.start();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        if (this.robot == null) {
            return;
        }

        try {
            this.robot.stop();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void shutdown() {
        if (this.robot == null) {
            return;
        }

        try {
            this.robot.shutdown();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public IMovement getMovement() {
        if (this.brick == null) {
            this.movement = null;

            return null;
        }

        if (this.movement == null) {
            try {
                this.movement = (IMovement) Naming.lookup("rmi://" + this.brick.getIPAddress() + ":1199/movement");
            } catch (Exception e) {
                e.printStackTrace();

                return null;
            }
        }

        return this.movement;
    }

    @Override
    public ISensors getSensors() {
        if (this.brick == null) {
            this.sensors = null;

            return null;
        }

        if (this.sensors == null) {
            try {
                this.sensors = (ISensors) Naming.lookup("rmi://" + this.brick.getIPAddress() + ":1199/sensors");
            } catch (Exception e) {
                e.printStackTrace();

                return null;
            }
        }

        return this.sensors;
    }

    @Override
    public IController getController() {
        if (this.brick == null) {
            this.controller = null;

            return null;
        }

        if (this.controller == null) {
            try {
                this.controller = (IController) Naming.lookup("rmi://" + this.brick.getIPAddress() + ":1199/controller");
            } catch (Exception e) {
                e.printStackTrace();

                return null;
            }
        }

        return this.controller;
    }
}
