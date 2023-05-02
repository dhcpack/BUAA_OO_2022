package com.oocourse.spec1.myexception;

import com.oocourse.spec1.exceptions.RelationNotFoundException;

import java.util.HashMap;

public class MyRelationNotFoundException extends RelationNotFoundException {
    private static int relationNotFoundTimes = 0;
    private static HashMap<Integer, Integer> relationNotFoundError = new HashMap<>();
    private int timesY;
    private int id1;
    private int timesZ;
    private int id2;

    public MyRelationNotFoundException(int id1, int id2) {
        if (id1 > id2) {
            this.id1 = id2;
            this.id2 = id1;
        } else {
            this.id1 = id1;
            this.id2 = id2;
        }
        if (relationNotFoundError.containsKey(this.id1)) {
            timesY = relationNotFoundError.get(this.id1) + 1;
            relationNotFoundError.replace(this.id1, timesY);
        } else {
            timesY = 1;
            relationNotFoundError.put(this.id1, 1);
        }
        if (relationNotFoundError.containsKey(this.id2)) {
            timesZ = relationNotFoundError.get(this.id2) + 1;
            relationNotFoundError.replace(this.id2, timesZ);
        } else {
            timesZ = 1;
            relationNotFoundError.put(this.id2, 1);
        }
        relationNotFoundTimes++;
    }

    @Override
    public void print() {
        System.out.println("rnf-" + relationNotFoundTimes + ", " +
                id1 + "-" + timesY + ", " + id2 + "-" + timesZ);
    }
}
