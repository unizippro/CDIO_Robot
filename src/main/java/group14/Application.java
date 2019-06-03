package group14;

import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lejos.hardware.BrickFinder;
import lejos.hardware.BrickInfo;
import robot.rmi_interfaces.IMovement;
import robot.rmi_interfaces.IRobot;
import robot.rmi_interfaces.ISensors;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.Naming;


public class Application extends javafx.application.Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Injector injector = Guice.createInjector(new Container());

        FXMLLoader fxmlLoader = new FXMLLoader();

        fxmlLoader.setControllerFactory(injector::getInstance);

        try (InputStream main = ClassLoader.getSystemResourceAsStream("group14/gui/main.fxml")) {
            assert main != null;
            Parent root = fxmlLoader.load(main);

            setUserAgentStylesheet(STYLESHEET_MODENA);

            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("CDIO Robot GUI");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }


    public static String getBrickAddress() throws RuntimeException {
        BrickInfo[] bricks = BrickFinder.discover();
        if (bricks.length == 0) {
            throw new RuntimeException("No bricks on network");
        }

        return bricks[0].getIPAddress();
    }
}
