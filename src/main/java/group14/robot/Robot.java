package group14.robot;

import group14.Application;
import robot.rmi_interfaces.IRobot;

import java.rmi.Naming;
import java.rmi.RemoteException;

public class Robot implements IRobot {
    IRobot robot;

    public Robot() {
        try {
            this.robot = (IRobot) Naming.lookup("rmi://" + Application.getBrickAddress() + ":1199/robot");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() throws RemoteException {
        this.robot.start();
    }

    @Override
    public void stop() throws RemoteException {
        this.robot.stop();
    }

    @Override
    public void shutdown() throws RemoteException {
        this.robot.shutdown();
    }
}
