package implement.entity;

import com.oocourse.spec1.main.Person;

import java.util.ArrayList;

public class MyPerson implements Person {
    private int id;
    private String name;
    private int age;
    private ArrayList<Person> acquaintances;
    private ArrayList<Integer> value;

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.acquaintances = new ArrayList<>();
        this.value = new ArrayList<>();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getAge() {
        return age;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Person) {
            return ((Person) obj).getId() == id;
        }
        else {
            return false;
        }
    }

    public void addAcquaintance(Person person, int value) {
        this.acquaintances.add(person);
        this.value.add(value);
    }

    public boolean isLinked(Person person) {
        if (this.id == person.getId()) {
            return true;
        }
        for (Person acquaintance : acquaintances) {
            if (acquaintance.getId() == person.getId()) {
                return true;
            }
        }
        return false;
    }

    public int queryValue(Person person) {
        for (int i = 0; i < acquaintances.size(); i++) {
            if (acquaintances.get(i).getId() == person.getId()) {
                return value.get(i);
            }
        }
        return 0;
    }

    public int compareTo(Person p2) {
        return name.compareTo(p2.getName());
    }
}
