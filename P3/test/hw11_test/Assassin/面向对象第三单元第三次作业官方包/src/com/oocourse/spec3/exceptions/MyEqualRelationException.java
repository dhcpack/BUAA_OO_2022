package com.oocourse.spec3.exceptions;

public class MyEqualRelationException extends EqualRelationException {
    private static Counter counter = new Counter();
    private int i1;
    private int i2;

    public MyEqualRelationException(int id1, int id2) {
        counter.setCount(counter.getCount() + 1);
        if (!counter.getCountById().containsKey(id1)) {
            counter.getCountById().put(id1, 1);
        } else {
            int temp = counter.getCountById().get(id1);
            counter.getCountById().put(id1, temp + 1);
        }
        if (id2 != id1) {
            if (!counter.getCountById().containsKey(id2)) {
                counter.getCountById().put(id2, 1);
            } else {
                int temp = counter.getCountById().get(id2);
                counter.getCountById().put(id2, temp + 1);
            }
        }
        i1 = Math.min(id1, id2);
        i2 = Math.max(id1, id2);
    }

    public void print() {
        System.out.print("er-" + counter.getCount() + ", ");
        System.out.print(i1 + "-" + counter.getCountById().get(i1) + ", ");
        System.out.println(i2 + "-" + counter.getCountById().get(i2));
    }

}
