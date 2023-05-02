package myinstance.main;

public class Relation implements Comparable<Relation> {
    private int id1;
    private int id2;
    private int value;

    public int getId1() {
        return id1;
    }

    public int getId2() {
        return id2;
    }

    public Relation(int id1, int id2, int value) {
        this.id1 = id1;
        this.id2 = id2;
        this.value = value;
    }

    @Override
    public int compareTo(Relation o) {
        return (this.value - o.getValue());
    }

    public int getValue() {
        return value;
    }
}
