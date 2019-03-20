package robot;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface IMovement extends Remote {
    void forward() throws RemoteException;
    void backward() throws RemoteException;
    void stop() throws RemoteException;
    void turn(int degree) throws RemoteException;
    void setSpeedPercentage(double percent) throws RemoteException;

    void shutdown() throws RemoteException;
}
