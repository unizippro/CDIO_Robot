package group14.navigator.data;

import group14.navigator.Calculator;
import group14.robot.data.Instruction;

public abstract class Robot {

    private final Point2D frontPoint;
    private final Point2D rearPoint;

    public Robot(Point2D frontPoint, Point2D rearPoint) {
        this.frontPoint = frontPoint;
        this.rearPoint = rearPoint;
    }

    protected abstract double getDistanceRearPointToFront();
    protected abstract double getDistanceRearPointToRotating();

    public void updatePosition(Point2D frontPoint, Point2D rearPoint) {
        this.frontPoint.setLocation(frontPoint);
        this.rearPoint.setLocation(rearPoint);
    }

    public boolean hasValidPosition() {
        return this.rearPoint.distance(this.frontPoint) <= 20;
    }

    public Point2D getFrontPosition() {
        return Calculator.getVectorEndPoint(this.rearPoint, this.getDirectionAngle(), this.getDistanceRearPointToFront());
    }

    public double getDirectionAngle() {
        return Calculator.getAngleBetweenPoint(this.rearPoint, this.frontPoint);
    }

    public Point2D getRotatingPoint() {
        return Calculator.getVectorEndPoint(this.rearPoint, this.getDirectionAngle(), this.getDistanceRearPointToRotating());
    }

    public double getDistanceTo(Point2D point) {
        var rotatingPoint = this.getRotatingPoint();

        return rotatingPoint.distance(point) - (this.getDistanceRearPointToFront() - this.getDistanceRearPointToRotating());
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
                var distanceBetweenPoints = this.rearPoint.distance(this.frontPoint);

                this.rearPoint.setLocation(Calculator.getVectorEndPoint(rotatingPoint, Calculator.getOppositeAngle(newAngle), this.getDistanceRearPointToRotating()));
                this.frontPoint.setLocation(Calculator.getVectorEndPoint(this.rearPoint, newAngle, distanceBetweenPoints));
                break;

            case BACKWARD:
                this.rearPoint.setLocation(Calculator.getVectorEndPoint(this.rearPoint, Calculator.getOppositeAngle(directionAngle), instruction.getAmount()));
                this.frontPoint.setLocation(Calculator.getVectorEndPoint(this.frontPoint, Calculator.getOppositeAngle(directionAngle), instruction.getAmount()));
                break;
        }
    }

    @Override
    public String toString() {
        return "Robot(" + this.getRotatingPoint() + ")";
    }
}
