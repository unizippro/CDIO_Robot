package group14.robot;

import group14.Application;
import robot.rmi_interfaces.IMovement;

import java.rmi.Naming;
import java.rmi.RemoteException;


public class Movement implements IMovement {
    IMovement movement;

    public Movement() {
        try {
            this.movement = (IMovement) Naming.lookup("rmi://" + Application.getBrickAddress() + ":1199/movement");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void forward() throws RemoteException {
        this.movement.forward();
    }

    @Override
    public void backward() throws RemoteException {
        this.movement.backward();
    }

    @Override
    public void stop() throws RemoteException {
        this.movement.stop();
    }

    @Override
    public void turn(int degree) throws RemoteException {
        this.movement.turn(degree);
    }

    @Override
    public void setSpeedPercentage(double percent) throws RemoteException {
        this.movement.setSpeedPercentage(percent);
    }
}
