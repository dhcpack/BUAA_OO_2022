package my.network.util;

import com.oocourse.spec3.main.Person;
import my.network.MyPerson;

import java.util.ArrayList;
import java.util.HashMap;

public class FindUnionSet {
    private final ArrayList<MyPerson> people = new ArrayList<>();
    private final HashMap<Person, Person> father = new HashMap<>();
    private final HashMap<Person, Integer> depth = new HashMap<>();

    public void merge(Person p1, Person p2) {
        Person f1 = find(p1);
        Person f2 = find(p2);
        if (depth.get(f1) <= depth.get(f2)) {
            father.put(f1, f2);
        } else {
            father.put(f2, f1);
        }
        if (depth.get(f1).equals(depth.get(f2)) && f1 != f2) {
            depth.put(f2, depth.get(f2) + 1);
        }
    }

    public Person find(Person person) {
        if (!father.get(person).equals(person)) {
            father.put(person, find(father.get(person)));
        }
        return father.get(person);
    }

    public void putFather(Person junior, Person senior) {
        father.put(junior, senior);
    }

    public void putDepth(Person person, Integer rank) {
        depth.put(person, rank);
    }

    public void add(Person person) {
        people.add((MyPerson) person);
        putFather(person, person);
        putDepth(person, 1);
    }

    public boolean isFather(Person person) {
        return find(person).equals(person);
    }

    public boolean isRelated(Person person1, Person person2) {
        return find(person1).equals(find(person2));
    }

    public void clear() {
        people.clear();
        father.entrySet().clear();
        depth.entrySet().clear();
    }

    public ArrayList<MyPerson> getPeople() {
        return people;
    }

    public int size() {
        return people.size();
    }
}
