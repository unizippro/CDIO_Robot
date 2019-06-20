package group14.navigator.data;

public class Point2D extends lejos.robotics.geometry.Point2D.Double {

    public Point2D() {
        super();
    }

    public Point2D(double x, double y) {
        super(x, y);
    }

    @Override
    public String toString() {
        return "Point2D(" + String.format("%.2f", this.x) + ", " + String.format("%.2f", this.y) + ")";
    }
}
