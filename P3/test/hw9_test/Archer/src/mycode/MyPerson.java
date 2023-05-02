package mycode;

import com.oocourse.spec1.main.Person;

import java.util.HashMap;

public class MyPerson implements Person {
    private int id;
    private String name;
    private int age;
    private HashMap<Person, Integer> acquaintance;

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.acquaintance = new HashMap<>();
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getAge() {
        return this.age;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Person) {
            return ((MyPerson) obj).getId() == id;
        } else {
            return false;
        }
    }

    public boolean isLinked(Person person) {
        if (person.getId() == id) {
            return true;
        }
        for (Person tmp : acquaintance.keySet()) {
            if (tmp.getId() == person.getId()) {
                return true;
            }
        }
        return false;
    }

    public int queryValue(Person person) {
        for (Person tmp : acquaintance.keySet()) {
            if (tmp.getId() == person.getId()) {
                return acquaintance.get(tmp);
            }
        }
        return 0;
    }

    public int compareTo(Person p2) {
        return name.compareTo(p2.getName());
    }

    public void addAcc(Person person, int value) {
        acquaintance.put(person, value);
    }
}
