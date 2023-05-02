package myexceptions;

import mymain.CalFre;
import com.oocourse.spec2.exceptions.MessageIdNotFoundException;

public class MyMessageIdNotFoundException extends MessageIdNotFoundException {
    private final int type = 5;
    private final int id;

    public MyMessageIdNotFoundException(int id) {
        this.id = id;
        CalFre.getInstance().updateCal(type, id, true);
    }

    @Override
    public void print() {
        int x = CalFre.getInstance().querySum(type);
        int y = CalFre.getInstance().queryId(type, id);
        System.out.println("minf-" + x + ", " + id + "-" + y);
    }
}