package robot;

import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.motor.UnregulatedMotor;
import robot.rmi_interfaces.IController;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.atomic.AtomicBoolean;

public class Controller extends UnicastRemoteObject implements IController {

    private AtomicBoolean fanRunning = new AtomicBoolean(false);
    private AtomicBoolean vibRunning = new AtomicBoolean(false);
    private UnregulatedMotor controllerMotor;
    private UnregulatedMotor gateMotor;

    Controller(UnregulatedMotor controllerMotor) throws RemoteException {//, UnregulatedMotor gateMotor){
        this.controllerMotor = controllerMotor;
        //this.gateMotor = gateMotor;


    }

    @Override
    public void fanOn() {
        controllerMotor.forward();
        fanRunning.set(true);
    }

    @Override
    public void fanOff() {
        controllerMotor.stop();
        vibRunning.set(false);
        fanRunning.set(false);
    }

    @Override
    public void vibOn() {
        controllerMotor.backward();
        vibRunning.set(true);
    }

    @Override
    public void vibOff() {
        controllerMotor.stop();
        vibRunning.set(false);
        fanRunning.set(false);
    }

    @Override
    public boolean getVibStatus() {
        return vibRunning.get();
    }

    @Override
    public boolean getFanStatus() {
        return fanRunning.get();
    }
}
