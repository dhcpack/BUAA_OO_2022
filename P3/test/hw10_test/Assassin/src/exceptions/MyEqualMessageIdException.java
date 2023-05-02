package exceptions;

import com.oocourse.spec2.exceptions.EqualMessageIdException;

public class MyEqualMessageIdException extends EqualMessageIdException {
    private static Counter counter = new Counter();
    private int id;

    public MyEqualMessageIdException(int id) {
        counter.add(id);
        this.id = id;
    }

    @Override
    public void print() {
        System.out.println("emi-" + counter.getNum() + ", " + id + "-" + counter.getNum(this.id));
    }
}
