package com.oocourse.spec1.myexception;

import com.oocourse.spec1.exceptions.EqualPersonIdException;

import java.util.HashMap;

public class MyEqualPersonIdException extends EqualPersonIdException {
    private static int equalPersonIdTimes = 0;
    private static HashMap<Integer, Integer> equalPersonIdError = new HashMap<>();
    private int times;
    private int id;

    public MyEqualPersonIdException(int id) {
        if (equalPersonIdError.containsKey(id)) {
            times = equalPersonIdError.get(id) + 1;
            equalPersonIdError.replace(id, times);
        } else {
            times = 1;
            equalPersonIdError.put(id, 1);
        }
        this.id = id;
        equalPersonIdTimes++;
    }

    @Override
    public void print() {
        System.out.println("epi-" + equalPersonIdTimes + ", " + id + "-" + times);
    }
}
