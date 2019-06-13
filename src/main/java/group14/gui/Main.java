package group14.gui;

import com.google.inject.Inject;
import group14.Application;
import group14.Resources;
import group14.gui.components.CoordinateSystem;
import group14.opencv.ICameraController;
import group14.opencv.detectors.ball_detector.BallDetector;
import group14.opencv.detectors.board_detector.BoardDetector;
import group14.opencv.detectors.robot_detector.RobotDetector;
import group14.robot.IRobotManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.*;

public class Main {

    private IRobotManager robotManager;
    private ICameraController cameraController;

    private BallDetector ballDetector = new BallDetector();
    private BoardDetector boardDetector = new BoardDetector();
    private RobotDetector robotDetector = new RobotDetector();


    @FXML
    private ChoiceBox testImages;
    @FXML
    private Slider speedSlider;
    @FXML
    private Label currentSpeedValue;
    @FXML
    private Label distanceLabel;
    @FXML
    public CoordinateSystem plot;
    @FXML
    public ImageView image;
    @FXML
    public ImageView imageBalls;
    @FXML
    public Slider blurSizeSlider;
    @FXML
    public Slider lowerThresholdSlider;
    @FXML
    public Slider houghParam1;
    @FXML
    public Slider houghParam2;
    @FXML
    public ImageView imageBoard;
    @FXML
    public Slider cornerMarginSlider;
    @FXML
    public ImageView imageRobot;


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
    public Main(IRobotManager robotManager, ICameraController cameraController) {
        this.robotManager = robotManager;
        this.cameraController = cameraController;
        this.cameraController.addUpdateListener(this::cameraControllerUpdated);
    }


    @FXML
    public void initialize() {
//        this.timer.schedule(this.updateDistance, 0, 1000);

        var fileItems = new ArrayList<FileSelectItem>();
        for (String file : Resources.TestImages.getAllFiles()) {
            fileItems.add(new FileSelectItem(file));
        }

        this.testImages.setItems(FXCollections.observableArrayList(fileItems));

        this.setPoints(this.generateCoordinates());

        this.plot.setRobot(new Point2D(5, 16));
        this.plot.setCross(new Point2D(20, 10));

        var ballDetectorConfig = this.ballDetector.getConfig();
        this.blurSizeSlider.setValue(ballDetectorConfig.blurSize.get());
        this.lowerThresholdSlider.setValue(ballDetectorConfig.lowerThreshold.get());
        this.houghParam1.setValue(ballDetectorConfig.houghParam1.get());
        this.houghParam2.setValue(ballDetectorConfig.houghParam2.get());

        var boardDetectorConfig = this.boardDetector.getConfig();
        this.cornerMarginSlider.setValue(boardDetectorConfig.cornerMarginPercentage.get());

        if (Application.openCvLoaded) {
            this.cameraController.start(1, 60);
        }
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

        var controller = this.robotManager.getController();

        try {
            if (button.isSelected()) {
                controller.fanOn();
            } else {
                controller.fanOff();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onTestImageChanged(ActionEvent actionEvent) {
        this.cameraController.stop();
        this.cameraController.updateWithImage(((FileSelectItem) this.testImages.getValue()).filePath);
    }

    private void cameraControllerUpdated() {
        var source = this.cameraController.getSource();
        var imageSource = this.cameraController.getSourceAsImageFX();

        var resultBalls = this.ballDetector.run(source);
        var imageBalls = this.cameraController.matToImageFX(resultBalls.getOutput());

        var resultBoard = this.boardDetector.run(source);
        var imageBoard = this.cameraController.matToImageFX(resultBoard.getOutput());

        var resultRobot = this.robotDetector.run(source);
        var imageRobot = this.cameraController.matToImageFX(resultRobot.getOutput());

        Platform.runLater(() -> {
            this.image.setImage(imageSource);
            this.imageBalls.setImage(imageBalls);
            this.imageBoard.setImage(imageBoard);
            this.imageRobot.setImage(imageRobot);
        });
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

    @FXML
    public void ballDetectorConfigUpdated(MouseEvent mouseEvent) {
        var ballDetectorConfig = this.ballDetector.getConfig();
        ballDetectorConfig.blurSize.set((int) this.blurSizeSlider.getValue());
        ballDetectorConfig.lowerThreshold.set((int) this.lowerThresholdSlider.getValue());
        ballDetectorConfig.houghParam1.set((int) this.houghParam1.getValue());
        ballDetectorConfig.houghParam2.set((int) this.houghParam2.getValue());

        var boardDetectorConfig = this.boardDetector.getConfig();
        boardDetectorConfig.cornerMarginPercentage.set(this.cornerMarginSlider.getValue());
    }


    private class FileSelectItem {
        private String filePath;

        FileSelectItem(String filePath) {
            this.filePath = filePath;
        }

        @Override
        public String toString() {
            var parts = filePath.split("/");

            return parts[parts.length - 1];
        }
    }
}
