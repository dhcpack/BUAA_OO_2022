package com.oocourse.spec3.main;

import java.util.HashMap;

public class MyGroup implements Group {
    private final int id;
    private HashMap<Person, Integer> people;
    private int sumOfValue;
    private int ageSum;
    private int ageVarSum;

    public MyGroup(int id) {
        this.id = id;
        people = new HashMap<>();
        sumOfValue = 0;
        ageSum = 0;
        ageVarSum = 0;
    }

    public int getSumOfValue() {
        return sumOfValue;
    }

    public void setSumOfValue(int sumOfValue) {
        this.sumOfValue = sumOfValue;
    }

    public HashMap<Person, Integer> getPeople() {
        return people;
    }

    public int getId() {
        return this.id;
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Group)) {
            return false;
        }
        return (((Group) obj).getId() == this.id);
    }

    public void addPerson(Person person) {
        if (!people.containsKey(person)) {
            people.put(person, person.getId());
        }
        //ageSum
        ageSum += person.getAge();
        //ageVarSum
        ageVarSum += person.getAge() * person.getAge();
        //valueSum
        for (Person person1 : people.keySet()) {
            if (person1.isLinked(person)) {
                sumOfValue += person1.queryValue(person);
            }
        }
    }

    public boolean hasPerson(Person person) {
        return people.containsKey(person);
    }

    public int getValueSum() {
        return sumOfValue * 2;
    }

    public int getAgeMean() {
        if (people.size() == 0) {
            return 0;
        }
        return ageSum / people.size();
    }

    public int getAgeVar() {
        if (people.size() == 0) {
            return 0;
        }
        return (ageVarSum - (this.getAgeMean() * ageSum * 2) +
                this.getAgeMean() * this.getAgeMean() * people.size()) / people.size();
    }

    public void delPerson(Person person) {
        if (hasPerson(person)) {
            people.remove(person);
            //ageSum
            ageSum -= person.getAge();
            //ageVarSum
            ageVarSum -= person.getAge() * person.getAge();
            //valueSum
            for (Person person1 : people.keySet()) {
                if (person1.isLinked(person)) {
                    sumOfValue -= person1.queryValue(person);
                }
            }
        }
    }

    public int getSize() {
        return people.size();
    }

}
