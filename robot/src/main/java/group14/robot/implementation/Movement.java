package group14.robot.implementation;

import group14.robot.data.Instruction;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.robotics.RegulatedMotor;
import group14.robot.interfaces.IMovement;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Movement extends UnicastRemoteObject implements IMovement {

    private static final int DEFAULT_SPEED = 75;

    // All values below are in mm
    private static final double MARGIN_OF_ERROR = 0;
    private static final double ROBOT_DIAMETER = 130.8;
    private static final double WHEEL_DIAMETER = 30.4;


    private final EV3LargeRegulatedMotor motorLeft;
    private final EV3LargeRegulatedMotor motorRight;

    private final float maxSpeedLeft;
    private final float maxSpeedRight;


    Movement(EV3LargeRegulatedMotor motorLeft, EV3LargeRegulatedMotor motorRight) throws RemoteException {
        this.motorLeft = motorLeft;
        this.motorRight = motorRight;

        this.motorLeft.synchronizeWith(new RegulatedMotor[]{this.motorRight});

        this.maxSpeedLeft = this.motorLeft.getMaxSpeed();
        this.maxSpeedRight = this.motorRight.getMaxSpeed();

        this.motorLeft.setAcceleration(5500);
        this.motorRight.setAcceleration(5500);


        this.setSpeedPercentage(DEFAULT_SPEED);
    }

    @Override
    public void setSpeedPercentage(double percent) {
        this.motorLeft.setSpeed((int) ((percent / 100) * this.maxSpeedLeft));
        this.motorRight.setSpeed((int) ((percent / 100) * this.maxSpeedRight));
    }

    @Override
    public void runInstruction(Instruction instruction) {
        if (instruction.getAngle() != 0) {
            this.turn((int) instruction.getAngle());
        }

        this.forward(instruction.getDistance() * 10);
    }

    @Override
    public void forward() {
        this.motorLeft.startSynchronization();
        this.motorLeft.backward();
        this.motorRight.backward();
        this.motorLeft.endSynchronization();
    }

    /**
     * calculates required degrees to rotate to move the required distance
     * @param distance as mm
     */
    @Override
    public void forward(double distance){
        double deg = -(360*distance/(WHEEL_DIAMETER * Math.PI));
        if (distance < 0){
            deg -= MARGIN_OF_ERROR;
        } else {
            deg += MARGIN_OF_ERROR;
        }
        this.motorLeft.endSynchronization();
        this.motorLeft.rotate((int)deg, true);
        this.motorRight.rotate((int)deg, true);
        this.motorLeft.endSynchronization();

        while (this.motorLeft.isMoving() || this.motorRight.isMoving()) {}
    }

    @Override
    public void backward() {
        this.motorLeft.startSynchronization();
        this.motorLeft.forward();
        this.motorRight.forward();
        this.motorLeft.endSynchronization();
    }

    /**
     *
     * @param distance as mm
     */
    @Override
    public void backward(double distance) {
        this.forward(-distance);
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
        double deg = ((((ROBOT_DIAMETER * Math.PI)+0.5*MARGIN_OF_ERROR) / (WHEEL_DIAMETER * Math.PI)) * 360) * ((double) degree / 360);
        int tempSpeed = this.motorLeft.getSpeed();
        this.setSpeedPercentage(25);

        this.motorLeft.rotate((int) deg, true);
        this.motorRight.rotate((int) -deg, true);

        while (this.motorLeft.isMoving() || this.motorRight.isMoving()) {}

        this.motorLeft.setSpeed(tempSpeed);
        this.motorRight.setSpeed(tempSpeed);
    }
}