package myexceptions;

import mymain.CalFre;
import com.oocourse.spec2.exceptions.EqualGroupIdException;

public class MyEqualGroupIdException extends EqualGroupIdException {
    private final int type = 0;
    private final int id;

    public MyEqualGroupIdException(int id) {
        this.id = id;
        CalFre.getInstance().updateCal(type, id, true);
    }

    @Override
    public void print() {
        int x = CalFre.getInstance().querySum(type);
        int y = CalFre.getInstance().queryId(type, id);
        System.out.println("egi-" + x + ", " + id + "-" + y);
    }
}
