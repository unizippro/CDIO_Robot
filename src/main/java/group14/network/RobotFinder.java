package group14.network;

import lejos.hardware.BrickFinder;
import lejos.hardware.BrickInfo;
import robot.rmi_interfaces.IController;
import robot.rmi_interfaces.IMovement;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RobotFinder {
    public IMovement findRobot() {
        try {
            return this.searchNetwork();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private IMovement searchNetwork() throws RemoteException, NotBoundException, MalformedURLException {
        BrickInfo[] bricks = BrickFinder.discover();
        if (bricks.length == 0) {
            throw new RuntimeException("No bricks on group14.network");
        }

        return (IMovement) Naming.lookup("rmi://" + bricks[0].getIPAddress() + ":1199/movement");
    }

    public IController findController(){
        try {
            return this.searchNetworkForController();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private IController searchNetworkForController() throws RemoteException, NotBoundException, MalformedURLException {
        BrickInfo[] bricks = BrickFinder.discover();
        if (bricks.length == 0) {
            throw new RuntimeException("No bricks on network");
        }

        return (IController) Naming.lookup("rmi://" + bricks[0].getIPAddress() + ":1199/controller");
    }
}
