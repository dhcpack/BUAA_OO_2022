package com.oocourse.spec1.mymain;

import com.oocourse.spec1.main.Person;

import java.util.ArrayList;

public class MyPerson implements Person {
    private int id;
    private String name;
    private int age;
    private ArrayList<Person> people;
    private ArrayList<Integer> values;

    public void addPerson(Person person) {
        this.people.add(person);
    }

    public void addValue(int value) {
        this.values.add(value);
    }

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        people = new ArrayList<>();
        values = new ArrayList<>();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getAge() {
        return age;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null) {
            if (obj instanceof Person) {
                return ((Person) obj).getId() == this.id;
            }
        }
        return false;
    }

    @Override
    public boolean isLinked(Person person) {
        if (person.getId() == this.id) {
            return true;
        }
        for (Person mine : people) {
            if (mine.getId() == person.getId()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int queryValue(Person person) {
        for (int i = 0; i < people.size(); i++) {
            if (people.get(i).getId() == person.getId()) {
                return values.get(i);
            }
        }
        return 0;
    }

    @Override
    public int compareTo(Person p2) {
        return this.name.compareTo(p2.getName());
    }
}
