package group14.opencv;

import javafx.scene.image.Image;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import java.io.ByteArrayInputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class CameraController implements ICameraController {

    private ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> currentFuture;

    private AtomicReference<Mat> source = new AtomicReference<>(new Mat());
    private VideoCapture camera = new VideoCapture();

    private CameraUpdatedHandler cameraUpdatedHandler;


    @Override
    public void start(int cameraIndex, int fps) {
        this.stop();

        this.camera.open(cameraIndex);
        if (! this.camera.isOpened()) {
            throw new RuntimeException("Camera at index " + cameraIndex + " is not usable");
        }

        this.camera.set(Videoio.CAP_PROP_FRAME_WIDTH, 1920);
        this.camera.set(Videoio.CAP_PROP_FRAME_HEIGHT, 1080);

        this.currentFuture = this.exec.scheduleAtFixedRate(() -> {
            Mat mat = new Mat();
            this.camera.read(mat);
            this.source.set(mat);

            if (this.cameraUpdatedHandler != null) {
                try {
                    this.cameraUpdatedHandler.updated();
                } catch (Exception e) {
//                    e.printStackTrace();
                }
            }
        }, 0, 1000 / fps, TimeUnit.MILLISECONDS);
    }

    @Override
    public void stop() {
        if (this.currentFuture != null) {
            this.currentFuture.cancel(true);
            this.currentFuture = null;
        }

        if (this.camera.isOpened()) {
            this.camera.release();
        }
    }

    @Override
    public void addUpdateListener(CameraUpdatedHandler cameraUpdatedHandler) {
        this.cameraUpdatedHandler = cameraUpdatedHandler;
    }

    @Override
    public void updateWithImage(String filePath) {
        new Thread(() -> {
            Mat src = Imgcodecs.imread(filePath, Imgcodecs.IMREAD_COLOR);
            this.source.set(src);

            if (this.cameraUpdatedHandler != null) {
                this.cameraUpdatedHandler.updated();
            }
        }).start();
    }

    @Override
    public Mat getSource() {
        return this.source.get();
    }

    @Override
    public Image getSourceAsImageFX() {
        return this.matToImageFX(this.getSource());
    }

    @Override
    public Image matToImageFX(Mat frame) {
        MatOfByte buffer = new MatOfByte();
        Imgcodecs.imencode(".bmp", frame, buffer);

        return new Image(new ByteArrayInputStream(buffer.toArray()));
    }
}
