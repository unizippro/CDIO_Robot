package group14.opencv.detectors;

import org.opencv.core.Mat;

public abstract class Detector<T, U> {
    private U config;

    public Detector() {
        this.config = this.createConfig();
    }

    public U getConfig() {
        return this.config;
    }

    public void setConfig(U config) {
        this.config = config;
    }

    abstract public T run(Mat src);
    abstract protected U createConfig();
}
