import java.util.ArrayList;

public class VerElevator extends Elevator {

    public VerElevator(ArrayList<Integer> reachAble, ArrayList<Integer> stoppable, int id,
                       int capacity, char building, int floor, int movePerFloorTime,
                       int openTime, int closeTime, int beginFloor) {
        super(reachAble, stoppable, id, capacity, building, floor,
                movePerFloorTime, openTime, closeTime, beginFloor);
    }
}
