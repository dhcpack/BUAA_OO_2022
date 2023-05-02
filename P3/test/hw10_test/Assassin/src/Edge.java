public class Edge implements Comparable<Edge> {
    private int id1;
    private int id2;
    private int value;

    public Edge(int id1, int id2, int value) {
        this.id1 = id1;
        this.id2 = id2;
        this.value = value;
    }

    public int getId1() {
        return id1;
    }

    public int getId2() {
        return id2;
    }

    public int getValue() {
        return value;
    }

    @Override
    public int compareTo(Edge o) {
        return this.value - o.value;
    }
}
