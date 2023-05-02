import java.util.ArrayList;

public class MyPerson {
    private final int personId;
    // 1. (buildingFr, floorFr) -> (buildingFr, floorMid)
    // 2. (buildingFr, floorMid) -> (buildingTo, floorMid)
    // 3. (buildingTo, floorMid) -> (buildingTo, floorTo)

    private Position currPos;
    private final ArrayList<Position> steps = new ArrayList<>();

    public MyPerson(int pId, int floorFrom, int floorMid, int floorTo, char buildingFr,
            char buildingTo) {
        this.personId = pId;
        this.currPos = new Position(buildingFr, floorFrom);
        if (buildingFr != buildingTo) {
            if (floorFrom != floorMid) {
                steps.add(new Position(buildingFr, floorMid));
            }
            if (floorMid != floorTo) {
                steps.add(new Position(buildingTo, floorMid));
            }
        }
        steps.add(new Position(buildingTo, floorTo));
    }

    public boolean stepDone() {
        if (steps.isEmpty()) {
            return false;
        }
        currPos = steps.remove(0);
        return !steps.isEmpty();
    }

    public char getCurrBuilding() {
        return currPos.getBuilding();
    }

    public int getCurrFloor() {
        return currPos.getFloor();
    }

    public char getNextBuilding() {
        return steps.get(0).getBuilding();
    }

    public int getNextFloor() {
        return steps.get(0).getFloor();
    }

    public int getPersonId() {
        return personId;
    }
}
