package camera;

import java.util.List;

public class CameraCoordinates {
    private List<Integer> board;
    private List<Integer> balls;
    private List<Integer> robot;

    /**
     * TODO: Generate coordinates for map
     */
    public CameraCoordinates() {
    }

    public List<Integer> getBoard() {
        return this.board;
    }

    public List<Integer> getBalls() {
        return this.balls;
    }

    public List<Integer> getRobot() {
        return this.robot;
    }
}
