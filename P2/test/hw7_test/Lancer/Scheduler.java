import com.oocourse.elevator3.ElevatorInput;
import com.oocourse.elevator3.ElevatorRequest;
import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.Request;

import java.io.IOException;
import java.util.ArrayList;

public class Scheduler extends Thread {
    private ArrayList<WaitQueueVer> waitQueueVers;
    private ArrayList<WaitQueueHor> waitQueueHors;
    private ArrayList<ArrayList<ElevatorVer>> allElevatorVers;
    private ArrayList<ArrayList<ElevatorHor>> allElevatorHors;

    public Scheduler() {
        waitQueueVers = new ArrayList<WaitQueueVer>();
        waitQueueHors = new ArrayList<WaitQueueHor>();
        allElevatorVers = new ArrayList<ArrayList<ElevatorVer>>();
        allElevatorHors = new ArrayList<ArrayList<ElevatorHor>>();
    }

    public void init() {
        // 每座有一个总等待队列和总电梯列表
        for (int i = 0; i < 5; i++) {
            WaitQueueVer waitQueueVer = new WaitQueueVer();
            ElevatorVer elevatorVer = new ElevatorVer(i,
                    (i + 1), waitQueueVer, 600, 8, waitQueueHors);
            elevatorVer.start();
            waitQueueVers.add(waitQueueVer);
            allElevatorVers.add(new ArrayList<ElevatorVer>());
            allElevatorVers.get(i).add(elevatorVer);
        } // 每层有一个总等待队列和总电梯列表
        for (int i = 0; i < 10; i++) {
            waitQueueHors.add(new WaitQueueHor());
            allElevatorHors.add(new ArrayList<ElevatorHor>());
        }
        // 初始1层有一个横向电梯
        ElevatorHor elevatorHor = new ElevatorHor(0, 6,
                waitQueueHors.get(0), 600, 8, 31, waitQueueVers);
        elevatorHor.start();
        allElevatorHors.get(0).add(elevatorHor);
    }

    @Override
    public void run() {
        init();
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        PersonRequest personRequest = null;
        ElevatorRequest elevatorRequest = null;

        ArrayList<WaitQueueVer> tmpWaitVers = null;
        ArrayList<WaitQueueHor> tmpWaitHors = null;
        User tmpUser = null;
        WaitQueueVer tmpWaitVer;
        WaitQueueHor tmpWaitHor;
        ElevatorVer tmpElevatorVer = null;
        ElevatorHor tmpElevatorHor = null;
        while (true) {
            Request request = elevatorInput.nextRequest();
            if (request == null) {
                Output.finish(waitQueueVers, waitQueueHors);
                break;
            } else {
                if (request instanceof PersonRequest) {
                    Output.addIn();
                    personRequest = (PersonRequest)request;
                    tmpUser = new User(personRequest.getPersonId(),
                            personRequest.getFromBuilding() - 'A', personRequest.getFromFloor() - 1,
                            personRequest.getToBuilding() - 'A', personRequest.getToFloor() - 1);
                    dispatchToEle(tmpUser);
                } else if (request instanceof ElevatorRequest) {
                    elevatorRequest = (ElevatorRequest)request;
                    if (elevatorRequest.getType().equals("building")) { // 加竖直运动的电梯
                        int building = elevatorRequest.getBuilding() - 'A';
                        tmpElevatorVer = new ElevatorVer(building, elevatorRequest.getElevatorId(),
                                waitQueueVers.get(building),
                                (int)(elevatorRequest.getSpeed() * 1000),
                                elevatorRequest.getCapacity(), waitQueueHors);
                        tmpElevatorVer.start();
                        allElevatorVers.get(building).add(tmpElevatorVer);
                    } else {
                        int floor = elevatorRequest.getFloor() - 1;
                        tmpElevatorHor = new ElevatorHor(floor, elevatorRequest.getElevatorId(),
                                waitQueueHors.get(floor), (int)(elevatorRequest.getSpeed() * 1000),
                                elevatorRequest.getCapacity(), elevatorRequest.getSwitchInfo(),
                                waitQueueVers);
                        tmpElevatorHor.start();
                        allElevatorHors.get(floor).add(tmpElevatorHor);
                    }
                }
            }
        } try { elevatorInput.close(); }
        catch (IOException e) { e.printStackTrace(); }
    }

