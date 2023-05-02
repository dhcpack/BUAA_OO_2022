package exceptions;

import com.oocourse.spec2.exceptions.GroupIdNotFoundException;
import main.Calculator;

public class MyGroupIdNotFoundException extends GroupIdNotFoundException {
    private final int id;
    private final int type = 4;

    public MyGroupIdNotFoundException(int id) {
        this.id = id;
        Calculator.getInstance().updateExcCal(type);
        Calculator.getInstance().updateGroupCal(type, id);
    }

    @Override
    public void print() {
        int timesExc = Calculator.getInstance().getExcCal(type);
        int timesGroup = Calculator.getInstance().getGroupCal(type, id);
        System.out.println("ginf-" + timesExc + ", " + id + "-" + timesGroup);
    }
}
