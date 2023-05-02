package myexceptions;

import mymain.CalFre;
import com.oocourse.spec2.exceptions.EqualMessageIdException;

public class MyEqualMessageIdException extends EqualMessageIdException {
    private final int type = 1;
    private final int id;

    public MyEqualMessageIdException(int id) {
        this.id = id;
        CalFre.getInstance().updateCal(type, id, true);
    }

    @Override
    public void print() {
        int x = CalFre.getInstance().querySum(type);
        int y = CalFre.getInstance().queryId(type, id);
        System.out.println("emi-" + x + ", " + id + "-" + y);
    }
}
