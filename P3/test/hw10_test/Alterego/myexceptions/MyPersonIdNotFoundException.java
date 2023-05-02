package myexceptions;

import mymain.CalFre;
import com.oocourse.spec2.exceptions.PersonIdNotFoundException;

public class MyPersonIdNotFoundException extends PersonIdNotFoundException {
    private final int type = 6;
    private final int id;

    public MyPersonIdNotFoundException(int id) {
        this.id = id;
        CalFre.getInstance().updateCal(type, id, true);
    }

    @Override
    public void print() {
        int x = CalFre.getInstance().querySum(type);
        int y = CalFre.getInstance().queryId(type, id);
        System.out.println("pinf-" + x + ", " + id + "-" + y);
    }
}
