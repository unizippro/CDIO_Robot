package group14.navigator;

import group14.navigator.data.*;
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

    private NavigationState state = new NavigationState();
    private final List<Point2D> ballPositions = new ArrayList<>();
    private Point2D depositPoint;

    private final int turnThreshold = 1;

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

    public void updateBoard(Rectangle2D boundingRect, Rectangle2D cross) {
        this.board.updateBoard(boundingRect, cross);
    }

    public boolean isDone() {
        return this.state.hasDoneState();
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

        System.out.println(this.state);

        if (! this.ballPositions.isEmpty() || this.state.hasBallState()) {
            var statePointInCurrentArea = this.state.hasBallState() && currentBoard.contains(this.state.getPoint());

            if (statePointInCurrentArea || this.ballsWithinArea(currentBoard)) {
                var ball = this.state.hasBallState() ? this.state.getPoint() : this.getClosestBall(robotPosition);

                if (currentBoard.isWithinSafetyArea(ball)) {
                    this.state.setSafe(ball);
                    this.goToBallInSafeArea(instructionSet, robotPosition, ball);
                } else {
                    this.state.setNotSafe(ball);
                    this.goToBallOutsideSafeArea(instructionSet, robotPosition, currentBoard, ball);
                }
            } else {
                this.goToNextArea(instructionSet, robotPosition, currentBoard);
            }
        } else {
            var currentDepositPointSafe = Utils.rectangleWithCenter(this.depositPoint, 5);

            if (this.state.hasPreDoneState()) {
                this.state.setDone();
                instructionSet.add(Instruction.dance(true));
            } else if (this.state.getState() == NavigationState.State.DEPOSIT_RECHECK) {
                this.goToNextArea(instructionSet, robotPosition, currentBoard);
                instructionSet.add(Instruction.sleep(100));
                this.state.setHasPreDone();
            } else if (currentDepositPointSafe.contains(robotPosition)) {
                this.handleDepositAction(instructionSet, robotPosition, robotAngle);
            } else if (currentBoard.contains(this.depositPoint)) {
                this.goToDepositPoint(instructionSet, robotPosition, this.depositPoint);
            } else {
                this.goToDepositOtherArea(instructionSet, robotPosition, currentBoard);
            }
        }

        return instructionSet;
    }

    private void goToBallInSafeArea(InstructionSet instructionSet, Point2D robotPosition, Point2D ball) {
        instructionSet.setData(this.robot.getRotatingPoint(), ball, "Navigator: Safe ball");

        this.addTurnIfNeeded(instructionSet, robotPosition, ball);
        instructionSet.add(Instruction.forward(this.robot.getDistanceTo(ball)));

        this.state.resetState();
    }

    private void goToBallOutsideSafeArea(InstructionSet instructionSet, Point2D robotPosition, Area currentBoard, Point2D ball) throws Exception {
        var direction = currentBoard.getDangerousAreaDirection(ball);
        var safePoint = currentBoard.getProjectedPoint(ball, direction);

        var safePointArea = Utils.rectangleWithCenter(safePoint, 5);
        if (safePointArea.contains(robotPosition)) {
            instructionSet.setData(this.robot.getRotatingPoint(), ball, "Navigator: Unsafe Ball in direction " + direction);

            this.addTurnIfNeeded(instructionSet, robotPosition, ball);

            double distance;
            if (direction == Area.DangerousAreaDirection.TOP || direction == Area.DangerousAreaDirection.BOTTOM) {
                distance = robotPosition.distance(ball) * 0.75;
            } else if (Area.DangerousAreaDirection.isCorner(direction)) {
                distance = robotPosition.distance(ball) * 0.67;
            } else {
                distance = robotPosition.distance(ball) * 0.55;
            }

            instructionSet.add(Instruction.forward(distance));
            instructionSet.add(Instruction.sleep(500));
            instructionSet.add(Instruction.backward(distance * 1.2));

            this.state.resetState();
        } else {
            instructionSet.setData(this.robot.getRotatingPoint(), safePoint, "Navigator: Unsafe ball with safe point");

            this.addTurnAndForward(instructionSet, robotPosition, safePoint);
        }
    }

    private void goToNextArea(InstructionSet instructionSet, Point2D robotPosition, Area currentBoard) {
        var currentSafePoint = currentBoard.getNearestSafePoint(robotPosition);
        var currentSafePointArea = Utils.rectangleWithCenter(currentSafePoint, 5);

        if (currentSafePointArea.contains(robotPosition)) {
            var nextArea = this.board.getAreaAfter(currentBoard);
            var newSafePoint = nextArea.getNearestSafePoint(robotPosition);

            instructionSet.setData(this.robot.getRotatingPoint(), newSafePoint, "Navigator: Safe point next area");

            this.addTurnAndForward(instructionSet, robotPosition, newSafePoint);
        } else {
            instructionSet.setData(this.robot.getRotatingPoint(), currentSafePoint, "Navigator: Safe point current area");

            this.addTurnAndForward(instructionSet, robotPosition, currentSafePoint);
        }
    }

    private void handleDepositAction(InstructionSet instructionSet, Point2D robotPosition, double robotAngle) {
        var projectedDepositPoint = new Point2D(this.depositPoint.x + 30, this.depositPoint.y);

        instructionSet.setData(this.robot.getRotatingPoint(), projectedDepositPoint, "Navigator: Deposit plan started");

        if (Math.abs(robotAngle) >= 2.2) {
            var currentDepositPointAngle = Calculator.getAngleBetweenPoint(robotPosition, projectedDepositPoint);
            var turnAngle = Calculator.getTurnAngle(robotAngle, currentDepositPointAngle);

            if (Math.abs(turnAngle) <= 1) {
                turnAngle *= 1.5;
            }

            instructionSet.add(Instruction.turn(turnAngle));
        } else {
            instructionSet.add(Instruction.deposit());
            this.state.setDepositRecheck();
        }
    }

    private void goToDepositPoint(InstructionSet instructionSet, Point2D robotPosition, Point2D depositPoint) {
        instructionSet.setData(this.robot.getRotatingPoint(), depositPoint, "Navigator: Deposit point current area");

        this.addTurnAndForward(instructionSet, robotPosition, depositPoint);
    }

    private void goToDepositOtherArea(InstructionSet instructionSet, Point2D robotPosition, Area currentBoard) {
        var currentSafePoint = currentBoard.getNearestSafePoint(robotPosition);
        var currentSafePointArea = Utils.rectangleWithCenter(currentSafePoint, 5);

        if (currentSafePointArea.contains(robotPosition)) {
            var nextArea = this.board.getAreaAfter(currentBoard);
            var newSafePoint = nextArea.getNearestSafePoint(robotPosition);

            instructionSet.setData(this.robot.getRotatingPoint(), newSafePoint, "Navigator: Deposit point - safe point next area");

            this.addTurnAndForward(instructionSet, robotPosition, newSafePoint);
        } else {
            instructionSet.setData(this.robot.getRotatingPoint(), currentSafePoint, "Navigator: Deposit point - safe point current area");

            this.addTurnAndForward(instructionSet, robotPosition, currentSafePoint);
        }
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
