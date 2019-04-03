package RoadPlanner;

public class Robot {


    enum Sector{
        UPPER,
        LOWER,
        RIGHT,
        LEFT
    }

    enum Compas{
        UP,
        DOWN,
        RIGHT,
        LEFT
    }

    Coordinate front;
    Coordinate rear;
    Coordinate mid;
    Vector vector;
    Sector sector;
    Compas compas;

    public Robot() {
        //For Planner constructor
    }

    public Robot(Coordinate front, Coordinate rear) {
        this.front = front;
        this.rear = rear;
        sector = Sector.UPPER;
        calcMid();
        calcVector();

    }

    public void setFront(Coordinate c) {
        this.front = c;
    }
    public void setRear(Coordinate c) {
        this.rear = c;
    }

    /**
     * @param front
     * @param back
     */
    public void update(Coordinate front, Coordinate back) {
        setFront(front);
        setRear(back);
        calcMid();
        calcVector();

    }

    public void calcMid() {
        this.mid = new Coordinate((front.x + rear.x)/2,(front.y + rear.y)/2);
    }

    /**
     *  Calculate the vector from 2 Coordinates c1 to c2.
     */
    public void calcVector() {
        vector = new Vector(front.x - mid.x, front.y - mid.y);
    }


}
