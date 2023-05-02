package exceptions;

import com.oocourse.spec2.exceptions.EqualGroupIdException;

public class MyEqualGroupIdException extends EqualGroupIdException {
    private static Counter counter = new Counter();
    private int id;

    public MyEqualGroupIdException(int id) {
        counter.add(id);
        this.id = id;
    }

    @Override
    public void print() {
        System.out.println("egi-" + counter.getNum() + ", " + id + "-" + counter.getNum(this.id));
    }
}
