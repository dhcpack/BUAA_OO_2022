package mymain;

import com.oocourse.spec2.main.Person;

import java.util.ArrayList;
import java.util.HashMap;

public class Kruskal {
    private ArrayList<Edge> edges;
    private ArrayList<Person> people;
    private int[] father;
    private HashMap<Integer, Integer> indHashMap;
    private int blockSum;

    public Kruskal(ArrayList<Person> people) {
        this.people = people;
        this.edges = new ArrayList<>();
        father = new int[12000];
        indHashMap = new HashMap<>();
        for (int i = 0; i < people.size(); i += 1) {
            father[i] = i;
            indHashMap.put(people.get(i).getId(), i);
        }
        for (Person person: people) {
            ArrayList<Person> acquaintance = ((MyPerson) person).getAcquaintance();
            ArrayList<Integer> value = ((MyPerson) person).getValue();
            for (int i = 0; i < value.toArray().length; i += 1) {
                if (indHashMap.containsKey(acquaintance.get(i).getId()) == false) {
                    continue;
                }
                Edge edge = new Edge();
                edge.setEdge(person.getId(), acquaintance.get(i).getId(), value.get(i));
                edges.add(edge);
            }
        }
        edges.sort(new SortByIndex());
        blockSum = people.size();
        //        for (Edge edge: edges) {
        //            System.out.println(edge.getStart());
        //            System.out.println(edge.getEnd());
        //            System.out.println(edge.getWeight());
        //            System.out.println("_____");
        //        }
        //        System.out.println(edges.toArray().length);
    }

    private int find(int id) {
        if (id != father[id]) {
            father[id] = find(father[id]);
        }
        return father[id];
    }

    private void merge(int id1, int id2) {
        int fa1 = find(id1);
        int fa2 = find(id2);
        if (father[fa1] != fa2) {
            blockSum -= 1;
        }
        father[fa1] = fa2;
        return;
    }

    public int calKruskal() {
        int sum = 0;
        int cnt = 0;
        for (Edge edge: edges) {
            int start = edge.getStart();
            int end = edge.getEnd();
            if (find(indHashMap.get(start)) != find(indHashMap.get(end))) {
                merge(indHashMap.get(start), indHashMap.get(end));
                sum += edge.getWeight();
                cnt += 1;
                if (cnt == people.size() - 1) {
                    break;
                }
            }
        }
        return sum;
    }
}