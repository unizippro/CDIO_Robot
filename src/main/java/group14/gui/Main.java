package group14.gui;

import com.google.inject.Inject;
import group14.robot.IRobotManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;

import java.util.Timer;
import java.util.TimerTask;

public class Main {

    private IRobotManager robotManager;

    @Inject
    public Main(IRobotManager robotManager) {
        this.robotManager = robotManager;
    }

    private Timer timer = new Timer();
    private TimerTask updateDistance = new TimerTask() {
        public void run() {
            try {
                double percentage = robotManager.getSensors().getRange();
                if (Double.isInfinite(percentage)) {
                    Platform.runLater(() -> distanceLabel.setText("∞"));
                } else {
                    final double range = (90 * percentage) / 100;
                    Platform.runLater(() -> distanceLabel.setText(range + "cm"));
                }
            } catch (Exception exception) {
                exception.printStackTrace();
                timer.cancel();
            }
        }
    };

    @FXML
    private ToggleButton toggleStartStop;

    @FXML
    private Slider speedSlider;

    @FXML
    private Label currentSpeedValue;

    @FXML
    private Label distanceLabel;

    @FXML
    public void initialize() {
        this.timer.schedule(this.updateDistance, 0, 1000);
    }


    @FXML
    public void toggleButton(ActionEvent event) {
        toggleStartStop.setText("Foo");
    }


    @FXML
    public void updateSpeedSlider() throws Exception {
        int value = (int) this.speedSlider.getValue();
        this.speedSlider.setValue(value);
        this.currentSpeedValue.setText(value + "%");

        this.robotManager.getMovement().setSpeedPercentage(value);
    }


    @FXML
    public void onForwardClick() throws Exception {
        this.robotManager.getMovement().forward();
    }


    @FXML
    public void onBackwardClick() throws Exception {
        this.robotManager.getMovement().backward();
    }
    @FXML
    public void onLeftClick() throws Exception {
        this.robotManager.getMovement().turn(-15);
    }
    @FXML
    public void onRightClick() throws Exception {
        this.robotManager.getMovement().turn(15);
    }
    @FXML
    public void onStopClick() throws Exception {
        this.robotManager.getMovement().stop();
    }

    @FXML
    public void onShutdownClick() throws Exception {
        this.robotManager.shutdown();
    }


}
