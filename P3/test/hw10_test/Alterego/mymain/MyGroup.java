package mymain;

import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Person;

import java.util.ArrayList;

public class MyGroup implements Group {
    private int id;
    private ArrayList<Person> people;

    public MyGroup(int id) {
        people = new ArrayList<>();
        this.id = id;
    }

    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Group) {
            return ((Group) obj).getId() == id;
        }
        else {
            return false;
        }
    }

    public int getId() {
        return id;
    }

    public ArrayList<Person> getPeople() {
        return people;
    }

    public void addPerson(Person person) {
        if (!hasPerson(person)) {
            people.add(person);
        }
        return;
    }

    public boolean hasPerson(Person person) {
        for (int i = 0; i < people.toArray().length; i += 1) {
            if (people.get(i).equals(person)) {
                return true;
            }
        }
        return false;
    }

    public int getValueSum() {
        int sum = 0;
        int i;
        for (i = 0; i < people.toArray().length; i += 1) {
            int j;
            for (j = 0; j < people.toArray().length; j += 1) {
                if (people.get(i).isLinked(people.get(j))) {
                    sum += people.get(i).queryValue(people.get(j));
                }
            }
        }
        return sum;
    }

    public int getAgeMean() {
        if (people.toArray().length == 0) {
            return 0;
        }
        else {
            int sum = 0;
            int i;
            for (i = 0; i < people.toArray().length; i += 1) {
                sum += people.get(i).getAge();
            }
            return sum / people.toArray().length;
        }
    }

    public int getAgeVar() {
        if (people.toArray().length == 0) {
            return 0;
        }
        else {
            int i;
            int sum = 0;
            for (i = 0; i < people.toArray().length; i += 1) {
                sum += ((people.get(i).getAge() - getAgeMean()) *
                        (people.get(i).getAge() - getAgeMean()));
            }
            sum = sum / people.toArray().length;
            return sum;
        }
    }

    public void delPerson(Person person) {
        if (hasPerson(person)) {
            people.remove(person);
        }
        return;
    }

    public int getSize() {
        return people.toArray().length;
    }

    public int getPeopleLen() {
        return people.toArray().length;
    }
}
