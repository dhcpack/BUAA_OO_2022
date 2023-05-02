package com.oocourse.spec1.myexception;

import com.oocourse.spec1.exceptions.GroupIdNotFoundException;

import java.util.HashMap;

public class MyGroupIdNotFoundException extends GroupIdNotFoundException {
    private static int groupIdNotFoundTimes = 0;
    private static HashMap<Integer, Integer> groupIdNotFoundError = new HashMap<>();
    private int times;
    private int id;

    public MyGroupIdNotFoundException(int id) {
        if (groupIdNotFoundError.containsKey(id)) {
            times = groupIdNotFoundError.get(id) + 1;
            groupIdNotFoundError.replace(id, times);
        } else {
            times = 1;
            groupIdNotFoundError.put(id, 1);
        }
        groupIdNotFoundTimes++;
        this.id = id;
    }

    @Override
    public void print() {
        System.out.println("ginf-" + groupIdNotFoundTimes + ", " + id + "-" + times);
    }
}
