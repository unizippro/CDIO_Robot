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

    public double getLenght() {
        //Calculate the length
        lenght = sqrt(pow(x,2)+ pow(y,2));
        return lenght;
    }

    public String toString() {
        return "Vector = ( x = " + x + ", y = " + y + ", length = " + String.format("%.2f",lenght)+ " )";
    }
}
