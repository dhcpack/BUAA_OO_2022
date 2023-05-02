package com.oocourse.spec1.myexception;

import com.oocourse.spec1.exceptions.PersonIdNotFoundException;

import java.util.HashMap;

public class MyPersonIdNotFoundException extends PersonIdNotFoundException {
    private static int personIdNotFoundTimes = 0;
    private static HashMap<Integer, Integer> personIdNotFoundError = new HashMap<>();
    private int times;
    private int id;

    public MyPersonIdNotFoundException(int id) {
        if (personIdNotFoundError.containsKey(id)) {
            times = personIdNotFoundError.get(id) + 1;
            personIdNotFoundError.replace(id, times);
        } else {
            times = 1;
            personIdNotFoundError.put(id, 1);
        }
        personIdNotFoundTimes++;
        this.id = id;
    }

    @Override
    public void print() {
        System.out.println("pinf-" + personIdNotFoundTimes + ", " + id + "-" + times);
    }
}
