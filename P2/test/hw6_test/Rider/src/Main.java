import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Output.initStartTimestamp();
        ArrayList<RequestQueue> waitQueues = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            RequestQueue waitQueue = new RequestQueue();
            Elevator elevator = new Elevator(i + 1, (char) ('A' + i),waitQueue);
            elevator.start();
            waitQueues.add(waitQueue);
        }
        Input input = new Input(waitQueues);
        input.start();
    }
}
