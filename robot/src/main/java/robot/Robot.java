package robot;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3IRSensor;
import robot.rmi_interfaces.IRobot;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

public class Robot extends UnicastRemoteObject implements IRobot, Runnable {
    private final int SHUTDOWN_DELAY = 2;

    private Timer shutdownTimer = new Timer();
    private TimerTask shutdownApp = new TimerTask() {
        public void run() {
            System.exit(0);
        }
    };

    private Movement movement = new Movement(new EV3LargeRegulatedMotor(MotorPort.A), new EV3LargeRegulatedMotor(MotorPort.B));
    private Sensors sensors = new Sensors(new EV3IRSensor(SensorPort.S1));

    private AtomicBoolean running = new AtomicBoolean(false);
    private Thread runningThread = null;


    public Robot() throws RemoteException { }

    public Movement getMovement() {
        return this.movement;
    }

    public Sensors getSensors() {
        return this.sensors;
    }

    @Override
    public void start() {
        if (this.running.get()) {
            return;
        }

        this.runningThread = new Thread(this);
        this.runningThread.start();
    }

    @Override
    public void stop() {
        this.running.set(false);
    }

    @Override
    public void shutdown() {
        this.shutdownTimer.schedule(this.shutdownApp, new Date(System.currentTimeMillis() + 1000 * SHUTDOWN_DELAY));
    }


    @Override
    public void run() {
        this.running.set(true);

        while (this.running.get()) {
            if (this.sensors.isWithinRange(25)) {
                this.movement.stop();
            }
        }
    }
}
