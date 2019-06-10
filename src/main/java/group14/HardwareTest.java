package group14;


import group14.robot.RobotManager;
import robot.rmi_interfaces.IController;

public class HardwareTest {


    public static void main(String[] args) {
        IController cont = new RobotManager().getController();

        try {
            cont.fanOn();
            Thread.sleep(2000);
            cont.fanOff();
            System.out.println("Start gate is open = " + cont.gateIsOpen());
            cont.gateOpen();
            cont.vibOn();
            System.out.println("Gate is open now = " + cont.gateIsOpen());
            Thread.sleep(2000);

            cont.gateClose();
            cont.vibOff();
            System.out.println("Gate is open now = " + cont.gateIsOpen());

        } catch (Exception e) {
            e.printStackTrace();
        }




    }
}
