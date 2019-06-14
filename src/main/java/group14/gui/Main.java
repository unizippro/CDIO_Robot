package group14.gui;

import com.google.inject.Inject;
import group14.Application;
import group14.Resources;
import group14.gui.components.CoordinateSystem;
import group14.opencv.CalibratedCamera;
import group14.opencv.detectors.ball_detector.BallDetector;
import group14.opencv.detectors.board_detector.BoardDetector;
import group14.opencv.detectors.robot_detector.RobotDetector;
import group14.opencv.utils.ImageConverter;
import group14.robot.IRobotManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.opencv.core.Mat;

import java.util.*;

public class Main {

    private IRobotManager robotManager;

    private CalibratedCamera camera = new CalibratedCamera(0, 7, 9);

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
    public Button imageCalibrateSnapshot;
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
    @FXML
    public ImageView imageThreshBalls;
    @FXML
    public ImageView imageThreshRobotGreen;
    @FXML
    public ImageView imageThreshRobotBlue;
    @FXML
    public Slider rbgMinRedSliderGreen;
    @FXML
    public Slider rbgMaxRedSliderGreen;
    @FXML
    public Slider rbgMinBlueSliderGreen;
    @FXML
    public Slider rbgMaxBlueSliderGreen;
    @FXML
    public Slider rbgMinGreenSliderGreen;
    @FXML
    public Slider rbgMaxGreenSliderGreen;
    @FXML
    public Slider rbgMinRedSliderBlue;
    @FXML
    public Slider rbgMaxRedSliderBlue;
    @FXML
    public Slider rbgMinBlueSliderBlue;
    @FXML
    public Slider rbgMaxBlueSliderBlue;
    @FXML
    public Slider rbgMinGreenSliderBlue;
    @FXML
    public Slider rbgMaxGreenSliderBlue;
    @FXML
    public ImageView imageThreshCorners;
    @FXML
    public Slider rbgMinRedSliderCorners;
    @FXML
    public Slider rbgMaxRedSliderCorners;
    @FXML
    public Slider rbgMinBlueSliderCorners;
    @FXML
    public Slider rbgMaxBlueSliderCorners;
    @FXML
    public Slider rbgMinGreenSliderCorners;
    @FXML
    public Slider rbgMaxGreenSliderCorners;


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

        this.camera.setCalibrationPossibleListener(this::cameraCalibrationChanged);
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

        var robotDetectorConfig = this.robotDetector.getConfig();

        // Color 1 - Blue
        this.rbgMinRedSliderBlue.setValue(robotDetectorConfig.minRedColorBlue.get());
        this.rbgMaxRedSliderBlue.setValue(robotDetectorConfig.maxRedColorBlue.get());
        this.rbgMinGreenSliderBlue.setValue(robotDetectorConfig.minGreenColorBlue.get());
        this.rbgMaxGreenSliderBlue.setValue(robotDetectorConfig.maxGreenColorBlue.get());
        this.rbgMinBlueSliderBlue.setValue(robotDetectorConfig.minBlueColorBlue.get());
        this.rbgMaxBlueSliderBlue.setValue(robotDetectorConfig.maxBlueColorBlue.get());

        // Color 2 - Green
        this.rbgMinRedSliderGreen.setValue(robotDetectorConfig.minRedColorGreen.get());
        this.rbgMaxRedSliderGreen.setValue(robotDetectorConfig.maxRedColorGreen.get());
        this.rbgMinBlueSliderGreen.setValue(robotDetectorConfig.minBlueColorGreen.get());
        this.rbgMaxBlueSliderGreen.setValue(robotDetectorConfig.maxBlueColorGreen.get());
        this.rbgMinGreenSliderGreen.setValue(robotDetectorConfig.minGreenColorGreen.get());
        this.rbgMaxGreenSliderGreen.setValue(robotDetectorConfig.maxGreenColorGreen.get());

