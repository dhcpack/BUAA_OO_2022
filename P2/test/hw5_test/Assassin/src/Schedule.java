import java.util.ArrayList;

public class Schedule extends Thread {
    private PeopleQueue waitQueue;
    private ArrayList<PeopleQueue> processingQueues;

    public Schedule(PeopleQueue waitQueue, ArrayList<PeopleQueue> processingQueues) {
        this.waitQueue = waitQueue;
        this.processingQueues = processingQueues;
    }

    @Override
    public void run() {
        while (true) {
            if (waitQueue.isEmpty() && waitQueue.isEnd()) {
                for (int i = 0; i < processingQueues.size(); i++) {
                    processingQueues.get(i).setEnd(true);
                }
                return;
            }
            People people = waitQueue.getOnePeople();
            if (people == null) {
                continue;
            }
            char building = people.getBuilding();
            processingQueues.get(building - 'A').addPeople(people);
        }
    }
}
