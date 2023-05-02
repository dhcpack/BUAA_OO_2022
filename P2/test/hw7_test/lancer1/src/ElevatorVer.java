import java.util.ArrayList;

public class ElevatorVer extends Thread {
    private WaitQueueVer waitQueueVer;
    private int building;
    private int id;
    private long moveTime;
    private long openTime;
    private long closeTime;
    private int limit;
    private ArrayList<WaitQueueHor> waitQueueHors;

    private long millis; // 真正系统时间
    private int load;
    private int floor;  // 目前所在楼层
    private int direction;  // 方向：0不动，1向上，2向下
    private ArrayList<ArrayList<User>> passengers;

    public ElevatorVer(int building, int id, WaitQueueVer waitQueueVer, int moveTime, int limit,
            ArrayList<WaitQueueHor> waitQueueHors) {
        this.building = building;
        this.id = id;
        this.waitQueueVer = waitQueueVer;
        this.moveTime = moveTime;
        openTime = 200;
        closeTime = 200;
        this.limit = limit;
        load = 0;
        floor = 0;
        direction = 0;
        passengers = new ArrayList<ArrayList<User>>(); // Passengers[i] 表示电梯内目标是 i 层的人
        for (int i = 0; i < 10; i++) {
            passengers.add(new ArrayList<User>());
        }
        this.waitQueueHors = waitQueueHors;
        millis = System.currentTimeMillis();
    }

    @Override
    public void run() {
        while (true) {
            if (load == 0 && waitQueueVer.noWaiterCur()) {
                direction = 0;
                waitQueueVer.trap(); //仅当：电梯中没人，等待队列中暂时没人，但输入未结束时，电梯闲置
            }
            checkAndExecute();
            if (load == 0 && waitQueueVer.isAllOver()) {
                return;
            }
        }
    }

    public void openDoor() {
        Output.out("OPEN-" + (char)(building + 'A') + "-" + (floor + 1) + "-" + id);
        try { Thread.sleep(openTime + closeTime); } // 开门让人上电梯
        catch (InterruptedException e) { e.printStackTrace(); }
    }

    public void closeDoor() {
        Output.out("CLOSE-" + (char)(building + 'A') + "-" + (floor + 1) + "-" + id);
        millis = System.currentTimeMillis();
    }

    public void checkAndRunStop() {
        if (waitQueueVer.noWaiterCur()) { return; }
        int goUp = waitQueueVer.getUpWaiterNum(floor);
        int goDown = waitQueueVer.getDownWaiterNum(floor);
        if (goUp + goDown != 0) { // 有可能获取up down时还有人，结果中途被其它电梯带走了
            openDoor();
            if (goUp >= goDown) {
                direction = 1;
                load = waitQueueVer.getInUp(limit, load, floor, passengers, id);
            } else {
                direction = 2;
                load = waitQueueVer.getInDown(limit, load, floor, passengers, id);
            } closeDoor();
        } if (goUp + goDown == 0 || load == 0) {
            goUp = waitQueueVer.getUpSum(floor);
            goDown = waitQueueVer.getDownSum(floor);
            if (goUp + goDown == 0) { direction = 0; }
            else if (goUp >= goDown) { direction = 1; }
            else { direction = 2; }
        }
    }

    public void checkAndRunUp() {
        int goOut = passengers.get(floor).size(); // 只要有人上或者下就开
        if (goOut != 0 || (load < limit && waitQueueVer.getUpWaiterNum(floor) != 0)) {
            openDoor();
            while (goOut != 0) { // 下
                User out = passengers.get(floor).remove(--goOut);
                load--;
                Output.out("OUT-" + out.getId() + "-" + (char)(building + 'A') +
                        "-" + (floor + 1) + "-" + id);
                if (out.getTmpZuo() != out.getToZuo()) {
                    out.setFromZuo(building);
                    out.setFromFloor(floor);
                    out.setTmpZuo(out.getToZuo());
                    out.setTmpFloor(floor);
                    out.setDist(2);
                    waitQueueHors.get(floor).addWaiter(out);
                } else { Output.addOut(); }
            }
            load = waitQueueVer.getInUp(limit, load, floor, passengers, id);
            closeDoor();
        } if (load == 0 && !waitQueueVer.hasWaiterUp(floor)) {
            direction = 0;
            checkAndRunStop();
        } // 说明都到目的地了，重新指定起始方向。
    }

    public void checkAndRunDown() {
        int goOut = passengers.get(floor).size(); //有人上或者下就开
        if (goOut != 0 || (load < limit && waitQueueVer.getDownWaiterNum(floor) != 0)) {
            openDoor();
            while (goOut != 0) { // 下
                User out = passengers.get(floor).remove(--goOut);
                load--;
                Output.out("OUT-" + out.getId() + "-" + (char)(building + 'A') +
                        "-" + (floor + 1) + "-" + id);
                if (out.getTmpZuo() != out.getToZuo()) {
                    out.setFromZuo(building);
                    out.setFromFloor(floor);
                    out.setTmpZuo(out.getToZuo());
                    out.setTmpFloor(floor);
                    out.setDist(2);
                    waitQueueHors.get(floor).addWaiter(out);
                } else { Output.addOut(); }
            }
            load = waitQueueVer.getInDown(limit, load, floor, passengers, id);
            closeDoor();
        } if (load == 0 && !waitQueueVer.hasWaiterDown(floor)) {
            direction = 0;
            checkAndRunStop();
        }
    }

    public void checkAndExecute() {
        if (direction == 0) { checkAndRunStop(); } // stop now
        else if (direction == 1) { checkAndRunUp(); } // go up
        else { checkAndRunDown(); } // go down
        execute();
        millis = System.currentTimeMillis();
    }

    public void execute() {
        if (direction == 0) { return; }
        long deltaMillis = System.currentTimeMillis() - millis;
        if (deltaMillis < moveTime) {
            try { Thread.sleep(moveTime - deltaMillis); }
            catch (InterruptedException e) { e.printStackTrace(); }
        } if (direction == 1) { floor++; }
        else { floor--; }
        Output.out("ARRIVE-" + (char)(building + 'A') + "-" + (floor + 1) + "-" + id);
    }
}
