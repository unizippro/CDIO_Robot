package group14;

import javafx.stage.Stage;
import org.opencv.core.Core;

import java.io.IOException;
import java.io.InputStream;


public class Application extends javafx.application.Application {
    public static boolean openCvLoaded = false;

    static {
        try {
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            openCvLoaded = true;
        } catch (UnsatisfiedLinkError error) {
            System.out.println("OpenCV is not accessible");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        boolean isDemo = Boolean.parseBoolean(System.getProperty("app.demo", "false"));

        SceneManager.initialize(new Container(isDemo));

        try (InputStream main = ClassLoader.getSystemResourceAsStream("group14/gui/BrickSelector.fxml")) {
            assert main != null;
            SceneManager.getInstance().setRoot(main);

            setUserAgentStylesheet(STYLESHEET_MODENA);

            primaryStage.setScene(SceneManager.getInstance().getScene());
            primaryStage.setTitle("CDIO Robot GUI");
            primaryStage.setHeight(740);
            primaryStage.setWidth(968);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
