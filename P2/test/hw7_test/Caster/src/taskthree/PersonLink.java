package taskthree;

import com.oocourse.elevator3.PersonRequest;

public class PersonLink extends PersonRequest {

    public static final int WIDTH = 1;
    public static final int VERTICAL = 2;
    private int fromFloor;
    private int toFloor;
    private char toBuilding;
    private char fromBuilding;
    private int state;
    private PersonRequest person;

    public int getState() {
        return state;
    }

    public boolean isNoTrs() {
        return toBuilding == person.getToBuilding() &&
                toFloor == person.getToFloor();
    }

    public PersonLink(PersonRequest person, int toFloor, char toBuilding) {
        super(person.getFromFloor(), person.getToFloor(), person.getFromBuilding(),
                person.getToBuilding(), person.getPersonId());
        this.person = person;
        this.toFloor = toFloor;
        this.toBuilding = toBuilding;
        this.fromFloor = person.getFromFloor();
        this.fromBuilding = person.getFromBuilding();
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public int getFromFloor() {
        return fromFloor;
    }

    @Override
    public int getToFloor() {
        return toFloor;
    }

    @Override
    public char getToBuilding() {
        return toBuilding;
    }

    @Override
    public char getFromBuilding() {
        return fromBuilding;
    }

    public boolean transfer() {
        if (toFloor == person.getToFloor() && toBuilding == person.getToBuilding()) {
            return false;
        }
        fromBuilding = toBuilding;
        fromFloor = toFloor;
        if (toFloor == person.getToFloor()) {
            toBuilding = person.getToBuilding();
            state = WIDTH;
        } else if (toBuilding == person.getToBuilding()) {
            toFloor = person.getToFloor();
            state = VERTICAL;
        } else {
            toBuilding = person.getToBuilding();
            state = WIDTH;
        }
        return true;
    }

}
