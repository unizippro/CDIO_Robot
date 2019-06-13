package group14.robot.interfaces;

import group14.robot.data.Instruction;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface IMovement extends Remote {
    void runInstruction(Instruction instruction) throws RemoteException;
    void forward() throws RemoteException;
    void forward(double distance) throws RemoteException;
    void backward() throws RemoteException;
    void backward(double distance) throws RemoteException;
    void stop() throws RemoteException;
    void turn(int degree) throws RemoteException;
    void setSpeedPercentage(double percent) throws RemoteException;
}
