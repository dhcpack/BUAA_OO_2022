import java.util.ArrayList;

public class Person {
    private int personId;
    private char fromBuilding;
    private int fromFloor;
    private char toBuilding;
    private int toFloor;
    private ArrayList<Person> nxt;

    public Person(int personId, char fromBuilding, int fromFloor, char toBuilding, int toFloor) {
        this.personId = personId;
        this.fromBuilding = fromBuilding;
        this.fromFloor = fromFloor;
        this.toBuilding = toBuilding;
        this.toFloor = toFloor;
        nxt = new ArrayList<>();
    }

    public Person(int personId, char fromBuilding,
                  int fromFloor, char toBuilding, int toFloor, ArrayList<Person> nxt) {
        this.personId = personId;
        this.fromBuilding = fromBuilding;
        this.fromFloor = fromFloor;
        this.toBuilding = toBuilding;
        this.toFloor = toFloor;
        this.nxt = nxt;
    }

    public void setNxt(ArrayList<Person> nxt) {
        this.nxt = nxt;
    }

    public ArrayList<Person> getNxt() {
        return nxt;
    }

    public int getPersonId() {
        return personId;
    }

    public char getFromBuilding() {
        return fromBuilding;
    }

    public int getFromFloor() {
        return fromFloor;
    }

    public char getToBuilding() {
        return toBuilding;
    }

    public int getToFloor() {
        return toFloor;
    }

    @Override
    public String toString() {
        return String.format("%d-FROM-%c-%d-TO-%c-%d", personId,
                fromBuilding, fromFloor, toBuilding, toFloor);
    }
}
