package movement_queue;

import network.RobotFinder;
import robot.rmi_interfaces.IController;

public class ControllerController {
    private IController controller;

    ControllerController(){
        this.controller = new RobotFinder().findController();
    }


}
