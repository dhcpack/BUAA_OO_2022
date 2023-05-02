import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Person;

import java.util.HashMap;

public class MyGroup implements Group {
    private int id;
    private HashMap<Integer, Person> people;
    private int valSum;

    public MyGroup(int id) {
        this.id = id;
        this.people = new HashMap<>();
        this.valSum = 0;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Group) {
            return ((Group) obj).getId() == id;
        }
        return false;
    }

    //如果people里没有person，那么加入people
    @Override
    public void addPerson(Person person) {
        if (!hasPerson(person)) {
            people.put(person.getId(), person);
            //维护边权和
            for (Person person1 : this.people.values()) {
                this.valSum += person1.queryValue(person) * 2;//TODO 每个关系应该是计算两次还是一次?
            }
        }
    }

    @Override
    public boolean hasPerson(Person person) {
        return people.containsKey(person.getId());
    }

    @Override
    public int getValueSum() {
        return valSum;
    }

    @Override
    public int getAgeMean() {
        int sum = 0;
        for (Person person : people.values()) {
            sum += person.getAge();
        }
        if (people.size() > 0) {
            sum /= people.size();
        }
        return sum;
    }

    @Override
    public int getAgeVar() {
        int sum = 0;
        int mean = getAgeMean();
        for (Person p : people.values()) {
            int age = p.getAge();
            sum += (age - mean) * (age - mean);
        }
        if (people.size() > 0) {
            sum /= people.size();
        }
        return sum;
    }

    @Override
    public void delPerson(Person person) {
        people.remove(person.getId());
        for (Person person1 : this.people.values()) {
            //维护边权和
            valSum -= person1.queryValue(person) * 2;
        }
    }

    @Override
    public int getSize() {
        return people.size();
    }

    public void addSocialValue(int val) {
        for (Person person : people.values()) {
            person.addSocialValue(val);
        }
    }

    public void addRelatioin(int val) {
        this.valSum += val * 2;
    }
}
