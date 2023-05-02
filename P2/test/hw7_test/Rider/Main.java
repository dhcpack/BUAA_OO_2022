package src;

import com.oocourse.TimableOutput;
import src.io.InputHandler;
import src.io.OutputHandler;
import src.lift.LiftP;
import src.lift.LiftV;
import src.reqhandler.RequestDispatcher;
import src.reqhandler.RequestQueue;
import src.singleton.LiftData;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Main {

    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        OutputHandler.setDebug(false);

        // for inputHandler and requestDispatcher
        ConcurrentHashMap<Integer, CopyOnWriteArrayList<Integer>>
                verticalData = LiftData.fetch().verticalData();
        ConcurrentHashMap<Integer, CopyOnWriteArrayList<Integer>>
                parallelData = LiftData.fetch().parallelData();
        ConcurrentHashMap<Integer, LiftV> liftVList =
                LiftData.fetch().verticalLiftMap();

        // init verticalData
        for (int i = 0; i < 5; i++) {
            verticalData.put(i, new CopyOnWriteArrayList<>());
        }

        // init parallelQueue
        for (int i = 1; i <= 10; i++) {
            parallelData.put(i, new CopyOnWriteArrayList<>());
        }

        // create initial threads
        InputHandler inputHandler = new InputHandler();
        inputHandler.start();
        RequestDispatcher dispatcher = new RequestDispatcher();
        dispatcher.start();

        for (int i = 0; i < 5; i++) {
            RequestQueue serveQueue = new RequestQueue();
            verticalData.get(i).add(i + 1);
            LiftData.fetch().verticalQueue().put(i + 1, serveQueue);
            LiftV lift = new LiftV(i, serveQueue, i + 1, 0.6, 8);
            liftVList.put(i + 1, lift);
            lift.start();
        }

        RequestQueue serveQueue = new RequestQueue();
        parallelData.get(1).add(6);
        LiftData.fetch().parallelQueue().put(6, serveQueue);
        LiftP lift = new LiftP(1, serveQueue, 6, 0.6, 8, 31);
        LiftData.fetch().parallelLiftMap().put(6, lift);
        lift.start();

    }

}
