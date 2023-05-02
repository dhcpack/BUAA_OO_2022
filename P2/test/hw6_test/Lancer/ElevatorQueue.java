import com.oocourse.elevator2.PersonRequest;

import java.util.ArrayList;

public class ElevatorQueue {
    private final ArrayList<Elevator> elevators;

    //private Queue queue = new Queue();

    private int ref = 0;

    public ElevatorQueue(ArrayList<Elevator> elevators) {
        this.elevators = elevators;
    }

    public void add(Elevator newOne) {
        elevators.add(newOne);
        newOne.start();
    }

    public Elevator dispatch(PersonRequest p) {
        //判断是垂直还是水平
        //随机分配一个
        Elevator target = elevators.get(ref);
        ref = (ref + 1) % elevators.size();
        return target;
    }

    public void notifyElevator() {
        for (Elevator e : elevators) {
            synchronized (e.getQueue()) {
                e.getQueue().setEnd(true);
                if (!e.getQueue().isSleep()) {
                    e.getQueue().notifyAll();
                }
            }
        }
    }

}
