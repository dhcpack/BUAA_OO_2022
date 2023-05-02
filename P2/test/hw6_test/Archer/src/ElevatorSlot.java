import java.util.Objects;

public class ElevatorSlot {
    private final MyElevatorRequest.ElevatorType type;
    private final int floorOrBuilding;

    public ElevatorSlot(MyElevatorRequest.ElevatorType type, int floorOrBuilding) {
        this.type = type;
        this.floorOrBuilding = floorOrBuilding;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ElevatorSlot that = (ElevatorSlot) o;
        return floorOrBuilding == that.floorOrBuilding && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, floorOrBuilding);
    }
}
