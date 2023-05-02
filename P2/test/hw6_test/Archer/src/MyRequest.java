import com.oocourse.elevator2.PersonRequest;

/**
 * transport request, same as PersonRequest in some sense.
 * it is immutable thus thread safe
 */
public class MyRequest {
    private final int fromBuilding;
    private final int toBuilding;
    private final int fromFloor;
    private final int toFloor;
    private final int id;

    public MyRequest(int fromBuilding, int toBuilding, int fromFloor, int toFloor, int id) {
        this.fromBuilding = fromBuilding;
        this.toBuilding = toBuilding;
        this.fromFloor = fromFloor;
        this.toFloor = toFloor;
        this.id = id;
    }

    public MyRequest(PersonRequest request) {
        this.fromBuilding = Elevator.Building.valueOf(
                String.valueOf(request.getFromBuilding())).ordinal();
        this.toBuilding = Elevator.Building.valueOf(
                String.valueOf(request.getToBuilding())).ordinal();
        this.fromFloor = request.getFromFloor();
        this.toFloor = request.getToFloor();
        this.id = request.getPersonId();
    }

    public int getFromBuilding() {
        return fromBuilding;
    }

    public int getToBuilding() {
        return toBuilding;
    }

    public int getFromFloor() {
        return fromFloor;
    }

    public int getToFloor() {
        return toFloor;
    }

    public int getId() {
        return id;
    }
}
