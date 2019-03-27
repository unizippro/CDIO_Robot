import RoadPlanner.Planner;

public class Program {
    public static void main(String[] args) throws Exception {
        System.out.println("Starting program");
        Planner t = new Planner();
        System.out.println(t.nextInstruction());
    }
}