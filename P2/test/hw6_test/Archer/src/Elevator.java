import java.util.ArrayList;

public abstract class Elevator extends Thread {
    public static final long OPEN_DOOR_INTERVAL = 200;
    public static final long CLOSE_DOOR_INTERVAL = 200;
    public static final long MOVING_INTERVAL_VERTICAL = 400;
    public static final long MOVING_INTERVAL_HORIZONTAL = 200;
    public static final int MAX_FLOOR = 10;
    public static final int MAX_CARRY = 6;
    private final RequestQueue requestQueue;
    private final int capacity;
    private final int id;
    private ArrayList<MyRequest> carrying;
    private int floor;
    private Building building;
    private long lastInvokeTime = 0; // in millis
    private ElevatorState elevatorState = ElevatorState.Closed;

    public Elevator(RequestQueue requestQueue, int capacity, Building building, int id, int floor) {
        this.requestQueue = requestQueue;
        this.capacity = capacity;
        this.building = building;
        this.id = id;
        this.carrying = new ArrayList<>();
        this.floor = floor;
    }

    public int getBuilding() {
        return building.ordinal();
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public long getLastInvokeTime() {
        return lastInvokeTime;
    }

    public void setLastInvokeTime(long lastInvokeTime) {
        this.lastInvokeTime = lastInvokeTime;
    }

    public ElevatorState getElevatorState() {
        return elevatorState;
    }

    public void setElevatorState(ElevatorState elevatorState) {
        this.elevatorState = elevatorState;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public int getCapacity() {
        return capacity;
    }

    public ArrayList<MyRequest> getCarrying() {
        return carrying;
    }

    public void setCarrying(ArrayList<MyRequest> carrying) {
        this.carrying = carrying;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public void openDoor() {
        this.setElevatorState(ElevatorState.Opening);
        OutputWrapper.println(String.format("OPEN-%s-%d-%d", building.toString(), floor, id));
    }

    public void beginCloseDoor() {
        this.setElevatorState(ElevatorState.Closing);
        this.setLastInvokeTime(System.currentTimeMillis());
    }

    public void endCloseDoor() {
        this.setElevatorState(ElevatorState.Closed);
        this.setLastInvokeTime(System.currentTimeMillis());
        OutputWrapper.println(String.format("CLOSE-%s-%d-%d", building.toString(), floor, id));
    }

    public void invitePerson(MyRequest request) {
        requestQueue.removeRequest(request);
        OutputWrapper.println(
                String.format("IN-%d-%s-%d-%d", request.getId(), building.toString(), floor, id)
        );
        this.carrying.add(request);

    }

    public void moveUp() {
        this.setElevatorState(ElevatorState.MovingUp);
        this.setLastInvokeTime(System.currentTimeMillis());
    }

    public void moveDown() {
        this.setElevatorState(ElevatorState.MovingDown);
        this.setLastInvokeTime(System.currentTimeMillis());
    }

    public void moveClockwise() {
        this.setElevatorState(ElevatorState.MovingClock);
        this.setLastInvokeTime(System.currentTimeMillis());
    }

    public void moveCounterClockwise() {
        this.setElevatorState(ElevatorState.MovingRClock);
        this.setLastInvokeTime(System.currentTimeMillis());
    }

    /**
     * 下客
     *
     * @param request 要下的乘客
     */
    public void dropPerson(MyRequest request) {
        OutputWrapper.println(
                String.format("OUT-%d-%s-%d-%d", request.getId(), building.toString(), floor, id)
        );
        this.carrying.remove(request);
    }

    public void upFloor() {
        this.floor++;
        arrive();
    }

    public void downFloor() {
        this.floor--;
        arrive();
    }

    public void clockwiseBuilding() {
        if (this.building.ordinal() == Building.values().length - 1) {
            this.building = Building.values()[0];
        } else {
            this.building = Building.values()[this.building.ordinal() + 1];
        }
        arrive();
    }

    public void counterClockwiseBuilding() {
        if (this.building.ordinal() == 0) {
            this.building = Building.values()[Building.values().length - 1];
        } else {
            this.building = Building.values()[this.building.ordinal() - 1];
        }
        arrive();
    }

    private void arrive() {
        OutputWrapper.println(String.format("ARRIVE-%s-%d-%d", building.toString(), floor, id));
        this.setElevatorState(ElevatorState.Closed);
        this.setLastInvokeTime(System.currentTimeMillis());
    }

    enum ElevatorState {
        Opened, // door is open
        MovingUp,
        MovingDown,
        MovingClock,
        MovingRClock,
        Opening, // door is opening
        Closing, // door is closing
        Closed, // only possible initially
    }

    enum Building {
        A,
        B,
        C,
        D,
        E,
    }
}
