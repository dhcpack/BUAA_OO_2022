public class MoveRequest {
    private final int personId;
    private int fromFloor;
    private int toFloor;
    private char fromBuilding;
    private char toBuilding;
    private char curBuilding;
    private int curFloor;
    private boolean lock;
    private char finalBuilding;
    private int finalFloor;

    public MoveRequest(int fromFloor, int toFloor, char fromBuilding,
                       char toBuilding, int personId) {
        this.lock = false;
        this.fromFloor = fromFloor;
        this.toFloor = toFloor;
        this.fromBuilding = fromBuilding;
        this.toBuilding = toBuilding;
        this.personId = personId;
        this.curBuilding = fromBuilding;
        this.curFloor = fromFloor;
        this.finalBuilding = toBuilding;
        this.finalFloor = toFloor;
    }

    public MoveRequest(int fromFloor, int toFloor, char fromBuilding,
                       char toBuilding, int personId, int curFloor, char curBuilding
            , int finalFloor, char finalBuilding) {
        this.lock = false;
        this.fromFloor = fromFloor;
        this.toFloor = toFloor;
        this.fromBuilding = fromBuilding;
        this.toBuilding = toBuilding;
        this.personId = personId;
        this.curBuilding = curBuilding;
        this.curFloor = curFloor;
        this.finalFloor = finalFloor;
        this.finalBuilding = finalBuilding;
    }

    public char getFinalBuilding() {
        return finalBuilding;
    }

    public void setFinalBuilding(char finalBuilding) {
        this.finalBuilding = finalBuilding;
    }

    public int getFinalFloor() {
        return finalFloor;
    }

    public void setFinalFloor(int finalFloor) {
        this.finalFloor = finalFloor;
    }

    public int getFromFloor() {
        return fromFloor;
    }

    public void setFromFloor(int fromFloor) {
        this.fromFloor = fromFloor;
    }

    public int getToFloor() {
        return toFloor;
    }

    public void setToFloor(int toFloor) {
        this.toFloor = toFloor;
    }

    public char getFromBuilding() {
        return fromBuilding;
    }

    public void setFromBuilding(char fromBuilding) {
        this.fromBuilding = fromBuilding;
    }

    public char getToBuilding() {
        return toBuilding;
    }

    public void setToBuilding(char toBuilding) {
        this.toBuilding = toBuilding;
    }

    public int getPersonId() {
        return personId;
    }

    public synchronized boolean isLock() {
        return lock;
    }

    public synchronized void setLock(boolean lock) {
        this.lock = lock;
    }

    public char getCurBuilding() {
        return curBuilding;
    }

    public void setCurBuilding(char curBuilding) {
        this.curBuilding = curBuilding;
    }

    public int getCurFloor() {
        return curFloor;
    }

    public void setCurFloor(int curFloor) {
        this.curFloor = curFloor;
    }

    public void arrive() {
        this.curBuilding = toBuilding;
        this.curFloor = toFloor;
    }

    public String toString() {
        return "mainPr:" + personId + " from: " + fromFloor + " to " + toFloor;
    }

    public MoveRequest clone() {
        MoveRequest t = new MoveRequest(this.fromFloor, this.toFloor,
                this.fromBuilding, this.toBuilding, this.personId,
                this.curFloor, this.curBuilding, this.finalFloor,
                this.finalBuilding);
        return t;
    }

}
