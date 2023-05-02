import java.util.ArrayList;
import java.util.Iterator;

public class FloorElevator extends Thread {
    private final RequestQueue requestQueue;
    private final OutputQueue outputQueue;
    private final int id;
    private int capacity;
    private int speed;
    private int openTime = 200;
    private int closeTime = 200;
    private int currentFloor;
    private char currentBuilding = 'A';
    private int dir;
    private ArrayList<Person> currentPersons = new ArrayList<>();
    private int switchInfo;
    private boolean isOpen = false;

    public FloorElevator(RequestQueue requestQueue, OutputQueue outputQueue,
                         int id, int currentFloor, int capacity, int speed, int switchInfo) {
        this.requestQueue = requestQueue;
        this.outputQueue = outputQueue;
        this.id = id;
        this.currentFloor = currentFloor;
        this.capacity = capacity;
        this.speed = speed;
        this.switchInfo = switchInfo;
        dir = 0;
    }

    private boolean checkNewTarget() {
        if (currentPersons.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isAccessible() {
        return ((switchInfo >> (currentBuilding - 'A')) & 1) == 1;
    }

    @Override
    public void run() {
        while (true) {
            // 结束条件
            if (requestQueue.isEnd() && requestQueue.isEmpty() && currentPersons.isEmpty()) {
                // System.out.println("Elevator floor-" + currentFloor + " over");
                return;
            }

            requestOut();

            // 因为横向电梯在获取方向时优先原则当前楼座，所以不需要在这里更新
            requestIn();

            closeDoor();

            // 如果没有请求，则获取请求
            if (checkNewTarget()) {
                // 注意这里可能导致wait
                int nextDir = requestQueue.getOneDir(currentBuilding, switchInfo);

                // 这部分似乎是多余的，且会导致等待队列为空时电梯无法停下
                //if (nextDir.equals("NONE")) {
                //    continue;
                //}
                dir = nextDir;
            }
            // 注意区分FromFloor和ToFloor

            move(dir);
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

    // 当电梯内没有乘客时，忽略当前的方向，以便载入新客人
    private int getDir() {
        if (currentPersons.isEmpty()) {
            return 0;
        }
        return dir;
    }

    private void move(int dir) {
        if (dir == 0) {
            return;
        }
        mySleep(speed);
        if (dir == 1) {
            if (currentBuilding == 'E') {
                currentBuilding = 'A';
            } else {
                currentBuilding++;
            }
        } else {
            if (currentBuilding == 'A') {
                currentBuilding = 'E';
            }
            else {
                currentBuilding--;
            }
        }
        outputQueue.print("ARRIVE-" + currentBuilding
                + "-" + currentFloor + "-" + id);
        // 走到可以开门的地方为止，用递归替代循环
        if (!isAccessible()) {
            move(dir);
        }
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
            if (person.getToBuilding() == currentBuilding) {
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
            if (person.getToBuilding() == currentBuilding) {
                return true;
            }
        }
        return false;
    }

    private boolean checkIn() {
        if (isFull() || !isAccessible()) {
            // 判断是否能开门
            return false;
        }
        int dir = getDir();
        return requestQueue.checkGivenFloorRequest(currentBuilding, dir, switchInfo);
    }

    private void requestIn() {
        synchronized (requestQueue) {
            while (!isFull()) {
                int dir = getDir();
                Person person = requestQueue.getGivenFloorRequest(currentBuilding, dir, switchInfo);
                if (person == null) {
                    return;
                }
                openDoor();
                currentPersons.add(person);
                outputQueue.print("IN-" + person.getPersonId()
                        + "-" + currentBuilding + "-" + currentFloor + "-" + id);
                updateCurrentDir();
                // updateCurrentTarget();
            }
        }
    }

    private int calcDir(char st, char ed) {
        // 这里st == ed时本来应该返回NONE，但实际好像不会出现这种情况
        int cntRight = ed - st;
        if (cntRight < 0) {
            cntRight += 5;
        }
        if (cntRight <= 2) {
            return 1;
        } else {
            return -1;
        }
    }

    private void updateCurrentDir() {
        if (currentPersons.isEmpty()) {
            dir = 0;
            return;
        }
        dir = calcDir(currentBuilding, currentPersons.get(0).getToBuilding());
    }

    private void openAndClose() {
        outputQueue.print("OPEN-" + currentBuilding +
                "-" + currentFloor + "-" + id);
        /*
        outputQueue.print(String.format("OPEN-%c-%d-%d",
                currentBuilding, currentFloor, id));

         */
        requestOut();
        requestIn();
        mySleep(openTime + closeTime);
        requestIn();
        outputQueue.print("CLOSE-" + currentBuilding +
                "-" + currentFloor + "-" + id);
        /*
        outputQueue.print(String.format("CLOSE-%c-%d-%d",
                currentBuilding, currentFloor, id));

         */
    }

    private void mySleep(int time) {
        try {
            sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
