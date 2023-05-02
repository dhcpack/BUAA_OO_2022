import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Optional;

public class Scheduler {
    private final HashMap<ElevatorSlot,ArrayList<RequestQueue>> requestQueues;
    private final ArrayList<Elevator> elevators;

    public Scheduler(
            HashMap<ElevatorSlot,ArrayList<RequestQueue>> requestQueues,
            ArrayList<Elevator> elevators
    ) {
        this.requestQueues = requestQueues;
        this.elevators = elevators;
    }

    /**
     * 派发请求到各个建筑对应的队列
     *
     * @param request 请求
     */
    public void dispatchRequest(MyRequest request) {
        ElevatorSlot slot = null;
        if (request.getFromBuilding() == request.getToBuilding() &&
                request.getFromFloor() != request.getToFloor()) {
            slot = new ElevatorSlot(
                    MyElevatorRequest.ElevatorType.Vertical,
                    request.getFromBuilding()
            );
        } else if (request.getFromBuilding() != request.getToBuilding() &&
                request.getFromFloor() == request.getToFloor()) {
            slot = new ElevatorSlot(
                    MyElevatorRequest.ElevatorType.Horizontal,
                    request.getFromFloor()
            );
        } else {
            assert false;
        }
        ArrayList<RequestQueue> requestQueuesOfElevatorsCanUsed = requestQueues.get(slot);
        //选择请求队列最短的电梯处理这个请求。
        Optional<RequestQueue> selected =
                requestQueuesOfElevatorsCanUsed.stream()
                        .min(Comparator.comparingInt(RequestQueue::size));
        if (selected.isPresent()) {
            selected.get().addRequest(request);
        } else {
            assert false;
        }
    }

    public void executeAddRequest(MyElevatorRequest request) {
        RequestQueue requestQueue = new RequestQueue();
        ElevatorSlot slot;
        if (request.getType() == MyElevatorRequest.ElevatorType.Vertical) {
            Elevator elevator = new LookElevator(
                    requestQueue,
                    Elevator.MAX_CARRY,
                    Elevator.Building.values()[request.getBuilding()],
                    request.getId()
            );
            elevators.add(
                  elevator
            );
            elevator.start();
            slot = new ElevatorSlot(MyElevatorRequest.ElevatorType.Vertical, request.getBuilding());
        } else {
            Elevator elevator =
                    new HorizontalElevator(
                            requestQueue,
                            Elevator.MAX_CARRY,
                            request.getId(),
                            request.getFloor()
                    );
            elevators.add(
                   elevator
            );
            elevator.start();
            slot = new ElevatorSlot(MyElevatorRequest.ElevatorType.Horizontal, request.getFloor());
        }

        requestQueues.computeIfAbsent(slot, k -> new ArrayList<>());
        requestQueues.get(slot).add(requestQueue);

    }

    public void setFinished() {
        requestQueues.forEach((k,v) -> v.forEach(x -> x.setNoMoreInput(true)));
    }

    public void joinAll() {
        elevators.forEach(elevator -> {
            try {
                elevator.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}
