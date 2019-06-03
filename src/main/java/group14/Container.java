package group14;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import group14.robot.IRobotManager;
import group14.robot.RobotManager;

public class Container extends AbstractModule {
    @Override
    protected void configure() {
        this.bind(IRobotManager.class).to(RobotManager.class).in(Singleton.class);
    }
}
