import java.util.ArrayList;

public class Controller extends Thread {
    private final WaitQueue waitQueue;
    private final ArrayList<RequestQueue> buildingQueues;

    public Controller(String name, ArrayList<RequestQueue> processingQueues) {
        super(name);
        this.waitQueue = WaitQueue.getInstance();
        this.buildingQueues = processingQueues;
    }

    @Override
    public void run() {
        while (true) {
            if (waitQueue.isEmpty() && waitQueue.isEnd()) {
                for (int i = 0; i < buildingQueues.size(); i++) {
                    buildingQueues.get(i).setEnd(true);
                }
                // System.out.println("Controller End");
                return;
            }
            Person person = waitQueue.getOneRequest();
            if (person == null) {
                continue;
            }

            if (person.getFromBuilding() == person.getToBuilding()) {
                // 同一栋楼
                if (person.getFromBuilding() == 'A') {
                    buildingQueues.get(0).addRequest(person);
                } else if (person.getFromBuilding() == 'B') {
                    buildingQueues.get(1).addRequest(person);
                } else if (person.getFromBuilding() == 'C') {
                    buildingQueues.get(2).addRequest(person);
                } else if (person.getFromBuilding() == 'D') {
                    buildingQueues.get(3).addRequest(person);
                } else if (person.getFromBuilding() == 'E') {
                    buildingQueues.get(4).addRequest(person);
                } else {
                    System.out.println("Controller: Wrong Building!");
                }
            } else {
                // 同层楼
                buildingQueues.get(person.getFromFloor() + 4).addRequest(person);
            }
        }
    }
}