    public void dispatchToEle(User tmpUser) {
        if (tmpUser.getFromFloor() != tmpUser.getToFloor() &&
                (tmpUser.getFromZuo() == tmpUser.getToZuo() ||
                        canArr(tmpUser, tmpUser.getToFloor()))) { // 1.只纵向移动 2.要换乘，但是floor层有横向电梯直达
            tmpUser.setTmpZuo(tmpUser.getFromZuo());
            tmpUser.setDist(1);
            waitQueueVers.get(tmpUser.getFromZuo()).addWaiter(tmpUser);
            return;
        }
        if (tmpUser.getFromZuo() != tmpUser.getToZuo() && // 只要座不一样，就优先直接横向走
            canArr(tmpUser, tmpUser.getFromFloor())) {
            tmpUser.setTmpFloor(tmpUser.getFromFloor());
            tmpUser.setDist(2);
            waitQueueHors.get(tmpUser.getFromFloor()).addWaiter(tmpUser);
            return;
        }
        int fromFloor = tmpUser.getFromFloor();
        int toFloor = tmpUser.getToFloor();
        int chosenFloor = 0;
        int i = 1;
        while ((fromFloor + i) < 10 || (fromFloor - i) >= 0) {
            if (fromFloor + i < 10 && canArr(tmpUser, fromFloor + i)) {
                chosenFloor = getBetterFloor(chosenFloor, fromFloor, toFloor, fromFloor + i);
            } if (i != 0 && fromFloor - i >= 0 && canArr(tmpUser, fromFloor - i)) {
                chosenFloor = getBetterFloor(chosenFloor, fromFloor, toFloor, fromFloor - i);
            }
            i++;
        }
        tmpUser.setTmpZuo(tmpUser.getFromZuo());
        tmpUser.setTmpFloor(chosenFloor);
        tmpUser.setDist(1);
        waitQueueVers.get(tmpUser.getFromZuo()).addWaiter(tmpUser);
    }

    int getBetterFloor(int chosen, int from, int to, int tmp) {
        int dis = getDist(from, to, tmp);
        int chosenDis = getDist(from, to, chosen);
        if (dis < chosenDis) { return tmp; }
        if (chosenDis < dis) { return chosen; }

        ArrayList<ElevatorHor> arr = allElevatorHors.get(chosen);
        long sumTime1 = 0;
        long sumTime2 = 0;
        int cnt1 = 0;
        int cnt2 = 0;
        for (int i = 0; i < arr.size(); i++) {
            if ((arr.get(i).getSwitchInfo() & (1 << from)) != 0 &&
                    (arr.get(i).getSwitchInfo() & (1 << to)) != 0) {
                cnt1++;
                sumTime1 += arr.get(i).getMoveTime();
            }
        } arr = allElevatorHors.get(tmp);
        for (int i = 0; i < arr.size(); i++) {
            if ((arr.get(i).getSwitchInfo() & (1 << from)) != 0 &&
                    (arr.get(i).getSwitchInfo() & (1 << to)) != 0) {
                cnt2++;
                sumTime2 += arr.get(i).getMoveTime();
            }
        }
        if (sumTime1 * cnt2 < sumTime2 * cnt1) { return chosen; }
        if (sumTime1 * cnt1 > sumTime2 * cnt1) { return tmp; }
        if (waitQueueHors.get(chosen).getWait(31) * cnt2 <
            waitQueueHors.get(tmp).getWait(31) * cnt1) { return chosen; }
        return tmp;
    }

    int getDist(int from, int to, int tmp) {
        return Math.abs(from - tmp) + Math.abs(to - tmp);
    }

    boolean canArr(User user, int tmp) {
        int from = user.getFromZuo();
        int to = user.getTmpZuo();
        ArrayList<ElevatorHor> arr = allElevatorHors.get(tmp);
        for (int i = 0; i < arr.size(); i++) {
            if ((arr.get(i).getSwitchInfo() & (1 << from)) != 0 &&
                    (arr.get(i).getSwitchInfo() & (1 << to)) != 0) {
                return true;
            }
        } return false;
    }
}