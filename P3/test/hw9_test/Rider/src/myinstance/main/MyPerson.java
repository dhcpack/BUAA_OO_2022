package myinstance.main;

import com.oocourse.spec1.main.Person;

import java.util.HashMap;

public class MyPerson implements Person {

    private final int id;
    private final String name;
    private final int age;
    private Integer valueSum;
    private final HashMap<Integer, Person> acquaintance;
    //private ArrayList<Person> acquaintance;
    //private ArrayList<Integer> value;
    private final HashMap<Integer, Integer> value;

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.valueSum = 0;
        this.acquaintance = new HashMap<>();
        this.value = new HashMap<>();
    }

    public Integer getValueSum() {
        return valueSum;
    }

    public void addAcquaintance(Person person, int value) {
        acquaintance.put(person.getId(), person);
        this.value.put(person.getId(), value);
        valueSum += value;
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
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof Person)) {
            return false;
        } else {
            return (id == ((Person) obj).getId());
        }
    }

    @Override
    public boolean isLinked(Person person) {
        if (person.getId() == id) {
            return true;
        } else {
            return acquaintance.containsKey(person.getId());
        }
    }

    @Override
    public int queryValue(Person person) {
        if (acquaintance.containsKey(person.getId())) {
            return value.get(person.getId());
        } else {
            return 0;
        }
    }

    @Override
    public int compareTo(Person p2) {
        return name.compareTo(p2.getName());
    }
}
