package group14.opencv.utils;

import javafx.scene.image.Image;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.ByteArrayInputStream;

public class ImageConverter {

    public static Image matToImageFX(Mat frame) {
        MatOfByte buffer = new MatOfByte();
        Imgcodecs.imencode(".bmp", frame, buffer);

        return new Image(new ByteArrayInputStream(buffer.toArray()));
    }

}
