package group14;


import group14.robot.RobotManager;
import group14.robot.interfaces.IController;
import group14.robot.interfaces.IMovement;
import group14.robot.interfaces.IRobot;

public class HardwareTest {


    public static void main(String[] args) {
        var robotManager = new RobotManager();

        robotManager.setBrick(robotManager.getBricksOnNetwork()[0]);

        IController cont = robotManager.getController();
        IMovement movement = robotManager.getMovement();
        int delaytime = 7500;

        try {
            //SOUND HERE: Sound.playSample(new File("/home/lejos/programs/Basken 2017.wav"), 100);

            //cont.fanOn();
            System.out.println("Start: ");

            robotManager.playSound("/home/lejos/sound/R2_beeping.wav");
            System.out.println("End.");

            //cont.gateClose();
            //System.out.println("Gate: " + cont.gateIsOpen() +"\n\tFan: "+ cont.getFanStatus() + "\n\tVibe: " + cont.getVibStatus());
            //Thread.sleep(delaytime);
            //cont.fanOff();
            //Thread.sleep(delaytime/4);
            /*for (int i = 0; i < 1 ; i++) {
                movement.forward(1000);
                Thread.sleep(200);
                //movement.backward(1000);
                Thread.sleep(200);
            }*/


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
