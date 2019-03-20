package robot;

import lejos.hardware.motor.EV3LargeRegulatedMotor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Movement extends UnicastRemoteObject implements IMovement {

    // All values below are in mm
    private static final double MARGIN_OF_ERROR = 8;
    private static final double WHEEL_WIDTH = 21.85;
    private static final double ROBOT_DIAMETER = 93 + WHEEL_WIDTH;
    private static final double WHEEL_DIAMETER = 44.5;


    private final EV3LargeRegulatedMotor motorLeft;
    private final EV3LargeRegulatedMotor motorRight;

    private final float maxSpeedLeft;
    private final float maxSpeedRight;


    public Movement(EV3LargeRegulatedMotor motorLeft, EV3LargeRegulatedMotor motorRight) throws RemoteException {
        this.motorLeft = motorLeft;
        this.motorRight = motorRight;

        this.maxSpeedLeft = this.motorLeft.getMaxSpeed();
        this.maxSpeedRight = this.motorRight.getMaxSpeed();

        this.setSpeedPercentage(100);
    }

    @Override
    public void setSpeedPercentage(double percent) {
        this.motorLeft.setSpeed((int) ((percent / 100) * this.maxSpeedLeft));
        this.motorRight.setSpeed((int) ((percent / 100) * this.maxSpeedRight));
    }

    @Override
    public void forward() {
        this.stop();
        this.motorLeft.forward();
        this.motorRight.forward();
    }

    @Override
    public void backward() {
        this.stop();
        this.motorLeft.backward();
        this.motorRight.backward();
    }

    @Override
    public void stop() {
        this.motorLeft.stop(true);
        this.motorRight.stop(true);
    }

    @Override
    public void turn(int degree) {
        double deg = ((((ROBOT_DIAMETER * Math.PI) + MARGIN_OF_ERROR) / (WHEEL_DIAMETER * Math.PI)) * 360) * ((double) degree / 360);

        this.motorLeft.rotate((int) deg, true);
        this.motorRight.rotate((int) -deg, true);
    }

    @Override
    public void shutdown() {
        int secondsDelay = 5;
        this.timer.schedule(this.exitApp, new Date(System.currentTimeMillis() + 1000 * secondsDelay));
    }


    private Timer timer = new Timer();
    private TimerTask exitApp = new TimerTask() {
        public void run() {
            System.exit(0);
        }
    };
}
