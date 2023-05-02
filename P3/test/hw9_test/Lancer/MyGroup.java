import com.oocourse.spec1.main.Group;
import com.oocourse.spec1.main.Person;

import java.util.ArrayList;

public class MyGroup implements Group {
    private final int id;
    private final ArrayList<MyPerson> people;
    
    public MyGroup(int id) {
        this.id = id;
        this.people = new ArrayList<>();
    }
    
    public int getId() {
        return id;
    }
    
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Group) {
            return (((Group) obj).getId() == id);
        } else {
            return false;
        }
    }
    
    public void addPerson(Person person) {
        if (!hasPerson(person)) {
            people.add((MyPerson) person);
        }
    }
    
    public boolean hasPerson(Person person) {
        for (Person value : people) {
            if (value.equals(person)) {
                return true;
            }
        }
        return false;
    }
    
    public int getValueSum() {
        int sum = 0;
        for (int i = 0; i < people.size(); i++) {
            for (Person person : people) {
                if (people.get(i).isLinked(person)) {
                    sum += people.get(i).queryValue(person);
                }
            }
        }
        return sum;
    }
    
    public int getAgeMean() {
        if (people.size() == 0) {
            return 0;
        }
        int sum = 0;
        for (Person person : people) {
            sum += person.getAge();
        }
        return (sum / people.size());
    }
    
    public int getAgeVar() {
        if (people.size() == 0) {
            return 0;
        }
        int sum = 0;
        int avr = getAgeMean();
        for (Person person : people) {
            sum += (person.getAge() - avr) * (person.getAge() - avr);
        }
        return (sum / people.size());
    }
    
    public void delPerson(Person person) {
        if (hasPerson(person)) {
            people.remove(person);
        }
    }
    
    public int getSize() {
        return people.size();
    }
}
