package group14.navigator.data;

public class Rectangle2D extends lejos.robotics.geometry.Rectangle2D.Double {

    public Rectangle2D() {
        super();
    }

    public Rectangle2D(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    public String toString() {
        return "Rect(" + String.format("%.2f", this.x) + ", " + String.format("%.2f", this.y) + ", " + String.format("%.2f", this.getMaxX()) + ", " + String.format("%.2f", this.getMaxY()) + ") Size(" + String.format("%.2f", this.width) + ", " + String.format("%.2f", this.height) + ")";
    }

}
