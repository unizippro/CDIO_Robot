package group14.gui;

import com.google.inject.Inject;
import group14.gui.components.CoordinateSystem;
import group14.robot.IRobotManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;

import java.util.*;

public class Main {

    private IRobotManager robotManager;


    @FXML
    private Slider speedSlider;
    @FXML
    private Label currentSpeedValue;
    @FXML
    private Label distanceLabel;
    @FXML
    public CoordinateSystem plot;


//    private Timer timer = new Timer();
//    private TimerTask updateDistance = new TimerTask() {
//        public void run() {
//            try {
//                double percentage = robotManager.getSensors().getRange();
//                if (Double.isInfinite(percentage)) {
//                    Platform.runLater(() -> distanceLabel.setText("∞"));
//                } else {
//                    final double range = (90 * percentage) / 100;
//                    Platform.runLater(() -> distanceLabel.setText(range + "cm"));
//                }
//            } catch (Exception exception) {
//                exception.printStackTrace();
//                timer.cancel();
//            }
//        }
//    };


    @Inject
    public Main(IRobotManager robotManager) {
        this.robotManager = robotManager;
    }


    @FXML
    public void initialize() {
//        this.timer.schedule(this.updateDistance, 0, 1000);

        this.setPoints(this.generateCoordinates());

        this.plot.setRobot(new Point2D(5, 16));
        this.plot.setCross(new Point2D(20, 10));
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
        Platform.exit();
    }

    @FXML
    public void onFanToggled(ActionEvent actionEvent) {
        ToggleButton button = (ToggleButton) actionEvent.getTarget();

        button.setText(button.isSelected() ? "Fan on" : "Fan off");
    }

    public void setPoints(List<Point2D> points) {
        this.plot.clearPoints();
        points.forEach(coordinate -> this.plot.setPoint(coordinate));
    }


    private List<Point2D> generateCoordinates() {
        Random random = new Random();
        List<Point2D> coordinates = new ArrayList<>();

        for (int i = 0; i <= 20; i++) {
            coordinates.add(new Point2D(random.nextInt(40), random.nextInt(20)));
        }

        return coordinates;
    }
}
