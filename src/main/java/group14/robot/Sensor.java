package group14.robot;

import group14.Application;
import robot.rmi_interfaces.ISensors;

import java.rmi.Naming;
import java.rmi.RemoteException;

public class Sensor implements ISensors {
    ISensors sensor;

    public Sensor() {
        try {
            this.sensor = (ISensors) Naming.lookup("rmi://" + Application.getBrickAddress() + ":1199/sensors");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public double getRange() throws RemoteException {
        return this.sensor.getRange();
    }

    @Override
    public boolean isWithinRange(double range) throws RemoteException {
        return this.sensor.isWithinRange(range);
    }
}
