import com.oocourse.TimableOutput;

import java.util.Vector;

public class Elevator extends Thread {
    private final RequestQueue waitList;
    private final Vector<RequestQueue> allCircleList;
    //电梯基本属性
    private char buildingName;
    private int elevatorId;
    private int moveTime; // 400ms
    private int openTime; // 200ms
    private int closeTime; // 200ms
    private int num;
    private int maxNum; // 6
    //电梯运动属性
    private int curFloor;
    private int targetFloor;
    private int direction; //1,-1,0
    private int state;
    private int opened;
    //乘客 -- 侯程表+内部乘客表
    private MoveRequest mainPr;
    private Vector<MoveRequest> processingList;
    //外部交互
    private boolean isTerminated;//是否终止
    private RequestQueue changeList;
    //
    private int lowestFloor;
    private int highestFloor;

    //    public Elevator(int id) {
    //        this.elevatorId = id;
    //        this.buildingName = (char) ((int) 'A' + id);
    //        moveTime = 400;
    //        openTime = 200;
    //        closeTime = 200;
    //        maxNum = 6;
    //        num = 0;
    //        curFloor = 1;
    //        targetFloor = 1;
    //        direction = 0;
    //        state = "waiting";
    //        mainPr = null;
    //        waitList = new RequestQueue();
    //        processingList = new Vector<>();
    //        isTerminated = false;
    //        lowestFloor = 1;
    //        highestFloor = 10;
    //    }

    public Elevator(int id, char buildingName, int parameterType, RequestQueue waitList,
                    Vector<RequestQueue> allCircleList, RequestQueue changeList) {
        this.elevatorId = id;
        this.buildingName = buildingName;
        this.waitList = waitList;
        this.allCircleList = allCircleList;
        this.opened = 0;
        this.state = 0; // wait
        this.changeList = changeList;
        switch (parameterType) {
            case 1:
                moveTime = 400;
                openTime = 200;
                closeTime = 200;
                maxNum = 6;
                num = 0;
                curFloor = 1;
                targetFloor = 1;
                direction = 0;
                mainPr = null;
                processingList = new Vector<>();
                isTerminated = false;
                lowestFloor = 1;
                highestFloor = 10;
                break;
            default:
                break;
        }
    }

    public void setTerminated() {
        isTerminated = true;
    }

    public char getBuildingName() {
        return buildingName;
    }

    public boolean isTerminated() {
        return isTerminated;
    }

    public int getCurFloor() {
        return curFloor;
    }

    public int getTargetFloor() {
        return targetFloor;
    }

    public int getDirection() {
        return direction;
    }

    private void setMainPr() {
        if (mainPr == null) {
            if (processingList.isEmpty()) {
                mainPr = waitList.peek();
                if (mainPr == null) {
                    state = 0;
                    return;
                }
                targetFloor = mainPr.getFromFloor();
            } else {
                mainPr = processingList.get(0);
                targetFloor = mainPr.getToFloor();
            }
        } else if (!processingList.contains(mainPr) && !processingList.isEmpty()) {
            //去接主请求的路上带上了人，就切换主请求
            //但如果要去接的主请求目标是边缘楼层，就不再更换主请求.不然及特殊情况会TLE
            if ((targetFloor != 10 && targetFloor != 1) || (num == maxNum)) {
                mainPr = processingList.get(0);
                targetFloor = mainPr.getToFloor();
            } else {
                targetFloor = mainPr.getFromFloor();
            }
        } else if (processingList.contains(mainPr)) {
            targetFloor = mainPr.getToFloor();
        } else if (mainPr.isLock()) {
            mainPr = null;
        } else {
            targetFloor = mainPr.getFromFloor();
        }
    }

    private int comp(int a, int b) {
        if (a > b) {
            return 1;
        } else if (a == b) {
            return 0;
        } else {
            return -1;
        }
    }

    public void open() {
        synchronized (TimableOutput.class) {
            TimableOutput.println("OPEN-" + buildingName + "-" + curFloor + "-" + elevatorId);
        }
    }

    public void close() {
        synchronized (TimableOutput.class) {
            TimableOutput.println("CLOSE-" + buildingName + "-" + curFloor + "-" + elevatorId);
        }
    }

