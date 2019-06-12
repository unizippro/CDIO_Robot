package group14;


import group14.robot.RobotManager;
import robot.rmi_interfaces.IController;
import robot.rmi_interfaces.IMovement;

public class HardwareTest {


    public static void main(String[] args) {
        IController cont = new RobotManager().getController();
        IMovement movement = new RobotManager().getMovement();
        int delaytime = 7500;

        try {
            //cont.fanOn();
            System.out.println("Start:");
            //System.out.println("Gate: " + cont.gateIsOpen() +"\n\tFan: "+ cont.getFanStatus() + "\n\tVibe: " + cont.getVibStatus());
            //Thread.sleep(delaytime);
            //cont.fanOff();
            //Thread.sleep(delaytime/4);
            for (int i = 0; i < 1 ; i++) {
                movement.forward(1000);
                Thread.sleep(200);
                //movement.backward(1000);
                Thread.sleep(200);
            }


            /*
            cont.gateOpen();
            cont.vibOn();
            System.out.println("Gate: " + cont.gateIsOpen() +"\n\tFan: "+ cont.getFanStatus() + "\n\tVibe: " + cont.getVibStatus());

            Thread.sleep(delaytime/4);
            cont.vibOff();
            Thread.sleep(delaytime/2);
            cont.vibOn();
            Thread.sleep(delaytime/4);
            cont.vibOff();
            Thread.sleep(delaytime/2);


            cont.vibOff();
            cont.gateClose();
            System.out.println("Gate: " + cont.gateIsOpen() +"\n\tFan: "+ cont.getFanStatus() + "\n\tVibe: " + cont.getVibStatus());
            */
        } catch (Exception e) {
            e.printStackTrace();
        }




    }
}
