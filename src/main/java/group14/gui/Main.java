package group14.gui;

import com.google.common.base.Stopwatch;
import com.google.inject.Inject;
import group14.Application;
import group14.gui.components.CoordinateSystem;
import group14.navigator.Navigator;
import group14.navigator.NavigatorDrawing;
import group14.navigator.Utils;
import group14.navigator.data.Board;
import group14.opencv.CalibratedCamera;
import group14.opencv.Homeography;
import group14.opencv.detectors.ball_detector.BallDetector;
import group14.opencv.detectors.board_detector.BoardDetector;
import group14.opencv.detectors.robot_detector.RobotDetector;
import group14.opencv.utils.ImageConverter;
import group14.robot.IRobotManager;
import group14.robot.Robot;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.opencv.core.Mat;
import org.opencv.core.Point;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Main {

    private IRobotManager robotManager;

    private BallDetector ballDetector = new BallDetector();
    private BoardDetector boardDetector = new BoardDetector();
    private RobotDetector robotDetector = new RobotDetector();
    private Homeography homeography = new Homeography(this.boardDetector);

    private Navigator navigator;
    private NavigatorDrawing navigatorDrawing;
    private boolean isInitialized = false;
    private Thread runThread;
    private Stopwatch stopwatch;

    private CalibratedCamera camera = new CalibratedCamera(1, 7, 9);


    @FXML
    private Label timer;
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
    public ImageView imageNavigator;
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
        this.camera.setCalibrationCustomHandler(this.homeography);
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

        this.timer.setText(this.time());
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

        if (this.camera.isCalibrated()) {
            if (! this.isInitialized) {
                var corners = resultBoard.getCorners();
                if (corners.size() != 4) {
                    return;
                }

                var boardPoints = Arrays.asList(corners.get(0), corners.get(3));
                var boardRect = Utils.createRectangleFromPoints(Utils.toNavigatorPoints(boardPoints, this.homeography.getPixelsPrCm()));

                var cross = resultBoard.getCross();
                if (cross == null) {
                    System.out.println("No cross detected");
                    return;
                }
                var crossCenter = Utils.toNavigatorPoint(new Point(cross.x + cross.width / 2, cross.y + cross.height / 2), this.homeography.getPixelsPrCm());

                var board = new Board(boardRect, 2.5, crossCenter, 22);
                var robot = new Robot(Utils.toNavigatorPoint(resultRobot.getPointFront(), this.homeography.getPixelsPrCm()), Utils.toNavigatorPoint(resultRobot.getPointBack(), this.homeography.getPixelsPrCm()));

                System.out.println(board);

                this.navigator = new Navigator(board, robot);
                this.navigatorDrawing = new NavigatorDrawing(this.navigator, this.homeography.getPixelsPrCm());

                this.isInitialized = true;
            }

            this.navigator.updateRobotPosition(Utils.toNavigatorPoint(resultRobot.getPointFront(), this.homeography.getPixelsPrCm()), Utils.toNavigatorPoint(resultRobot.getPointBack(), this.homeography.getPixelsPrCm()));
            this.navigator.updateBallPositions(Utils.toNavigatorPoints(resultBalls.getBalls(), this.homeography.getPixelsPrCm()));

            var imageNavigator = ImageConverter.matToImageFX(this.navigatorDrawing.drawOn(frame));
            Platform.runLater(() -> this.imageNavigator.setImage(imageNavigator));
        }
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

    private String time() {
        try {
            return String.valueOf(this.stopwatch.elapsed(TimeUnit.SECONDS));
        } catch (Exception e) {
            return "";
        }
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
        this.stopwatch = Stopwatch.createStarted();
        if (this.runThread != null) {
            this.runThread.interrupt();
            this.runThread = null;
        }

        this.runThread = new Thread(() -> {
            try {
                this.robotManager.getController().fanOn();
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            while (! this.navigator.isEmpty() && ! Thread.currentThread().isInterrupted()) {
                try {
                    var instructionSet = this.navigator.calculateInstructionSet();
                    System.out.println(instructionSet);

                    instructionSet.run(instruction -> {
                        try {
                            this.robotManager.getMovement().runInstruction(instruction);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            try {
                this.robotManager.getController().fanOff();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });

        this.stopwatch.stop();
        this.runThread.start();
    }

    public void stopRobotRun(MouseEvent mouseEvent) {
        if (this.runThread != null) {
            this.runThread.interrupt();
            this.runThread = null;
        }

        new Thread(() -> {
            try {
                this.robotManager.getController().fanOff();
                this.robotManager.getController().deposit();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
