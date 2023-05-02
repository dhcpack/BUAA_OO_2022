package com.oocourse.spec3.exceptions;

public class MyEqualMessageIdException extends EqualMessageIdException {
    private static Counter counter = new Counter();
    private int index;

    public MyEqualMessageIdException(int id) {
        counter.setCount(counter.getCount() + 1);
        if (!counter.getCountById().containsKey(id)) {
            counter.getCountById().put(id, 1);
        } else {
            int temp = counter.getCountById().get(id);
            counter.getCountById().put(id, temp + 1);
        }
        index = id;
    }

    public void print() {
        System.out.print("emi-" + counter.getCount() + ", ");
        System.out.println(index + "-" + counter.getCountById().get(index));
    }
}
