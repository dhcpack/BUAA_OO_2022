package main;

import java.util.ArrayList;
import java.util.HashMap;

public class Kruskal {
    private final HashMap<Integer, Integer> map;
    private final int[] father;
    private final ArrayList<Edge> edges;
    private int size;

    public Kruskal(ArrayList<Edge> edges) {
        this.edges = edges;
        map = new HashMap<>();
        this.father = new int[20000];
        size = 0;
    }

    public void addElement(int x) {
        map.put(x, size);
        size++;
        father[map.get(x)] = map.get(x);
    }

    public void union(int x, int y) {
        int root1 = find(x);
        int root2 = find(y);
        father[root1] = root2;
    }

    public int find(int x) {
        if (x != father[x]) {
            father[x] = find(father[x]);
        }
        return father[x];
    }

    public int run() {
        ArrayList<Edge> edges1 = new ArrayList<>();
        int min = 0;
        for (Edge edge : edges) {
            int st = edge.getSt();
            int end = edge.getEnd();
            int value = edge.getValue();
            if (!map.containsKey(st)) {
                addElement(st);
            }
            if (!map.containsKey(end)) {
                addElement(end);
            }
            int root1 = find(map.get(st));
            int root2 = find(map.get(end));
            if (root1 != root2) {
                edges1.add(edge);
                union(map.get(st), map.get(end));
                min += value;
            }
        }
        /*
        for (Edge edge : edges1) {
            System.out.println("the st is:  " + edge.getSt() +
                    ", the end is:   " + edge.getEnd() + ", the weight is:   " + edge.getValue());
        }*/
        return min;
    }
}
