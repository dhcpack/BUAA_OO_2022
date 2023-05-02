package mycode;

import com.oocourse.spec1.main.Group;
import com.oocourse.spec1.main.Person;

import java.util.HashMap;

public class MyGroup implements Group {
    private int id;
    private HashMap<Integer, Person> personHashMap;
    private int valueSum;
    private int ageSum;

    public MyGroup(int id) {
        this.id = id;
        this.personHashMap = new HashMap<>();
        this.valueSum = 0;
        this.ageSum = 0;
    }

    public int getId() {
        return this.id;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Group) {
            if (obj != null) {
                return (((Group) obj).getId() == id);
            }
        }
        return false;
    }

    public boolean hasPerson(Person person) {
        return personHashMap.containsKey(person.getId());
    }

    public void addPerson(Person person) {
        if (!hasPerson(person)) {
            for (int i : personHashMap.keySet()) {
                if (personHashMap.get(i).isLinked(person)) {
                    valueSum += personHashMap.get(i).queryValue(person) << 1;
                } else {
                    continue;
                }
            }
            ageSum += person.getAge();
            personHashMap.put(person.getId(), person);
        } else {
            return;
        }
    }

    public int getValueSum() {
        return (personHashMap.size() == 0) ? 0 : valueSum;
    }

    public int getAgeMean() {
        return (personHashMap.size() == 0) ? 0 : ageSum / personHashMap.size();
    }

    public int getAgeVar() {
        if (personHashMap.size() == 0) {
            return 0;
        } else {
            int sum = 0;
            for (int i : personHashMap.keySet()) {
                sum += (personHashMap.get(i).getAge() - getAgeMean()) *
                        ((personHashMap.get(i).getAge() - getAgeMean())
                                / personHashMap.size());
            }
            return sum;
        }
    }

    @Override
    public void delPerson(Person person) {
        if (hasPerson(person) == true) {
            personHashMap.remove(person.getId());
            for (int i : personHashMap.keySet()) {
                if (personHashMap.get(i).isLinked(person)) {
                    valueSum -= personHashMap.get(i).queryValue(person) << 1;
                }
            }
            ageSum -= person.getAge();
        } else {
            return;
        }
    }

    public int getSize() {
        return personHashMap.size();
    }

    public void addValueSum(int id1, int id2, int value) {
        if (personHashMap.containsKey(id1) && personHashMap.containsKey(id2)) {
            valueSum += 2 * value;
        }
    }
}
