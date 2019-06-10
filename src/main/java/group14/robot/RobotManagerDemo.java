package group14.robot;

import lejos.hardware.BrickInfo;
import robot.rmi_interfaces.IController;
import robot.rmi_interfaces.IMovement;
import robot.rmi_interfaces.ISensors;

import java.rmi.RemoteException;

public class RobotManagerDemo implements IRobotManager {
    @Override
    public BrickInfo[] getBricksOnNetwork() {
        return new BrickInfo[] {
                new BrickInfo("Brick 1", "192.168.1.20", "robot"),
                new BrickInfo("Brick 2", "192.168.1.24", "robot"),
                new BrickInfo("Brick 3", "192.168.1.29", "robot"),
        };
    }

    @Override
    public void setBrick(BrickInfo brick) {
        System.out.println("Setting brick: " + brick.getName() + " (" + brick.getIPAddress() + ")");
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void shutdown() {

    }

    @Override
    public IMovement getMovement() {
        return new IMovement() {
            @Override
            public void forward() throws RemoteException {

            }

            @Override
            public void forward(double distance) throws RemoteException {

            }

            @Override
            public void backward() throws RemoteException {

            }

            @Override
            public void backward(double distance) throws RemoteException {

            }

            @Override
            public void stop() throws RemoteException {

            }

            @Override
            public void turn(int degree) throws RemoteException {

            }

            @Override
            public void setSpeedPercentage(double percent) throws RemoteException {

            }
        };
    }

    @Override
    public ISensors getSensors() {
        return new ISensors() {
            @Override
            public double getRange() throws RemoteException {
                return 0;
            }

            @Override
            public boolean isWithinRange(double range) throws RemoteException {
                return false;
            }
        };
    }

    @Override
    public IController getController() {
        return null;
    }
}
