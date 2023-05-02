package mymain;

public class Edge {
    private int start;
    private int end;
    private int weight;

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public int getWeight() {
        return weight;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setEdge(int u, int v, int w) {
        setStart(u);
        setEnd(v);
        setWeight(w);
    }
}
