import com.oocourse.elevator2.PersonRequest;

public class MyPersonRequest {
    private static final int UP = 1;
    private static final int DOWN = -1;
    private static final int RIGHT = 1;
    private static final int LEFT = -1;

    private final PersonRequest personRequest;
    private final String type;

    public MyPersonRequest(PersonRequest personRequest) {
        this.personRequest = personRequest;
        if (personRequest.getFromBuilding() == personRequest.getToBuilding()) {
            type = "building";
        } else {
            type = "floor";
        }
    }

    public int getPersonId() {
        return personRequest.getPersonId();
    }

    public int getFromFloor() {
        return personRequest.getFromFloor();
    }

    public int getToFloor() {
        return personRequest.getToFloor();
    }

    public int getFromBuilding() {
        return personRequest.getFromBuilding() - 'A';
    }

    public int getToBuilding() {
        return personRequest.getToBuilding() - 'A';
    }

    public boolean isSameEndDirection(int direction, int currentFloor, int currentBuilding) {
        // 终点在当前方向上
        if (type.equals("building") &&
                ((personRequest.getToFloor() > currentFloor && direction == UP) ||
                        (personRequest.getToFloor() < currentFloor && direction == DOWN))) {
            return true;
        }
        if (type.equals("floor") && (((currentBuilding + direction + 5) % 5 == getToBuilding()) ||
                (currentBuilding + direction * 2 + 5) % 5 == getToBuilding())) {
            return true;
        }
        return false;
    }

    public boolean isSameStartDirection(int direction, int currentFloor, int currentBuilding) {
        // 起点在当前方向上
        if (type.equals("building") &&
                ((personRequest.getFromFloor() >= currentFloor && direction == UP) ||
                        (personRequest.getFromFloor() <= currentFloor && direction == DOWN))) {
            return true;
        }
        if (type.equals("floor") && (getFromBuilding() == currentBuilding ||
                (getFromBuilding() == (currentBuilding + direction + 5) % 5) ||
                (getFromBuilding() == (currentBuilding + direction * 2 + 5) % 5))) {
            return true;
        }
        return false;
    }
}
