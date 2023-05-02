package src.reqhandler;

import src.io.OutputHandler;
import src.lift.LiftP;
import src.lift.LiftV;
import src.singleton.LiftData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class RequestDispatcher extends Thread {

    private final ConcurrentHashMap<Integer, CopyOnWriteArrayList<Integer>>
            verticalData = LiftData.fetch().verticalData();
    private final ConcurrentHashMap<Integer, CopyOnWriteArrayList<Integer>>
            parallelData = LiftData.fetch().parallelData();

    private final RequestQueue inputQueue = LiftData.fetch().inputQueue();
    private final ConcurrentHashMap<Integer, RequestQueue>
            verticalQueue = LiftData.fetch().verticalQueue();
    private final ConcurrentHashMap<Integer, RequestQueue>
            parallelQueue = LiftData.fetch().parallelQueue();

    private final ConcurrentHashMap<Integer, LiftP>
            idParallelLiftMap = LiftData.fetch().parallelLiftMap();
    private final ConcurrentHashMap<Integer, LiftV>
            idVerticalLiftMap = LiftData.fetch().verticalLiftMap();

    @Override
    public void run() {
        int index;
        int dispatch;
        while (true) {
            if (inputQueue.isEmpty() && inputQueue.isReachEnd()) {
                OutputHandler.println("dispatcher close", true);
                verticalQueue.forEach((k, v) -> v.setEnd(true));
                parallelQueue.forEach((k, v) -> v.setEnd(true));
                return;
            }
            if (inputQueue.isEmpty()) {
                synchronized (inputQueue) {
                    try {
                        inputQueue.wait(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                MyPersonRequest request = inputQueue.peek();
                if (request.getFromBuilding() != request.getToBuilding()) {
                    // parallel moving
                    index = request.getFromFloor();
                    int access = (1 << (request.getFromBuilding() - 'A')) +
                            (1 << (request.getToBuilding() - 'A'));
                    ArrayList<Integer> sortQueue = new ArrayList<>();
                    parallelData.get(index).forEach(val -> {
                        LiftP v = idParallelLiftMap.get(val);
                        if ((v.getAccessibility() & access) == access) {
                            sortQueue.add(val);
                        }
                    });
                    sortQueue.sort(Comparator.comparingInt(o ->
                            (int) (1.5 * parallelQueue.get(o).size() +
                                    idParallelLiftMap.get(o).getInsideNum()) *
                                    idParallelLiftMap.get(o).getMoveDur()));
                    dispatch = sortQueue.get(0);
                    /*dispatch = parallelData.get(index)
                            .get(random.nextInt(parallelData.get(index).size()));*/
                    OutputHandler.println("index = " + index + "; dispatched to " + dispatch, true);
                    parallelQueue.get(dispatch).addRequest(inputQueue.poll());
                } else {
                    // vertical moving
                    index = request.getFromBuilding() - 'A';
                    ArrayList<Integer> sortQueue = new ArrayList<>(verticalData.get(index));
                    sortQueue.sort(Comparator.comparingInt(o ->
                            (int) (1.5 * verticalQueue.get(o).size() +
                                    idVerticalLiftMap.get(o).getInsideNum()) *
                                    idVerticalLiftMap.get(o).getMoveDur()));
                    dispatch = sortQueue.get(0);
                    /*dispatch = verticalData.get(index)
                            .get(random.nextInt(verticalData.get(index).size()));*/
                    OutputHandler.println("index = " + index + "; dispatched to " + dispatch, true);
                    verticalQueue.get(dispatch).addRequest(inputQueue.poll());
                }
            }
        }
    }

}
