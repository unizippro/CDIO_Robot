package robot;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ISensors extends Remote {
    double getRange() throws RemoteException;
    boolean isWithinRange(double range) throws RemoteException;
}
