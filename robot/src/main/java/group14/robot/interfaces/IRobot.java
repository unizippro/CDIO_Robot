package group14.robot.interfaces;

import group14.robot.data.Instruction;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRobot extends Remote {
    void start() throws RemoteException;
    void stop() throws RemoteException;
    void shutdown() throws RemoteException;
    void runInstruction(Instruction instruction) throws RemoteException;
    void playSound(String path) throws RemoteException;
    void playMarch() throws RemoteException;
}
