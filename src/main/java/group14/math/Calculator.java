package group14.math;

import group14.road_planner.Vector;

import java.awt.*;

import static java.lang.Math.atan2;
import static java.lang.Math.toDegrees;

public class Calculator {

    /**
     *
     * @param v1
     * @param v2
     * @return value between -180 to 180.
     */
    public static double CALCULATE_ANGLE(Vector v1, Vector v2) {
        // http://www.euclideanspace.com/maths/algebra/vectors/angleBetween/
        double angle = toDegrees(atan2(v1.getY(), v1.getX()) - atan2(v2.getY(), v2.getX()));
        //System.out.println("Before manipulation: " + angle);
        if (angle < (-180)) {
            //System.out.println(angle + 360);
            return angle + 360;
        } else if (angle > 180) {
            //System.out.println(angle - 360);
            return angle - 360;
        } else {
            //System.out.println(angle);
            return angle;
        }
    }

    public static Vector CALCULATE_VECTOR(Point c1, Point c2) {
        return new Vector(c2.x - c1.x, c2.y - c1.y);
    }
}
