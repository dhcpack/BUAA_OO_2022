import com.oocourse.elevator1.PersonRequest;

public class Passenger {
    private PersonRequest personRequest;
    private int dir;

    public Passenger(PersonRequest personRequest) {
        this.personRequest = personRequest;
        int from = personRequest.getFromFloor();
        int to = personRequest.getToFloor();
        if (to - from > 0) {
            dir = 1;
        } else {
            dir = -1;
        }
    }

    public int getId() {
        return personRequest.getPersonId();
    }

    public int getFromFloor() {
        return personRequest.getFromFloor();
    }

    public int getToFloor() {
        return personRequest.getToFloor();
    }

    public char getFromBuilding() {
        return personRequest.getFromBuilding();
    }

    public char getToBuilding() {
        return personRequest.getToBuilding();
    }

    public int getDir() {
        return dir;
    }

    @Override
    public String toString() {
        return personRequest.toString();
    }
}
