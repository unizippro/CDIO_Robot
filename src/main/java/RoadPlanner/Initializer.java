package RoadPlanner;

import RoadPlanner.ball.BallList;
import RoadPlanner.board.Board;

public class Initializer {
    private Board board;
    private BallList ballList;

    public Initializer(Board board, BallList ballList) {
        this.board = board;
        this.ballList = ballList;
    }

    public void initialize() {
        this.findBalls();
        this.findBoard();
    }

    private void findBoard() {
        //TODO implement logic with OpenCV
    }

    private void findBalls() {
        //TODO implement logic with OpenCV
    }


}
