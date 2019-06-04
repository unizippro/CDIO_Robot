package group14;

import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.io.InputStream;

public class SceneManager {
    private static SceneManager instance;

    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }

        return instance;
    }


    private Injector injector = Guice.createInjector(new Container());
    private Scene scene;

    private SceneManager() { }

    public Scene getScene() {
        return this.scene;
    }

    public void setRoot(InputStream fxmlStream) {
        try {
            Parent root = this.createLoader().load(fxmlStream);

            if (this.scene == null) {
                this.scene = new Scene(root);
            } else {
                this.scene.setRoot(root);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private FXMLLoader createLoader() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(this.injector::getInstance);

        return fxmlLoader;
    }
}
