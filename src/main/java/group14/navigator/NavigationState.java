package group14.navigator;

import group14.navigator.data.Point2D;

class NavigationState {

    private State state = State.NON_SAVEABLE;
    private Point2D point = null;

    public enum State {
        SAFE, NOT_SAFE, NON_SAVEABLE;
    }


    NavigationState() {
        this.resetState();
    }

    public Point2D getPoint() {
        return this.point;
    }

    void setSafe(Point2D point) {
        this.state = State.SAFE;
        this.point = point;
    }

    void setNotSafe(Point2D point) {
        this.state = State.NOT_SAFE;
        this.point = point;
    }

    boolean hasState() {
        return this.state != State.NON_SAVEABLE;
    }

    void resetState() {
        this.state = State.NON_SAVEABLE;
        this.point = null;
    }

    @Override
    public String toString() {
        return "NavigationState(" + this.state + ", " + this.point + ")";
    }
}
