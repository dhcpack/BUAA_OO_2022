package main;

import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Person;

import java.util.ArrayList;

public class MyGroup implements Group {
    private final int id;
    private final ArrayList<Person> people;

    public MyGroup(int id) {
        this.id = id;
        people = new ArrayList<>();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Group) {
            return ((Group) obj).getId() == id;
        }
        return false;
    }

    @Override
    public void addPerson(Person person) {
        if (!hasPerson(person)) {
            people.add(person);
        }
    }

    @Override
    public boolean hasPerson(Person person) {
        for (Person person1 : people) {
            if (person1.equals(person)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getValueSum() {
        int sum = 0;
        for (Person value : people) {
            for (Person person : people) {
                if (value.isLinked(person)) {
                    sum += value.queryValue(person);
                }
            }
        }
        return sum;
    }

    @Override
    public int getAgeMean() {
        if (people.size() == 0) {
            return 0;
        } else {
            int sum = 0;
            for (Person person : people) {
                sum += person.getAge();
            }
            return sum / people.size();
        }
    }

    @Override
    public int getAgeVar() {
        if (people.size() == 0) {
            return 0;
        } else {
            int sum = 0;
            int mean = getAgeMean();
            for (Person person : people) {
                sum += (person.getAge() - mean) * (person.getAge() - mean);
            }
            return sum / people.size();
        }
    }

    @Override
    public void delPerson(Person person) {
        boolean require = hasPerson(person);
        if (require) {
            people.remove(person);
        }
    }

    @Override
    public int getSize() {
        return people.size();
    }
}
