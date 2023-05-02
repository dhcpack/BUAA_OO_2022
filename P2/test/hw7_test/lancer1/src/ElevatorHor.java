import java.util.ArrayList;

public class ElevatorHor extends Thread {
    private WaitQueueHor waitQueueHor;
    private int floor;
    private int id;
    private long moveTime;
    private long openTime;
    private long closeTime;
    private int limit;
    private int switchInfo;
    private ArrayList<WaitQueueVer> waitQueueVers;

    private long millis;
    private int load; // 目前载人
    private int building;  // 目前所在座
    private int direction;  // 方向：0不动，1向右，2向左
    private ArrayList<ArrayList<User>> passengers;

    public ElevatorHor(int floor, int id, WaitQueueHor waitQueueHor, int moveTime, int limit,
                       int switchInfo, ArrayList<WaitQueueVer> waitQueueVers) {
        this.floor = floor;
        this.id = id;
        this.waitQueueHor = waitQueueHor;
        this.moveTime = moveTime;
        openTime = 200;
        closeTime = 200;
        this.limit = limit;
        load = 0;
        building = 0; // 代表A座
        direction = 0;
        passengers = new ArrayList<ArrayList<User>>(); // Passengers[i] 表示电梯内目标是 i+1 座的人
        for (int i = 0; i < 5; i++) {  // 这里是5
            passengers.add(new ArrayList<User>());
        }
        this.switchInfo = switchInfo;
        this.waitQueueVers = waitQueueVers;
        millis = System.currentTimeMillis();
    }

    public int getSwitchInfo() { return switchInfo; }

    public long getMoveTime() { return moveTime; }

    @Override
    public void run() {
        while (true) {
            if (load == 0 && waitQueueHor.noWaiterCur(switchInfo)) {
                direction = 0;
                waitQueueHor.trap(); //仅当：电梯中没人，等待队列中暂时没人，但输入未结束时，电梯闲置
            }
            checkAndExecute();
            if (load == 0 && waitQueueHor.isAllOver(switchInfo)) {
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
        //System.out.println("Hor, running" + id);
        if (waitQueueHor.noWaiterCur(switchInfo)) { return; }
        int goRight = 0;
        int goLeft = 0;
        //System.out.println("running");
        if ((switchInfo & (1 << building)) == 0) {
            goRight = goLeft = 0; // can not open at this building
        } else {
            goRight = waitQueueHor.getRightWaiterNum(building, switchInfo);
            goLeft = waitQueueHor.getLeftWaiterNum(building, switchInfo);
        }
        if (goRight + goLeft != 0) {
            openDoor();
            if (goRight >= goLeft) {
                direction = 1;
                load = waitQueueHor.getInRight(limit, load, building, passengers,
                        id, switchInfo);
            } else {
                direction = 2;
                load = waitQueueHor.getInLeft(limit, load, building, passengers,
                        id, switchInfo);
            } closeDoor();
        } if (goRight + goLeft == 0 || load == 0) { // 新加了考虑因素：switch
            direction = waitQueueHor.getNearestDir(building, switchInfo); // 看哪个方向最快遇到人，若没人返回0.
        }
    }

    public void checkAndRunRight() {
        int goOut = passengers.get(building).size();  //有人上或者下就开
        if (goOut != 0 || ((switchInfo & (1 << building)) != 0 && load < limit &&
                waitQueueHor.getRightWaiterNum(building, switchInfo) != 0)) {
            openDoor();                                     // TODO 改为：只要有人下电梯 | 只要有人上电梯且未坐满
            while (goOut != 0) { // 下
                User out = passengers.get(building).remove(--goOut);
                load--;
                Output.out("OUT-" + out.getId() + "-" + (char)(building + 'A') +
                        "-" + (floor + 1) + "-" + id);
                if (out.getTmpFloor() != out.getToFloor()) {
                    out.setFromFloor(floor);
                    out.setFromZuo(building);
                    out.setTmpZuo(out.getToZuo());
                    out.setTmpFloor(out.getToFloor());
                    out.setDist(1);
                    waitQueueVers.get(building).addWaiter(out);
                } else { Output.addOut(); }
            }
            if ((switchInfo & (1 << building)) != 0) {
                load = waitQueueHor.getInRight(limit, load, building,
                        passengers, id, switchInfo);
            } closeDoor();
        } if (load == 0 && !waitQueueHor.hasRWaiterRight(building, switchInfo)) {
            direction = 0;
            checkAndRunStop();
        } // 说明都到目的地了，重新指定起始方向。
    }

    public void checkAndRunLeft() {
        int goOut = passengers.get(building).size();   // 只要有人上或者下就开门
        if (goOut != 0 || ((switchInfo & (1 << building)) != 0 && load < limit &&
                waitQueueHor.getLeftWaiterNum(building, switchInfo) != 0)) {
            openDoor();
            while (goOut != 0) { // 下
                User out = passengers.get(building).remove(--goOut);
                load--;
                Output.out("OUT-" + out.getId() + "-" + (char)(building + 'A') +
                        "-" + (floor + 1) + "-" + id);
                if (out.getTmpFloor() != out.getToFloor()) {
                    out.setFromFloor(floor);
                    out.setFromZuo(building);
                    out.setTmpZuo(out.getToZuo());
                    out.setTmpFloor(out.getToFloor());
                    out.setDist(1);
                    waitQueueVers.get(building).addWaiter(out);
                } else { Output.addOut(); }
            }
            if ((switchInfo & (1 << building)) != 0) {
                load = waitQueueHor.getInLeft(limit, load, building,
                        passengers, id, switchInfo);
            } closeDoor();
        } if (load == 0 && !waitQueueHor.hasLWaiterLeft(building, switchInfo)) {
            direction = 0;
            checkAndRunStop();
        }
    }

    public void checkAndExecute() {
        if (direction == 0) { checkAndRunStop(); } // stop now
        else if (direction == 1) { checkAndRunRight(); } // go up
        else { checkAndRunLeft(); } // go down
        execute();
        millis = System.currentTimeMillis();
    }

    public void execute() {
        if (direction == 0) { return; }
        long deltaMillis = System.currentTimeMillis() - millis;
        if (deltaMillis < moveTime) {
            try { Thread.sleep(moveTime - deltaMillis); }
            catch (InterruptedException e) { e.printStackTrace(); }
        } if (direction == 1) { building = (building + 1) % 5; }
        else { building = (building + 4) % 5; }
        Output.out("ARRIVE-" + (char)(building + 'A') + "-" + (floor + 1) + "-" + id);
    }
}
