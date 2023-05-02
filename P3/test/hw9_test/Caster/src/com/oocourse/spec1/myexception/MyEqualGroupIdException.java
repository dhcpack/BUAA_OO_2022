package com.oocourse.spec1.myexception;

import com.oocourse.spec1.exceptions.EqualGroupIdException;

import java.util.HashMap;

public class MyEqualGroupIdException extends EqualGroupIdException {
    private static int equalGroupIdTimes = 0;
    private static HashMap<Integer, Integer> equalGroupIdError = new HashMap<>();
    private int times;
    private int id;

    public MyEqualGroupIdException(int id) {
        if (equalGroupIdError.containsKey(id)) {
            times = equalGroupIdError.get(id) + 1;
            equalGroupIdError.replace(id, times);
        } else {
            times = 1;
            equalGroupIdError.put(id, 1);
        }
        equalGroupIdTimes++;
        this.id = id;
    }

    @Override
    public void print() {
        System.out.println("egi-" + equalGroupIdTimes + ", " + id + "-" + times);
    }
}
