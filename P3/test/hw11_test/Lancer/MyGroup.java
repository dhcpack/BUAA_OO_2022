import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Person;

import java.util.HashMap;

public class MyGroup implements Group {
    private final int id;
    private final HashMap<Integer, Person> idToPerson;
    private int valSum;

    public MyGroup(int id) {
        this.id = id;
        idToPerson = new HashMap<>();
        valSum = 0;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Group) {
            return (((Group) obj).getId() == id);
        }
        return false;
    }

    @Override
    public void addPerson(Person person) {
        idToPerson.put(person.getId(), person);
        for (Integer id1 : idToPerson.keySet()) {
            Person p1 = idToPerson.get(id1);
            if (p1.isLinked(person)) {
                valSum += 2 * p1.queryValue(person);
            }
        }
    }

    @Override
    public boolean hasPerson(Person person) {
        return idToPerson.containsKey(person.getId());
    }

    @Override
    public int getValueSum() {
        return valSum;
    }

    @Override
    public int getAgeMean() {
        int ageSum = 0;
        for (Integer id : idToPerson.keySet()) {
            Person p = idToPerson.get(id);
            ageSum += p.getAge();
        }
        return (idToPerson.size() == 0) ? 0 : (ageSum / idToPerson.size());
    }

    @Override
    public int getAgeVar() {
        int ageMean = getAgeMean();
        int varSum = 0;
        for (Integer id : idToPerson.keySet()) {
            Person p = idToPerson.get(id);
            varSum += (p.getAge() - ageMean) * (p.getAge() - ageMean);
        }
        return (idToPerson.size() == 0) ? 0 : (varSum / idToPerson.size());
    }

    @Override
    public void delPerson(Person person) {
        for (Integer id1 : idToPerson.keySet()) {
            Person p1 = idToPerson.get(id1);
            if (p1.isLinked(person)) {
                valSum -= 2 * p1.queryValue(person);
            }
        }
        idToPerson.remove(person.getId());
    }

    @Override
    public int getSize() {
        return idToPerson.size();
    }

    public void addValSum(int val) {
        valSum += val;
    }
}
