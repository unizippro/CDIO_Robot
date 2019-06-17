package group14.navigator;

import group14.navigator.data.Area;
import group14.navigator.data.Board;
import group14.navigator.data.Robot;
import lejos.robotics.geometry.Point2D;
import lejos.robotics.geometry.Rectangle2D;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


// if boardCurrentArea contains balls
    // b = getClosestBall
    // if ball is within safety area
        // goToPoint(b)
    // else
        // p = project point to go to (vinkelret på bold/bane)
        // goToPoint(p)
        // go closer to ball (keeping some safe distance)
        // goToPoint(p) (backwards)
// else
    // go to opposite area

// todo: maybe implement max distance for forward
public class Navigator {

    private final Board board;
    private final Robot robot;

    private final ArrayList<Point2D.Double> ballPositions = new ArrayList<>();
    private final int turnThreshold = 1;

    public Navigator(Board board, Robot robot) {
        this.board = board;
        this.robot = robot;
    }

    public void updateRobotPosition(Point2D.Double frontPoint, Point2D.Double rearPoint) {
        this.robot.updatePosition(frontPoint, rearPoint);
    }

    public void updateBallPositions(List<Point2D.Double> ballPositions) {
        this.ballPositions.clear();

        ballPositions.stream()
                .filter(this.board::contains)
                .collect(Collectors.toCollection(() -> this.ballPositions));
    }

    public InstructionSet calculateInstructionSet() throws Exception {
        var instructionSet = new InstructionSet();

        if (! this.ballPositions.isEmpty()) {
            var robotPosition = this.robot.getRotatingPoint();
            var robotAngle = this.robot.getDirectionAngle();

            var currentBoard = this.board.getAreaForPoint(robotPosition);

            if (this.ballsWithinArea(currentBoard)) {
                var ball = this.getClosestBall(robotPosition);

                if (currentBoard.isWithinSafetyArea(ball)) {
                    var ballAngle = Calculator.getAngleBetweenPoint(robotPosition, ball);

                    var turnAngle = Calculator.getTurnAngle(robotAngle, ballAngle);
                    if (Math.abs(turnAngle) >= this.turnThreshold) {
                        instructionSet.add(new Instruction(Instruction.InstructionType.TURN, turnAngle));
                    }

                    instructionSet.add(new Instruction(Instruction.InstructionType.FORWARD, this.robot.getDistanceTo(ball)));

                    instructionSet.setDestination(ball);
                } else {
                    var direction = currentBoard.getDangerousAreaDirection(ball);
                    var safePoint = currentBoard.getProjectedPoint(ball, direction);

                    var safePointArea = Utils.rectangleWithCenter(safePoint, 5);
                    if (safePointArea.contains(robotPosition)) {
                        var ballAngle = Calculator.getAngleBetweenPoint(robotPosition, ball);

                        var turnAngle = Calculator.getTurnAngle(robotAngle, ballAngle);
                        if (Math.abs(turnAngle) >= this.turnThreshold) {
                            instructionSet.add(new Instruction(Instruction.InstructionType.TURN, turnAngle));
                        }

                        instructionSet.add(new Instruction(Instruction.InstructionType.FORWARD, 5));
                        instructionSet.add(new Instruction(Instruction.InstructionType.BACKWARD, 5));

                        instructionSet.setDestination(ball);
                    } else {
                        var safePointAngle = Calculator.getAngleBetweenPoint(robotPosition, safePoint);

                        var turnAngle = Calculator.getTurnAngle(robotAngle, safePointAngle);
                        if (Math.abs(turnAngle) >= this.turnThreshold) {
                            instructionSet.add(new Instruction(Instruction.InstructionType.TURN, turnAngle));
                        }

                        instructionSet.add(new Instruction(Instruction.InstructionType.FORWARD, this.robot.getDistanceTo(safePoint)));

                        instructionSet.setDestination(safePoint);
                    }
                }
            } else {
                var currentSafePoint = currentBoard.getNearestSafePoint(robotPosition);
                var currentSafePointArea = Utils.rectangleWithCenter(currentSafePoint, 5);

                if (currentSafePointArea.contains(robotPosition)) {
                    var nextArea = this.board.getAreaAfter(currentBoard);
                    var newSafePoint = nextArea.getNearestSafePoint(robotPosition);

                    var newSafePointAngle = Calculator.getAngleBetweenPoint(robotPosition, newSafePoint);

                    var turnAngle = Calculator.getTurnAngle(robotAngle, newSafePointAngle);
                    if (Math.abs(turnAngle) >= this.turnThreshold) {
                        instructionSet.add(new Instruction(Instruction.InstructionType.TURN, turnAngle));
                    }

                    instructionSet.add(new Instruction(Instruction.InstructionType.FORWARD, this.robot.getDistanceTo(newSafePoint)));

                    instructionSet.setDestination(newSafePoint);
                } else {
                    var currentSafePointAngle = Calculator.getAngleBetweenPoint(robotPosition, currentSafePoint);

                    var turnAngle = Calculator.getTurnAngle(robotAngle, currentSafePointAngle);
                    if (Math.abs(turnAngle) >= this.turnThreshold) {
                        instructionSet.add(new Instruction(Instruction.InstructionType.TURN, turnAngle));
                    }

                    instructionSet.add(new Instruction(Instruction.InstructionType.FORWARD, this.robot.getDistanceTo(currentSafePoint)));

                    instructionSet.setDestination(currentSafePoint);
                }
            }
        } else {
            // todo: deposit plan here...
        }

        return instructionSet;
    }


    protected Point2D.Double getClosestBall(Point2D.Double point) {
        if (this.ballPositions.isEmpty()) {
            return null;
        }

        return this.ballPositions.stream()
                .reduce(null, (o, ball) -> {
                    if (o == null) {
                        return ball;
                    }

                    return point.distance(o) < point.distance(ball) ? o : ball;
                });
    }


    protected boolean ballsWithinArea(Area area) {
        return this.ballPositions.stream().anyMatch(area::contains);
    }

}
