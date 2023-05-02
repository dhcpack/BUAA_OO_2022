package src.singleton;

import src.io.OutputHandler;

import java.util.ArrayList;

public class Counter {

    private static final Counter COUNTER = new Counter();
    private static int reqCount = 0;
    private static ArrayList<Integer> list = new ArrayList<>();

    public static Counter fetch() {
        return COUNTER;
    }

    public synchronized void getRequest(int i) {
        reqCount++;
        list.add(i);
        OutputHandler.println("get & remain --- " + reqCount, true);
        notifyAll();
    }

    public synchronized void releaseRequest(int i) {
        reqCount--;
        list.remove(list.indexOf(i));
        OutputHandler.println("rls & remain --- " + reqCount, true);
        notifyAll();
    }

    public synchronized boolean isDone() {
        return reqCount == 0;
    }

}
