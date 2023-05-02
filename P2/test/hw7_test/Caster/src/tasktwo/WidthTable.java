package tasktwo;

import com.oocourse.elevator3.PersonRequest;
import java.util.ArrayList;
import java.util.LinkedList;

public class WidthTable {
    private final int length = 5;
    private ArrayList<LinkedList<PersonRequest>> table = new ArrayList<>();

    public int judge(char building, boolean []flag) {
        int buildingId = building - 'A';
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < length; i++) {
            if (!table.get(i).isEmpty() && flag[i]) {
                synchronized (this) {
                    for (PersonRequest request : table.get(i)) {
                        if (flag[request.getToBuilding() - 'A']) {
                            int judge = buildingId - i;
                            judge = judge >= 0 ? judge : judge + length;
                            if (judge > max) {
                                max = judge;
                            }
                            if (judge < min) {
                                min = judge;
                            }
                        }
                    }
                }
            }
        }
        if (min == 0 || (min == Integer.MAX_VALUE && max == Integer.MIN_VALUE)) {
            return WidthElevator.STOP;
        }
        if (Math.abs(min - 3) <= Math.abs(max - 3)) {
            return WidthElevator.CLOCK;
        } else { return WidthElevator.COUNTER; }
    }

    public boolean judgeClock(char building) {
        for (int i = building - 'A', k = 0; k < 2; k++) {
            if (i == length - 1) { i = 0; }
            else { i++; }
            if (!table.get(i).isEmpty()) { return true; }
        }
        return false;
    }

    public boolean judgeCounter(char building) {
        for (int i = building - 'A', k = 0; k < 2; k++) {
            if (i == 0) { i = length - 1; }
            else { i--; }
            if (!table.get(i).isEmpty()) { return true; }
        }
        return false;
    }

    public int getPerson(boolean []judge) {
        int sum = 0;
        for (int i = 0; i < judge.length; i++) {
            if (judge[i]) {
                sum += table.get(i).size();
            }
        }
        return sum;
    }

    public WidthTable() {
        for (int i = 0; i < length; i++) {
            table.add(new LinkedList<>());
        }
    }

    public synchronized void add(PersonRequest request) {
        table.get(request.getFromBuilding() - 'A').add(request);
    }

    public boolean isEmpty(boolean []judge) {
        int i = 0;
        for (LinkedList<PersonRequest> personRequests : table) {
            if (!personRequests.isEmpty() &&  judge[i]) {
                synchronized (this) {
                    for (PersonRequest person : personRequests) {
                        if (judge[person.getToBuilding() - 'A']) {
                            return false;
                        }
                    }
                }
            }
            i++;
        }
        return true;
    }

    public synchronized LinkedList<PersonRequest> enterList(char building, int num, int state,
        boolean []flag) {
        int size = num;
        LinkedList<PersonRequest> list = table.get(building - 'A');
        LinkedList<PersonRequest> newList = new LinkedList<>();
        for (PersonRequest personRequest : list) {
            if (size == 0) { break; }
            int judge = personRequest.getToBuilding() - building;
            judge = judge >= 0 ? judge : judge + length;
            if ((state == WidthElevator.CLOCK && judge <= 2) ||
                    (state == WidthElevator.COUNTER && judge > 2)) {
                if (flag[personRequest.getToBuilding() - 'A']) {
                    newList.add(personRequest);
                    size--;
                }
            }
        }
        list.removeAll(newList);
        for (PersonRequest personRequest : list) {
            if (size == 0) { break; }
            if (flag[personRequest.getToBuilding() - 'A']) {
                newList.add(personRequest);
                size--;
            }
        }
        list.removeAll(newList);
        return newList;
    }

    @Override
    public String toString() {
        return "WidthTable{" +
                "table=" + table +
                '}';
    }
}
