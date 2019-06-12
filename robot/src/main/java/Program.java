import lejos.hardware.Sound;
import robot.Robot;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;


public class Program {
    private final String IP_ADDRESS = "192.168.43.195";
    public static void main(String[] args) throws Exception {

        System.out.println("Robot starting");
        new Program();
        System.out.println("Robot ready for remote connection");
        Sound.setVolume(Sound.VOL_MAX);
        Sound.beep();
    }

    private Program() throws Exception {
        Robot robot = new Robot();

        try {
            LocateRegistry.createRegistry(1199);
            Naming.rebind("rmi://" + IP_ADDRESS + ":1199/robot", robot);
            Naming.rebind("rmi://" + IP_ADDRESS + ":1199/movement", robot.getMovement());
            //Naming.rebind("rmi://" + IP_ADDRESS + ":1199/sensors", robot.getSensors());
            Naming.rebind("rmi://" + IP_ADDRESS + ":1199/controller", robot.getController());
        } catch (RemoteException exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }
}
