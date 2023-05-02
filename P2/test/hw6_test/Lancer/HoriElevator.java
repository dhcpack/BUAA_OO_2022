import java.util.ArrayList;

public class HoriElevator extends Elevator {

    public HoriElevator(ArrayList<Integer> reachAble, ArrayList<Integer> stoppable,
                        int id, int capacity, char building, int floor, int movePerFloorTime,
                        int openTime, int closeTime, int beginFloor) {
        super(reachAble, stoppable, id, capacity, building, floor,
                movePerFloorTime, openTime, closeTime, beginFloor);
    }

    @Override
    public int getNext() {
        return (getCurrent() - 'A' + getDirection() + 5) % 5 + 'A';
    }

    @Override
    public void arrivePrint() {
        print("ARRIVE-" + (char) getCurrent() + "-" + getFloor() + "-" + getNUm());
    }

    @Override
    public void openPrint() {
        print("OPEN-" + (char) getCurrent() + "-" + getFloor() + "-" + getNUm());
    }

    @Override
    public void closePrint() {
        print("CLOSE-" + (char) getCurrent() + "-" + getFloor() + "-" + getNUm());
    }

    @Override
    public void inPrint(int passengerId) {
        print("IN-" + passengerId + "-" + (char) getCurrent() + "-" + getFloor() + "-" + getNUm());
    }

    @Override
    public void outPrint(int passengerId) {
        print("OUT-" + passengerId + "-" + (char) getCurrent() + "-" + getFloor() + "-" + getNUm());
    }
}
