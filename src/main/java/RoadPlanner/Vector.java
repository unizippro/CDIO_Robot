package RoadPlanner;

import static java.lang.Math.*;

public class Vector {
    double lenght;
    int x,y;

    public Vector() {
    }

    public Vector(int x, int y) {
        this.x = x;
        this.y = y;
        getLenght();
    }
    public Vector(Point c1, Point c2) {
        this.x = c2.x - c1.x;
        this.y = c2.y - c1.y;
        getLenght();
    }

    public double getLenght() {
        //Calculate the length
        lenght = sqrt(pow(x,2)+ pow(y,2));
        return lenght;
    }

    public String toString() {
        return "Vector = ( x = " + x + ", y = " + y + ", length = " + String.format("%.2f",lenght)+ " )";
    }

    public void update(Point c1, Point c2) {
        this.x = c2.x - c1.x;
        this.y = c2.y - c1.y;
        getLenght();
    }
}
