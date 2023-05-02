package com.oocourse.spec3.main;

import java.util.HashSet;

public class Block {
    private HashSet<Person> items = new HashSet<>();
    private Person father;
    private int sign;
    private int leastValue;

    public Block(HashSet<Person> hashSet, Person father, int sign) {
        this.sign = sign;
        this.items = hashSet;
        this.father = father;
        this.leastValue = 0;
    }

    //0 -- able to query, 1 -- been edited
    public void setSign(int sign) {
        this.sign = sign;
    }

    public int getSign() {
        return sign;
    }

    public HashSet<Person> getItems() {
        return items;
    }

    public int getLeastValue() {
        return leastValue;
    }

    public void setLeastValue(int leastValue) {
        this.leastValue = leastValue;
    }

    public void addAll(HashSet<Person> hashSet) {
        this.items.addAll(hashSet);
    }
}
