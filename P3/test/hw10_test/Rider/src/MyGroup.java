import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Person;

import java.util.HashSet;
import java.util.Objects;

public class MyGroup implements Group {
    private final int id;
    private int valueSum;
    private int ageVarSum;
    private int ageSum;
    private final HashSet<MyPerson> people;

    public MyGroup(int id) {
        this.id = id;
        people = new HashSet<>();
        valueSum = 0;
        ageVarSum = 0;
        ageSum = 0;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void addPerson(Person person) {
        if (!hasPerson(person)) {
            people.add((MyPerson) person);
        }

        ageSum += person.getAge();
        ageVarSum = 0;
        for (MyPerson one : people) {
            ageVarSum += ((one).getAge() - getAgeMean()) * (one.getAge() - getAgeMean());
            if (one.equals(person)) {
                continue;
            }
            if (one.isLinked(person)) {
                valueSum += 2 * ((MyPerson) person).getIdToValue().get(one.getId());
            }
        }
    }

    @Override
    public boolean hasPerson(Person person) {
        return people.contains((MyPerson) person);
    }

    @Override
    public int getValueSum() {
        return valueSum;
    }

    public void setValueSum(int value) {
        valueSum = value;
    }

    public HashSet<MyPerson> getPeople() {
        return people;
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
        if (people.size() == 0) {
            return 0;
        }
        return ageVarSum / people.size();
    }

    @Override
    public void delPerson(Person person) {
        if (hasPerson(person)) {
            people.remove((MyPerson) person);
        }
        ageSum -= person.getAge();
        ageVarSum = 0;
        for (MyPerson one : people) {
            if (one.isLinked(person)) {
                valueSum -= 2 * ((MyPerson) person).getIdToValue().get(one.getId());
            }
            ageVarSum += ((one).getAge() - getAgeMean()) * (one.getAge() - getAgeMean());
        }
    }

    @Override
    public int getSize() {
        return people.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MyGroup)) {
            return false;
        }
        MyGroup myGroup = (MyGroup) o;
        return getId() == myGroup.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
