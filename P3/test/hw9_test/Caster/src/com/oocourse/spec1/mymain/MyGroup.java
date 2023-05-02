package com.oocourse.spec1.mymain;

import com.oocourse.spec1.main.Group;
import com.oocourse.spec1.main.Person;

import java.util.ArrayList;

public class MyGroup implements Group {
    private int id;
    private ArrayList<Person> people;

    public MyGroup(int id) {
        this.id = id;
        people = new ArrayList<>();
    }

    public int peopleLen() {
        return people.size();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null) {
            if (obj instanceof Group) {
                return ((Group) obj).getId() == this.id;
            }
        }
        return false;
    }

    @Override
    public void addPerson(Person person) {
        people.add(person);
    }

    @Override
    public boolean hasPerson(Person person) {
        for (Person mine : people) {
            if (mine.equals(person)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getValueSum() {
        int sum = 0;
        for (int i = 0; i < people.size(); i++) {
            for (Person person : people) {
                if (people.get(i).isLinked(person)) {
                    sum += people.get(i).queryValue(person);
                }
            }
        }
        return sum;
    }

    @Override
    public int getAgeMean() {
        if (people.size() == 0) {
            return 0;
        }
        int sum = 0;
        for (Person person : people) {
            sum += person.getAge();
        }
        return sum / people.size();
    }

    @Override
    public int getAgeVar() {
        if (people.size() == 0) {
            return 0;
        }
        int ageMean = getAgeMean();
        int sum = 0;
        for (Person person : people) {
            sum += (person.getAge() - ageMean) * (person.getAge() - ageMean);
        }
        return sum / people.size();
    }

    @Override
    public void delPerson(Person person) {
        people.remove(person);
    }

    @Override
    public int getSize() {
        return people.size();
    }
}
