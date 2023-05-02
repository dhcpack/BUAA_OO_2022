package society.graph;

import com.oocourse.spec2.main.Person;

public class Relation {
    private int value;
    private Person person;

    public Relation(int value, Person person) {
        this.value = value;
        this.person = person;
    }

    public int getValue() {
        return value;
    }

    public Person getPerson() {
        return person;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
