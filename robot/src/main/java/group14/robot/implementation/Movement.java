package group14.robot.implementation;

import group14.robot.data.Instruction;
import group14.robot.data.InstructionOld;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.robotics.RegulatedMotor;
import group14.robot.interfaces.IMovement;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Movement extends UnicastRemoteObject implements IMovement {

    private static final int DEFAULT_SPEED = 75;

    // All values below are in mm
    private static final double MARGIN_OF_ERROR = -0.05;
    private static final double MARGIN_OF_ERROR_TURN = 2;
    private static final double ROBOT_DIAMETER = 114.3+22;
    private static final double WHEEL_DIAMETER = 39;


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

        this.motorLeft.setAcceleration(1000);
        this.motorRight.setAcceleration(1000);


        this.setSpeedPercentage(DEFAULT_SPEED);
    }

    @Override
    public void setSpeedPercentage(double percent) {
        this.motorLeft.setSpeed((int) ((percent / 100) * this.maxSpeedLeft));
        this.motorRight.setSpeed((int) ((percent / 100) * this.maxSpeedRight));
    }

    @Override
    public void runInstruction(Instruction instruction) {
        switch (instruction.getType()) {
            case FORWARD:
                this.forward(instruction.getAmount() * 10);
                break;

            case BACKWARD:
                this.backward(instruction.getAmount() * 10);
                break;

            case TURN:
                this.turn((int) instruction.getAmount());
        }
    }

    @Override
    public void runInstruction(InstructionOld instruction) {
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
        distance = distance + (MARGIN_OF_ERROR * distance);
        double deg = -(360*distance/(WHEEL_DIAMETER * Math.PI));
//        if (distance < 0){
//            deg -= MARGIN_OF_ERROR;
//        } else {
//            deg += MARGIN_OF_ERROR;
//        }
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
        double deg = ((((ROBOT_DIAMETER * Math.PI)) / (WHEEL_DIAMETER * Math.PI)) * 360) * ((double) degree / 360);
        deg += deg/360*MARGIN_OF_ERROR_TURN;
        int tempSpeed = this.motorLeft.getSpeed();
        this.setSpeedPercentage(25);

        this.motorLeft.rotate((int) deg, true);
        this.motorRight.rotate((int) -deg, true);

        while (this.motorLeft.isMoving() || this.motorRight.isMoving()) {}

        this.motorLeft.setSpeed(tempSpeed);
        this.motorRight.setSpeed(tempSpeed);
    }
}
