package myinstance.main;

import com.oocourse.spec1.main.Group;
import com.oocourse.spec1.main.Person;

import java.util.ArrayList;

public class MyGroup implements Group {
    private final int id;
    private final ArrayList<Person> people;
    private int valueSum;
    private int ageSum;
    private int ageMean;
    private int ageVar;
    private boolean[] updateNeed;

    public MyGroup(int id) {
        this.id = id;
        this.people = new ArrayList<>();
        this.valueSum = 0;
        this.ageSum = 0;
        this.ageMean = 0;
        this.ageVar = 0;
        this.updateNeed = new boolean[3];
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || /*obj.getClass() != getClass()*/ !(obj instanceof Group)) {
            return false;
        } else {
            return (id == (((Group) obj).getId()));
        }
        // return false;
    }

    @Override
    public void addPerson(Person person) {
        people.add(person);
        // valueSum += ((MyPerson) person).getValueSum();
        ageSum += person.getAge();
        updateNeed[0]
                = updateNeed[1]
                = updateNeed[2] = true;
    }

    @Override
    public boolean hasPerson(Person person) {
        return people.contains(person);
    }

    @Override
    public int getValueSum() {
        if (updateNeed[0]) {
            valueSum = 0;
            for (Person person : people) {
                for (Person person1 : people) {
                    if (person.isLinked(person1)) {
                        valueSum += person.queryValue(person1);
                    }
                }
            }
            updateNeed[0] = false;
        }
        return valueSum;
    }

    @Override
    public int getAgeMean() {
        if (people.isEmpty()) {
            ageMean = 0;
            return 0;
        } else {
            if (updateNeed[1]) {
                ageMean = ageSum / people.size();
                updateNeed[1] = false;
            }
            return ageMean;
        }
        // return 0;
    }

    @Override
    public int getAgeVar() {
        if (people.isEmpty()) {
            ageVar = 0;
            return 0;
        }
        if (updateNeed[2]) {
            int ageVarSum = 0;
            for (Person person : people) {
                ageVarSum +=
                        (person.getAge() - getAgeMean())
                                * (person.getAge() - getAgeMean());
            }
            ageVar = ageVarSum / (people.size());
            updateNeed[2] = false;
        }
        return ageVar;
    }

    @Override
    public void delPerson(Person person) {
        people.remove(person);
        ageSum -= person.getAge();
        updateNeed[0]
                = updateNeed[1]
                = updateNeed[2] = true;
    }

    @Override
    public int getSize() {
        return people.size();
    }
}
