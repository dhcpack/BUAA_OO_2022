package exceptions;

import com.oocourse.spec2.exceptions.EqualMessageIdException;
import main.Calculator;

public class MyEqualMessageIdException extends EqualMessageIdException {
    private final int type;
    private final int id;

    public MyEqualMessageIdException(int id) {
        this.id = id;
        this.type = 7;
        Calculator.getInstance().updateExcCal(type);
        Calculator.getInstance().updateMessageCal(type, id);
    }

    @Override
    public void print() {
        int timeExc = Calculator.getInstance().getExcCal(type);
        int timeMsg = Calculator.getInstance().getMessageCal(type, id);
        System.out.println("emi-" + timeExc + ", " + id + "-" + timeMsg);
    }
}
