package group14;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import group14.robot.IRobotManager;
import group14.robot.RobotManager;
import group14.robot.RobotManagerDemo;

public class Container extends AbstractModule {
    private boolean isDemo;

    public Container(boolean isDemo) {
        this.isDemo = isDemo;
    }

    @Override
    protected void configure() {
        if (this.isDemo) {
            this.bind(IRobotManager.class).to(RobotManagerDemo.class).in(Singleton.class);
        } else {
            this.bind(IRobotManager.class).to(RobotManager.class).in(Singleton.class);
        }
    }
}
