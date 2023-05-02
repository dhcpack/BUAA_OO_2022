import java.util.ArrayList;
import java.util.Iterator;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Elevator extends Thread {
    private final RequestQueue requestQueue;
    private final OutputQueue outputQueue;
    private final int id;
    private int capacity;
    private int speed;
    private int openTime = 200;
    private int closeTime = 200;
    private int currentFloor = 1;
    private char currentBuilding;
    private int targetFloor;
    private ArrayList<Person> currentPersons = new ArrayList<>();
    private int lastDir = 0;
    private boolean isOpen = false;

    public Elevator(RequestQueue requestQueue, OutputQueue outputQueue,
                    char building, int id, int capacity, int speed) {
        this.requestQueue = requestQueue;
        this.outputQueue = outputQueue;
        this.currentBuilding = building;
        this.id = id;
        this.capacity = capacity;
        this.speed = speed;
        targetFloor = currentFloor;
    }

    private boolean checkNewTarget() {
        // 只有当前电梯内没人且当前楼层==目标楼层时才获取新目标
        if (currentPersons.isEmpty() && currentFloor == targetFloor) {
            return true;
        } else {
            return false;
        }
    }

    private void getNewTarget() {
        // 注意这里不wait
        int nextTarget = requestQueue.getOneTarget(currentFloor, lastDir, false);// 优先保持原方向
        if (nextTarget == 0) {
            return;
        }
        targetFloor = nextTarget;
    }

    @Override
    public void run() {
        while (true) {
            // 结束条件
            if (requestQueue.isEnd() && requestQueue.isEmpty() && currentPersons.isEmpty()) {
                // System.out.println("Elevator " + building + " over");
                return;
            }
            requestOut();

            if (checkNewTarget()) {
                getNewTarget();
            }

            requestIn();

            closeDoor();
            // 如果没有请求，则获取请求
            if (checkNewTarget()) {
                // 注意这里可能导致wait
                int nextTarget = requestQueue.getOneTarget(currentFloor, lastDir, true);// 优先保持原方向
                if (nextTarget == 0) {
                    continue;
                }
                targetFloor = nextTarget;
            }
            // 出发前先检查更新
            updateCurrentTarget();
            moveTo(targetFloor);
        }
    }

    private void openDoor() {
        if (!isOpen) {
            outputQueue.print("OPEN-" + currentBuilding +
                    "-" + currentFloor + "-" + id);
            isOpen = true;
        }
    }

    private void closeDoor() {
        if (isOpen) {
            mySleep(openTime + closeTime);
            requestIn();
            isOpen = false;
            outputQueue.print("CLOSE-" + currentBuilding +
                    "-" + currentFloor + "-" + id);
        }
    }

    private int getCurrentCapacity() {
        return currentPersons.size();
    }

    private boolean isFull() {
        if (getCurrentCapacity() > capacity) {
            System.out.println("Elevator" + id + "Overflow!!!");
        }
        return getCurrentCapacity() == capacity;
    }

    private int getDir() {
        if (targetFloor > currentFloor) {
            return 1;
        } else if (targetFloor < currentFloor) {
            return -1;
        } else {
            return 0;
        }
    }

    // 向着目标楼层移动一层
    private void moveTo(int toFloor) {
        if (currentFloor == toFloor) {
            return;
        }
        mySleep(speed);
        if (toFloor > currentFloor) {
            currentFloor++;
            lastDir = 1;
        } else {
            currentFloor--;
            lastDir = -1;
        }
        outputQueue.print("ARRIVE-" + currentBuilding
                + "-" + currentFloor + "-" + id);
        /*
        outputQueue.print(String.format("ARRIVE-%c-%d-%d",
                currentBuilding, currentFloor, id));
         */
    }

    private void checkEnd(Person person) {
        ArrayList<Person> nxt = person.getNxt();
        if (nxt.isEmpty()) {
            RequestCounter.getInstance().release();
        } else {
            Person nxtPerson = nxt.get(0);
            nxt.remove(0);
            WaitQueue.getInstance().addRequest(nxtPerson);
        }
    }

    private void requestOut() {
        Iterator<Person> iter = currentPersons.iterator();
        while (iter.hasNext()) {
            Person person = iter.next();
            if (person.getToFloor() == currentFloor) {
                openDoor();
                outputQueue.print("OUT-" + person.getPersonId()
                        + "-" + currentBuilding + "-" + currentFloor + "-" + id);
                checkEnd(person);
                iter.remove();
            }
        }
    }

    private boolean checkOut() {
        Iterator<Person> iter = currentPersons.iterator();
        while (iter.hasNext()) {
            Person person = iter.next();
            if (person.getToFloor() == currentFloor) {
                return true;
            }
        }
        return false;
    }

    private boolean checkIn() {
        if (isFull()) {
            return false;
        }
        if (checkNewTarget()) {
            getNewTarget();
        }
        int dir = getDir();
        return requestQueue.checkGivenRequest(currentFloor, dir);
    }

    private void requestIn() {
        synchronized (requestQueue) {
            while (!isFull()) {
                int dir = getDir();
                Person person = requestQueue.getGivenRequest(currentFloor, dir, lastDir);
                if (person == null) {
                    return;
                }
                openDoor();
                currentPersons.add(person);
                outputQueue.print("IN-" + person.getPersonId()
                        + "-" + currentBuilding + "-" + currentFloor + "-" + id);
                updateCurrentTarget();
            }
        }
    }

    private void updateCurrentTarget() {
        for (Person person : currentPersons) {
            if (person.getToFloor() > currentFloor) {
                targetFloor = max(targetFloor, person.getToFloor());
            } else {
                targetFloor = min(targetFloor, person.getToFloor());
            }
        }
    }

    private void openAndClose() {
        outputQueue.print("OPEN-" + currentBuilding +
                "-" + currentFloor + "-" + id);

        requestOut();
        if (checkNewTarget()) {
            getNewTarget();
        }
        requestIn();
        mySleep(openTime + closeTime);// 尝试换一下位置，让最先能走的抢到，可能会导致
        requestIn();
        outputQueue.print("CLOSE-" + currentBuilding +
                "-" + currentFloor + "-" + id);
    }

    private void mySleep(int time) {
        try {
            sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
