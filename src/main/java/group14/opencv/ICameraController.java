package group14.opencv;

import javafx.scene.image.Image;
import org.opencv.core.Mat;

import java.awt.image.BufferedImage;

public interface ICameraController {
    interface CameraUpdatedHandler {
        void updated();
    }

    void start(int cameraIndex, int fps);
    void stop();
    void addUpdateListener(CameraUpdatedHandler cameraUpdatedHandler);

    void updateWithImage(String filePath);

    Mat getSource();
    Image getSourceAsImageFX();
    Image matToImageFX(Mat mat);
}
