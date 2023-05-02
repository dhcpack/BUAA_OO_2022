package src.reqhandler;

import com.oocourse.elevator3.PersonRequest;
import src.io.OutputHandler;
import src.lift.LiftP;
import src.singleton.LiftData;

import java.util.ArrayList;
import java.util.Comparator;

public class MyPersonRequest {

    private final PersonRequest request;
    private final boolean vertical;
    private boolean hasFirstShifted;
    private boolean hasSecondShifted;
    private final int shiftFloor;

    public boolean isVertical() {
        return vertical;
    }

    public boolean isFirstShifted() {
        return hasFirstShifted;
    }

    public boolean isSecondShifted() {
        return hasSecondShifted;
    }

    public MyPersonRequest(PersonRequest req) {
        this.request = req;
        this.vertical = req.getFromBuilding() == req.getToBuilding();
        int access = (1 << (req.getFromBuilding() - 'A')) + (1 << (req.getToBuilding() - 'A'));
        ArrayList<LiftP> sortList = new ArrayList<>();
        LiftData.fetch().parallelLiftMap().forEach((k, v) -> {
            if ((v.getAccessibility() & access) == access) {
                sortList.add(v);
                OutputHandler.println("added lift " + v.getIdent() + " to sortList", true);
            }
        });
        sortList.sort(Comparator.comparingInt(o -> weight(o, req)));
        OutputHandler.println("finally chose lift " + sortList.get(0).getIdent(), true);
        this.shiftFloor = sortList.get(0).getFloor();

        if (vertical) {
            this.hasFirstShifted = true;
            this.hasSecondShifted = true;
        } else if (shiftFloor == req.getFromFloor()) {
            this.hasFirstShifted = true;
            this.hasSecondShifted = false;
        } else {
            this.hasFirstShifted = false;
            this.hasSecondShifted = false;
        }
    }

    private int weight(LiftP lift, PersonRequest req) {
        int upper = Integer.max(req.getFromFloor(), req.getToFloor());
        int lower = Integer.min(req.getFromFloor(), req.getToFloor());
        int floor = lift.getFloor();
        int speed = lift.getMoveDur();
        int boundWeight = (upper < floor || lower > floor) ?
                (upper < floor) ? 100 + 10 * (floor - upper) :
                        100 + 10 * (lower - floor) : 0;
        int speedWeight = speed / 25;
        int insidePressureWeight = lift.getInsideNum() * 13;
        int outsidePressureWeight = lift.getOutsideNum() * 7;
        return (boundWeight + speedWeight +
                insidePressureWeight + outsidePressureWeight);
    }

    public char getFromBuilding() {
        return (!vertical & hasSecondShifted) ? request.getToBuilding() : request.getFromBuilding();
    }

    public char getToBuilding() {
        return (!vertical & hasFirstShifted) ? request.getToBuilding() : request.getFromBuilding();
    }

    public int getFromFloor() {
        return (!vertical & hasFirstShifted) ? shiftFloor : request.getFromFloor();
    }

    public int getToFloor() {
        return (vertical || hasSecondShifted) ? request.getToFloor() : shiftFloor;
    }

    public void setFirstShifted() {
        this.hasFirstShifted = true;
    }

    public void setSecondShifted() {
        this.hasSecondShifted = true;
    }

    public int getPersonId() {
        return request.getPersonId();
    }
}
