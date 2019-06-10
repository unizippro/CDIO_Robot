package group14.road_planner.board;

import java.awt.*;
import java.util.List;

/**
 * Class whose purpose is to calculate board's methods
 */
public class BoardHelper {

    /**
     * Creates safepoints inside the board's quadrants
     *
     * @param quadrants
     */
    public void createPoints(List<Quadrant> quadrants) {
        for (int i = 0; i < quadrants.size(); i++) {
            if (i == 0) {
                Point p1 = new Point();
                Point p2 = new Point();
                p1.x = quadrants.get(i).getSafetyArea().getLowerLeft().x + (quadrants.get(i).getSafetyArea().getLowerRight().x - quadrants.get(i).getSafetyArea().getLowerLeft().x) / 2;
                p2.x = quadrants.get(i).getSafetyArea().getLowerLeft().x + (quadrants.get(i).getSafetyArea().getLowerRight().x - quadrants.get(i).getSafetyArea().getLowerLeft().x) / 2;
                p1.y = quadrants.get(i).getSafetyArea().getUpperLeft().y + (quadrants.get(i).getSafetyArea().getLowerLeft().y - quadrants.get(i).getSafetyArea().getUpperLeft().y) / 4;
                p2.y = quadrants.get(i).getSafetyArea().getLowerLeft().y - (quadrants.get(i).getSafetyArea().getLowerLeft().y - quadrants.get(i).getSafetyArea().getUpperLeft().y) / 4;
//                this.safePointLinkedList.add(p2);
//                this.safePointLinkedList.add(p1);
                quadrants.get(0).setSafePoints(p2, p1);

            }
            if (i == 1) {
                Point p1 = new Point();
                p1.x = quadrants.get(i).getSafetyArea().getLowerLeft().x + (quadrants.get(i).getSafetyArea().getLowerRight().x - quadrants.get(i).getSafetyArea().getLowerLeft().x) / 2;
                p1.y = quadrants.get(i).getSafetyArea().getUpperLeft().y + (quadrants.get(i).getSafetyArea().getLowerLeft().y - quadrants.get(i).getSafetyArea().getUpperLeft().y) / 2;
//                this.safePointLinkedList.add(p1);
                quadrants.get(1).setSafePoints(p1);
            }
            if (i == 2) {
                Point p1 = new Point();
                Point p2 = new Point();
                p1.x = quadrants.get(i).getSafetyArea().getLowerLeft().x + (quadrants.get(i).getSafetyArea().getLowerRight().x - quadrants.get(i).getSafetyArea().getLowerLeft().x) / 2;
                p2.x = quadrants.get(i).getSafetyArea().getLowerLeft().x + (quadrants.get(i).getSafetyArea().getLowerRight().x - quadrants.get(i).getSafetyArea().getLowerLeft().x) / 2;
                p1.y = quadrants.get(i).getSafetyArea().getUpperLeft().y + (quadrants.get(i).getSafetyArea().getLowerLeft().y - quadrants.get(i).getSafetyArea().getUpperLeft().y) / 4;
                p2.y = quadrants.get(i).getSafetyArea().getLowerLeft().y - (quadrants.get(i).getSafetyArea().getLowerLeft().y - quadrants.get(i).getSafetyArea().getUpperLeft().y) / 4;
//                this.safePointLinkedList.add(p1);
//                this.safePointLinkedList.add(p2);
                quadrants.get(2).setSafePoints(p1, p2);
            }
            if (i == 3) {
                Point p1 = new Point();
                p1.x = quadrants.get(i).getSafetyArea().getLowerLeft().x + (quadrants.get(i).getSafetyArea().getLowerRight().x - quadrants.get(i).getSafetyArea().getLowerLeft().x) / 2;
                p1.y = quadrants.get(i).getSafetyArea().getLowerLeft().y - (quadrants.get(i).getSafetyArea().getLowerLeft().y - quadrants.get(i).getSafetyArea().getUpperLeft().y) / 2;
//                this.safePointLinkedList.add(p1);
                quadrants.get(3).setSafePoints(p1);
            }
        }
        //This null is added so we can do a check and roll around back to element 0
//        this.safePointLinkedList.add(null);
    }

    public int getRobotPlacement(Point robotMid, Board board) {
        if (robotMid.x >= board.getQuadrants().get(0).getLowerLeft().x && robotMid.x <= board.getQuadrants().get(0).getLowerRight().x
            && robotMid.y >= board.getQuadrants().get(0).getUpperLeft().y && robotMid.y <= board.getQuadrants().get(0).getLowerRight().y) {
            return 0;
        }

        if (robotMid.x >= board.getQuadrants().get(1).getLowerLeft().x && robotMid.x <= board.getQuadrants().get(1).getLowerRight().x
            && robotMid.y >= board.getQuadrants().get(1).getUpperLeft().y && robotMid.y <= board.getQuadrants().get(1).getLowerRight().y) {
            return 1;
        }

        if (robotMid.x >= board.getQuadrants().get(2).getLowerLeft().x && robotMid.x <= board.getQuadrants().get(2).getLowerRight().x
                && robotMid.y >= board.getQuadrants().get(2).getUpperLeft().y && robotMid.y <= board.getQuadrants().get(2).getLowerRight().y) {
            return 2;
        }

        if (robotMid.x >= board.getQuadrants().get(3).getLowerLeft().x && robotMid.x <= board.getQuadrants().get(3).getLowerRight().x
                && robotMid.y >= board.getQuadrants().get(3).getUpperLeft().y && robotMid.y <= board.getQuadrants().get(3).getLowerRight().y) {
            return 3;
        }
        //shouldnt happen
        return 4;
    }
}
