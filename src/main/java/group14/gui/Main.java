package group14.gui;

import group14.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;

import java.util.Timer;
import java.util.TimerTask;

public class Main {

    private Timer timer = new Timer();
    private TimerTask updateDistance = new TimerTask() {
        public void run() {
            try {
                double percentage = Application.getSensors().getRange();
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

        Application.getMovement().setSpeedPercentage(value);
    }


    @FXML
    public void onForwardClick() throws Exception {
        Application.getMovement().forward();
    }


    @FXML
    public void onBackwardClick() throws Exception {
        Application.getMovement().backward();
    }
    @FXML
    public void onLeftClick() throws Exception {
        Application.getMovement().turn(-15);
    }
    @FXML
    public void onRightClick() throws Exception {
        Application.getMovement().turn(15);
    }
    @FXML
    public void onStopClick() throws Exception {
        Application.getMovement().stop();
    }

    @FXML
    public void onShutdownClick() throws Exception {
        Application.getRobot().shutdown();
    }


}
