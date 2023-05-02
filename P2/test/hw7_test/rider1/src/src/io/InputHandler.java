package src.io;

import com.oocourse.elevator3.ElevatorInput;
import com.oocourse.elevator3.ElevatorRequest;
import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.Request;
import src.lift.LiftP;
import src.lift.LiftV;
import src.reqhandler.MyPersonRequest;
import src.reqhandler.RequestQueue;
import src.singleton.Counter;
import src.singleton.LiftData;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class InputHandler extends Thread {

    private final ConcurrentHashMap<Integer,
            CopyOnWriteArrayList<Integer>> verticalData =
            LiftData.fetch().verticalData();
    private final ConcurrentHashMap<Integer,
            CopyOnWriteArrayList<Integer>> parallelData =
            LiftData.fetch().parallelData();

    private final ConcurrentHashMap<Integer, LiftP> idParallelLiftMap =
            LiftData.fetch().parallelLiftMap();
    private final ConcurrentHashMap<Integer, LiftV> idVerticalLiftMap =
            LiftData.fetch().verticalLiftMap();

    private final RequestQueue inputQueue =
            LiftData.fetch().inputQueue();
    private final ConcurrentHashMap<Integer, RequestQueue> verticalQueue =
            LiftData.fetch().verticalQueue();
    private final ConcurrentHashMap<Integer, RequestQueue> parallelQueue =
            LiftData.fetch().parallelQueue();

    @Override
    public void run() {
        ElevatorInput input = new ElevatorInput(System.in);
        while (true) {
            Request request = input.nextRequest();
            if (request == null && Counter.fetch().isDone()) {
                OutputHandler.println("inputHandler close", true);
                inputQueue.setEnd(true);
                break;
            } else if (request == null) {
                synchronized (inputQueue) {
                    try {
                        inputQueue.wait(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else if (request instanceof PersonRequest) {
                inputQueue.addRequest(new MyPersonRequest((PersonRequest) request));
                Counter.fetch().getRequest(((PersonRequest) request).getPersonId());
            } else if (request instanceof ElevatorRequest) {
                // create lifts
                RequestQueue queue = new RequestQueue();
                int id = ((ElevatorRequest) request).getElevatorId();
                int floor = ((ElevatorRequest) request).getFloor();
                char type = ((ElevatorRequest) request).getBuilding();
                double speed = ((ElevatorRequest) request).getSpeed();
                int accessibility = ((ElevatorRequest) request).getSwitchInfo();
                int capacity = ((ElevatorRequest) request).getCapacity();
                if (Objects.equals(((ElevatorRequest) request).getType(), "floor")) {
                    // parallel lift
                    parallelData.get(floor).add(id);
                    LiftP lift = new LiftP(floor, queue, id, speed, capacity, accessibility);
                    parallelQueue.put(id, queue);
                    idParallelLiftMap.put(id, lift);
                    lift.start();
                } else {
                    // vertical lift
                    verticalData.get(type - 'A').add(id);
                    LiftV lift = new LiftV(type - 'A', queue, id, speed, capacity);
                    verticalQueue.put(id, queue);
                    idVerticalLiftMap.put(id, lift);
                    lift.start();
                }
            }
        }
        try {
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
