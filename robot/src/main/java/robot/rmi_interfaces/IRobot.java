package robot.rmi_interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRobot extends Remote {
    void start() throws RemoteException;
    void stop() throws RemoteException;
    void shutdown() throws RemoteException;
}
