package main;

import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Person;

import java.util.ArrayList;

public class MyGroup implements Group {

    private final int id;
    private final ArrayList<Person> people = new ArrayList<>();
    private int ageSum = 0;
    private int ageVar = -1;

    public MyGroup(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void addPerson(Person person) {
        people.add(person);
        ageSum += person.getAge();
        ageVar = -1; //represents not valid
    }

    @Override
    public boolean hasPerson(Person person) {
        return people.contains(person);
    }

    @Override
    public int getValueSum() {
        // TODO 维护valve sum?
        int sum = 0;
        for (Person p1 : people) {
            for (Person p2 : people) {
                sum += p1.queryValue(p2);
            }
        }
        return sum;
    }

    @Override
    public int getAgeMean() {
        if (people.size() == 0) {
            return 0;
        }
        return ageSum / people.size();
    }

    @Override
    public int getAgeVar() {
        //方差
        if (people.size() == 0) {
            return 0;
        }

        if (ageVar < 0) {
            int sum = 0;
            int mean = getAgeMean();
            for (Person p : people) {
                sum += (mean - p.getAge()) * (mean - p.getAge());
            }
            ageVar = sum / people.size();
        }
        return ageVar;
    }

    @Override
    public void delPerson(Person person) {
        people.remove(person);
        ageSum -= person.getAge();
        ageVar = -1;
    }

    @Override
    public int getSize() {
        return people.size();
    }

    public void addSocialValue(int add) {
        for (Person p : people) {
            p.addSocialValue(add);
        }
    }
}
