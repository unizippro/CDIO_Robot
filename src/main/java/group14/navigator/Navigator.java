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
    private Point2D depositPoint;

    private final int turnThreshold = 1;
    private boolean hasDeposit = false;
    private boolean depositCorrectionMode = false;

    public Navigator(Board board, Robot robot) {
        this.board = board;
        this.robot = robot;

        this.depositPoint = this.board.getDepositPoint();
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
        return this.hasDeposit;
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

        var robotPosition = this.robot.getRotatingPoint();
        var robotAngle = this.robot.getDirectionAngle();

        var currentBoard = this.board.getAreaForPoint(robotPosition);

        if (! this.ballPositions.isEmpty()) {
            if (this.ballsWithinArea(currentBoard)) {
                var ball = this.getClosestBall(robotPosition);

                if (currentBoard.isWithinSafetyArea(ball)) {
                    System.out.println("Navigator: Safe ball");

                    instructionSet.setDestination(ball);

                    this.addTurnIfNeeded(instructionSet, robotPosition, ball);
                    instructionSet.add(Instruction.forward(this.robot.getDistanceTo(ball)));
                } else {
                    var direction = currentBoard.getDangerousAreaDirection(ball);
                    var safePoint = currentBoard.getProjectedPoint(ball, direction);

                    var safePointArea = Utils.rectangleWithCenter(safePoint, 5);
                    if (safePointArea.contains(robotPosition)) {
                        System.out.println("Navigator: Unsafe Ball in direction " + direction);

                        instructionSet.setDestination(ball);

                        this.addTurnIfNeeded(instructionSet, robotPosition, ball);

                        double distance;
                        if (direction == Area.DangerousAreaDirection.TOP || direction == Area.DangerousAreaDirection.BOTTOM) {
                            distance = robotPosition.distance(ball) / 2;
                        } else if (Area.DangerousAreaDirection.isCorner(direction)) {
                            distance = robotPosition.distance(ball) / 3;
                        } else {
                            distance = robotPosition.distance(ball) / 4;
                        }

                        instructionSet.add(Instruction.forward(distance));
                        instructionSet.add(Instruction.sleep(500));
                        instructionSet.add(Instruction.backward(distance * 1.2));
                    } else {
                        System.out.println("Navigator: Unsafe ball with safe point");

                        instructionSet.setDestination(safePoint);

                        this.addTurnAndForward(instructionSet, robotPosition, safePoint);
                    }
                }
            } else {
                var currentSafePoint = currentBoard.getNearestSafePoint(robotPosition);
                var currentSafePointArea = Utils.rectangleWithCenter(currentSafePoint, 5);

                if (currentSafePointArea.contains(robotPosition)) {
                    System.out.println("Navigator: Safe point next area");

                    var nextArea = this.board.getAreaAfter(currentBoard);
                    var newSafePoint = nextArea.getNearestSafePoint(robotPosition);

                    instructionSet.setDestination(newSafePoint);

                    this.addTurnAndForward(instructionSet, robotPosition, newSafePoint);
                } else {
                    System.out.println("Navigator: Safe point current area");

                    instructionSet.setDestination(currentSafePoint);

                    this.addTurnAndForward(instructionSet, robotPosition, currentSafePoint);
                }
            }
        } else {
            var currentDepositPointSafe = Utils.rectangleWithCenter(this.depositPoint, 5);
            if (currentDepositPointSafe.contains(robotPosition)) {
                System.out.println("Navigator: Deposit plan started");

                var projectedDepositPoint = new Point2D(this.depositPoint.x - 30, this.depositPoint.y);

                instructionSet.setDestination(projectedDepositPoint);

                var currentDepositPointAngle = Calculator.getAngleBetweenPoint(robotPosition, projectedDepositPoint);

                var turnAngle = Calculator.getTurnAngle(robotAngle, currentDepositPointAngle);
                if (Math.abs(turnAngle) >= 1.5) {
                    instructionSet.add(Instruction.turn(turnAngle));
                    this.depositCorrectionMode = true;
                } else {
                    this.depositCorrectionMode = false;
                }

                if (! this.depositCorrectionMode) {
                    instructionSet.add(Instruction.turn(180));
                    instructionSet.add(Instruction.deposit());
                    instructionSet.add(Instruction.dance());

                    this.hasDeposit = true;
                }
            } else if (currentBoard.contains(this.depositPoint)) {
                System.out.println("Navigator: Deposit point current area");

                instructionSet.setDestination(this.depositPoint);

                this.addTurnAndForward(instructionSet, robotPosition, this.depositPoint);
            } else {
                var currentSafePoint = currentBoard.getNearestSafePoint(robotPosition);
                var currentSafePointArea = Utils.rectangleWithCenter(currentSafePoint, 5);

                if (currentSafePointArea.contains(robotPosition)) {
                    System.out.println("Navigator: Deposit point - safe point next area");

                    var nextArea = this.board.getAreaAfter(currentBoard);
                    var newSafePoint = nextArea.getNearestSafePoint(robotPosition);

                    instructionSet.setDestination(newSafePoint);

                    this.addTurnAndForward(instructionSet, newSafePoint, robotPosition);
                } else {
                    System.out.println("Navigator: Deposit point - safe point current area");

                    instructionSet.setDestination(currentSafePoint);
                }
            }
        }

        return instructionSet;
    }

    private void addTurnAndForward(InstructionSet instructionSet, Point2D from, Point2D to) {
        var toAngle = Calculator.getAngleBetweenPoint(from, to);
        var turnAngle = Calculator.getTurnAngle(this.robot.getDirectionAngle(), toAngle);
        if (Math.abs(turnAngle) >= this.turnThreshold) {
            instructionSet.add(Instruction.turn(turnAngle));
        }

        instructionSet.add(Instruction.forward(from.distance(to)));
    }

    private void addTurnIfNeeded(InstructionSet instructionSet, Point2D from, Point2D to) {
        var toAngle = Calculator.getAngleBetweenPoint(from, to);
        var turnAngle = Calculator.getTurnAngle(this.robot.getDirectionAngle(), toAngle);
        if (Math.abs(turnAngle) >= this.turnThreshold) {
            instructionSet.add(Instruction.turn(turnAngle));
        }
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
