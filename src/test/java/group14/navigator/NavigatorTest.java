package group14.navigator;

import group14.navigator.data.Board;
import group14.navigator.data.Robot;
import group14.robot.data.Instruction;
import lejos.robotics.geometry.Point2D;
import lejos.robotics.geometry.Rectangle2D;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

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

    @Test
    public void completeNavigationTest() throws Exception {
        var robot = new Robot(new Point2D.Double(1, 1), new Point2D.Double(0, 0));
        var board = new Board(new Rectangle2D.Double(0, 0, 300, 200), new Point2D.Double(200, 100));
        var navigator = new Navigator(board, robot);

        navigator.updateBallPositions(Arrays.asList(
                new Point2D.Double(15, 15),
                new Point2D.Double(60, 60),
                new Point2D.Double(200, 40),
                new Point2D.Double(280, 80),
                new Point2D.Double(175, 175)
        ));


        /**
         * First instruction
         */
        var instructionSet = navigator.calculateInstructionSet();
        assertEquals(1, instructionSet.size(), 0);

        var instruction = instructionSet.poll();
        assertEquals(Instruction.InstructionType.FORWARD, instruction.getType());
        assertEquals(11.21, instruction.getAmount(), 0.1);


        robot.updateFromInstruction(instruction);
        navigator.updateBallPositions(Arrays.asList(
                new Point2D.Double(60, 60),
                new Point2D.Double(200, 40),
                new Point2D.Double(280, 80),
                new Point2D.Double(175, 175)
        ));


        /**
         * Second instruction
         */
        instructionSet = navigator.calculateInstructionSet();
        assertEquals(1, instructionSet.size(), 0);

        instruction = instructionSet.poll();
        assertEquals(Instruction.InstructionType.FORWARD, instruction.getType());
        assertEquals(63.64, instruction.getAmount(), 0.1);

        robot.updateFromInstruction(instruction);
        navigator.updateBallPositions(Arrays.asList(
                new Point2D.Double(200, 40),
                new Point2D.Double(280, 80),
                new Point2D.Double(175, 175)
        ));


        /**
         * Third instruction
         */
        instructionSet = navigator.calculateInstructionSet();
        assertEquals(2, instructionSet.size(), 0);

        instruction = instructionSet.poll();
        assertEquals(Instruction.InstructionType.TURN, instruction.getType());
        assertEquals(-52, instruction.getAmount(), 0.1);

        robot.updateFromInstruction(instruction);

        instruction = instructionSet.poll();
        assertEquals(Instruction.InstructionType.FORWARD, instruction.getType());
        assertEquals(129.54, instruction.getAmount(), 0.1);

        robot.updateFromInstruction(instruction);


        /**
         * Fourth instruction
         */
        instructionSet = navigator.calculateInstructionSet();
        assertEquals(3, instructionSet.size(), 0);

        instruction = instructionSet.poll();
        assertEquals(Instruction.InstructionType.TURN, instruction.getType());
        assertEquals(4.68, instruction.getAmount(), 0.1);

        robot.updateFromInstruction(instruction);

        instruction = instructionSet.poll();
        assertEquals(Instruction.InstructionType.FORWARD, instruction.getType());
        assertEquals(5, instruction.getAmount(), 0.1);

        robot.updateFromInstruction(instruction);

        instruction = instructionSet.poll();
        assertEquals(Instruction.InstructionType.BACKWARD, instruction.getType());
        assertEquals(5, instruction.getAmount(), 0.1);

        robot.updateFromInstruction(instruction);
        navigator.updateBallPositions(Arrays.asList(
                new Point2D.Double(280, 80),
                new Point2D.Double(175, 175)
        ));


        /**
         * Fifth instruction
         */
        instructionSet = navigator.calculateInstructionSet();
        assertEquals(2, instructionSet.size(), 0);

        instruction = instructionSet.poll();
        assertEquals(Instruction.InstructionType.TURN, instruction.getType());
        assertEquals(96.61, instruction.getAmount(), 0.1);

        robot.updateFromInstruction(instruction);

        instruction = instructionSet.poll();
        assertEquals(Instruction.InstructionType.FORWARD, instruction.getType());
        assertEquals(129.76, instruction.getAmount(), 0.1);

        robot.updateFromInstruction(instruction);
        navigator.updateBallPositions(Collections.singletonList(
                new Point2D.Double(280, 80)
        ));


        /**
         * Sixth instruction
         */
        instructionSet = navigator.calculateInstructionSet();
        assertEquals(2, instructionSet.size(), 0);

        instruction = instructionSet.poll();
        assertEquals(Instruction.InstructionType.TURN, instruction.getType());
        assertEquals(100.59, instruction.getAmount(), 0.1);

        robot.updateFromInstruction(instruction);

        instruction = instructionSet.poll();
        assertEquals(Instruction.InstructionType.FORWARD, instruction.getType());
        assertEquals(72.98, instruction.getAmount(), 0.1);

        robot.updateFromInstruction(instruction);


        /**
         * Seventh instruction
         */
        instructionSet = navigator.calculateInstructionSet();
        assertEquals(2, instructionSet.size(), 0);

        instruction = instructionSet.poll();
        assertEquals(Instruction.InstructionType.TURN, instruction.getType());
        assertEquals(164.62, instruction.getAmount(), 0.1);

        robot.updateFromInstruction(instruction);

        instruction = instructionSet.poll();
        assertEquals(Instruction.InstructionType.FORWARD, instruction.getType());
        assertEquals(140.17, instruction.getAmount(), 0.1);

        robot.updateFromInstruction(instruction);



        /**
         * Eighth instruction
         */
        instructionSet = navigator.calculateInstructionSet();
        assertEquals(2, instructionSet.size(), 0);

        instruction = instructionSet.poll();
        assertEquals(Instruction.InstructionType.TURN, instruction.getType());
        assertEquals(-62.94, instruction.getAmount(), 0.1);

        robot.updateFromInstruction(instruction);

        instruction = instructionSet.poll();
        assertEquals(Instruction.InstructionType.FORWARD, instruction.getType());
        assertEquals(73.30, instruction.getAmount(), 0.1);

        robot.updateFromInstruction(instruction);
        navigator.updateBallPositions(Collections.emptyList());


        /**
         * End
         */
        instructionSet = navigator.calculateInstructionSet();
        assertEquals(0, instructionSet.size(), 0);
    }

}