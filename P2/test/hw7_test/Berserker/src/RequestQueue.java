import java.util.ArrayList;
import java.util.Iterator;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class RequestQueue {
    private ArrayList<Person> persons = new ArrayList<>();
    private boolean isEnd = false;
    private String type;
    private char building;
    private int floor;

    public RequestQueue(String type, char building, int floor) {
        this.building = building;
        this.floor = floor;
        this.type = type;
    }

    public synchronized void setEnd(boolean isEnd) {
        this.isEnd = isEnd;
        notifyAll();
    }

    public synchronized boolean isEnd() {
        return isEnd;
    }

    public synchronized boolean isEmpty() {
        return persons.isEmpty();
    }

    public synchronized void addRequest(Person request) {
        persons.add(request);
        notifyAll();
    }

    // 纵向电梯方法
    public synchronized int getOneTarget(int currentFloor, int lastDir, boolean isWait) {
        if (!isEnd && persons.isEmpty()) {
            if (!isWait) {
                return currentFloor;
            }
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (persons.isEmpty()) {
            return 0;
        }
        int up = 0;
        int upMax = 1;
        int down = 0;
        int downMin = 10;
        int now = 0;
        for (Person person : persons) {
            int fromFloor = person.getFromFloor();
            if (fromFloor > currentFloor) {
                up++;
                upMax = max(upMax, fromFloor);
            } else if (fromFloor < currentFloor) {
                down++;
                downMin = min(downMin, fromFloor);
            } else {
                now++;
            }
        }

        if (up == 0 && down == 0) {
            return currentFloor;
        }

        // 优先拓展原方向
        if (lastDir == 1 && up > 0) {
            return upMax;
        } else if (lastDir == -1 && down > 0) {
            return downMin;
        }

        // 无法拓展原方向则优先默认无方向
        if (now > 0) {
            return currentFloor;
        }

        if (up > down) {
            return upMax;
        } else {
            return downMin;
        }
    }

    // 必须要获得，否则就wait
    public synchronized Person getOneRequest() {
        if (!isEnd && persons.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (persons.isEmpty()) {
            return null;
        }
        Person person = persons.get(0);
        persons.remove(0);
        notifyAll();
        return person;
    }

    public synchronized Person getGivenRequest(int floor, int dir, int lastDir) {
        // 优先参考dir，否则参考lastDir
        if (persons.isEmpty()) {
            return null;
        }
        Iterator<Person> iter = persons.iterator();

        boolean have = false;

        // 电梯目前无方向则优先带原方向的
        if (dir == 0) {
            Iterator<Person> iter2 = persons.iterator();
            while (iter2.hasNext()) {
                Person person = iter2.next();
                if (person.getFromFloor() == floor) {
                    int diff = person.getToFloor() - floor;
                    if ((lastDir > 0 && diff < 0) || (lastDir < 0 && diff > 0)) {
                        continue;
                    }
                    iter2.remove();
                    return person;
                }
            }
        }

        // 不带反方向的
        while (iter.hasNext()) {
            Person person = iter.next();
            if (person.getFromFloor() == floor) {
                int diff = person.getToFloor() - floor;
                if ((dir > 0 && diff < 0) || (dir < 0 && diff > 0)) {
                    continue;
                }
                iter.remove();
                return person;
            }
        }
        return null;
    }

    public synchronized boolean checkGivenRequest(int floor, int dir) {
        if (persons.isEmpty()) {
            return false;
        }
        Iterator<Person> iter = persons.iterator();
        while (iter.hasNext()) {
            Person person = iter.next();
            if (person.getFromFloor() == floor) {
                int diff = person.getToFloor() - floor;
                if ((dir > 0 && diff < 0) || (dir < 0 && diff > 0)) {
                    continue;
                }
                return true;
            }
        }
        return false;
    }

    // 横向电梯方法

    private boolean isAccessible(int switchInfo, char from, char to) {
        return (((switchInfo >> (from - 'A')) & 1) == 1) && (((switchInfo >> (to - 'A')) & 1) == 1);
    }

    public synchronized boolean checkGivenFloorRequest(char currentBuilding,
                                                       int dir, int switchInfo) {
        if (persons.isEmpty()) {
            return false;
        }
        Iterator<Person> iter = persons.iterator();
        while (iter.hasNext()) {
            Person person = iter.next();
            if (person.getFromBuilding() == currentBuilding &&
                    isAccessible(switchInfo, person.getFromBuilding(), person.getToBuilding())) {
                // /*
                int diff = calcDir(person.getFromBuilding(), person.getToBuilding());
                if ((dir == 1 && diff == -1)
                    || (dir == -1 && diff == 1)) {
                    continue;
                }
                // */
                return true;
            }
        }
        return false;
    }

    public synchronized Person getGivenFloorRequest(char currentBuilding, int dir, int switchInfo) {
        if (persons.isEmpty()) {
            return null;
        }
        Iterator<Person> iter = persons.iterator();
        while (iter.hasNext()) {
            Person person = iter.next();
            if (person.getFromBuilding() == currentBuilding
                    && isAccessible(switchInfo, person.getFromBuilding(), person.getToBuilding())) {
                // /*
                int diff = calcDir(person.getFromBuilding(), person.getToBuilding());
                if ((dir == 1 && diff == -1) ||
                        (dir == -1 && diff == 1)) {
                    continue;
                }
                // */
                iter.remove();
                return person;
            }
        }
        return null;
    }

    private synchronized boolean myIsEmpty(int switchInfo) {
        if (persons.isEmpty()) {
            return true;
        }

        for (Person person : persons) {
            char fromBuilding = person.getFromBuilding();
            char toBuilding = person.getToBuilding();
            if (isAccessible(switchInfo, fromBuilding, toBuilding)) {
                return false;
            }
        }
        return true;
    }

    public synchronized int getOneDir(char currentBuilding, int switchInfo) {
        if (!isEnd && myIsEmpty(switchInfo)) {
            // 注意此时有可能非空但没有合适的乘客，所以不能只判断队列是否为空，否则会轮询
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (persons.isEmpty()) {
            // 这里可以只判断是否为空
            return 0;
        }
        int right = 0;
        int left = 0;
        int now = 0;
        for (Person person : persons) {
            char fromBuilding = person.getFromBuilding();
            char toBuilding = person.getToBuilding();
            if (!isAccessible(switchInfo, fromBuilding, toBuilding)) {
                continue;
            }
            if (calcDir(currentBuilding, fromBuilding) == 1) {
                right++;
            } else if (calcDir(currentBuilding, fromBuilding) == -1) {
                left++;
            } else {
                now++;
            }
        }
        // notifyAll(); 没必要

        // 优先当前座
        if (now > 0) {
            return 0;
        }

        if (right == 0 && left == 0) {
            return 0;
        }
        // 前往较多的一侧
        if (right > left) {
            return 1;
        } else {
            return -1;
        }
    }

    private int calcDir(char st, char ed) {
        if (st == ed) {
            return 0;
        }
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

}
