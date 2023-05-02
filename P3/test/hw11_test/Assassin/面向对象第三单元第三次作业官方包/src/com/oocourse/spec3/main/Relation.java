package com.oocourse.spec3.main;

import java.util.ArrayList;

public class Relation implements Comparable {
    private ArrayList<Person> couple = new ArrayList<>(2);
    private int value;

    public Relation(Person person1,Person person2) {
        couple.add(person1);
        couple.add(person2);
        this.value = person1.queryValue(person2);
    }

    public ArrayList<Person> getCouple() {
        return couple;
    }

    public int getValue() {
        return value;
    }

    @Override
    public int compareTo(Object o) {
        return this.value - ((Relation) o).getValue();
    }
}
