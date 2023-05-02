package main;

import com.oocourse.spec2.main.Person;

public class Relation implements Comparable<Relation> {
    private final Person person1;
    private final Person person2;
    private final Integer value;

    public Relation(Person person1, Person person2) {
        this.person1 = person1;
        this.person2 = person2;
        this.value = person1.queryValue(person2);
    }

    public Person getPerson1() {
        return person1;
    }

    public Person getPerson2() {
        return person2;
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Relation{" +
                "person1=" + person1.getId() +
                ", person2=" + person2.getId() +
                ", value=" + value +
                '}';
    }

    @Override
    public int compareTo(Relation o) {
        if (o != null) {
            return value.compareTo(((Relation) o).getValue());
        }
        return -1;
    }
}
