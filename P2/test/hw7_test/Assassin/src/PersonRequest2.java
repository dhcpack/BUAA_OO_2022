import com.oocourse.elevator3.PersonRequest;

public class PersonRequest2 {
    private int midfloor;
    private char midblock;
    private int fromFloor; //改
    private final int toFloor;
    private char fromBuilding;//改
    private final char toBuilding;
    private int personId;
    private int flag;

    public PersonRequest2(PersonRequest request) {
        midfloor = request.getToFloor();
        midblock = request.getToBuilding();
        toBuilding = request.getToBuilding();
        fromBuilding = request.getFromBuilding();
        toFloor = request.getToFloor();
        fromFloor = request.getFromFloor();
        personId = request.getPersonId();
        flag = Const.FIRST;
    }

    public int getFlag() {
        return flag;
    }

    public void setToFloor(int toFloor) {
        this.midfloor = toFloor;
    }

    public void setToBuilding(char building) {
        this.midblock = building;
    }

    public int getFromFloor() {
        return fromFloor;
    }

    public int getToFloor() {
        return midfloor; //返回中转
    }

    public char getFromBuilding() {
        return fromBuilding;
    }

    public char getToBuilding() {
        return midblock; //返回中转
    }

    public int getPersonId() {
        return personId;
    }

    public void setFromFloor(int fromFloor) {
        this.fromFloor = fromFloor;
    }

    public void setFromBuilding(char fromBuilding) {
        this.fromBuilding = fromBuilding;
    }

    public boolean isarrive(int curfloor, int curblock) {
        char blo = (char) (curblock - 1 + 'A');
        return toFloor == curfloor && blo == toBuilding;
    }

    public boolean isfinish() {
        return fromBuilding == toBuilding && fromFloor == toFloor;
    }

    public void outpor() {
        midblock = toBuilding;
        midfloor = fromFloor;
    }

    public void flagAdd() {
        this.flag++;
    }

    public void outcro() {
        midblock = fromBuilding;
        midfloor = toFloor;
    }
}
