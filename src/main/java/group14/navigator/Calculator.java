package group14.navigator;

import group14.navigator.data.Point2D;

public class Calculator {

    public static double getAngleBetweenPoint(Point2D point1, Point2D point2) {
        var dx = point2.x - point1.x;
        var dy = -(point1.y - point2.y); // Reverse math sign due to y positive from to bottom

        var result = Math.toDegrees(Math.atan2(dy, dx));

        return result < 0 ? (360d + result) : result;
    }

    public static Point2D getVectorEndPoint(Point2D startPoint, double angle, double magnitude) {
        var dx = magnitude * Math.cos(Math.toRadians(angle));
        var dy = magnitude * Math.sin(Math.toRadians(angle));

        return new Point2D(startPoint.x + dx, startPoint.y + dy);
    }

    public static double getOppositeAngle(double angle) {
        return (angle + 180) % 360;
    }

    public static double getTurnAngle(double angle1, double angle2) {
        var diff = angle2 - angle1;
        var phi = Math.abs(diff) % 360;
        var distance = phi > 180 ? 360 - phi : phi;

        var sign = (diff >= 0 && diff <= 180) || (diff <=-180 && diff >= -360) ? 1 : -1;

        return distance * sign;
    }

}
