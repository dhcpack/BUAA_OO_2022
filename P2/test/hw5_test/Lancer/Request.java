public class Request {
    private int id;
    private int fromFloor;
    private int toFloor;
    private char fromBuilding;
    private char toBuilding;

    public Request(int id, int fromFloor, int toFloor, char fromBuilding, char toBuilding) {
        this.id = id;
        this.fromFloor = fromFloor;
        this.toFloor = toFloor;
        this.fromBuilding = fromBuilding;
        this.toBuilding = toBuilding;
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

    public char getFromBuilding() {
        return fromBuilding;
    }

    public char getToBuilding() {
        return toBuilding;
    }

    public boolean isUp() {
        return (toFloor - fromFloor) > 0;
    }

    @Override
    public String toString() {
        return "Request{" +
                "id=" + id +
                ", fromFloor=" + fromFloor +
                ", toFloor=" + toFloor +
                ", fromBuilding=" + fromBuilding +
                ", toBuilding=" + toBuilding +
                '}';
    }
}
