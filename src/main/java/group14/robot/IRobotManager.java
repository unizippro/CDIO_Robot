package group14.robot;

import lejos.hardware.BrickInfo;
import robot.rmi_interfaces.IController;
import robot.rmi_interfaces.IMovement;
import robot.rmi_interfaces.ISensors;

public interface IRobotManager {
    BrickInfo[] getBricksOnNetwork();
    void setBrick(BrickInfo brick);

    void start();
    void stop();
    void shutdown();

    IMovement getMovement();
    ISensors getSensors();
    IController getController();
}
