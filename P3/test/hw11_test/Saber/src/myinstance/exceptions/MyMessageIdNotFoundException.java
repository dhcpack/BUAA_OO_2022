package myinstance.exceptions;

import com.oocourse.spec3.exceptions.MessageIdNotFoundException;

import java.util.HashMap;

public class MyMessageIdNotFoundException extends MessageIdNotFoundException {

    private final int id;
    private static int numOfException = 0;
    private static HashMap<Integer, Integer> exceptionMap = new HashMap<>();

    public MyMessageIdNotFoundException(int id) {
        this.id = id;
        numOfException++;
        exceptionMap.merge(id, 1, (oldValue, newValue) -> oldValue + 1);
    }

    @Override
    public void print() {
        System.out.println("minf-" + numOfException + ", " + id + "-" + exceptionMap.get(id));
    }
}
