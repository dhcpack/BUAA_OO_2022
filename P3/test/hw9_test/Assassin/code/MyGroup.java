import com.oocourse.spec1.main.Group;
import com.oocourse.spec1.main.Person;
import java.util.HashMap;

public class MyGroup implements Group {
    private int id;
    private HashMap<Integer, Person> people;
    private int ageSum;

    public MyGroup(int id) {
        this.id = id;
        this.people = new HashMap<>();
        this.ageSum = 0;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Group) {
            return ((Group) obj).getId() == this.id;
        }
        return false;
    }

    @Override
    public void addPerson(Person person) {
        this.people.put(person.getId(), person);
        this.ageSum += person.getAge();
    }

    @Override
    public boolean hasPerson(Person person) {
        return people.containsKey(person.getId());
    }

    @Override
    public int getValueSum() {
        int ans = 0;
        for (Person person : this.people.values()) {
            for (Person person1 : this.people.values()) {
                if (person.isLinked(person1)) {
                    ans += person.queryValue(person1);
                }
            }
        }
        return ans;
    }

    @Override
    public int getAgeMean() {
        return ageSum / people.size();
    }

    @Override
    public int getAgeVar() {
        int peopleSize = people.size();
        if (peopleSize == 0) {
            return 0;
        }
        int ageMean = getAgeMean();
        int ans = 0;
        for (Person person : this.people.values()) {
            ans += (person.getAge() - ageMean) * (person.getAge() - ageMean);
        }
        return ans / peopleSize;

    }

    @Override
    public void delPerson(Person person) {
        this.people.remove(person.getId());
        this.ageSum -= person.getAge();
    }

    @Override
    public int getSize() {
        return this.people.size();
    }
}
