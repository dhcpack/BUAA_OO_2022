package exceptions;

import com.oocourse.spec2.exceptions.EqualPersonIdException;

public class MyEqualPersonIdException extends EqualPersonIdException {
    private static Counter counter = new Counter();
    private int id;

    public MyEqualPersonIdException(int id) {
        counter.add(id);
        this.id = id;
    }

    @Override
    public void print() {
        System.out.println("epi-" + counter.getNum() + ", " + id + "-" + counter.getNum(this.id));
    }
}
