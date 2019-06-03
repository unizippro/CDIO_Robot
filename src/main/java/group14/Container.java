package group14;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import group14.robot.Movement;
import group14.robot.Robot;
import group14.robot.Sensor;
import robot.rmi_interfaces.IMovement;
import robot.rmi_interfaces.IRobot;
import robot.rmi_interfaces.ISensors;

public class Container extends AbstractModule {
    @Override
    protected void configure() {
        this.bind(IRobot.class).to(Robot.class).in(Singleton.class);
        this.bind(IMovement.class).to(Movement.class).in(Singleton.class);
        this.bind(ISensors.class).to(Sensor.class).in(Singleton.class);
    }
}
