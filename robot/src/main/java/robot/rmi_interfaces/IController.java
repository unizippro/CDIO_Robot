package robot.rmi_interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IController extends Remote {
    void fanOn() throws RemoteException;
    void fanOff() throws RemoteException;

    void vibOn() throws RemoteException;
    void vibOff() throws RemoteException;

    void gateOpen() throws RemoteException;
    void gateClose() throws RemoteException;

    boolean gateIsOpen() throws RemoteException;
    boolean getVibStatus() throws RemoteException;
    boolean getFanStatus() throws RemoteException;
}
