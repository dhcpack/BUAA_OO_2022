import com.oocourse.TimableOutput;

import java.util.ArrayList;
import java.util.Iterator;

public class Elevator extends Thread {
    private int id;
    private boolean up;
    private int curfloor;
    private int maxNum;
    private int curNum;
    private RequestQueue processingQueue;
    private ArrayList<Request> insideRequests;

    public Elevator(int id, RequestQueue processingQueue) {
        this.id = id;
        this.up = true;
        this.curfloor = 1;
        this.maxNum = 6;
        this.processingQueue = processingQueue;
        this.insideRequests = new ArrayList<>();
    }

    @Override
    public void run() {
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (processingQueue) {
            if (processingQueue.isEmpty() && !processingQueue.isEnd()) {
                try {
                    processingQueue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        update();
        while (true) {
            synchronized (processingQueue) {
                if (processingQueue.isEmpty() && processingQueue.isEnd()
                        && insideRequests.isEmpty()) {
                    //   TimableOutput.println("elevator " + id + " end");
                    return;
                }
            }
            if (up) {
                goUp();
            } else {
                goDown();
            }
        }
    }

    public void update() {
        //先判断要不要在这层停
        boolean pause = false;
        if (!insideRequests.isEmpty()) {
            for (Request request : insideRequests) {
                if (request.getToFloor() == curfloor) {
                    pause = true;
                    break;
                }
            }
        }
        if (!pause) {
            synchronized (processingQueue) {
                for (int i = 0; i < processingQueue.getSize(); i++) {
                    if (insideRequests.isEmpty() &&
                            processingQueue.getRequest(i).getFromFloor() == curfloor) {
                        pause = true;
                        break;
                    } else if (processingQueue.getRequest(i).getFromFloor() == curfloor
                            && processingQueue.getRequest(i).isUp() == up) {
                        pause = true;
                        break;
                    }
                }
            }
        }
        if (pause) {
            //停下开门
            TimableOutput.println("OPEN-" + (char) (id - 1 + 'A') + "-" + curfloor + "-" + id);
            try {
                sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //进出电梯,先出后进
            //出人
            peopleOut();
            synchronized (processingQueue) {
                //进人
                peopleIn();
                //决定电梯走向
                if (!insideRequests.isEmpty()) {
                    up = insideRequests.get(0).isUp();
                } else if (!processingQueue.isEmpty()) {
                    up = (processingQueue.getRequest(0).getFromFloor() - curfloor) > 0;
                }
                //关门
                TimableOutput.println("CLOSE-" + (char) (id - 1 + 'A') + "-" + curfloor + "-" + id);
            }
        }
        //保证电梯在1-10层运行
        if (curfloor == 10) {
            up = false;
        } else if (curfloor == 1) {
            up = true;
        }
    }

    public void peopleIn() {
        synchronized (processingQueue) {
            ArrayList<Request> requests = processingQueue.getRequests();
            Iterator<Request> iterator2 = requests.iterator();
            while (iterator2.hasNext()) {
                if (curNum == maxNum) {
                    break;
                }
                Request request = iterator2.next();
                if (insideRequests.isEmpty() && request.getFromFloor() == curfloor) {
                    up = request.isUp();
                    insideRequests.add(request);
                    curNum++;
                    TimableOutput.println("IN-" + request.getId() + "-" +
                            (char) (id - 1 + 'A') + "-" + curfloor + "-" + id);
                    iterator2.remove();
                    processingQueue.notifyAll();
                } else if (request.getFromFloor() == curfloor &&
                        request.isUp() == up) {
                    insideRequests.add(request);
                    curNum++;
                    TimableOutput.println("IN-" + request.getId() + "-" +
                            (char) (id - 1 + 'A') + "-" + curfloor + "-" + id);
                    iterator2.remove();
                    processingQueue.notifyAll();
                }
            }
        }
    }

    public void peopleOut() {
        Iterator<Request> iterator = insideRequests.iterator();
        while (iterator.hasNext()) {
            Request request = iterator.next();
            if (request.getToFloor() == curfloor) {
                TimableOutput.println("OUT-" + request.getId() + "-" +
                        (char) (id - 1 + 'A') + "-" + curfloor + "-" + id);
                iterator.remove();
                curNum--;
            }
        }
    }

    public void goUp() {
        up = true;
        curfloor++;
        try {
            sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        TimableOutput.println("ARRIVE-" + (char) (id - 1 + 'A') + "-" + curfloor + "-" + id);
        update();
    }

    public void goDown() {
        up = false;
        curfloor--;
        try {
            sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        TimableOutput.println("ARRIVE-" + (char) (id - 1 + 'A') + "-" + curfloor + "-" + id);
        update();
    }
}
