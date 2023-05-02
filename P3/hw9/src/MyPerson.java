import com.oocourse.spec1.main.Person;

import java.util.HashMap;

public class MyPerson implements Person {
    private final int id;
    private final String name;
    private final int age;
    private final HashMap<MyPerson, Integer> acquaintanceValueMap = new HashMap<>();

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
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
            return ((Person) obj).getId() == id;
        }
        return false;
    }

    public boolean isLinked(Person person) {
        if (person.getId() == id) {
            return true;
        }
        for (MyPerson person1 : acquaintanceValueMap.keySet()) {
            if (person1.getId() == person.getId()) {
                return true;
            }
        }
        return false;
    }

    public int queryValue(Person person) {
        for (MyPerson myPerson : acquaintanceValueMap.keySet()) {
            if (myPerson.id == person.getId()) {
                return acquaintanceValueMap.get(myPerson);
            }
        }
        return 0;
    }

    public int compareTo(Person p2) {
        return name.compareTo(p2.getName());
    }

    public void addAcquaintance(Person person, int value) {
        acquaintanceValueMap.put((MyPerson) person, value);
    }
}
