public class Position {
    private final char building;
    private final int floor;

    public Position(char building, int floor) {
        this.building = building;
        this.floor = floor;
    }

    public char getBuilding() {
        return building;
    }

    public int getFloor() {
        return floor;
    }
}
