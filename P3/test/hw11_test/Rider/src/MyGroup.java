import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Person;

import java.util.HashMap;
import java.util.Objects;

public class MyGroup implements Group {
    private final int id;
    private final HashMap<Integer, MyPerson> people = new HashMap<>();
    private int totalAge = 0;
    private int totalAgeSquare = 0;
    private int valueSum = 0;

    public MyGroup(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void addPerson(Person person) {
        people.put(person.getId(), (MyPerson) person);
        totalAge += person.getAge();
        totalAgeSquare += person.getAge() * person.getAge();

        for (MyPerson myPerson : people.values()) {
            if (myPerson.isLinked(person)) {
                valueSum += 2 * person.queryValue(myPerson);
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
        if (people.size() == 0) {
            return 0;
        }
        return totalAge / people.size();
    }

    public int getAgeVar() {
        if (people.size() == 0) {
            return 0;
        }
        int mean = getAgeMean();
        return (totalAgeSquare - 2 * mean * totalAge + mean * mean * people.size()) / people.size();
    }

    public void delPerson(Person person) {
        people.remove(person.getId(), (MyPerson) person);
        totalAge -= person.getAge();
        totalAgeSquare -= person.getAge() * person.getAge();

        for (MyPerson myPerson : people.values()) {
            if (myPerson.isLinked(person)) {
                valueSum -= 2 * person.queryValue(myPerson);
            }
        }
    }

    public int getSize() {
        return people.size();
    }

    public void addSocialValue(int num) {
        for (MyPerson person : people.values()) {
            person.addSocialValue(num);
        }
    }

    public void addMoney(MyPerson person, int money) {
        int i = money / (people.size());
        for (MyPerson person1 : people.values()) {
            if (person1.equals(person)) {
                person.addMoney(-i * (people.size()) - 1);
            } else {
                person.addMoney(i);
            }
        }
    }

    public void addRelation(int value) {
        valueSum += 2 * value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MyGroup group = (MyGroup) o;
        return id == group.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
