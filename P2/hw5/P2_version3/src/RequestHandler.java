import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Predicate;

public class RequestHandler {
    private static final int UP = 1;
    private static final int DOWN = -1;
    private static final int RIGHT = 1;
    private static final int LEFT = -1;
    private static final int CAPACITY = 6;

    private boolean end;
    private final String type;
    private Queue<MyPersonRequest> personRequestQueue = new LinkedList<>();
    private ArrayList<Elevator> elevatorArrayList = new ArrayList<>();

    public RequestHandler(String type) {
        this.type = type;
    }

    public synchronized void addElevator(Elevator elevator) {
        elevatorArrayList.add(elevator);
    }

    public synchronized void inputRequest(MyPersonRequest personRequest) {
        personRequestQueue.offer(personRequest);
        notifyAll();
    }

    public synchronized void arrangeRequests(Elevator elevator, boolean changeDirt) {
        if (changeDirt) {
            getSameStartEndDirectionRequest(elevator);
            if (elevator.getWaitingQueue().size() != 0) {
                return;
            }
            getSameStartDirectionRequest(elevator);
            return;
        }
        getSameStartEndDirectionRequest(elevator);
    }

    public synchronized boolean isEnd() {
        if (personRequestQueue.size() == 0 && !end) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return personRequestQueue.size() == 0 && end;
    }

    public synchronized Queue<MyPersonRequest> getRequests() {
        return personRequestQueue;
    }

    public synchronized void setEnd() {
        this.end = true;
        notifyAll();
    }

    private void getSameStartEndDirectionRequest(Elevator elevator) {  // 起点在当前方向上且运行方向与当前方向一致
        personRequestQueue.removeIf(new Predicate<MyPersonRequest>() {
            @Override
            public boolean test(MyPersonRequest personRequest) {
                if (elevator.numOfRequests() == CAPACITY) {
                    return false;
                }
                if (personRequest.isSameEndDirection(elevator.getDirection(),
                        personRequest.getFromFloor(), personRequest.getFromBuilding()) &&
                        personRequest.isSameStartDirection(elevator.getDirection(),
                                elevator.getCurrentFloor(), elevator.getCurrentBuilding())) {
                    elevator.getWaitingQueue().add(personRequest);
                    return true;
                }
                return false;
            }
        });
    }

    private void getSameStartDirectionRequest(Elevator elevator) {  // 起点在当前方向上
        personRequestQueue.removeIf(new Predicate<MyPersonRequest>() {
            @Override
            public boolean test(MyPersonRequest personRequest) {
                if (elevator.numOfRequests() == CAPACITY) {
                    return false;
                }
                if (personRequest.isSameStartDirection(elevator.getDirection(),
                        elevator.getCurrentFloor(), elevator.getCurrentBuilding())) {
                    elevator.getWaitingQueue().add(personRequest);
                    return true;
                }
                return false;
            }
        });
    }
}
