package group14.gui;

import com.google.inject.Inject;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import robot.rmi_interfaces.IMovement;
import robot.rmi_interfaces.IRobot;
import robot.rmi_interfaces.ISensors;

import java.util.Timer;
import java.util.TimerTask;

public class Main {

    private IRobot robot;
    private ISensors sensors;
    private IMovement movement;

    @Inject
    public Main(IRobot robot, ISensors sensors, IMovement movement) {
        this.robot = robot;
        this.sensors = sensors;
        this.movement = movement;
    }

    private Timer timer = new Timer();
    private TimerTask updateDistance = new TimerTask() {
        public void run() {
            try {
                double percentage = sensors.getRange();
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

        this.movement.setSpeedPercentage(value);
    }


    @FXML
    public void onForwardClick() throws Exception {
        this.movement.forward();
    }


    @FXML
    public void onBackwardClick() throws Exception {
        this.movement.backward();
    }
    @FXML
    public void onLeftClick() throws Exception {
        this.movement.turn(-15);
    }
    @FXML
    public void onRightClick() throws Exception {
        this.movement.turn(15);
    }
    @FXML
    public void onStopClick() throws Exception {
        this.movement.stop();
    }

    @FXML
    public void onShutdownClick() throws Exception {
        this.robot.shutdown();
    }


}
