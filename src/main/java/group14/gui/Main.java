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
import group14.road_planner.RoadController;
import group14.road_planner.board.SmartConverter;
import group14.robot.IRobotManager;
import group14.robot.data.Instruction;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.opencv.core.Mat;

import java.awt.Point;
import java.rmi.RemoteException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    private IRobotManager robotManager;

    private CalibratedCamera camera = new CalibratedCamera(1, 7, 9);

    private BallDetector ballDetector = new BallDetector();
    private BoardDetector boardDetector = new BoardDetector();
    private RobotDetector robotDetector = new RobotDetector();
    private RoadController roadController = new RoadController();
    private boolean isInitialized = false;


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
    public Slider ballThreshold1;
    @FXML
    public Slider ballThreshold2;
    @FXML
    public Slider ballGausBlurSize;
    @FXML
    public Slider lowerThresholdSlider;
    @FXML
    public Slider houghParam1;
    @FXML
    public Slider houghParam2;
    @FXML
    public ImageView imageBoard;
    @FXML
    public Slider robotBlueMinH;
    @FXML
    public Slider robotBlueMinS;
    @FXML
    public Slider robotBlueMinV;
    @FXML
    public Slider robotBlueMaxH;
    @FXML
    public Slider robotBlueMaxS;
    @FXML
    public Slider robotBlueMaxV;
    @FXML
    public Slider robotGreenMinH;
    @FXML
    public Slider robotGreenMinS;
    @FXML
    public Slider robotGreenMinV;
    @FXML
    public Slider robotGreenMaxH;
    @FXML
    public Slider robotGreenMaxS;
    @FXML
    public Slider robotGreenMaxV;
    @FXML
    public Slider cornerMarginSlider;
    @FXML
    public ImageView imageRobot;
    @FXML
    public ImageView imageThreshBalls;
    @FXML
    public ImageView imageRobotGreenHSV;
    @FXML
    public ImageView imageRobotBlueHSV;
    @FXML
    public ImageView imageThreshCorners;
    @FXML
    public Slider minHBoardSliderCorners;
    @FXML
    public Slider maxHBoardSliderCorners;
    @FXML
    public Slider minSBoardSliderCorners;
    @FXML
    public Slider maxSBoardSliderCorners;
    @FXML
    public Slider minVBoardSliderCorners;
    @FXML
    public Slider maxVBoardSliderCorners;
    @FXML
    public Slider blockSizeSlider;
    @FXML
    public Slider kSizeSlider;


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

        //var fileItems = new ArrayList<FileSelectItem>();
        //for (String file : Resources.TestImages.getAllFiles()) {
        //    fileItems.add(new FileSelectItem(file));
        //}

        //this.testImages.setItems(FXCollections.observableArrayList(fileItems));

        this.setPoints(this.generateCoordinates());

        this.plot.setRobot(new Point2D(5, 16));
        this.plot.setCross(new Point2D(20, 10));

        var ballDetectorConfig = this.ballDetector.getConfig();
        this.ballThreshold1.setValue(ballDetectorConfig.ballThreshold1.get());
        this.ballThreshold2.setValue(ballDetectorConfig.ballThreshold2.get());
        this.ballGausBlurSize.setValue(ballDetectorConfig.ballGausBlurSize.get());

        this.lowerThresholdSlider.setValue(ballDetectorConfig.lowerThreshold.get());
        this.houghParam1.setValue(ballDetectorConfig.houghParam1.get());
        this.houghParam2.setValue(ballDetectorConfig.houghParam2.get());

        var robotDetectorConfig = this.robotDetector.getConfig();
        this.robotBlueMinH.setValue(robotDetectorConfig.blueMinH.get());
        this.robotBlueMinS.setValue(robotDetectorConfig.blueMinS.get());
        this.robotBlueMinV.setValue(robotDetectorConfig.blueMinV.get());
        this.robotBlueMaxH.setValue(robotDetectorConfig.blueMaxH.get());
        this.robotBlueMaxS.setValue(robotDetectorConfig.blueMaxS.get());
        this.robotBlueMaxV.setValue(robotDetectorConfig.blueMaxV.get());

        this.robotGreenMinH.setValue(robotDetectorConfig.greenMinH.get());
        this.robotGreenMinS.setValue(robotDetectorConfig.greenMinS.get());
        this.robotGreenMinV.setValue(robotDetectorConfig.greenMinV.get());
        this.robotGreenMaxH.setValue(robotDetectorConfig.greenMaxH.get());
        this.robotGreenMaxS.setValue(robotDetectorConfig.greenMaxS.get());
        this.robotGreenMaxV.setValue(robotDetectorConfig.greenMaxV.get());

        var boardDetectorConfig = this.boardDetector.getConfig();
        this.cornerMarginSlider.setValue(boardDetectorConfig.cornerMarginPercentage.get());
        this.blockSizeSlider.setValue(boardDetectorConfig.blockSize.get());
        this.kSizeSlider.setValue(boardDetectorConfig.kSize.get());
        this.minHBoardSliderCorners.setValue(boardDetectorConfig.minHBoard.get());
        this.maxHBoardSliderCorners.setValue(boardDetectorConfig.maxHBoard.get());
        this.minSBoardSliderCorners.setValue(boardDetectorConfig.minSBoard.get());
        this.maxSBoardSliderCorners.setValue(boardDetectorConfig.maxSBoard.get());
        this.minVBoardSliderCorners.setValue(boardDetectorConfig.minVBoard.get());
        this.maxVBoardSliderCorners.setValue(boardDetectorConfig.maxVBoard.get());


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
        //var imageGreenRobot = ImageConverter.matToImageFX(resultRobot.getOutput());

        var imageBallsThresh = ImageConverter.matToImageFX(resultBalls.getOutputThresh());

        var imageRobotGreenHSV = ImageConverter.matToImageFX(resultRobot.getOutputThreshGreen());
        var imageRobotBlueHSV = ImageConverter.matToImageFX(resultRobot.getOutputThreshBlue());

        var imageThreshCorners = ImageConverter.matToImageFX(resultBoard.getBgrThresh());


        Platform.runLater(() -> {
            this.image.setImage(image);
            this.imageBalls.setImage(imageBalls);
            this.imageBoard.setImage(imageBoard);
            this.imageRobot.setImage(imageRobot);
            this.imageThreshBalls.setImage(imageBallsThresh);
            this.imageRobotGreenHSV.setImage(imageRobotGreenHSV);
            this.imageRobotBlueHSV.setImage(imageRobotBlueHSV);
            this.imageThreshCorners.setImage(imageThreshCorners);

        });

        var balls = resultBalls.getBalls().stream()
                .map(point -> new java.awt.Point((int)point.x, (int)point.y))
                .collect(Collectors.toList());

        var robotPosition = Stream.of(resultRobot.getPointFront(), resultRobot.getPointBack())
                .map(point -> new java.awt.Point((int)point.x, (int)point.y))
                .collect(Collectors.toList());

        if (!isInitialized) {
            List<Point> crossPoints = new ArrayList<>();
            crossPoints.add(new Point(1920/2-70, 1080/2));
            crossPoints.add(new Point(1920/2+70, 1080/2));
            crossPoints.add(new Point(1920/2, 1080/2+70));
            crossPoints.add(new Point(1920/2, 1080/2-70));

            this.roadController.initialize(
                    resultBoard.getCorners().stream().map(point -> new java.awt.Point((int)point.x, (int)point.y)).collect(Collectors.toList()),
                    balls,
                    crossPoints,
                    //resultBoard.getCross().stream().map(point -> new java.awt.Point((int)point.x, (int)point.y)).collect(Collectors.toList()),
                    robotPosition
            );
            new SmartConverter().calculateBoard(resultBoard.getCorners().stream().map(point -> new java.awt.Point((int)point.x, (int)point.y)).collect(Collectors.toList()));
            isInitialized = true;
        }

        this.roadController.updateRobot(robotPosition);
        this.roadController.setBalls(balls);
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
        ballDetectorConfig.ballThreshold1.set((int) this.ballThreshold1.getValue());
        ballDetectorConfig.ballGausBlurSize.set((int) this.ballGausBlurSize.getValue());
        ballDetectorConfig.ballThreshold2.set((int) this.ballThreshold2.getValue());
        ballDetectorConfig.lowerThreshold.set((int) this.lowerThresholdSlider.getValue());
        ballDetectorConfig.houghParam1.set((int) this.houghParam1.getValue());
        ballDetectorConfig.houghParam2.set((int) this.houghParam2.getValue());

        var boardDetectorConfig = this.boardDetector.getConfig();
        boardDetectorConfig.cornerMarginPercentage.set(this.cornerMarginSlider.getValue());
        boardDetectorConfig.blockSize.set((int) this.blockSizeSlider.getValue());
        boardDetectorConfig.kSize.set((int) this.kSizeSlider.getValue());
        boardDetectorConfig.minHBoard.set((int) this.minHBoardSliderCorners.getValue());
        boardDetectorConfig.maxHBoard.set((int) this.maxHBoardSliderCorners.getValue());
        boardDetectorConfig.minSBoard.set((int) this.minSBoardSliderCorners.getValue());
        boardDetectorConfig.maxSBoard.set((int) this.maxSBoardSliderCorners.getValue());
        boardDetectorConfig.minVBoard.set((int) this.minVBoardSliderCorners.getValue());
        boardDetectorConfig.maxVBoard.set((int) this.maxVBoardSliderCorners.getValue());

        var robotDetector = this.robotDetector.getConfig();
        robotDetector.blueMinH.set((int) this.robotBlueMinH.getValue());
        robotDetector.blueMinS.set((int) this.robotBlueMinS.getValue());
        robotDetector.blueMinV.set((int) this.robotBlueMinV.getValue());
        robotDetector.blueMaxH.set((int) this.robotBlueMaxH.getValue());
        robotDetector.blueMaxS.set((int) this.robotBlueMaxS.getValue());
        robotDetector.blueMaxV.set((int) this.robotBlueMaxV.getValue());
        robotDetector.greenMinH.set((int) this.robotGreenMinH.getValue());
        robotDetector.greenMinS.set((int) this.robotGreenMinS.getValue());
        robotDetector.greenMinV.set((int) this.robotGreenMinV.getValue());
        robotDetector.greenMaxH.set((int) this.robotGreenMaxH.getValue());
        robotDetector.greenMaxS.set((int) this.robotGreenMaxS.getValue());
        robotDetector.greenMaxV.set((int) this.robotGreenMaxV.getValue());
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

    @FXML
    private void startRobotRun() {
        new Thread(() -> {
            while(this.roadController.getBalls().size() > -1 ){
                if (this.roadController.readyToDeposit) {
                    try {
                        this.robotManager.getController().fanOff();
                        this.robotManager.getController().deposit();
                        this.roadController.readyToDeposit = false;
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("robot: " + this.roadController.getRobot().getRotationalPoint());

                System.out.println("lowerleft i kvadrant m robot: " + this.roadController.getCurrentQuadrant().getLowerLeft());

                try {
                    Instruction temp = this.roadController.getNextInstruction();
                    Instruction ins = new Instruction(temp.getAngle(), temp.getDistance()/(SmartConverter.getPixelsPerCm())) ;
                    this.robotManager.getMovement().runInstruction(ins);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
}
