package com.oocourse.spec3.exceptions;

import java.util.HashMap;

public class Counter {
    private int count;
    private HashMap<Integer, Integer> countById;

    public HashMap<Integer, Integer> getCountById() {
        return countById;
    }

    public Counter() {
        count = 0;
        countById = new HashMap<>();
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
