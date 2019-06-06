package group14.movement_queue;

import group14.network.RobotFinder;
import robot.rmi_interfaces.IController;

import java.rmi.RemoteException;

public class ControllerController {
    private IController controller;

    public ControllerController(){
        this.controller = new RobotFinder().findController();
        if (this.controller == null) {
            System.exit(1);
        }
    }

    public void fanOn() {
        try {
            controller.fanOn();
        } catch (RemoteException e){
            e.printStackTrace();
        }
    }

    public void fanOff() {
        try {
            controller.fanOff();
        } catch (RemoteException e){
            e.printStackTrace();
        }
    }


    public void vibOn() {
        try {
            controller.vibOn();
        } catch (RemoteException e){
            e.printStackTrace();
        }
    }


    public void vibOff() {
        try {
            controller.vibOff();
        } catch (RemoteException e){
            e.printStackTrace();
        }
    }


    public boolean getVibStatus() {
        boolean ret = false;
        try {
            ret = controller.getVibStatus();
        } catch (RemoteException e){
            e.printStackTrace();
        }
        return ret;
    }


    public boolean getFanStatus() {
        boolean ret = false;
        try {
            ret = controller.getFanStatus();
        } catch (RemoteException e){
            e.printStackTrace();
        }
        return ret;
    }
}
