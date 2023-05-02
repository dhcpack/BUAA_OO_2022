package exceptions;

import com.oocourse.spec2.exceptions.MessageIdNotFoundException;

public class MyMessageIdNotFoundException extends MessageIdNotFoundException {
    private static Counter counter = new Counter();
    private int id;

    public MyMessageIdNotFoundException(int id) {
        counter.add(id);
        this.id = id;
    }

    @Override
    public void print() {
        System.out.println("minf-" + counter.getNum() + ", " + id + "-" + counter.getNum(id));
    }
}
