package group14;


import group14.robot.RobotManager;
import robot.rmi_interfaces.IController;

public class HardwareTest {


    public static void main(String[] args) {
        IController cont = new RobotManager().getController();
        int delaytime = 5000;

        try {
            //cont.fanOn();
            System.out.println("Start:");
            System.out.println("Gate: " + cont.gateIsOpen() +"\n\tFan: "+ cont.getFanStatus() + "\n\tVibe: " + cont.getVibStatus());
//            Thread.sleep(delaytime);
//            cont.fanOff();
//            cont.gateOpen();
//            cont.vibOn();
//            System.out.println("Gate: " + cont.gateIsOpen() +"\n\tFan: "+ cont.getFanStatus() + "\n\tVibe: " + cont.getVibStatus());
//            Thread.sleep(delaytime);
//
//            cont.gateClose();
//            cont.vibOff();
            cont.gateOpen();
            System.out.println("Gate: " + cont.gateIsOpen() +"\n\tFan: "+ cont.getFanStatus() + "\n\tVibe: " + cont.getVibStatus());
            //cont.fanOn();
            Thread.sleep(delaytime);
            //cont.fanOff();
            cont.gateClose();
            System.out.println("Gate: " + cont.gateIsOpen() +"\n\tFan: "+ cont.getFanStatus() + "\n\tVibe: " + cont.getVibStatus());

        } catch (Exception e) {
            e.printStackTrace();
        }




    }
}
