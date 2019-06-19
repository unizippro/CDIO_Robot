package group14.navigator;

import group14.navigator.data.Area;
import group14.navigator.data.Board;
import group14.navigator.data.Point2D;
import group14.navigator.data.Robot;
import group14.robot.data.Instruction;

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

    private final List<Point2D> ballPositions = new ArrayList<>();
    private final int turnThreshold = 1;

    public Navigator(Board board, Robot robot) {
        this.board = board;
        this.robot = robot;
    }

    public void updateRobotPosition(Point2D frontPoint, Point2D rearPoint) {
        this.robot.updatePosition(frontPoint, rearPoint);
    }

    public void updateBallPositions(List<Point2D> ballPositions) {
        this.ballPositions.clear();

        ballPositions.stream()
                .filter(this.board::contains)
                .collect(Collectors.toCollection(() -> this.ballPositions));
    }

    public boolean isEmpty() {
        return this.ballPositions.isEmpty();
    }

    public Point2D getRobotPosition() {
        return this.robot.getRotatingPoint();
    }

    public Board getBoard() {
        return this.board;
    }

    public List<Point2D> getBallPositions() {
        return this.ballPositions;
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
                    System.out.println("Navigator: Safe ball");

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
                        System.out.println("Navigator: Unsafe Ball in direction " + direction);

                        var ballAngle = Calculator.getAngleBetweenPoint(robotPosition, ball);

                        var turnAngle = Calculator.getTurnAngle(robotAngle, ballAngle);
                        if (Math.abs(turnAngle) >= this.turnThreshold) {
                            instructionSet.add(new Instruction(Instruction.InstructionType.TURN, turnAngle));
                        }

//                        var distance = (this.robot.getDistanceTo(ball) / 3) * 2;
//                        var distance = this.robot.getDistanceTo(ball);
                        double distance;
                        if (direction == Area.DangerousAreaDirection.TOP || direction == Area.DangerousAreaDirection.BOTTOM) {
                            distance = robotPosition.distance(ball) / 2;
                        } else {
                            distance = robotPosition.distance(ball) / 4;
                        }

                        instructionSet.add(new Instruction(Instruction.InstructionType.FORWARD, distance));
                        instructionSet.add(new Instruction(Instruction.InstructionType.BACKWARD, distance));

                        instructionSet.setDestination(ball);
                    } else {
                        System.out.println("Navigator: Unsafe ball with safe point");

                        var safePointAngle = Calculator.getAngleBetweenPoint(robotPosition, safePoint);

                        var turnAngle = Calculator.getTurnAngle(robotAngle, safePointAngle);
                        if (Math.abs(turnAngle) >= this.turnThreshold) {
                            instructionSet.add(new Instruction(Instruction.InstructionType.TURN, turnAngle));
                        }

                        instructionSet.add(new Instruction(Instruction.InstructionType.FORWARD, robotPosition.distance(safePoint)));

                        instructionSet.setDestination(safePoint);
                    }
                }
            } else {
                var currentSafePoint = currentBoard.getNearestSafePoint(robotPosition);
                var currentSafePointArea = Utils.rectangleWithCenter(currentSafePoint, 5);

                if (currentSafePointArea.contains(robotPosition)) {
                    System.out.println("Navigator: Safe point next area");

                    var nextArea = this.board.getAreaAfter(currentBoard);
                    var newSafePoint = nextArea.getNearestSafePoint(robotPosition);

                    var newSafePointAngle = Calculator.getAngleBetweenPoint(robotPosition, newSafePoint);

                    var turnAngle = Calculator.getTurnAngle(robotAngle, newSafePointAngle);
                    if (Math.abs(turnAngle) >= this.turnThreshold) {
                        instructionSet.add(new Instruction(Instruction.InstructionType.TURN, turnAngle));
                    }

                    instructionSet.add(new Instruction(Instruction.InstructionType.FORWARD, robotPosition.distance(newSafePoint)));

                    instructionSet.setDestination(newSafePoint);
                } else {
                    System.out.println("Navigator: Safe point current area");

                    var currentSafePointAngle = Calculator.getAngleBetweenPoint(robotPosition, currentSafePoint);

                    var turnAngle = Calculator.getTurnAngle(robotAngle, currentSafePointAngle);
                    if (Math.abs(turnAngle) >= this.turnThreshold) {
                        instructionSet.add(new Instruction(Instruction.InstructionType.TURN, turnAngle));
                    }

                    instructionSet.add(new Instruction(Instruction.InstructionType.FORWARD, robotPosition.distance(currentSafePoint)));

                    instructionSet.setDestination(currentSafePoint);
                }
            }
        } else {
            // todo: deposit plan here...
        }

        return instructionSet;
    }


    protected Point2D getClosestBall(Point2D point) {
        if (this.ballPositions.isEmpty()) {
            return null;
        }

        var area = Utils.rectangleWithExpandedMargin(this.board.getAreaForPoint(point).getBoundingRect(), 1);

        return this.ballPositions.stream()
                .filter(area::contains)
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
