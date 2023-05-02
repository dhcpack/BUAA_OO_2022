package society.group;

import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Person;

import java.util.HashMap;

public class SocialGroup implements Group {
    private int id;
    private HashMap<Integer, Person> people;

    public SocialGroup(int id) {
        this.id = id;
        this.people = new HashMap<>();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Group && ((Group) obj).getId() == id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void addPerson(Person person) {
        people.put(person.getId(), person);
    }

    @Override
    public boolean hasPerson(Person person) {
        return people.containsKey(person.getId());
    }

    @Override
    public int getValueSum() {
        int valueSum = 0;
        for (Person person: people.values()) {
            for (Person linkPerson: people.values()) {
                valueSum += person.queryValue(linkPerson);
            }
        }
        return valueSum;
    }

    @Override
    public int getAgeMean() {
        if (people.size() == 0) {
            return 0;
        }
        int sum = 0;
        for (Person person: people.values()) {
            sum += person.getAge();
        }
        return sum / people.size();
    }

    @Override
    public int getAgeVar() {
        if (people.size() == 0) {
            return 0;
        }
        int sum = 0;
        int mean = getAgeMean();
        for (Person person: people.values()) {
            sum += (person.getAge() - mean) * (person.getAge() - mean);
        }
        return sum / people.size();
    }

    @Override
    public void delPerson(Person person) {
        people.remove(person.getId());
    }

    @Override
    public int getSize() {
        return people.size();
    }

    public HashMap<Integer, Person> getPeople() {
        return people;
    }
}
