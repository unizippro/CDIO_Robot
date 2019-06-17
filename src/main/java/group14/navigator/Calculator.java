package group14.navigator;

import lejos.robotics.geometry.Point2D;

public class Calculator {

    public static double getAngleBetweenPoint(Point2D.Double point1, Point2D.Double point2) {
        var dx = point2.x - point1.x;
        var dy = -(point1.y - point2.y); // Reverse math sign due to y positive from to bottom

        var result = Math.toDegrees(Math.atan2(dy, dx));

        return result < 0 ? (360d + result) : result;
    }

    public static Point2D.Double getVectorEndPoint(Point2D.Double startPoint, double angle, double magnitude) {
        var dx = magnitude * Math.cos(Math.toRadians(angle));
        var dy = magnitude * Math.sin(Math.toRadians(angle));

        return new Point2D.Double(startPoint.x + dx, startPoint.y + dy);
    }

    public static double getOppositeAngle(double angle) {
        return (angle + 180) % 360;
    }

    public static double getTurnAngle(double angle1, double angle2) {
        double angle = angle2 - angle1;
        if (Math.abs(angle) > 180) {
            if (angle >= 0) {
                angle = 360 - angle;
            } else {
                angle = 360 + angle;
            }
        }

        return angle;
    }

}
