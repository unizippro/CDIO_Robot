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
import group14.robot.data.Instruction;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.opencv.core.Mat;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
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
    private ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> currentFuture;

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
    public Slider ballDECircle;
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
    public Slider cornerMarginSlider;
    @FXML
    public ImageView imageRobot;
    @FXML
    public ImageView imageBallsHSV;
    @FXML
    public Slider ballMinH;
    @FXML
    public Slider ballMaxH;
    @FXML
    public Slider ballMinS;
    @FXML
    public Slider ballMaxS;
    @FXML
    public Slider ballMinV;
    @FXML
    public Slider ballMaxV;
    @FXML
    public ImageView imageRobotGreenHSV;
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
    public ImageView imageRobotBlueHSV;
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
        this.ballMinH.setValue(ballDetectorConfig.ballMinH.get());
        this.ballMinS.setValue(ballDetectorConfig.ballMinS.get());
        this.ballMinV.setValue(ballDetectorConfig.ballMinV.get());
        this.ballMaxH.setValue(ballDetectorConfig.ballMaxH.get());
        this.ballMaxS.setValue(ballDetectorConfig.ballMaxS.get());
        this.ballMaxV.setValue(ballDetectorConfig.ballMaxV.get());

        this.ballDECircle.setValue(ballDetectorConfig.ballDECircle.get());
        this.ballThreshold2.setValue(ballDetectorConfig.ballThreshold2.get());
        this.ballGausBlurSize.setValue(ballDetectorConfig.ballGausBlurSize.get());
        //this.lowerThresholdSlider.setValue(ballDetectorConfig.lowerThreshold.get());
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

        this.robotDetector.setPixelsPrCm(this.homeography.getPixelsPrCm());

        var resultBalls = this.ballDetector.run(frame);
        var imageBalls = ImageConverter.matToImageFX(resultBalls.getOutput());

        var resultBoard = this.boardDetector.run(frame);
        var imageBoard = ImageConverter.matToImageFX(resultBoard.getOutput());

        var resultRobot = this.robotDetector.run(frame);
        var imageRobot = ImageConverter.matToImageFX(resultRobot.getOutput());
        //var imageGreenRobot = ImageConverter.matToImageFX(resultRobot.getOutput());

        var imageBallsHSV = ImageConverter.matToImageFX(resultBalls.getOutputThresh());

        var imageRobotGreenHSV = ImageConverter.matToImageFX(resultRobot.getOutputThreshGreen());
        var imageRobotBlueHSV = ImageConverter.matToImageFX(resultRobot.getOutputThreshBlue());

        var imageThreshCorners = ImageConverter.matToImageFX(resultBoard.getBgrThresh());


        Platform.runLater(() -> {
            this.image.setImage(image);
            this.imageBalls.setImage(imageBalls);
            this.imageBoard.setImage(imageBoard);
            this.imageRobot.setImage(imageRobot);
            this.imageBallsHSV.setImage(imageBallsHSV);
            this.imageRobotGreenHSV.setImage(imageRobotGreenHSV);
            this.imageRobotBlueHSV.setImage(imageRobotBlueHSV);
            this.imageThreshCorners.setImage(imageThreshCorners);

        });

        if (this.camera.isCalibrated()) {
            var robotFront = resultRobot.getPointFront();
            var robotRear = resultRobot.getPointBack();
            if (robotFront == null || robotRear == null) {
                System.err.println("Robot position cannot be detected");
                return;
            }

            var corners = resultBoard.getCorners();
            var cross = resultBoard.getCross();

            if (! this.isInitialized) {
                if (corners.size() != 4) {
                    System.err.println("No board detected");
                    return;
                }

                if (cross == null) {
                    System.err.println("No cross detected");
                    return;
                }

                var boardPoints = Arrays.asList(corners.get(0), corners.get(3));
                var boardRect = Utils.createRectangleFromPoints(Utils.toNavigatorPoints(boardPoints, this.homeography.getPixelsPrCm()));

                var crossCenter = Utils.toNavigatorRectangle(cross, this.homeography.getPixelsPrCm());

                var board = new Board(boardRect, 2, crossCenter, 22);
                var robot = new Robot(Utils.toNavigatorPoint(robotFront, this.homeography.getPixelsPrCm()), Utils.toNavigatorPoint(robotRear, this.homeography.getPixelsPrCm()));

                System.out.println(board);

                this.navigator = new Navigator(board, robot);
                this.navigatorDrawing = new NavigatorDrawing(this.navigator, this.homeography.getPixelsPrCm());

                this.isInitialized = true;
            }

            this.navigator.updateRobotPosition(Utils.toNavigatorPoint(robotFront, this.homeography.getPixelsPrCm()), Utils.toNavigatorPoint(robotRear, this.homeography.getPixelsPrCm()));
            this.navigator.updateBallPositions(Utils.toNavigatorPoints(resultBalls.getBalls(), this.homeography.getPixelsPrCm()));

            if (corners.size() == 4 && cross != null) {
                var boardPoints = Arrays.asList(corners.get(0), corners.get(3));
                var boardRect = Utils.createRectangleFromPoints(Utils.toNavigatorPoints(boardPoints, this.homeography.getPixelsPrCm()));

                var crossCenter = Utils.toNavigatorRectangle(cross, this.homeography.getPixelsPrCm());

                this.navigator.updateBoard(boardRect, crossCenter);
            }

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

    @FXML
    public void ballDetectorConfigUpdated(MouseEvent mouseEvent) {
        var ballDetectorConfig = this.ballDetector.getConfig();
        ballDetectorConfig.ballDECircle.set((int) this.ballDECircle.getValue());
        ballDetectorConfig.ballGausBlurSize.set((int) this.ballGausBlurSize.getValue());
        ballDetectorConfig.ballThreshold2.set((int) this.ballThreshold2.getValue());
        //ballDetectorConfig.lowerThreshold.set((int) this.lowerThresholdSlider.getValue());
        ballDetectorConfig.houghParam1.set((int) this.houghParam1.getValue());
        ballDetectorConfig.houghParam2.set((int) this.houghParam2.getValue());
        ballDetectorConfig.ballMinH.set((int) this.ballMinH.getValue());
        ballDetectorConfig.ballMinS.set((int) this.ballMinS.getValue());
        ballDetectorConfig.ballMinV.set((int) this.ballMinV.getValue());
        ballDetectorConfig.ballMaxH.set((int) this.ballMaxH.getValue());
        ballDetectorConfig.ballMaxS.set((int) this.ballMaxS.getValue());
        ballDetectorConfig.ballMaxV.set((int) this.ballMaxV.getValue());

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
        if (this.runThread != null) {
            this.runThread.interrupt();
            this.runThread = null;
        }

        this.startTimer();
        this.camera.startRecording();

        this.runThread = new Thread(() -> {
            while (! this.navigator.isDone() && ! Thread.currentThread().isInterrupted()) {
                try {
                    this.robotManager.getController().fanOn();

                    var instructionSet = this.navigator.calculateInstructionSet();
                    System.out.println(instructionSet);
                    instructionSet.run(this.robotManager::runInstruction);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            try {
                this.robotManager.getController().fanOff();
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            this.stopTimer();
            this.camera.stopRecording();

            System.out.println("Robot done!");
        });

        this.runThread.start();
    }

    public void stopRobotRun(MouseEvent mouseEvent) {
        if (this.runThread != null) {
            this.runThread.interrupt();
            this.runThread = null;
        }

        this.stopTimer();
        this.camera.stopRecording();

        new Thread(() -> {
            try {
                this.robotManager.getController().fanOff();
                this.robotManager.getController().deposit();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void startTimer() {
        this.stopTimer();
        this.stopwatch = Stopwatch.createStarted();

        this.currentFuture = this.exec.scheduleAtFixedRate(() -> {
            var min = this.stopwatch.elapsed(TimeUnit.MINUTES);
            var sec = this.stopwatch.elapsed(TimeUnit.SECONDS) % 60;
            var milli = this.stopwatch.elapsed(TimeUnit.MILLISECONDS) % 1000;

            Platform.runLater(() -> this.timer.setText(min + "." + sec + "." + milli));
        }, 0, 100, TimeUnit.MILLISECONDS);
    }

    private void stopTimer() {
        if (this.stopwatch != null) {
            this.stopwatch.stop();
            this.stopwatch = null;
        }

        if (this.currentFuture != null) {
            this.currentFuture.cancel(true);
            this.currentFuture = null;
        }
    }
}
