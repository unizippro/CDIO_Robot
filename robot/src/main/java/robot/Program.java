package robot;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;


public class Program {
    public static void main(String[] args) throws Exception {
        System.out.println("Robot starting");
        Movement movement = new Movement(new EV3LargeRegulatedMotor(MotorPort.A), new EV3LargeRegulatedMotor(MotorPort.B));

        try {
            LocateRegistry.createRegistry(1199);
            Naming.rebind("rmi://192.168.2.6:1199/movement", movement);
        } catch (RemoteException exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        System.out.println("Robot ready for remote connection");
    }
}
