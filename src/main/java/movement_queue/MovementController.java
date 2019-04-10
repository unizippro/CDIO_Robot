package movement_queue;

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

    public void addMovement(Directions directions, double ... parameter) {
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
}
