package myexceptions;

import mymain.CalFre;
import com.oocourse.spec2.exceptions.EqualPersonIdException;

public class MyEqualPersonIdException extends EqualPersonIdException {
    private final int type = 2;
    private final int id;

    public MyEqualPersonIdException(int id) {
        this.id = id;
        CalFre.getInstance().updateCal(type, id, true);
    }

    @Override
    public void print() {
        int x = CalFre.getInstance().querySum(type);
        int y = CalFre.getInstance().queryId(type, id);
        System.out.println("epi-" + x + ", " + id + "-" + y);
    }
}
