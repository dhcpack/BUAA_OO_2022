import com.oocourse.elevator2.ElevatorRequest;

public class MyElevatorRequest {
    private final ElevatorType type;
    private final int id;
    private final int building;
    private final int floor;

    public MyElevatorRequest(ElevatorRequest request) {
        if (request.getType().equals("building")) {
            type = ElevatorType.Vertical;
            building = Elevator.Building.valueOf(
                    String.valueOf(request.getBuilding())).ordinal();
            floor = -1;
        } else {
            type = ElevatorType.Horizontal;
            building = -1;
            floor = request.getFloor();
        }
        id = request.getElevatorId();

    }

    public ElevatorType getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public int getBuilding() {
        return building;
    }

    public int getFloor() {
        return floor;
    }

    enum ElevatorType {
        Vertical,
        Horizontal,
    }
}
