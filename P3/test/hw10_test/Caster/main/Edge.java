package main;

public class Edge {
    private final int st;
    private final int end;
    private final int value;

    public Edge(int st, int end, int value) {
        this.st = st;
        this.end = end;
        this.value = value;
    }

    public int getEnd() {
        return end;
    }

    public int getSt() {
        return st;
    }

    public int getValue() {
        return value;
    }
}
