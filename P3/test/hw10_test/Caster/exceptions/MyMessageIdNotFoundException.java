package exceptions;

import com.oocourse.spec2.exceptions.MessageIdNotFoundException;
import main.Calculator;

public class MyMessageIdNotFoundException extends MessageIdNotFoundException {
    private final int type;
    private final int id;

    public MyMessageIdNotFoundException(int id) {
        this.id = id;
        this.type = 6;
        Calculator.getInstance().updateExcCal(type);
        Calculator.getInstance().updateMessageCal(type, id);
    }

    @Override
    public void print() {
        int timeExc = Calculator.getInstance().getExcCal(type);
        int timeMsg = Calculator.getInstance().getMessageCal(type, id);
        System.out.println("minf-" + timeExc + ", " + id + "-" + timeMsg);
    }
}
