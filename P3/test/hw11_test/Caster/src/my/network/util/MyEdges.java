package my.network.util;

import java.util.ArrayList;
import java.util.Collections;

public class MyEdges {
    private final ArrayList<MyEdge> edges = new ArrayList<>();

    public MyEdges() {}

    public int size() {
        return edges.size();
    }

    public void add(MyEdge edge) {
        edges.add(edge);
    }

    public void sort() {
        Collections.sort(edges);
    }

    public void clear() {
        edges.clear();
    }

    public MyEdge get(int i) {
        return edges.get(i);
    }

    public ArrayList<MyEdge> getEdges() {
        return edges;
    }
}
