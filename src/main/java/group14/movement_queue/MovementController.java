package group14.movement_queue;

import group14.RoadPlanner.Instruction;
import group14.network.RobotFinder;
import robot.rmi_interfaces.IMovement;

import java.rmi.RemoteException;

public class MovementController {
    private IMovement movement;
    private MovementQueue<Runnable> queue = new MovementQueue<>();

    public MovementController() {
        this.movement = new RobotFinder().findRobot();
        if (this.movement == null) {
            System.exit(1);
        }
    }

    private void generateMovement(Directions directions, double ... parameter) {
        queue.add(getAction(directions, parameter));
    }

    /**
     * Increments through the queue - executing every runnable added to MovementQueue
     */
    public void run() {
        while (!queue.isEmpty()) {
            queue.dequeue().run();
        }
    }

    private Runnable getAction(Directions direction, double... parameter) {
        return () -> {
            switch (direction) {
                case FORWARD:
                    try {
                        movement.forward(parameter[0]);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case BACKWARD:
                    try {
                        movement.backward(parameter[0]);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case TURN:
                    try {
                        movement.turn((int) parameter[0]);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case STOP:
                    try {
                        movement.stop();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        };
    }

    /**
     * Takes Instruction as argument, translates instructions to movements
     * @param inst
     */
    public void addMovement(Instruction inst) {
        if (inst.angle != 0) {
            generateMovement(Directions.TURN, inst.angle);

            if (inst.distance > 0 ){
                generateMovement(Directions.FORWARD, inst.distance*10);
            }

        }
        else {
            generateMovement(Directions.FORWARD, inst.distance*10);
        }
    }
}
