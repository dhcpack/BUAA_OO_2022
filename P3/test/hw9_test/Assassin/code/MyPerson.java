import com.oocourse.spec1.main.Person;

import java.util.HashMap;

public class MyPerson implements Person {
    private int id;
    private String name;
    private int age;
    private HashMap<Integer, Person> acquaintance;
    private HashMap<Integer, Integer> values;

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.acquaintance = new HashMap<>();
        this.values = new HashMap<>();
    }

    public void addAcquaintance(Person person, int value) {
        int id = person.getId();
        acquaintance.put(id, person);
        values.put(id, value);
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getAge() {
        return this.age;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Person) {
            return ((Person) obj).getId() == this.id;

        }
        return false;
    }

    @Override
    public boolean isLinked(Person person) {
        if (person.getId() == this.id) {
            return true;
        }
        return acquaintance.containsKey(person.getId());
    }

    @Override
    public int queryValue(Person person) {
        int personId = person.getId();
        if (acquaintance.containsKey(personId)) {
            return values.get(personId);
        } else {
            return 0;
        }
    }

    @Override
    public int compareTo(Person p2) {
        return this.name.compareTo(p2.getName());
    }
}
