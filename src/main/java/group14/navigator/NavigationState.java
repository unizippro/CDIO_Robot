package group14.navigator;

import group14.navigator.data.Point2D;

class NavigationState {

    private State state = State.NONE;
    private Point2D point = null;

    public enum State {
        NONE,
        BALL_SAFE, BALL_NOT_SAFE,
        DEPOSIT, DEPOSIT_RECHECK,
        DANCE, DONE
    }


    NavigationState() {
        this.resetState();
    }

    public Point2D getPoint() {
        return this.point;
    }

    State getState() {
        return this.state;
    }

    void setSafe(Point2D point) {
        this.state = State.BALL_SAFE;
        this.point = point;
    }

    void setNotSafe(Point2D point) {
        this.state = State.BALL_NOT_SAFE;
        this.point = point;
    }

    void setDepositRecheck() {
        this.resetState();
        this.state = State.DEPOSIT_RECHECK;
    }

    void setHasPreDone() {
        this.resetState();
        this.state = State.DANCE;
    }

    void setDone() {
        this.resetState();
        this.state = State.DONE;
    }

    boolean hasBallState() {
        return this.state == State.BALL_SAFE || this.state == State.BALL_NOT_SAFE;
    }

    boolean hasPreDoneState() {
        return this.state == State.DANCE;
    }

    boolean hasDoneState() {
        return this.state == State.DONE;
    }

    void resetState() {
        this.state = State.NONE;
        this.point = null;
    }

    @Override
    public String toString() {
        return "NavigationState(" + this.state + ", " + this.point + ")";
    }
}
