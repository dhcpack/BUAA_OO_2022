package taskone;

import com.oocourse.elevator3.PersonRequest;
import taskthree.PersonLink;
import tasktwo.Controller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Elevator implements Runnable {
    private final int speed;
    private final int max;
    public static final int UP = 1;
    public static final int DOWN = -1;
    public static final int STOP = 0;
    private int state = STOP;
    private int id;
    private boolean cancel = false;
    private RequestQueue requests;
    private WaitTable waitTable;
    private int floor = 1;
    private ArrayList<PersonRequest> persons = new ArrayList<>();
    private Output output;
    private char building;
    private Controller controller;

    public Elevator(RequestQueue requests, int id, Output output, char building,
                    WaitTable waitTable, int speed, int max, Controller controller) {
        this.requests = requests;
        this.id = id;
        this.output = output;
        this.building = building;
        this.waitTable = waitTable;
        this.speed = speed;
        this.max = max;
        this.controller = controller;
    }

    @Override
    public void run() {
        try {
            while (true) {
                List<PersonRequest> list = new LinkedList<>();
                requests.drainTo(list);
                if (state == STOP && list.isEmpty() &&
                        persons.isEmpty() && waitTable.isEmpty()) {
                    if (cancel) { break; }
                    PersonRequest request = requests.take();
                    if (request != null && request.getFromFloor() == 0) {
                        break;
                    }
                    list.add(request);
                    TimeUnit.MILLISECONDS.sleep(1);
                    requests.drainTo(list);
                }
                for (PersonRequest person : list) {
                    if (person != null && person.getFromBuilding() != '0') {
                        waitTable.add(person);
                    } else if (person != null) {
                        cancel = true;
                    }
                }
                open();
                trs();
                upDown();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void upDown() {
        if (state == STOP) { return; }
        try {
            if (state == UP) {
                floor++;
                TimeUnit.MILLISECONDS.sleep(speed);
                output.println(String.format("ARRIVE-%s-%d-%d",
                        building, floor, id));
            } else {
                floor--;
                TimeUnit.MILLISECONDS.sleep(speed);
                output.println(String.format("ARRIVE-%s-%d-%d",
                        building, floor, id));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void trs() {
        if (persons.isEmpty() && waitTable.isEmpty()) {
            state = STOP;
        }
        else if (persons.isEmpty() && state == STOP) {
            int tmp = waitTable.judge(floor);
            if (tmp == Integer.MAX_VALUE || tmp == 0) {
                state = STOP;
            } else {
                state = tmp > 0 ? DOWN : UP;
            }
        }
        else if (state == STOP) {
            int min = Integer.MAX_VALUE;
            for (PersonRequest person : persons) {
                min = Math.abs(floor - person.getToFloor()) > Math.abs(min) ?
                        min : floor - person.getToFloor();
            }
            if (min == 0) {
                state = STOP;
            } else {
                state = min < 0 ? UP : DOWN;
            }
        } else {
            int upMin = 0;
            int downMin = 0;
            for (PersonRequest person : persons) {
                if (person.getToFloor() - floor > 0) {
                    upMin = upMin == 0 ? person.getToFloor() :
                            Math.min(person.getToFloor() - floor, upMin);
                } else if (person.getToFloor() - floor < 0) {
                    downMin = Math.min(person.getToFloor() - floor, downMin);
                }
            }
            if (state == UP && upMin == 0 && !waitTable.judgeUp(floor)) {
                state = STOP;
            }
            if (state == DOWN && downMin == 0 && !waitTable.judgeDown(floor)) {
                state = STOP;
            }
        }
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
        List<PersonRequest> enterList = waitTable.enterList(floor, max - persons.size(), state);
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
                for (PersonRequest personRequest : list) {
                    if (personRequest.getFromBuilding() != '0') {
                        waitTable.add(personRequest);
                    } else { cancel = true; }
                }
                enterList.addAll(waitTable.enterList(floor, max -
                        persons.size() - enterList.size(), state));
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
