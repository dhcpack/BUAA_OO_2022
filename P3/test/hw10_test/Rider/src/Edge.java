import java.util.Objects;

public class Edge {
    private final int value;
    private final int id1;
    private final int id2;

    public Edge(int id1, int id2, int value) {
        this.id1 = id1;
        this.id2 = id2;
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public Integer getId1() {
        return id1;
    }

    public Integer getId2() {
        return id2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Edge)) {
            return false;
        }
        Edge edge = (Edge) o;
        return getValue().equals(edge.getValue()) && getId1().equals(edge.getId1()) &&
                getId2().equals(edge.getId2());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue(), getId1(), getId2());
    }
}
