package group14.navigator;

import group14.navigator.data.Board;
import group14.navigator.data.Robot;
import lejos.robotics.geometry.Point2D;
import lejos.robotics.geometry.Rectangle2D;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class NavigatorTest {

    private Navigator navigator;
    private Board board;
    private Robot robot;

    @Before
    public void setUp() throws Exception {
        this.board = new Board(new Rectangle2D.Double(0, 0, 200, 200));
        this.robot = new Robot(new Point2D.Double(30, 33), new Point2D.Double(30, 35));
        this.navigator = new Navigator(this.board, this.robot);
    }

    @Test
    public void updateRobotPosition() {
    }

    @Test
    public void updateBallPositions() {
    }

    @Test
    public void calculateInstructionSet() throws Exception {
        this.navigator.updateBallPositions(Arrays.asList(
                new Point2D.Double(30, 20),
                new Point2D.Double(75, 75),
                new Point2D.Double(30, 80)
        ));

        var instructionSet = this.navigator.calculateInstructionSet();

        assertEquals(1, instructionSet.size(), 0);

        var instruction = instructionSet.poll();

        assertEquals(Instruction.InstructionType.FORWARD, instruction.getType());
        assertEquals(5, instruction.getAmount(), 0);

//        this.navigator.updateRobotPosition();
        this.navigator.updateBallPositions(Arrays.asList(
                new Point2D.Double(75, 75),
                new Point2D.Double(30, 80)
        ));

        instructionSet = this.navigator.calculateInstructionSet();

        assertEquals(2, instructionSet.size(), 0);
    }

    @Test
    public void getClosestBall() {
        assertNull(this.navigator.getClosestBall(this.robot.getRotatingPoint()));

        var closestBall = new Point2D.Double(40, 40);
        this.navigator.updateBallPositions(Arrays.asList(closestBall, new Point2D.Double(75, 75)));

        assertEquals(closestBall, this.navigator.getClosestBall(this.robot.getRotatingPoint()));
    }


    @Test
    public void ballsWithinArea() {
        assertFalse(this.navigator.ballsWithinArea(this.board.getAreas().get(0)));

        var closestBall = new Point2D.Double(40, 40);
        this.navigator.updateBallPositions(Arrays.asList(closestBall, new Point2D.Double(75, 75)));

        assertTrue(this.navigator.ballsWithinArea(this.board.getAreas().get(0)));
    }

}