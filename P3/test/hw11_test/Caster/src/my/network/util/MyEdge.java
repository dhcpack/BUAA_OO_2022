package my.network.util;

import com.oocourse.spec3.main.Person;

public class MyEdge implements Comparable<MyEdge> {
    private final Person p1;
    private final Person p2;
    private final int value;

    public MyEdge(Person p1, Person p2, int value) {
        this.p1 = p1.getId() < p2.getId() ? p1 : p2;
        this.p2 = p1.getId() >= p2.getId() ? p1 : p2;
        this.value = value;
    }

    public Person getP1() {
        return p1;
    }

    public Person getP2() {
        return p2;
    }

    public Integer getValue() {
        return value;
    }

    public Integer getId1() {
        return p1.getId();
    }

    public Integer getId2() {
        return p2.getId();
    }

    @Override
    public int compareTo(MyEdge e) {
        if (getValue().equals(e.getValue())) {
            if (getId1().equals(e.getId1())) {
                return getId2().compareTo(e.getId2());
            }
            return getId1().compareTo(e.getId1());
        }
        return getValue().compareTo(e.getValue());
    }
}