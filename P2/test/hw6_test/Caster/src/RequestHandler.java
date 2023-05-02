import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Predicate;

public class RequestHandler {
    private static final int CAPACITY = 6;

    private boolean end;
    private Queue<MyPersonRequest> personRequestQueue = new LinkedList<>();

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
                if (personRequest.isSameEndDirection(elevator.getDirection(),  // 判断运行方向一致
                        personRequest.getFromFloor(), personRequest.getFromBuilding()) &&
                        personRequest.isSameStartDirection(elevator.getDirection(),  // 判断起点在当前方向上
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
                if (personRequest.isSameStartDirection(elevator.getDirection(),  // 判断起点在当前方向上
                        elevator.getCurrentFloor(), elevator.getCurrentBuilding())) {
                    elevator.getWaitingQueue().add(personRequest);
                    return true;
                }
                return false;
            }
        });
    }
}
