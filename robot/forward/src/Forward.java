import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;

public class Forward {
    public static void main(String[] args) throws InterruptedException {
        EV3LargeRegulatedMotor ev3LargeRegulatedMotor = new EV3LargeRegulatedMotor(MotorPort.A);

        System.out.println("Forward");

        ev3LargeRegulatedMotor.setSpeed(ev3LargeRegulatedMotor.getMaxSpeed());
        ev3LargeRegulatedMotor.forward();

        Thread.sleep(5000);

        System.out.println("Done");
        ev3LargeRegulatedMotor.close();
    }
}
