package group14.robot.implementation;

import lejos.hardware.Audio;
import lejos.hardware.Sound;
import lejos.hardware.ev3.EV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import group14.robot.interfaces.IRobot;
import lejos.remote.ev3.RemoteAudio;

import java.io.File;
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

    private Controller controller = new Controller( new EV3LargeRegulatedMotor(MotorPort.C), new EV3MediumRegulatedMotor(MotorPort.D));
    private Movement movement = new Movement(new EV3LargeRegulatedMotor(MotorPort.A), new EV3LargeRegulatedMotor(MotorPort.B), this.controller);
    //private Sensors sensors = new Sensors(new EV3IRSensor(SensorPort.S1));

    private AtomicBoolean running = new AtomicBoolean(false);
    private Thread runningThread = null;
    private Object soundSema = new Object();
    private File soundFile;


    public Robot() throws RemoteException { }

    public Movement getMovement() {
        return this.movement;
    }

    /*public Sensors getSensors() {
        return this.sensors;
    }*/

    public Controller getController() { return this.controller; }

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

    /**
     * Method plays a sound from a wav file
     * Might hang.
     * @param path - Recives a full path to a sound
     */
    @Override
    synchronized public void playSound(String path) throws RemoteException {
            Sound.setVolume(Sound.VOL_MAX);
            try {
                System.out.print(".");
                soundFile = new File(path);
                int i = Sound.playSample(soundFile, 100);
                soundFile = null;
                System.out.print(i);
            } catch (Exception e) {
                System.err.println("File not found; Sound not played.");
                e.printStackTrace();
            }
    }

    @Override
    synchronized public void playMarch() throws RemoteException {
        this.playSound("/home/lejos/sound/Imperial_March.wav");
    }


    @Override
    public void run() {
        this.running.set(true);

        while (this.running.get()) {
           /* if (this.sensors.isWithinRange(25)) {
                this.movement.stop();
            }*/
        }
    }
}
