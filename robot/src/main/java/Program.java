import robot.Robot;

import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;


public class Program {
    public static void main(String[] args) throws Exception {
        System.out.println("Robot starting");
        new Program();
        System.out.println("Robot ready for remote connection");
    }

    private Program() throws Exception {
        String ipAddress = InetAddress.getLocalHost().getHostAddress();

        Robot robot = new Robot();

        try {
            LocateRegistry.createRegistry(1199);
            Naming.rebind("rmi://" + ipAddress + ":1199/robot", robot);
            Naming.rebind("rmi://" + ipAddress + ":1199/movement", robot.getMovement());
            Naming.rebind("rmi://" + ipAddress + ":1199/sensors", robot.getSensors());
        } catch (RemoteException exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }
}
