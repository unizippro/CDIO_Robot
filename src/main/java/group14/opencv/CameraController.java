package group14.opencv;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class CameraController implements ICameraController {

    private ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> currentFuture;

    private AtomicReference<Mat> source = new AtomicReference<>(new Mat());
    private VideoCapture camera;

    private CameraUpdatedHandler cameraUpdatedHandler;


    @Override
    public void start(int cameraIndex, int fps) {
        this.stop();

        this.camera = new VideoCapture(cameraIndex);

        if (! this.camera.isOpened()) {
            throw new RuntimeException("Camera at index " + cameraIndex + " is not usable");
        }

        this.currentFuture = this.exec.scheduleAtFixedRate(() -> {
            Mat mat = new Mat();
            this.camera.read(mat);
            this.source.set(mat);

            if (this.cameraUpdatedHandler != null) {
                this.cameraUpdatedHandler.updated();
            }
        }, 0, 1000 / fps, TimeUnit.MILLISECONDS);
    }

    @Override
    public void stop() {
        if (this.currentFuture != null) {
            System.out.println("Stopping");
            this.currentFuture.cancel(true);
            this.camera.release();
            this.camera = null;
        }
    }

    @Override
    public void addUpdateListener(CameraUpdatedHandler cameraUpdatedHandler) {
        this.cameraUpdatedHandler = cameraUpdatedHandler;
    }

    @Override
    public Mat getSource() {
        return this.source.get();
    }

    @Override
    public BufferedImage getSourceAsBufferedImage() {
        return this.matToBufferedImage(this.getSource());
    }

    private BufferedImage matToBufferedImage(Mat matBGR) {
        var width = matBGR.width();
        var height = matBGR.height();
        var channels = matBGR.channels();

        var sourcePixels = new byte[width * height * channels];
        matBGR.get(0, 0, sourcePixels);

        var imageType = channels > 1 ? BufferedImage.TYPE_3BYTE_BGR : BufferedImage.TYPE_BYTE_GRAY;
        var image = new BufferedImage(width, height, imageType);

        final var targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(sourcePixels, 0, targetPixels, 0, sourcePixels.length);

        return image;
    }
}
