package movement_queue;

import RoadPlanner.Instruction;
import network.RobotFinder;
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

    public void addMovement(Instruction inst) {
        if (inst.angle > 0 ) {
            generateMovement(Directions.TURN, inst.angle);

        }
        else {
            generateMovement(Directions.FORWARD, inst.distance);
        }
    }
}
