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

        // todo: temp
        this.depositPoint = this.board.getDepositPointLeft();
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

    public enum DepositSide { LEFT, RIGHT }

    public void setDepositSide(DepositSide depositSide) {
        switch (depositSide) {
            case LEFT:
                this.depositPoint = this.board.getDepositPointLeft();
                break;

            case RIGHT:
                this.depositPoint = this.board.getDepositPointRight();
                break;
        }
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

                        double distance;
                        if (direction == Area.DangerousAreaDirection.TOP || direction == Area.DangerousAreaDirection.BOTTOM) {
                            distance = robotPosition.distance(ball) / 2;
                        } else if (Area.DangerousAreaDirection.isCorner(direction)) {
                            distance = robotPosition.distance(ball) / 3;
                        } else {
                            distance = robotPosition.distance(ball) / 4;
                        }

                        instructionSet.add(new Instruction(Instruction.InstructionType.FORWARD, distance));
                        instructionSet.add(new Instruction(Instruction.InstructionType.WAIT, 500));
                        instructionSet.add(new Instruction(Instruction.InstructionType.BACKWARD, distance * 1.2));

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
            if (this.depositPoint == null) {
                System.err.println("Navigator: No deposit point");

                return instructionSet;
            }

            System.out.println(this.depositPoint);
            System.out.println(robotPosition);
            System.out.println(currentBoard);
            System.out.println(currentBoard.contains(this.depositPoint));

            var currentDepositPointSafe = Utils.rectangleWithCenter(this.depositPoint, 5);
            if (currentDepositPointSafe.contains(robotPosition)) {
                System.out.println("Navigator: Deposit plan started");

                // todo: project point against goal
                // assuming left
                var currentDepositPointAngle = Calculator.getAngleBetweenPoint(robotPosition, new Point2D(this.depositPoint.x - 30, this.depositPoint.y));

                var turnAngle = Calculator.getTurnAngle(robotAngle, currentDepositPointAngle);
                if (Math.abs(turnAngle) >= 1.5) {
                    instructionSet.add(new Instruction(Instruction.InstructionType.TURN, turnAngle));
                    this.depositCorrectionMode = true;
                } else {
                    this.depositCorrectionMode = false;
                }

                if (! this.depositCorrectionMode) {
                    instructionSet.add(new Instruction(Instruction.InstructionType.TURN, 180));
                    instructionSet.add(new Instruction(Instruction.InstructionType.DEPOSIT));
                    instructionSet.add(new Instruction(Instruction.InstructionType.DANCE));

                    this.hasDeposit = true;
                }

                // todo: project point against goal
                instructionSet.setDestination(new Point2D(this.depositPoint.x - 10, this.depositPoint.y));
            } else if (currentBoard.contains(this.depositPoint)) {
                System.out.println("Navigator: Deposit point current area");

                var currentDepositPointAngle = Calculator.getAngleBetweenPoint(robotPosition, this.depositPoint);

                var turnAngle = Calculator.getTurnAngle(robotAngle, currentDepositPointAngle);
                if (Math.abs(turnAngle) >= this.turnThreshold) {
                    instructionSet.add(new Instruction(Instruction.InstructionType.TURN, turnAngle));
                }

                instructionSet.add(new Instruction(Instruction.InstructionType.FORWARD, robotPosition.distance(this.depositPoint)));

                instructionSet.setDestination(this.depositPoint);
            } else {
                var currentSafePoint = currentBoard.getNearestSafePoint(robotPosition);
                var currentSafePointArea = Utils.rectangleWithCenter(currentSafePoint, 5);

                if (currentSafePointArea.contains(robotPosition)) {
                    System.out.println("Navigator: Deposit point - safe point next area");

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
                    System.out.println("Navigator: Deposit point - safe point current area");

                    var currentSafePointAngle = Calculator.getAngleBetweenPoint(robotPosition, currentSafePoint);

                    var turnAngle = Calculator.getTurnAngle(robotAngle, currentSafePointAngle);
                    if (Math.abs(turnAngle) >= this.turnThreshold) {
                        instructionSet.add(new Instruction(Instruction.InstructionType.TURN, turnAngle));
                    }

                    instructionSet.add(new Instruction(Instruction.InstructionType.FORWARD, robotPosition.distance(currentSafePoint)));

                    instructionSet.setDestination(currentSafePoint);
                }
            }
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
