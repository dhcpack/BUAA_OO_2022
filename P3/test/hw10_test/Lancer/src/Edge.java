import java.util.Objects;

public class Edge implements Comparable<Edge> {
    private final int weight;
    private final int point1;
    private final int point2;

    public Edge(int point1, int point2, int weight) {
        this.point1 = point1;
        this.point2 = point2;
        this.weight = weight;
    }

    @Override
    public int compareTo(Edge edge) {
        if (this.weight < edge.weight) {
            return -1;
        } else if (this.weight > edge.weight) {
            return 1;
        }
        return 0;
    }

    public int getWeight() {
        return weight;
    }

    public int getPoint1() {
        return point1;
    }

    public int getPoint2() {
        return point2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Edge edge = (Edge) o;
        return weight == edge.weight && point1 == edge.point1 && point2 == edge.point2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(weight, point1, point2);
    }
}
