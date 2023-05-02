import java.util.HashMap;

public class Scheduler extends Thread {
    private WaitQueue allWaitQueue;
    private HashMap<Character, BuildingWaitQueue> buildingWaitQueues;
    private boolean debug = false;

    public Scheduler(WaitQueue allWaitQueue,
                     HashMap<Character, BuildingWaitQueue> buildingWaitQueues) {
        this.allWaitQueue = allWaitQueue;
        this.buildingWaitQueues = buildingWaitQueues;
        putRequest();
    }

    public void run() {
        while (true) {
            if (allWaitQueue.needEnd()) {
                end();
                if (debug) {
                    System.out.println("scheduler end");
                }
                break;
            }
            allWaitQueue.needWait();
            if (!allWaitQueue.needEnd()) {
                putRequest();
            }
        }
    }

    public void putRequest() {
        for (char i = 'A'; i <= 'E'; i++) {
            buildingWaitQueues.get(i).addPassenger(allWaitQueue.getPassenger(i));
        }
    }

    public void end() {
        for (char i = 'A'; i <= 'E'; i++) {
            buildingWaitQueues.get(i).setEnd();
        }
    }
}
