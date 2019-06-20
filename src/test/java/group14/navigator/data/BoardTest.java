package group14.navigator.data;

import org.junit.Test;

import static org.junit.Assert.*;

public class BoardTest {

    private Board board = new Board(new Rectangle2D(0, 0, 200, 200));


    @Test
    public void hasDefaultSplit() {
        var areas = this.board.getAreas();

        assertEquals("Board areas is not correct size", 2, areas.size());

        // Assert left
        assertEquals(0, areas.get(0).getX(), 0);
        assertEquals(100, areas.get(0).getWidth(), 0);

        // Assert right
        assertEquals(100, areas.get(1).getX(), 0);
        assertEquals(100, areas.get(1).getWidth(), 0);
    }

    @Test
    public void hasCustomSplit() {
        var board = new Board(new Rectangle2D(0, 0, 200, 100), 0, new Rectangle2D(40, 40, 20, 20), 10);

        var areas = board.getAreas();

        assertEquals("Board areas is not correct size", 2, areas.size());

        // Assert left
        assertEquals(0, areas.get(0).getX(), 0);
        assertEquals(50, areas.get(0).getWidth(), 0);

        // Assert right
        assertEquals(50, areas.get(1).getX(), 0);
        assertEquals(150, areas.get(1).getWidth(), 0);
    }

    @Test
    public void contains() {
        assertTrue(this.board.contains(new Point2D(50, 50)));
        assertFalse(this.board.contains(new Point2D(200, 200)));
    }

    @Test
    public void getAreaForPoint() {
        assertEquals(this.board.getAreas().get(0), this.board.getAreaForPoint(new Point2D(30, 30)));
    }

    @Test
    public void getAreaAfter() {
        var areas = this.board.getAreas();
        var lastArea = areas.get(1);

        assertEquals(lastArea, this.board.getAreaAfter(areas.get(0)));
    }
}