    public void arrive() {
        synchronized (TimableOutput.class) {
            TimableOutput.println("ARRIVE-" + buildingName + "-" + curFloor + "-" + elevatorId);
        }
    }

    public boolean judgeIn() {
        synchronized (waitList) {
            if (num < maxNum) {
                int n = waitList.getLength();
                for (int i = n - 1; i >= 0; i--) {
                    MoveRequest pr = waitList.get(i);
                    if (pr.getFromFloor() == curFloor) {
                        if (mainPr == null) {
                            return true;
                        } else {
                            //捎带请求
                            if (comp(pr.getToFloor(), pr.getFromFloor()) == direction) {
                                return true;
                            } else if (direction == 0) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean judgeOut() {
        for (MoveRequest pr : processingList) {
            if (pr.getToFloor() == curFloor) {
                return true;
            }
        }
        return false;
    }

    public void prIn() {
        //IN-乘客ID-所在座-所在层-电梯ID
        synchronized (waitList) {
            int n = waitList.getLength();
            for (int i = n - 1; i >= 0; i--) {
                MoveRequest pr = waitList.get(i);
                if (num == maxNum) {
                    break;
                }
                if (pr.isLock()) {
                    continue;
                }
                if (pr.getFromFloor() == curFloor) {
                    if ((direction == 0) || (comp(pr.getToFloor(),
                            pr.getFromFloor()) == direction)) {
                        pr.setLock(true);
                        if (opened == 0) {
                            opened = 1;
                            open();
                        }
                        synchronized (TimableOutput.class) {
                            TimableOutput.println("IN-" + pr.getPersonId() + "-" +
                                    buildingName + "-" + curFloor + "-" + elevatorId);
                        }
                        processingList.add(pr);
                        waitList.del(i);
                        num++;
                        if (mainPr == null) {
                            mainPr = pr;
                            targetFloor = mainPr.getToFloor();
                            direction = comp(targetFloor, curFloor);
                        }

                    }
                }
            }
        }
    }

    public void prOut() {
        int n = processingList.size();
        for (int i = n - 1; i >= 0; i--) {
            MoveRequest pr = processingList.get(i);
            if (pr.getToFloor() == curFloor) {
                if (opened == 0) {
                    opened = 1;
                    open();
                }
                synchronized (TimableOutput.class) {
                    TimableOutput.println("OUT-" + pr.getPersonId() + "-" +
                            buildingName + "-" + curFloor + "-" + elevatorId);
                }
                pr.setLock(false);
                if (pr.getFinalBuilding() != buildingName) {
                    //最终要去的地方不是这一座楼
                    pr.setFromFloor(curFloor);
                    pr.setFromBuilding(buildingName);
                    pr.setToBuilding(pr.getFinalBuilding());
                    synchronized (allCircleList) {
                        allCircleList.get(curFloor - 1).addRequest(pr);
                    }
                } else {
                    changeList.remove(pr);
                }
                processingList.remove(i);
                num--;
            }
        }
        if (mainPr != null && mainPr.getToFloor() == curFloor) {
            if (!processingList.isEmpty()) {
                mainPr = processingList.get(0);
                targetFloor = mainPr.getToFloor();
                direction = comp(targetFloor, curFloor);
            } else {
                mainPr = null;
                direction = 0;
            }
        }
    }

    public void move(int d) {
        sleep(moveTime);
        curFloor += d;
        arrive();
    }

    public void run() {
        while (true) {
            if (waitList.isEnd() && waitList.isEmpty() && processingList.isEmpty()
                    && changeList.isEmpty() && changeList.isEnd()) {
                break;
            }
            //System.out.printf("thread ele %d + running \n",this.getId());
            prOut();
            if (opened == 1) {
                sleep(openTime + closeTime);
                prIn();
            } else if (opened == 0) {
                prIn();
                if (opened == 1) {
                    sleep(openTime + closeTime);
                    prIn();
                }
            }
            if (opened == 1) {
                opened = 0;
                close();
            }
            setMainPr();
            //System.out.println(mainPr.toString());
            direction = comp(targetFloor, curFloor);
            if (mainPr != null && direction != 0) {
                move(direction);
            }
        }
    }

    public void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
