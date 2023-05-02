package com.oocourse.spec1.myexception;

import com.oocourse.spec1.exceptions.EqualRelationException;

import java.util.HashMap;

public class MyEqualRelationException extends EqualRelationException {
    private static int equalRelationTimes = 0;
    private static HashMap<Integer, Integer> equalRelationError = new HashMap<>();
    private int timesY;
    private int id1;
    private int timesZ;
    private int id2;

    public MyEqualRelationException(int id1, int id2) {
        if (id1 > id2) {
            this.id1 = id2;
            this.id2 = id1;
        } else {
            this.id1 = id1;
            this.id2 = id2;
        }
        equalRelationTimes++;
        if (equalRelationError.containsKey(this.id1)) {
            timesY = equalRelationError.get(this.id1) + 1;
            equalRelationError.replace(this.id1, timesY);
        } else {
            timesY = 1;
            equalRelationError.put(this.id1, 1);
        }

        if (id1 == id2) {
            timesZ = timesY;
            return;
        }
        if (equalRelationError.containsKey(this.id2)) {
            timesZ = equalRelationError.get(this.id2) + 1;
            equalRelationError.replace(this.id2, timesZ);
        } else {
            timesZ = 1;
            equalRelationError.put(this.id2, 1);
        }
    }

    @Override
    public void print() {
        System.out.println("er-" + equalRelationTimes + ", " + id1 + "-" +
                timesY + ", " + id2 + "-" + timesZ);
    }
}
