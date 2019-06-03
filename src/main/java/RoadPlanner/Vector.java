package RoadPlanner;

import java.awt.*;

import static java.lang.Math.*;

public class Vector {
    double length;
    int x,y;

    public Vector(int x, int y) {
        this.x = x;
        this.y = y;
//        getPixelsPerCm();
    }
    public Vector(Point c1, Point c2) {
        this.x = c2.x - c1.x;
        this.y = c2.y - c1.y;
//        getPixelsPerCm();
    }

    public Vector() {

    }

    public double getLength() {
//        length = sqrt(pow(x,2)+ pow(y,2));
        return this.length;
    }

    /**
     * Calculates the length
     */
    private void calculateLenght() {
        this.length = sqrt(pow(x,2)+ pow(y,2));
    }

    public String toString() {
        return "Vector = ( x = " + x + ", y = " + y + ", length = " + String.format("%.2f", length)+ " )";
    }

    public void update(Point c1, Point c2) {
        this.x = c2.x - c1.x;
        this.y = c2.y - c1.y;
//        getPixelsPerCm();
        this.calculateLenght();
    }
}
