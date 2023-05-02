import java.util.ArrayList;
import java.util.Vector;

public class MainScheduler extends Thread {
    private final RequestQueue changeList;
    //外部调度,主要负责安排waitList
    //具体怎么运行,由内部调度器确定
    private RequestQueue prQueue;
    private ArrayList<RequestQueue> buildingWaitList;
    private Vector<RequestQueue> allCircleList;
    private int[] hasCircleBuilding;
    private Vector<Thread> allElevator;

    public MainScheduler(RequestQueue inputQueue,
                         ArrayList<RequestQueue> allWaitList,
                         Vector<RequestQueue> allCircleList,
                         int[] hasCircleBuilding,
                         Vector<Thread> allElevator,
                         RequestQueue changeList
    ) {
        this.prQueue = inputQueue;
        this.buildingWaitList = allWaitList;
        this.allCircleList = allCircleList;
        this.hasCircleBuilding = hasCircleBuilding;
        this.allElevator = allElevator;
        this.changeList = changeList;
    }

    public void allocate(MoveRequest moveRequest) {
        if (moveRequest.getToFloor() == moveRequest.getFromFloor()
                && hasCircleBuilding[moveRequest.getFromFloor() - 1] > 0) {
            //能直接横向运输
            allCircleList.get(moveRequest.getFromFloor() - 1).addRequest(moveRequest);
        } else if (moveRequest.getToBuilding() == moveRequest.getFromBuilding()) {
            switch (moveRequest.getFromBuilding()) {
                case 'A':
                    buildingWaitList.get(0).addRequest(moveRequest);
                    break;
                case 'B':
                    buildingWaitList.get(1).addRequest(moveRequest);
                    break;
                case 'C':
                    buildingWaitList.get(2).addRequest(moveRequest);
                    break;
                case 'D':
                    buildingWaitList.get(3).addRequest(moveRequest);
                    break;
                case 'E':
                    buildingWaitList.get(4).addRequest(moveRequest);
                    break;
                default:
            }
        } else {
            int tempToFloor = getNearest(moveRequest.getFromFloor());
            if (tempToFloor != moveRequest.getFromFloor()) {
                //不能原地横向运输
                moveRequest.setToFloor(tempToFloor);
                changeList.addRequest(moveRequest);
                //先纵向运输
                switch (moveRequest.getFromBuilding()) {
                    case 'A':
                        buildingWaitList.get(0).addRequest(moveRequest);
                        break;
                    case 'B':
                        buildingWaitList.get(1).addRequest(moveRequest);
                        break;
                    case 'C':
                        buildingWaitList.get(2).addRequest(moveRequest);
                        break;
                    case 'D':
                        buildingWaitList.get(3).addRequest(moveRequest);
                        break;
                    case 'E':
                        buildingWaitList.get(4).addRequest(moveRequest);
                        break;
                    default:
                }
            } else {
                //先原地横向运输，再纵向运输
                allCircleList.get(moveRequest.getFromFloor() - 1).addRequest(moveRequest);
                changeList.addRequest(moveRequest);
            }
        }
    }

    private int getNearest(int floor) {
        int temp = floor - 1;
        int ans = temp;
        if (hasCircleBuilding[temp] > 0) {
            return ans + 1;
        }
        int max = 0;
        for (int i = temp; i < 10; i++) {
            if (hasCircleBuilding[i] > max) {
                max = hasCircleBuilding[i];
                ans = i;
            }
        }

        for (int i = temp; i >= 0; i--) {
            if (hasCircleBuilding[i] > max) {
                max = hasCircleBuilding[i];
                ans = i;
            }
        }
        if (max == 0) {
            System.out.println("Has no circle elevator");
            return -111111;
        }
        return ans + 1;
    }

    @Override
    public void run() {
        while (true) {
            //System.out.printf("thread schedule %d + running \n",this.getId());
            //TimableOutput.println("Main scheduler is running !!");
            if (prQueue.isEmpty() && prQueue.isEnd()) {
                synchronized (changeList) {
                    while (!changeList.isEmpty()) {
                        try {
                            changeList.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                changeList.setEnd(true);
                for (RequestQueue requestQueue : buildingWaitList) {
                    requestQueue.setEnd(true);
                }
                for (RequestQueue requestQueue : allCircleList) {
                    requestQueue.setEnd(true);
                }
                return;
            }
            MoveRequest moveRequest = prQueue.getOneRequest();
            if (moveRequest == null) {
                continue;
            }
            allocate(moveRequest);
        }
    }

}
