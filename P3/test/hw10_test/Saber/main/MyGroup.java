package main;

import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Person;

import java.util.HashSet;

public class MyGroup implements Group {
    private final int id;
    private final HashSet<Person> people = new HashSet<>();
    private int valueSum;
    private int ageSum;
    private int ageTimeSum;

    public MyGroup(int id) {
        this.id = id;
        this.valueSum = 0;
        this.ageSum = 0;
        this.ageTimeSum = 0;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Group)) {
            return false;
        }
        return ((Group) obj).getId() == id;
    }

    public HashSet<Person> getPeople() {
        return people;
    }

    @Override
    public void addPerson(Person person) {
        for (Person x: people) {
            if (x.isLinked(person)) {
                valueSum += 2 * x.queryValue(person);
            }
        }
        ageSum += person.getAge();
        ageTimeSum += person.getAge() * person.getAge();
        people.add(person);
    }

    @Override
    public boolean hasPerson(Person person) {
        return people.contains(person);
    }

    @Override
    public int getValueSum() {
        return this.valueSum;
    }

    public void addValue(int value) {
        this.valueSum += 2 * value;
    }

    @Override
    public int getAgeMean() {   // 维护个ageSum吧
        if (people.size() == 0) {
            return 0;
        }
        return ageSum / people.size();
    }

    @Override
    public int getAgeVar() {
        if (people.size() == 0) {
            return 0;
        }
        int mean = getAgeMean();
        return (ageTimeSum - 2 * mean * ageSum
                + people.size() * mean * mean) / people.size();
    }

    @Override
    public void delPerson(Person person) {
        ageSum -= person.getAge();
        ageTimeSum -= person.getAge() * person.getAge();
        people.remove(person);
        for (Person x: people) {
            if (x.isLinked(person)) {   // O(1)
                valueSum -= 2 * x.queryValue(person);   // O(1)
            }
        }
    }

    @Override
    public int getSize() {
        return people.size();
    }

    @Override
    public String toString() {
        return id + " " + valueSum;
    }
}
