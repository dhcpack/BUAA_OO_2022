import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Person;

import java.util.HashMap;

public class MyGroup implements Group {
    private final int id;
    private final HashMap<Integer, Person> people;
    private int sumValue;
    private int sumAge;
    private int sumAgeSquare;

    public MyGroup(int id) {
        this.id = id;
        this.sumAge = 0;
        this.sumValue = 0;
        this.sumAgeSquare = 0;
        this.people = new HashMap<>();
    }

    public HashMap<Integer, Person> getPeople() {
        return people;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MyGroup)) {
            return false;
        }
        return this.id == ((MyGroup) obj).id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void addPerson(Person person) {
        if (people.size() >= 1111) {
            return;
        }
        for (Person p : people.values()) {
            addValue(person.queryValue(p));
        }
        people.put(person.getId(), person);
        sumAge += person.getAge();
        sumAgeSquare += person.getAge() * person.getAge();
    }

    @Override
    public boolean hasPerson(Person person) {
        return people.get(person.getId()) != null;
    }

    public void addValue(int value) {
        sumValue += 2 * value;
    }

    @Override
    public int getValueSum() {
        return sumValue;
    }

    @Override
    public int getAgeMean() {
        if (people.size() == 0) {
            return 0;
        }
        return sumAge / people.size();
    }

    @Override
    public int getAgeVar() {
        if (people.size() == 0) {
            return 0;
        }
        return (sumAgeSquare - 2 * getAgeMean() * sumAge
                + getAgeMean() * getAgeMean() * people.size()) / people.size();
    }

    @Override
    public void delPerson(Person person) {
        people.remove(person.getId());
        for (Person p : people.values()) {
            addValue(-person.queryValue(p));
        }
        sumAge -= person.getAge();
        sumAgeSquare -= person.getAge() * person.getAge();
    }

    @Override
    public int getSize() {
        return people.size();
    }
}