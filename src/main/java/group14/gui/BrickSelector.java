package group14.gui;

import com.google.inject.Inject;
import group14.SceneManager;
import group14.robot.IRobotManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import lejos.hardware.BrickInfo;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class BrickSelector {
    @FXML
    public ChoiceBox brickSelectBox;
    @FXML
    public Button brickSelectConfirm;

    private IRobotManager robotManager;

    private ScheduledFuture<?> searchNetworkTask;


    @Inject
    public BrickSelector(IRobotManager robotManager) {
        this.robotManager = robotManager;
    }

    @FXML
    public void initialize() {
        this.searchNetworkTask = Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            if (this.brickSelectBox.getValue() == null) {
                var bricks = this.robotManager.getBricksOnNetwork();
                Platform.runLater(() -> this.setBricks(bricks));
            }
        }, 0, 5, TimeUnit.SECONDS);
    }

    @FXML
    public void selectBrick() {
        BrickInfoSelectItem brick = (BrickInfoSelectItem) this.brickSelectBox.getValue();

        if (brick != null) {
            System.out.println("Selecting brick: " + brick.toString());
            this.robotManager.setBrick(brick.brick);
            this.searchNetworkTask.cancel(true);
            this.searchNetworkTask = null;

            SceneManager.getInstance().setRoot(ClassLoader.getSystemResourceAsStream("group14/gui/main.fxml"));
        }
    }

    private void setBricks(BrickInfo[] bricks) {
        ArrayList<BrickInfoSelectItem> brickNames = new ArrayList<>();

        for (BrickInfo brick : bricks) {
            brickNames.add(new BrickInfoSelectItem(brick));
        }

        this.brickSelectBox.setItems(FXCollections.observableArrayList(brickNames));
    }


    private class BrickInfoSelectItem {
        private BrickInfo brick;

        private BrickInfoSelectItem(BrickInfo brick) {
            this.brick = brick;
        }

        @Override
        public String toString() {
            return this.brick.getName() + " (" + this.brick.getIPAddress() + ")";
        }
    }
}
