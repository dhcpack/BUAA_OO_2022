import java.util.ArrayList;

public class Scheduler extends Thread {
    private RequestQueue buffer;
    private ArrayList<RequestQueue> processingQueues;

    public Scheduler(RequestQueue buffer, ArrayList<RequestQueue> processingQueues) {
        this.buffer = buffer;
        this.processingQueues = processingQueues;
    }

    @Override
    public void run() {
        while (true) {
            if (buffer.isEmpty() && buffer.isEnd()) {
                for (RequestQueue requestQueue : processingQueues) {
                    requestQueue.setEnd(true);
                }
                return;
            }
            Request request = buffer.takeRequest();
            if (request == null) {
                continue;
            }
            processingQueues.get(request.getFromBuilding() - 'A').addRequest(request);
        }
    }
}
