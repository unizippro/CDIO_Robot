package group14.robot;

import group14.robot.data.Instruction;
import lejos.hardware.BrickInfo;
import group14.robot.interfaces.IController;
import group14.robot.interfaces.IMovement;
import group14.robot.interfaces.ISensors;

public interface IRobotManager {
    BrickInfo[] getBricksOnNetwork();
    void setBrick(BrickInfo brick);

    void start();
    void stop();
    void shutdown();

    void runInstruction(Instruction instruction);

    IMovement getMovement();
    ISensors getSensors();
    IController getController();
}
