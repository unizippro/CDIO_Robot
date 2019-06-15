package group14.opencv;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Camera {

    private static final int FPS = 30;
    protected static final Size FRAME_SIZE = new Size(1920, 1080);

    private ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> currentFuture;

    private final VideoCapture camera = new VideoCapture();

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

            if (updatedHandler != null) {
                new Thread(() -> {
                    try {
                        updatedHandler.frameUpdated(frame);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
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

    private void openCamera() {
        this.camera.open(this.cameraIndex);
        if (! this.camera.isOpened()) {
            throw new RuntimeException("Camera at index " + this.cameraIndex + " is not usable");
        }

        this.camera.set(Videoio.CAP_PROP_FRAME_WIDTH, FRAME_SIZE.width);
        this.camera.set(Videoio.CAP_PROP_FRAME_HEIGHT, FRAME_SIZE.height);
    }
}
