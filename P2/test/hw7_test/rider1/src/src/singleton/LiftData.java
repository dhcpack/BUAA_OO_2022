package src.singleton;

import src.lift.LiftP;
import src.lift.LiftV;
import src.reqhandler.RequestQueue;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class LiftData {
    private static final LiftData LIFT_DATA = new LiftData();
    // lift id ---> lift
    private final ConcurrentHashMap<Integer, LiftP> idParallelLiftMap =
            new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, LiftV> idVerticalLiftMap =
            new ConcurrentHashMap<>();

    private final ConcurrentHashMap<Integer,
            CopyOnWriteArrayList<Integer>> verticalData = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer,
            CopyOnWriteArrayList<Integer>> parallelData = new ConcurrentHashMap<>();

    private final RequestQueue inputQueue = new RequestQueue();
    private final ConcurrentHashMap<Integer, RequestQueue> verticalQueue =
            new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, RequestQueue> parallelQueue =
            new ConcurrentHashMap<>();

    public static LiftData fetch() {
        return LIFT_DATA;
    }

    public ConcurrentHashMap<Integer, LiftP> parallelLiftMap() {
        return idParallelLiftMap;
    }

    public ConcurrentHashMap<Integer, LiftV> verticalLiftMap() {
        return idVerticalLiftMap;
    }

    public ConcurrentHashMap<Integer, CopyOnWriteArrayList<Integer>> parallelData() {
        return parallelData;
    }

    public ConcurrentHashMap<Integer, CopyOnWriteArrayList<Integer>> verticalData() {
        return verticalData;
    }

    public ConcurrentHashMap<Integer, RequestQueue> parallelQueue() {
        return parallelQueue;
    }

    public ConcurrentHashMap<Integer, RequestQueue> verticalQueue() {
        return verticalQueue;
    }

    public RequestQueue inputQueue() {
        return inputQueue;
    }
}
