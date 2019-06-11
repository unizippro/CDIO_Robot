package group14;


import group14.robot.RobotManager;
import robot.rmi_interfaces.IController;

public class HardwareTest {


    public static void main(String[] args) {
        IController cont = new RobotManager().getController();
        int delaytime = 7500;

        try {
            cont.fanOn();
            System.out.println("Start:");
            System.out.println("Gate: " + cont.gateIsOpen() +"\n\tFan: "+ cont.getFanStatus() + "\n\tVibe: " + cont.getVibStatus());
            Thread.sleep(delaytime);
            cont.fanOff();
            Thread.sleep(delaytime/2);

            cont.vibOn();
            cont.gateOpen();
            System.out.println("Gate: " + cont.gateIsOpen() +"\n\tFan: "+ cont.getFanStatus() + "\n\tVibe: " + cont.getVibStatus());

            Thread.sleep(delaytime/2);
            cont.vibOff();
            Thread.sleep(delaytime/2);
            cont.vibOn();
            Thread.sleep(delaytime/2);


            cont.vibOff();
            cont.gateClose();
            System.out.println("Gate: " + cont.gateIsOpen() +"\n\tFan: "+ cont.getFanStatus() + "\n\tVibe: " + cont.getVibStatus());

        } catch (Exception e) {
            e.printStackTrace();
        }




    }
}
