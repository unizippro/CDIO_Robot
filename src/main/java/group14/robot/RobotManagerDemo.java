package group14.robot;

import group14.robot.data.Instruction;
import group14.robot.data.InstructionOld;
import lejos.hardware.BrickInfo;
import group14.robot.interfaces.IController;
import group14.robot.interfaces.IMovement;
import group14.robot.interfaces.ISensors;

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
            public void runInstruction(Instruction instruction) throws RemoteException {

            }

            @Override
            public void runInstruction(InstructionOld instruction) throws RemoteException {

            }

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
        return new IController() {
            @Override
            public void fanOn() throws RemoteException {

            }

            @Override
            public void fanOff() throws RemoteException {

            }

            @Override
            public void vibOn() throws RemoteException {

            }

            @Override
            public void vibOff() throws RemoteException {

            }

            @Override
            public void gateOpen() throws RemoteException {

            }

            @Override
            public void gateClose() throws RemoteException {

            }

            @Override
            public void gateCalibration(int angle, boolean openState) throws RemoteException {

            }

            @Override
            public boolean gateIsOpen() throws RemoteException {
                return false;
            }

            @Override
            public boolean getVibStatus() throws RemoteException {
                return false;
            }

            @Override
            public boolean getFanStatus() throws RemoteException {
                return false;
            }

            @Override
            public void deposit() throws RemoteException {

            }
        };
    }
}
