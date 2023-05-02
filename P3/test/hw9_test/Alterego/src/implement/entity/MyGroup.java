package implement.entity;

import com.oocourse.spec1.main.Group;
import com.oocourse.spec1.main.Person;

import java.util.ArrayList;

public class MyGroup implements Group {
    private int id;
    private ArrayList<Person> people;

    public MyGroup(int id) {
        this.id = id;
        this.people = new ArrayList<>();
    }

    @Override
    public int getId() {
        return id;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Group) {
            return ((Group) obj).getId() == id;
        }
        else {
            return false;
        }
    }

    public void addPerson(Person person) {
        people.add(person);
    }

    public boolean hasPerson(Person person) {
        for (Person person1 : people) {
            if (person1.equals(person)) {
                return true;
            }
        }
        return false;
    }

    public int getValueSum() {
        int sum = 0;
        for (int i = 0; i < people.size(); i++) {
            int peopleValue = 0;
            for (int j = 0; j < people.size(); j++) {
                if (people.get(i).isLinked(people.get(j))) {
                    peopleValue += people.get(i).queryValue(people.get(j));
                }
            }
            sum += peopleValue;
        }
        return sum;
    }

    public int getAgeMean() {
        if (people.size() == 0) {
            return 0;
        }
        int ageSum = 0;
        for (Person person : people) {
            ageSum += person.getAge();
        }
        return (int) (ageSum / people.size());
    }

    public int getAgeVar() {
        int ageMean = getAgeMean();
        if (people.size() == 0) {
            return 0;
        }
        int ageVar = 0;
        for (Person person : people) {
            ageVar += (person.getAge() - ageMean) * (person.getAge() - ageMean);
        }
        return (int) (ageVar / people.size());
    }

    public void delPerson(Person person) {
        for (int i = 0; i < people.size(); i++) {
            if (people.get(i).equals(person)) {
                people.remove(i);
                break;
            }
        }
    }

    public int getSize() {
        return people.size();
    }
}
