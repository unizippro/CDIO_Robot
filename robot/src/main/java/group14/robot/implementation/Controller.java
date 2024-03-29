package group14.robot.implementation;


import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import group14.robot.interfaces.IController;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.atomic.AtomicBoolean;

public class Controller extends UnicastRemoteObject implements IController {

    private AtomicBoolean fanRunning = new AtomicBoolean(false);
    private AtomicBoolean vibRunning = new AtomicBoolean(false);
    private AtomicBoolean gateOpen = new AtomicBoolean(false);
    private int openCloseAngle = 1440;

    private EV3LargeRegulatedMotor controllerMotor;
    private EV3MediumRegulatedMotor gateMotor;

    Controller(EV3LargeRegulatedMotor controllerMotor, EV3MediumRegulatedMotor gateMotor) throws RemoteException {
        this.controllerMotor = controllerMotor;
        this.gateMotor = gateMotor;
        gateMotor.setSpeed(gateMotor.getMaxSpeed()/2);

    }

    @Override
    public void fanOn() {
        controllerMotor.forward();
        fanRunning.set(true);
    }

    @Override
    public void fanOff() {
        controllerMotor.stop();
        controllerMotor.suspendRegulation();
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
        controllerMotor.suspendRegulation();
        vibRunning.set(false);
        fanRunning.set(false);
    }

    @Override
    public void gateOpen() {
        gateMotor.rotate(-openCloseAngle);
        gateOpen.set(true);

    }

    @Override
    public void gateClose() {
        gateMotor.rotate(openCloseAngle);
        gateOpen.set(false);

    }

    @Override
    public void gateCalibration(int angle, boolean openState) throws RemoteException {

        gateMotor.rotate(angle);
        gateOpen.set(openState);
    }

    @Override
    public boolean gateIsOpen() { return gateOpen.get(); }

    @Override
    public boolean getVibStatus() {
        return vibRunning.get();
    }

    @Override
    public boolean getFanStatus() {
        return fanRunning.get();
    }

    /**
     * Depostis balls
     * Opens gate and runs viberator
     * Turns fan off and leaves it off
     */
    @Override
    public void deposit() {
        try {
            this.fanOff();
            Thread.sleep(2000);

            this.gateOpen();

            this.vibOn();
            Thread.sleep(3500);
            this.vibOff();
            Thread.sleep(3500);
            this.vibOn();
            Thread.sleep(3000);
            this.vibOff();
            Thread.sleep(3000);

            this.gateClose();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
