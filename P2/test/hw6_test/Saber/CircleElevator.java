import com.oocourse.TimableOutput;

import java.util.ArrayList;
import java.util.Vector;

public class CircleElevator extends Thread {
    private final RequestQueue waitList; // from circle list
    private final ArrayList<RequestQueue> allBuildingList;
    //电梯基本属性
    private int floor;
    private int elevatorId;
    private int moveTime; // 400ms
    private int openTime; // 200ms
    private int closeTime; // 200ms
    private int num;
    private int maxNum; // 6
    //电梯运动属性
    private char curBuilding;
    private char targetBuilding;
    private int direction; //1,-1,0 顺时针 a-b-c-d-e-a 为正
    private String state;
    private int opened;
    //乘客 -- 侯程表+内部乘客表
    private MoveRequest mainPr;
    private Vector<MoveRequest> processingList;
    //交互
    private RequestQueue changeList;

    public CircleElevator(int id, int floor, int parameterType, RequestQueue waitList,
                          ArrayList<RequestQueue> allBuildingList, RequestQueue changeList) {
        switch (parameterType) {
            case 1:
                moveTime = 200;
                openTime = 200;
                closeTime = 200;
                maxNum = 6;
                num = 0;
                direction = 0;
                state = "waiting";
                mainPr = null;
                processingList = new Vector<>();
                this.elevatorId = id;
                this.floor = floor;
                this.curBuilding = 'A';
                this.targetBuilding = 'A';
                break;
            default:
                break;
        }
        this.changeList = changeList;
        this.waitList = waitList;
        this.opened = 0;
        this.allBuildingList = allBuildingList;
    }

    private void setMainPr() {
        if (mainPr == null) {
            if (processingList.isEmpty()) {
                mainPr = waitList.peek();
                if (mainPr == null) {
                    return;
                }
                targetBuilding = mainPr.getFromBuilding();
            } else {
                mainPr = processingList.get(0);
                targetBuilding = mainPr.getToBuilding();
            }
        } else if (!processingList.contains(mainPr) && !processingList.isEmpty()) {
            //去接主请求的路上带上了人，就切换主请求
            mainPr = processingList.get(0);
            targetBuilding = mainPr.getToBuilding();
        } else if (processingList.contains(mainPr)) {
            targetBuilding = mainPr.getToBuilding();
        } else if (mainPr.isLock()) {
            mainPr = null;
        } else {
            targetBuilding = mainPr.getFromBuilding();
        }
    }

    private int comp(char a, char b) {
        int x = a - 'A';
        int y = b - 'A';
        if (x == y) {
            return 0;
        } else if (y == (x + 1) % 5 || y == (x + 2) % 5) {
            return -1;
        } else if (x == (y + 1) % 5 || x == (y + 2) % 5) {
            return 1;
        } else {
            return 0;
        }
    }

    public void open() {
        synchronized (TimableOutput.class) {
            TimableOutput.println("OPEN-" + curBuilding + "-" + floor + "-" + elevatorId);
        }
    }

    public void close() {
        synchronized (TimableOutput.class) {
            TimableOutput.println("CLOSE-" + curBuilding + "-" + floor + "-" + elevatorId);
        }
    }

    public void arrive() {
        synchronized (TimableOutput.class) {
            TimableOutput.println("ARRIVE-" + curBuilding + "-" + floor + "-" + elevatorId);
        }
    }

    public boolean judgeIn() {
        synchronized (waitList) {
            int n = waitList.getLength();
            if (num < maxNum) {
                for (int i = n; i >= 0; i--) {
                    MoveRequest pr = waitList.get(i);
                    if (pr.getFromBuilding() == curBuilding) {
                        if (mainPr == null) {
                            return true;
                        } else {
                            //捎带请求
                            if (comp(pr.getToBuilding(), pr.getFromBuilding()) == direction) {
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
            if (pr.getToBuilding() == curBuilding) {
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
                if (pr.getFromBuilding() == curBuilding) {
                    if ((direction == 0) || (comp(pr.getToBuilding(),
                            pr.getFromBuilding()) == direction)) {
                        if (opened == 0) {
                            opened = 1;
                            open();
                        }
                        synchronized (TimableOutput.class) {
                            TimableOutput.println("IN-" + pr.getPersonId() + "-" +
                                    curBuilding + "-" + floor + "-" + elevatorId);
                        }
                        pr.setLock(true);
                        processingList.add(pr);
                        waitList.del(i);
                        num++;
                        if (mainPr == null) {
                            mainPr = pr;
                            targetBuilding = mainPr.getToBuilding();
                            direction = comp(targetBuilding, curBuilding);
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
            if (pr.getToBuilding() == curBuilding) {
                if (opened == 0) {
                    opened = 1;
                    open();
                }
                synchronized (TimableOutput.class) {
                    TimableOutput.println("OUT-" + pr.getPersonId() + "-" +
                            curBuilding + "-" + floor + "-" + elevatorId);
                }
                pr.setLock(false);
                if (pr.getFinalFloor() != floor) {
                    //最终要去的地方不是这一层
                    pr.setFromFloor(floor);
                    pr.setToFloor(pr.getFinalFloor());
                    pr.setFromBuilding(curBuilding);
                    pr.setToBuilding(pr.getFinalBuilding());
                    synchronized (allBuildingList) {
                        allBuildingList.get(curBuilding - 'A').addRequest(pr);
                    }
                    //allCircleList.get(curFloor - 1).addRequest(pr);
                } else {
                    changeList.remove(pr);
                }
                processingList.remove(i);
                num--;
            }
        }
        if (mainPr != null && mainPr.getToBuilding() == curBuilding) {
            if (!processingList.isEmpty()) {
                mainPr = processingList.get(0);
                targetBuilding = mainPr.getToBuilding();
                direction = comp(targetBuilding, curBuilding);
            } else {
                mainPr = null;
                direction = 0;
            }
        }
    }

    public void move(int d) {
        sleep(moveTime);
        if (curBuilding == 'A' && d == -1) {
            curBuilding = 'E';
        } else if (curBuilding == 'E' && d == 1) {
            curBuilding = 'A';
        } else {
            curBuilding += d;
        }
        arrive();
    }

    public void run() {
        while (true) {
            if (waitList.isEnd() && waitList.isEmpty() && processingList.isEmpty()
                    && changeList.isEmpty() && changeList.isEnd()) {
                break;
            }
            //System.out.printf("thread circle %d + running \n",this.getId());
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
            direction = comp(targetBuilding, curBuilding);
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
