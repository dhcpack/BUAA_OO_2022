public class People {
    private int id;
    private int fromFloor;
    private int toFloor;
    private char building;

    public People(int id, int fromFloor, int toFloor, char building) {
        this.id = id;
        this.fromFloor = fromFloor;
        this.toFloor = toFloor;
        this.building = building;
    }

    public int getId() {
        return id;
    }

    public int getFromFloor() {
        return fromFloor;
    }

    public int getToFloor() {
        return toFloor;
    }

    public char getBuilding() {
        return building;
    }

    public int getDirection() {
        if (toFloor > fromFloor) {
            return 1;
        } else if (toFloor < fromFloor) {
            return -1;
        } else {
            return 0;
        }
    }

    public int floors() {
        return (toFloor - fromFloor) * getDirection();
    }

    public int compareTo(People other) {
        if (this.floors() < other.floors()) {
            return 1;
        } else {
            return -1;
        }
    }
}