        var boardDetectorConfig = this.boardDetector.getConfig();
        this.cornerMarginSlider.setValue(boardDetectorConfig.cornerMarginPercentage.get());
        this.rbgMinRedSliderCorners.setValue(boardDetectorConfig.minRed.get());
        this.rbgMaxRedSliderCorners.setValue(boardDetectorConfig.maxRed.get());
        this.rbgMinBlueSliderCorners.setValue(boardDetectorConfig.minBlue.get());
        this.rbgMaxBlueSliderCorners.setValue(boardDetectorConfig.maxBlue.get());
        this.rbgMinGreenSliderCorners.setValue(boardDetectorConfig.minGreen.get());
        this.rbgMaxGreenSliderCorners.setValue(boardDetectorConfig.maxGreen.get());

        if (Application.openCvLoaded) {
            this.camera.start(this::cameraFrameUpdated);
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
//        this.camera.stop();
//        this.camera.updateWithImage(((FileSelectItem) this.testImages.getValue()).filePath);
    }

    private void cameraFrameUpdated(Mat frame) {
        var image = ImageConverter.matToImageFX(frame);

        var resultBalls = this.ballDetector.run(frame);
        var imageBalls = ImageConverter.matToImageFX(resultBalls.getOutput());

        var resultBoard = this.boardDetector.run(frame);
        var imageBoard = ImageConverter.matToImageFX(resultBoard.getOutput());

        var resultRobot = this.robotDetector.run(frame);
        var imageRobot = ImageConverter.matToImageFX(resultRobot.getOutput());

        var imageBallsThresh = ImageConverter.matToImageFX(resultBalls.getOutputThresh());

        var imageRobotThreshGreen = ImageConverter.matToImageFX(resultRobot.getOutputThreshGreen());
        var imageRobotThreshBlue = ImageConverter.matToImageFX(resultRobot.getOutputThreshBlue());

        var imageThreshCorners = this.cameraController.matToImageFX(resultBoard.getBgrThresh());

        Platform.runLater(() -> {
            this.image.setImage(image);
            this.imageBalls.setImage(imageBalls);
            this.imageBoard.setImage(imageBoard);
            this.imageRobot.setImage(imageRobot);
            this.imageThreshBalls.setImage(imageBallsThresh);
            this.imageThreshRobotGreen.setImage(imageRobotThreshGreen);
            this.imageThreshRobotBlue.setImage(imageRobotThreshBlue);
            this.imageThreshCorners.setImage(imageThreshCorners);

        });
    }

    private void cameraCalibrationChanged(boolean canCalibrate) {
        this.imageCalibrateSnapshot.setDisable(! canCalibrate);
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

        var robotDetector = this.robotDetector.getConfig();
        //Blue color sliders
        robotDetector.minRedColorBlue.set((int) this.rbgMinRedSliderBlue.getValue());
        robotDetector.minGreenColorBlue.set((int) this.rbgMinGreenSliderBlue.getValue());
        robotDetector.minBlueColorBlue.set((int) this.rbgMinBlueSliderBlue.getValue());
        robotDetector.maxRedColorBlue.set((int) this.rbgMaxRedSliderBlue.getValue());
        robotDetector.maxGreenColorBlue.set((int) this.rbgMaxGreenSliderBlue.getValue());
        robotDetector.maxBlueColorBlue.set((int) this.rbgMaxBlueSliderBlue.getValue());

        //Green color sliders
        robotDetector.minRedColorGreen.set((int) this.rbgMinRedSliderGreen.getValue());
        robotDetector.minGreenColorGreen.set((int) this.rbgMinGreenSliderGreen.getValue());
        robotDetector.minBlueColorGreen.set((int) this.rbgMinBlueSliderGreen.getValue());
        robotDetector.maxRedColorGreen.set((int) this.rbgMaxRedSliderGreen.getValue());
        robotDetector.maxGreenColorGreen.set((int) this.rbgMaxGreenSliderGreen.getValue());
        robotDetector.maxBlueColorGreen.set((int) this.rbgMaxBlueSliderGreen.getValue());

    }

    @FXML
    public void takeCalibrationSnapshot(MouseEvent mouseEvent) {
        this.camera.saveCalibrationData();
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
