package group14.opencv;

import com.google.common.base.Stopwatch;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.VideoWriter;
import org.opencv.videoio.Videoio;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class Camera {

    private static final int FPS = 30;
    protected static final Size FRAME_SIZE = new Size(1920, 1080);

    private ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> currentFuture;
    private Thread calculationThread;

    private final VideoCapture camera = new VideoCapture();

    private AtomicBoolean isRecording = new AtomicBoolean(false);
    private AtomicReference<VideoWriter> videoWriter = new AtomicReference<>();
    private AtomicReference<Stopwatch> stopwatch = new AtomicReference<>();

    private final int cameraIndex;

    public interface FrameUpdatedHandler {
        void frameUpdated(Mat frame);
    }


    public Camera(int cameraIndex) {
        this.cameraIndex = cameraIndex;
    }

    public void start(FrameUpdatedHandler updatedHandler) {
        this.openCamera();
        this.currentFuture = this.exec.scheduleAtFixedRate(() -> {
            var frame = new Mat();
            this.camera.read(frame);

            if (frame.empty()) {
                System.err.println("Frame is empty");

                return;
            }

            var videoWriter = this.videoWriter.get();
            var stopwatch = this.stopwatch.get();
            if (this.isRecording.get() && videoWriter != null && stopwatch != null) {
                this.saveFrame(frame, videoWriter, stopwatch);
            }

            if (updatedHandler != null) {
                if (this.calculationThread == null || ! this.calculationThread.isAlive()) {
                    this.calculationThread = new Thread(() -> {
                        try {
                            updatedHandler.frameUpdated(frame);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });

                    this.calculationThread.start();
                }
            }
        }, 0, 1000 / FPS, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        if (this.currentFuture != null) {
            this.currentFuture.cancel(true);
            this.currentFuture = null;
        }

        if (this.camera.isOpened()) {
            this.camera.release();
        }
    }

    public void startRecording() {
        this.stopRecording();

        var filename = "video/output_" + System.currentTimeMillis() + ".mov";
        var fourcc = VideoWriter.fourcc('m', 'p', '4', 'v');

        this.stopwatch.set(Stopwatch.createStarted());

        this.videoWriter.set(new VideoWriter(filename, fourcc, FPS, FRAME_SIZE, true));
        this.isRecording.set(true);
    }

    public void stopRecording() {
        this.isRecording.set(false);

        this.videoWriter.set(null);

        var stopwatch = this.stopwatch.getAndSet(null);
        if (stopwatch != null) {
            stopwatch.stop();
        }
    }

    private void saveFrame(Mat frame, VideoWriter videoWriter, Stopwatch stopwatch) {
        var min = stopwatch.elapsed(TimeUnit.MINUTES);
        var sec = stopwatch.elapsed(TimeUnit.SECONDS) % 60;
        var milli = stopwatch.elapsed(TimeUnit.MILLISECONDS) % 1000;


        var saveFrame = new Mat();
        frame.copyTo(saveFrame);

        Imgproc.putText(saveFrame, min + "." + sec + "." + milli, new Point(20, FRAME_SIZE.height - 20), 1, 12, new Scalar(0, 255, 255));

        videoWriter.write(saveFrame);
        saveFrame.release();
    }

    private void openCamera() {
        this.camera.open(this.cameraIndex);
        if (! this.camera.isOpened()) {
            throw new RuntimeException("Camera at index " + this.cameraIndex + " is not usable");
        }

        this.camera.set(Videoio.CAP_PROP_FRAME_WIDTH, FRAME_SIZE.width);
        this.camera.set(Videoio.CAP_PROP_FRAME_HEIGHT, FRAME_SIZE.height);
    }
}
