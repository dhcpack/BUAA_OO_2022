import com.oocourse.elevator2.PersonRequest;

import java.util.Arrays;

public class Person {
    private int fromFloor;
    private int toFloor;
    private char fromBuilding;
    private char toBuilding;
    private int personId;
    private int desFloor;
    private char desBuilding;
    private boolean needTransfer;
    private long timeFinishedTransfer;

    public Person(PersonRequest personRequest, int transFloor) {
        //super(fromFloor, toFloor, fromBuilding, toBuilding, personId);
        this.fromBuilding = personRequest.getFromBuilding();
        this.toBuilding = personRequest.getToBuilding();
        this.fromFloor = personRequest.getFromFloor();
        this.toFloor = personRequest.getToFloor();
        this.personId = personRequest.getPersonId();
        if (this.fromBuilding != this.toBuilding) {
            desBuilding = toBuilding;
            desFloor = toFloor;
            toBuilding = fromBuilding;
            toFloor = transFloor;
            needTransfer = true;
        } else {
            desBuilding = 0;
            desFloor = 0;
            needTransfer = false;
        }
    }

    public void setTimeFinishedTransfer(long timeFinishedTransfer) {
        this.timeFinishedTransfer = timeFinishedTransfer;
    }

    public long getTimeFinishedTransfer() {
        return timeFinishedTransfer;
    }

    public void transfer() {
        fromBuilding = desBuilding;
        fromFloor = toFloor;
        toBuilding = desBuilding;
        toFloor = desFloor;
        desBuilding = 0;
        desFloor = 0;
    }

    public boolean needTransfer() {
        return (desBuilding != 0);
    }

    public boolean isNeedTransfer() {
        return needTransfer;
    }

    public int getFromFloor() {
        return fromFloor;
    }

    public int getToFloor() {
        return toFloor;
    }

    public char getFromBuilding() {
        return fromBuilding;
    }

    public char getToBuilding() {
        return toBuilding;
    }

    public int getPersonId() {
        return personId;
    }

    @Override
    public String toString() {
        return String.format("%d-FROM-%c-%d-TO-%c-%d",
                personId, fromBuilding, fromFloor, toBuilding, toFloor);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new int[]{
            this.personId, this.fromFloor, this.toFloor, this.fromBuilding, this.toBuilding});
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof Person) {
            return (((Person) obj).fromFloor == this.fromFloor)
                    && (((Person) obj).toFloor == this.toFloor)
                    && (((Person) obj).personId == this.personId)
                    && (((Person) obj).fromBuilding == this.fromBuilding)
                    && (((Person) obj).toBuilding == this.toBuilding);
        } else {
            return false;
        }
    }
}
