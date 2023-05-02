import com.oocourse.spec1.main.Group;
import com.oocourse.spec1.main.Person;

import java.util.HashMap;

public class MyGroup implements Group {
    private final int id;
    private int totalAge = 0;
    private int totalAgeSquare = 0;
    private int valueSum = 0;
    private final HashMap<Integer, MyPerson> people = new HashMap<>();

    public MyGroup(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Group) {
            return (((Group) obj).getId() == id);
        } else {
            return false;
        }
    }

    public void addPerson(Person person) {
        people.put(person.getId(), (MyPerson) person);
        totalAge += person.getAge();
        totalAgeSquare += person.getAge() * person.getAge();
        for (MyPerson person1 : people.values()) {
            if (person1.isLinked(person)) {
                valueSum += 2 * person1.queryValue(person);
            }
        }
    }

    public boolean hasPerson(Person person) {
        return people.containsKey(person.getId());
    }

    public int getValueSum() {
        return valueSum;
    }

    public int getAgeMean() {
        return totalAge / people.size();
    }

    public int getAgeVar() {
        int mean = getAgeMean();
        return (totalAgeSquare - 2 * mean * totalAge + mean * mean * people.size()) / people.size();
    }

    public void delPerson(Person person) {
        people.remove(person.getId(), (MyPerson) person);
        totalAge -= person.getAge();
        totalAgeSquare -= person.getAge() * person.getAge();
        for (MyPerson person1 : people.values()) {
            if (person1.isLinked(person)) {
                valueSum -= 2 * person1.queryValue(person);
            }
        }
    }

    public int getSize() {
        return people.size();
    }
}
