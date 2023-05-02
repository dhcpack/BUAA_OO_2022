package myinstance.main;

import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;

public class Graph {
    private static final int INF = 0x3f3f3f3f;
    private static final Graph GRAPH = new Graph();
    private final HashMap<MyPerson, HashSet<MyPerson>> relations;
    private int num;

    private Graph() {
        relations = new HashMap<>();
        num = 0;
    }

    public static Graph getInstance() {
        return GRAPH;
    }

    public void addPerson(MyPerson person) {
        HashSet<MyPerson> relationHashSet = new HashSet<>();
        relations.put(person, relationHashSet);
        num++;
    }

    public void addRelation(MyPerson src, MyPerson dst, int value) {
        relations.get(src).add(dst);
    }

    public int getShortestPathValue(MyPerson src, MyPerson dst) {
        // boolean[] vis = new boolean[num];
        HashMap<Integer, Boolean> vis = new HashMap<>();
        HashMap<Integer, Integer> dis = new HashMap<>();
        Queue<Node> pq = new PriorityQueue<>((n1, n2) -> n1.value - n2.value);
        pq.add(new Node(src, 0));

        for (MyPerson person : relations.keySet()) {
            vis.put(person.getId(), false);
            dis.put(person.getId(), INF);
        }
        dis.replace(src.getId(), 0);

        while (!pq.isEmpty()) {
            Node node = pq.poll();
            MyPerson person1 = node.dstPerson;
            if (vis.get(person1.getId())) {
                continue;
            }
            if (person1.equals(dst)) {
                break;
                // return dis.get(dst.getId());
            }
            vis.replace(person1.getId(), true);
            // HashSet<Relation> relationHashSet = relations.get(person);
            HashSet<MyPerson> relationHashSet = relations.get(person1);
            for (MyPerson person2 : relationHashSet) {
                int value = person1.queryValue(person2);
                if (!vis.get(person2.getId()) && value + node.value < dis.get(person2.getId())) {
                    dis.replace(person2.getId(), value + node.value);
                    pq.add(new Node(person2, dis.get(person2.getId())));
                }
            }

        }

        if (dis.get(dst.getId()) == INF) {
            return -1;
        } else {
            return dis.get(dst.getId());
        }
        // pq.add();
    }

    static class Node {
        public MyPerson getDstPerson() {
            return dstPerson;
        }

        public int getValue() {
            return value;
        }

        private final MyPerson dstPerson;
        private final int value;

        public Node(MyPerson dstPerson, int value) {
            this.dstPerson = dstPerson;
            this.value = value;
        }
    }
}
