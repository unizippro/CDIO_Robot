import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lejos.hardware.BrickFinder;
import lejos.hardware.BrickInfo;
import robot.rmi_interfaces.IMovement;
import robot.rmi_interfaces.IRobot;
import robot.rmi_interfaces.ISensors;

import java.rmi.Naming;


public class Application extends javafx.application.Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("CDIO Robot GUI");

        setUserAgentStylesheet(STYLESHEET_MODENA);

        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("main.fxml"));
        Parent root = loader.load();

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    private static ISensors sensors;
    private static IMovement movement;
    private static IRobot robot;

    public static ISensors getSensors() throws Exception {
        if (sensors == null) {
            sensors = (ISensors) Naming.lookup("rmi://" + getBrickAddress() + ":1199/sensors");
        }

        return sensors;
    }


    public static IMovement getMovement() throws Exception {
        if (movement == null) {
            movement = (IMovement) Naming.lookup("rmi://" + getBrickAddress() + ":1199/movement");
        }

        return movement;
    }


    public static IRobot getRobot() throws Exception {
        if (robot == null) {
            robot = (IRobot) Naming.lookup("rmi://" + getBrickAddress() + ":1199/robot");
        }

        return robot;
    }


    private static String getBrickAddress() throws RuntimeException {
        BrickInfo[] bricks = BrickFinder.discover();
        if (bricks.length == 0) {
            throw new RuntimeException("No bricks on network");
        }

        return bricks[0].getIPAddress();
    }
}
