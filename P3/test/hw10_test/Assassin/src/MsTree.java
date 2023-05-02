import com.oocourse.spec2.main.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

public class MsTree {
    private JoinSearchSet searchSet;
    private ArrayList<Person> people;

    //优先队列实现，需要在queryLeastConnection调用
    public MsTree(ArrayList<Person> people) {
        this.searchSet = new JoinSearchSet();
        this.people = people;
    }

    public void addNode() {
        for (Person person : this.people) {
            this.searchSet.add(person.getId());
        }
    }

    public boolean hasLink(Edge edge) {
        if (edge == null) {
            return false;
        }
        return searchSet.hasLink(edge.getId1(), edge.getId2());
    }

    public int create() {
        this.addNode();
        //遍历边
        PriorityQueue<Edge> edges = new PriorityQueue<>();
        for (Person person : people) {
            int id1 = person.getId();
            HashMap<Integer, Integer> values = ((MyPerson) person).getValues();
            for (int id2 : values.keySet()) {
                //防止被二次保存
                if (id1 < id2) {
                    Edge edge = new Edge(id1, id2, values.get(id2));
                    edges.add(edge);
                }
            }
        }
        //添加边
        int sum = 0;
        int len = 0;
        int size = people.size() - 1;//最多需要的边数

        while (len < size) {
            while (hasLink(edges.peek())) {
                edges.poll();
            }
            Edge edge = edges.peek();
            if (edge == null) {
                break;
            }

            searchSet.link(edge.getId1(), edge.getId2());
            sum += edge.getValue();
            edges.poll();
            len++;
        }
        return sum;
    }
}
