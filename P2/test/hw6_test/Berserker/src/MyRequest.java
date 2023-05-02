public class MyRequest {
    private final int id;
    private final int fromTower;
    private final int fromFloor;
    private final int toTower;
    private final int toFloor;
    private final boolean up;

    public MyRequest(int id, char fromTower, int fromFloor, char toTower, int toFloor) {
        this.id = id;
        this.fromTower = fromTower - 'A';
        this.fromFloor = fromFloor;
        this.toTower = toTower - 'A';
        this.toFloor = toFloor;
        up = toFloor > fromFloor;
    }

    public int getId() {
        return id;
    }

    public int getFromTower() {
        return fromTower;
    }

    public int getToTower() {
        return toTower;
    }

    public int getFromFloor() {
        return fromFloor;
    }

    public int getToFloor() {
        return toFloor;
    }

    public boolean isUp() {
        return up;
    }

    private char towerToChar(int t) {
        return (char) (t + 'A');
    }

    @Override
    public String toString() {
        return id + "-FROM-" + towerToChar(fromTower) + "-" + fromFloor
                + "-TO-" + towerToChar(toTower) + "-" + toFloor;
    }

}
