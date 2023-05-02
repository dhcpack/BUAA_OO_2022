package my.network;

import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Person;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class MyGroup implements Group {
    private final ArrayList<Person> people = new ArrayList<>();
    private final int id;
    private int valueSum = 0;
    private long ageSum = 0;
    private long ageSqrSum = 0;

    public MyGroup(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Group) {
            return (((Group) obj).getId() == id);
        }
        return false;
    }

    @Override
    public void addPerson(Person person) {
        if (!hasPerson(person)) {
            people.add(person);
            MyPerson p = (MyPerson) person;
            IntStream.range(0, p.size()).filter(i -> hasPerson(p.get(i))).
                    forEach(i -> valueSum += 2 * p.getValue(i));
            ageSum += person.getAge();
            ageSqrSum += (long) person.getAge() * person.getAge();
        }
    }

    @Override
    public boolean hasPerson(Person person) {
        return people.contains(person);
    }

    @Override
    public int getValueSum() {
        return valueSum;
    }

    @Override
    public int getAgeMean() {
        return getSize() == 0 ? 0 : (int) (ageSum / getSize());
    }

    @Override
    public int getAgeVar() {
        int mean = getAgeMean();
        return getSize() == 0 ? 0 :
                (int) ((ageSqrSum - 2 * ageSum * mean + mean * mean * getSize()) / getSize());
    }

    @Override
    public void delPerson(Person person) {
        if (hasPerson(person)) {
            people.remove(person);
            MyPerson p = (MyPerson) person;
            IntStream.range(0, p.size()).filter(i -> hasPerson(p.get(i))).
                    forEach(i -> valueSum -= 2 * p.getValue(i));
            ageSum -= person.getAge();
            ageSqrSum -= (long) person.getAge() * person.getAge();
        }
    }

    public void updateValueSum(Person p1, Person p2, int value) {
        valueSum += (hasPerson(p1) && hasPerson(p2)) ? 2 * value : 0;
    }

    @Override
    public int getSize() {
        return people.size();
    }

    public void addSocialValue(int num) {
        people.forEach(person -> person.addSocialValue(num));
    }

    public void addMoney(int num) {
        people.forEach(person -> person.addMoney(num / getSize()));
    }
}
