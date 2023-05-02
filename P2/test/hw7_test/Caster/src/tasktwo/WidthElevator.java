package tasktwo;

import com.oocourse.elevator3.PersonRequest;
import taskone.Output;
import taskthree.PersonLink;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class WidthElevator implements Runnable {
    private final int speed;
    private final int max;
    private final boolean [] judge;
    public static final int CLOCK = 1;
    public static final int COUNTER = -1;
    public static final int STOP = 0;
    private int state = STOP;
    private int index;
    private int id;
    private boolean cancel = false;
    private WidthQueue requests;
    private WidthTable widthTable;
    private int floor;
    private ArrayList<PersonRequest> persons = new ArrayList<>();
    private Output output;
    private char building = 'A';
    private Controller controller;

    public boolean[] getJudge() {
        return judge;
    }

    public WidthElevator(WidthQueue requests, int id, Output out, int floor, WidthTable table,
                         int speed, int max, int switchto, Controller controller) {
        this.requests = requests;
        this.id = id;
        this.output = out;
        this.floor = floor;
        this.max = max;
        this.speed = speed;
        this.widthTable = table;
        this.controller = controller;
        judge = new boolean[5];
        for (int i = 0; i < 5; i++) {
            judge[i] = ((switchto >> i) & 1) == 1;
        }
        switch (speed) {
            case 200:
                index = 100;
                break;
            case 400:
                index = 150;
                break;
            case 600:
                index = 200;
                break;
            default:
                break;
        }
    }

    public int getFloor() {
        return floor;
    }

    boolean canArrive(char fromBuilding, char toBuilding) {
        return judge[fromBuilding - 'A'] && judge[toBuilding - 'A'];
    }

    @Override
    public void run() {
        try {
            int num = 0;
            while (true) {
                List<PersonRequest> list = new LinkedList<>();
                requests.drainTo(list);
                if (state == STOP && list.isEmpty() &&
                        persons.isEmpty() && widthTable.isEmpty(judge)) {
                    if (cancel) { break; }
                    list.add(requests.take());
                    TimeUnit.MILLISECONDS.sleep(1);
                    requests.drainTo(list);
                }
                for (PersonRequest person : list) {
                    if (person != null && person.getFromBuilding() != '0') {
                        widthTable.add(person);
                    } else if (person != null) {
                        cancel = true;
                    }
                }
                if (judge[building - 'A']) {
                    open();
                }
                trs();
                clockCounter();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clockCounter() {
        if (state == STOP) {
            return;
        }
        try {
            if (state == CLOCK) {
                if (building == 'E') { building = 'A'; }
                else { building++; };
                TimeUnit.MILLISECONDS.sleep(speed);
                output.println(String.format("ARRIVE-%s-%d-%d", building, floor, id));
            } else {
                if (building == 'A') { building = 'E'; }
                else { building--; }
                TimeUnit.MILLISECONDS.sleep(speed);
                output.println(String.format("ARRIVE-%s-%d-%d", building, floor, id));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void trs() {
        if (persons.isEmpty() && widthTable.isEmpty(judge)) {
            state = STOP;
        }
        else if (persons.isEmpty() && state == STOP) {
            state = widthTable.judge(building, judge);

        } else {
            int min = Integer.MAX_VALUE;
            int max = Integer.MIN_VALUE;
            for (PersonRequest person : persons) {
                int judge = person.getToBuilding() - building;
                judge = judge > 0 ? judge : judge + 5;
                if (judge < min) { min = judge; }
                if (judge > max) { max = judge; }
            }
            if (state == CLOCK && min > 2 && !widthTable.judgeClock(building)) {
                state = STOP;
            }
            if (state == COUNTER && max < 3 && !widthTable.judgeCounter(building)) {
                state = STOP;
            }
            if (state == STOP) {
                if (!(min == Integer.MAX_VALUE) || !(max == Integer.MIN_VALUE)) {
                    state = Math.abs(min - 3) >= Math.abs(max - 3) ? CLOCK : COUNTER;
                }
            }
        }
    }

    public int getCrowding(int fromFloor, int toFloor, int[] table) {
        int base = 0;
        if (fromFloor != floor && toFloor != floor) {
            if ((floor > fromFloor && floor < toFloor) ||
                    (floor > toFloor && floor < fromFloor)) {
                base = speed / 10 * 15;
            } else {
                base = 900 + Math.min(Math.abs(floor - fromFloor),
                        Math.abs(floor - toFloor)) * index;
            }
        }
        int temp = widthTable.getPerson(judge) + persons.size() + table[floor - 1];
        if (temp > max) {
            base += (temp - max) * index;
        }
        return base;
    }

    private List<PersonRequest> judgeLeave() {
        List<PersonRequest> leaveList = new ArrayList<>();
        for (PersonRequest person : persons) {
            if (floor == person.getToFloor() && person.getToBuilding() == building) {
                leaveList.add(person);
            }
        }
        return leaveList;
    }

    private boolean open() {
        boolean flag = false;
        List<PersonRequest> leaveList = judgeLeave();
        if (leaveList.size() > 0) { flag = true; }
        persons.removeAll(leaveList);
        List<PersonRequest> enterList;
        enterList = widthTable.enterList(building, max - persons.size(), state, judge);
        if (!enterList.isEmpty() && !flag) {
            flag = true;
        }
        if (flag) {
            try {
                output.println(String.format("OPEN-%c-%d-%d",
                        building, floor, id));
                TimeUnit.MILLISECONDS.sleep(400);
                for (PersonRequest person : leaveList) {
                    output.println(String.format("OUT-%d-%c-%d-%d", person.getPersonId(),
                            building, floor, id));
                    if (person instanceof PersonLink) {
                        controller.add((PersonLink) person);
                    }
                }
                List<PersonRequest> list = new LinkedList<>();
                requests.drainTo(list);
                for (PersonRequest person : list) {
                    if (person != null && person.getFromBuilding() != '0') {
                        widthTable.add(person);
                    } else { cancel = true; }
                }
                enterList.addAll(widthTable.enterList(building,
                        max - persons.size() - enterList.size(), state, judge));
                persons.addAll(enterList);
                for (PersonRequest person : enterList) {
                    output.println(String.format("IN-%d-%c-%d-%d", person.getPersonId(),
                            building, floor, id));
                }
                output.println(String.format("CLOSE-%c-%d-%d",
                        building, floor, id));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }
}
