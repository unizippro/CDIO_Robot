package robot;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.robotics.RegulatedMotor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Movement extends UnicastRemoteObject implements IMovement {

    private static final int DEFAULT_SPEED = 75;

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

        this.motorLeft.synchronizeWith(new RegulatedMotor[]{this.motorRight});

        this.maxSpeedLeft = this.motorLeft.getMaxSpeed();
        this.maxSpeedRight = this.motorRight.getMaxSpeed();

        this.setSpeedPercentage(DEFAULT_SPEED);
    }

    @Override
    public void setSpeedPercentage(double percent) {
        this.motorLeft.setSpeed((int) ((percent / 100) * this.maxSpeedLeft));
        this.motorRight.setSpeed((int) ((percent / 100) * this.maxSpeedRight));
    }

    @Override
    public void forward() {
        this.motorLeft.startSynchronization();
        this.motorLeft.forward();
        this.motorRight.forward();
        this.motorLeft.endSynchronization();
    }

    @Override
    public void backward() {
        this.motorLeft.startSynchronization();
        this.motorLeft.backward();
        this.motorRight.backward();
        this.motorLeft.endSynchronization();
    }

    @Override
    public void stop() {
        this.motorLeft.startSynchronization();
        this.motorLeft.stop();
        this.motorRight.stop();
        this.motorLeft.endSynchronization();
    }

    @Override
    public void turn(int degree) {
        double deg = ((((ROBOT_DIAMETER * Math.PI) + MARGIN_OF_ERROR) / (WHEEL_DIAMETER * Math.PI)) * 360) * ((double) degree / 360);

        this.setSpeedPercentage(25);

        this.motorLeft.rotate((int) deg, true);
        this.motorRight.rotate((int) -deg, true);

        while (this.motorLeft.isMoving() || this.motorRight.isMoving()) {}

        this.setSpeedPercentage(DEFAULT_SPEED);
    }
}
