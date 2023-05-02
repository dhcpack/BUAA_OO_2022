package taskone;

import com.oocourse.elevator3.PersonRequest;

import java.util.ArrayList;
import java.util.LinkedList;

public class WaitTable {
    private final int length = 10;
    private ArrayList<LinkedList<PersonRequest>> table = new ArrayList<>();

    public int judge(int floor) {
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < length; i++) {
            if (!table.get(i).isEmpty()) {
                min = Math.abs(floor - i - 1) > Math.abs(min) ? min : floor - i - 1;
            }
        }
        return min;
    }

    public boolean judgeUp(int floor) {
        for (int i = floor; i < length; i++) {
            if (!table.get(i).isEmpty()) { return true; }
        }
        return false;
    }

    public boolean judgeDown(int floor) {
        for (int i = floor - 2; i >= 0; i--) {
            if (!table.get(i).isEmpty()) { return true; }
        }
        return false;
    }

    public WaitTable() {
        for (int i = 0; i < length; i++) {
            table.add(new LinkedList<>());
        }
    }

    public synchronized void add(PersonRequest request) {
        table.get(request.getFromFloor() - 1).add(request);
    }

    public boolean isEmpty() {
        for (LinkedList<PersonRequest> personRequests : table) {
            if (!personRequests.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public synchronized LinkedList<PersonRequest> enterList(int floor, int num, int state) {
        int size = num;
        LinkedList<PersonRequest> list = table.get(floor - 1);
        LinkedList<PersonRequest> newList = new LinkedList<>();
        for (PersonRequest personRequest : list) {
            if (size == 0) { break; }
            if ((personRequest.getToFloor() - floor) * state >= 0) {
                newList.add(personRequest);
                size--;
            }
        }
        list.removeAll(newList);
        return newList;
    }

}
