package group14.robot.implementation;

import group14.robot.data.Instruction;
import lejos.hardware.Sound;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import group14.robot.interfaces.IRobot;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.Random;
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
    private Movement movement = new Movement(new EV3LargeRegulatedMotor(MotorPort.A), new EV3LargeRegulatedMotor(MotorPort.B));
    //private Sensors sensors = new Sensors(new EV3IRSensor(SensorPort.S1));

    private AtomicBoolean running = new AtomicBoolean(false);
    private Thread runningThread = null;


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

    @Override
    public void runInstruction(Instruction instruction) {
        switch (instruction.getType()) {
            case FORWARD:
                this.movement.forward(instruction.getAmount() * 10);
                break;

            case BACKWARD:
                this.movement.backward(instruction.getAmount() * 10);
                break;

            case TURN:
                this.movement.turn(instruction.getAmount());
                break;

            case DEPOSIT:
                this.controller.deposit();
                break;

            case WAIT:
                try {
                    Thread.sleep((long) instruction.getAmount());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;

            case DANCE:
                this.controller.fanOff();

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Sound.setVolume(Sound.VOL_MAX);
                        Sound.beep();
                        try {
                            playMarch();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                });

                thread.start();

                while (thread.getState() != Thread.State.TERMINATED) {
                    this.makeMoves();
                }

                this.makeMoves();
                break;

            case SHORT_DANCE:
                this.controller.fanOff();

                try {
                    Sound.setVolume(Sound.VOL_MAX);
                    Sound.beep();
                    Thread.sleep(100);
                    Sound.beep();
                    Thread.sleep(100);
                    Sound.beep();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void makeMoves() {
        if (new Random().nextBoolean()) {
            this.movement.backward(20);
        } else {
            this.movement.forward(20);
        }
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
                File soundFile = new File(path);
                Sound.playSample(soundFile, 100);
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
