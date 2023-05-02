package main;

import com.oocourse.spec2.main.Person;

public class Edge implements Comparable<Edge> {
    private final Person person1;
    private final Person person2;
    private final int value;

    public Edge(Person person1, Person person2, int value) {
        this.person1 = person1;
        this.person2 = person2;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public Person getPerson1() {
        return person1;
    }

    public Person getPerson2() {
        return person2;
    }

    @Override
    public int compareTo(Edge o) {
        return value - o.getValue();
    }

    public boolean equals(Edge o) {
        if (person1.equals(o.person1) && person2.equals(o.person2)) {
            return true;
        } else {
            return person1.equals(o.person2) && person2.equals(o.person1);
        }
    }
}
