package myinstance.main;

import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Person;

import java.util.ArrayList;
import java.util.HashMap;

public class MyGroup implements Group {
    private final int id;
    private final ArrayList<Person> people;
    private final HashMap<Integer, Person> personHashMap;
    private int valueSum;
    private int ageSum;
    private int ageMean;
    private int ageVar;
    private int ageSqrSum;
    // private boolean[] updateNeed;

    public MyGroup(int id) {
        this.id = id;
        this.people = new ArrayList<>();
        this.valueSum = 0;
        this.ageSum = 0;
        this.ageMean = 0;
        this.ageVar = 0;
        this.ageSqrSum = 0;
        // this.updateNeed = new boolean[3];
        personHashMap = new HashMap<>();
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
        ageSqrSum += person.getAge() * person.getAge();
        /*updateNeed[0]
                = updateNeed[1]
                = updateNeed[2] = true;*/
        personHashMap.put(person.getId(), person);
    }

    @Override
    public boolean hasPerson(Person person) {
        return people.contains(person);
    }

    @Override
    public int getValueSum() {
        RelationList relationList = RelationList.getInstance();
        valueSum = relationList.getValueSum(personHashMap);
        return valueSum;
    }

    @Override
    public int getAgeMean() {
        if (people.isEmpty()) {
            ageMean = 0;
            return 0;
        } else {
            //if (updateNeed[1]) {
            ageMean = ageSum / people.size();
            //updateNeed[1] = false;
            //}
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
        int ageVarSum =
                ageSqrSum -
                        2 * getAgeMean() * ageSum +
                        getSize() * getAgeMean() * getAgeMean();

        ageVar = ageVarSum / (people.size());
        return ageVar;
    }

    @Override
    public void delPerson(Person person) {
        people.remove(person);
        ageSum -= person.getAge();
        ageSqrSum -= person.getAge() * person.getAge();
        /*updateNeed[0]
                = updateNeed[1]
                = updateNeed[2] = true;*/
        personHashMap.remove(person.getId());
    }

    @Override
    public int getSize() {
        return people.size();
    }

    public void addMoney(int meanMoney, int id) {
        for (Person person : people) {
            if (person.getId() != id) {
                person.addMoney(meanMoney);
            }
        }
    }
}
