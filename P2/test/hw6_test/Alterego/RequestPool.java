import com.oocourse.elevator2.PersonRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Predicate;

public class RequestPool {
    private HashMap<Integer, PersonRequest> personPool;
    private boolean endSign;

    public RequestPool() {
        endSign = false;
        personPool = new HashMap<>();
    }

    public synchronized void addPersonReq(PersonRequest personRequest) {
        personPool.put(personRequest.getPersonId(), personRequest);
    }

    public synchronized void setEndSign(boolean endSign) {
        this.endSign = endSign;
    }

    public boolean isEndSign() {
        return endSign && personPool.isEmpty();
    }

    public synchronized PersonRequest[] getReq(
            char building, int floor, int dir,
            Predicate<Integer> predicate, boolean isVertical, boolean needCheckDir) {
        if (isVertical) {
            return personPool.values().stream().filter(
                personRequest -> {
                    int fromFloor = personRequest.getFromFloor();
                    int toFloor = personRequest.getToFloor();
                    char fromBuilding = personRequest.getFromBuilding();
                    return fromBuilding == building && fromFloor == floor &&
                            dir * (toFloor - fromFloor) >= 0 &&
                            fromFloor != toFloor &&
                            predicate.test(toFloor) && predicate.test(fromFloor);
                }
            ).toArray(PersonRequest[]::new);
        } else {
            return personPool.values().stream().filter(
                personRequest -> {
                    int fromFloor = personRequest.getFromFloor();
                    char fromBuilding = personRequest.getFromBuilding();
                    char toBuilding = personRequest.getToBuilding();
                    boolean reqSameDir = !needCheckDir || sameDir(fromBuilding, toBuilding, dir);
                    return fromBuilding == building && fromFloor == floor &&
                            reqSameDir &&
                            fromBuilding != toBuilding &&
                            predicate.test((int)fromBuilding) && predicate.test((int)toBuilding);
                }
            ).toArray(PersonRequest[]::new);
        }
    }

    public synchronized ArrayList<PersonRequest> getPersonReq(
            char building, int floor, int dir, int capacity,
            Predicate<Integer> predicate, boolean isVertical, boolean needCheckDir) {
        PersonRequest[] tempReq = getReq(building, floor, dir, predicate, isVertical, needCheckDir);
        ArrayList<PersonRequest> res = new ArrayList<>(
                Arrays.asList(tempReq).subList(0, (int) Math.min(capacity, tempReq.length)));
        res.forEach(u -> personPool.remove(u.getPersonId()));
        return res;
    }

    public synchronized boolean hasPersonReq(
            char building, int floor, int dir,
            Predicate<Integer> predicate, boolean isVertical, boolean needCheckDir) {
        //return getReq(building, floor, dir, predicate).length != 0;
        if (isVertical) {
            return personPool.values().stream().anyMatch(
                personRequest -> {
                    int fromFloor = personRequest.getFromFloor();
                    int toFloor = personRequest.getToFloor();
                    char fromBuilding = personRequest.getFromBuilding();
                    return fromBuilding == building && fromFloor == floor &&
                            dir * (toFloor - fromFloor) >= 0 &&
                            fromFloor != toFloor &&
                            predicate.test(toFloor) && predicate.test(fromFloor);
                }
            );
        } else {
            return personPool.values().stream().anyMatch(
                personRequest -> {
                    int fromFloor = personRequest.getFromFloor();
                    char fromBuilding = personRequest.getFromBuilding();
                    char toBuilding = personRequest.getToBuilding();
                    boolean reqSameDir = !needCheckDir || sameDir(fromBuilding, toBuilding, dir);
                    return fromBuilding == building && fromFloor == floor &&
                            reqSameDir &&
                            fromBuilding != toBuilding &&
                            predicate.test((int)fromBuilding) && predicate.test((int)toBuilding);
                }
            );
        }
    }

    public synchronized boolean hasReqAhead(
            char building, int floor, int dir, Predicate<Integer> predicate, boolean isVertical) {
        if (isVertical) {
            return personPool.values().stream().anyMatch(
                personRequest -> {
                    int fromFloor = personRequest.getFromFloor();
                    int toFloor = personRequest.getToFloor();
                    char fromBuilding = personRequest.getFromBuilding();
                    boolean dirCheck = dir == 0 || ((fromFloor - floor) * dir > 0);
                    return fromBuilding == building && dirCheck &&
                            fromFloor != toFloor &&
                            predicate.test(fromFloor) && predicate.test(toFloor);
                }
            );
        } else {
            return personPool.values().stream().anyMatch(
                personRequest -> {
                    int fromFloor = personRequest.getFromFloor();
                    char fromBuilding = personRequest.getFromBuilding();
                    char toBuilding = personRequest.getToBuilding();
                    boolean dirCheck = dir == 0 || sameDir(building, fromBuilding, dir);
                    return fromFloor == floor && dirCheck &&
                            fromBuilding != toBuilding &&
                            predicate.test((int)fromBuilding) && predicate.test((int)toBuilding);
                }
            );
        }
    }

    public boolean sameDir(char fromBuilding, char toBuilding, int dir) {
        return shortDir(fromBuilding, toBuilding) == dir;
    }

    public int shortDir(char fromBuilding, char toBuilding) {
        if (toBuilding > fromBuilding) {
            return (toBuilding - fromBuilding <= 2) ? 1 : -1;
        } else {
            return (fromBuilding - toBuilding <= 2) ? -1 : 1;
        }
    }
}