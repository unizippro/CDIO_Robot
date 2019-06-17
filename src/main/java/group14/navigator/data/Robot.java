package group14.navigator.data;

import group14.navigator.Calculator;
import group14.navigator.Instruction;
import lejos.robotics.geometry.Point2D;

public class Robot {

    private static final double DISTANCE_REAR_POINT_TO_FRONT = 10;//29.5;
    private static final double DISTANCE_REAR_POINT_TO_ROTATING = 5;//11;

    private final Point2D.Double frontPoint;
    private final Point2D.Double rearPoint;

    public Robot(Point2D.Double frontPoint, Point2D.Double rearPoint) {
        this.frontPoint = frontPoint;
        this.rearPoint = rearPoint;
    }

    public void updatePosition(Point2D.Double frontPoint, Point2D.Double rearPoint) {
        this.frontPoint.setLocation(frontPoint);
        this.rearPoint.setLocation(rearPoint);
    }

    public Point2D.Double getFrontPosition() {
        return Calculator.getVectorEndPoint(this.rearPoint, this.getDirectionAngle(), DISTANCE_REAR_POINT_TO_FRONT);
    }

    public double getDirectionAngle() {
        return Calculator.getAngleBetweenPoint(this.rearPoint, this.frontPoint);
    }

    public Point2D.Double getRotatingPoint() {
        return Calculator.getVectorEndPoint(this.rearPoint, this.getDirectionAngle(), DISTANCE_REAR_POINT_TO_ROTATING);
    }

    public double getDistanceTo(Point2D.Double point) {
        return this.getRotatingPoint().distance(point) - this.getDistanceFromRotatingPointToFront();
    }

    public void updateFromInstruction(Instruction instruction) {
        var directionAngle = this.getDirectionAngle();

        switch (instruction.getType()) {
            case FORWARD:
                this.rearPoint.setLocation(Calculator.getVectorEndPoint(this.rearPoint, directionAngle, instruction.getAmount()));
                this.frontPoint.setLocation(Calculator.getVectorEndPoint(this.frontPoint, directionAngle, instruction.getAmount()));
                break;

            case TURN:
                var rotatingPoint = this.getRotatingPoint();
                var newAngle = directionAngle + instruction.getAmount();

                this.rearPoint.setLocation(Calculator.getVectorEndPoint(rotatingPoint, Calculator.getOppositeAngle(newAngle), DISTANCE_REAR_POINT_TO_ROTATING));
                this.frontPoint.setLocation(Calculator.getVectorEndPoint(this.rearPoint, newAngle, 1)); // todo: calculate magnitude
                break;

            case BACKWARD:
                this.rearPoint.setLocation(Calculator.getVectorEndPoint(this.rearPoint, Calculator.getOppositeAngle(directionAngle), instruction.getAmount()));
                this.frontPoint.setLocation(Calculator.getVectorEndPoint(this.frontPoint, Calculator.getOppositeAngle(directionAngle), instruction.getAmount()));
                break;
        }
    }

    private double getDistanceFromRotatingPointToFront() {
        return DISTANCE_REAR_POINT_TO_FRONT - DISTANCE_REAR_POINT_TO_ROTATING;
    }
}
