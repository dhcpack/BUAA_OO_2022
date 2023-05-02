import java.util.HashMap;

public class JoinSearchSet {
    private HashMap<Integer, Integer> father;//子节点->父节点
    private HashMap<Integer, Integer> count;//根节点->根节点的节点数量

    public JoinSearchSet() {
        this.father = new HashMap<>();
        this.count = new HashMap<>();
    }

    public void add(int id) {
        father.put(id, id);
        count.put(id, 1);
    }

    public int find(int id) {
        if (id == father.get(id)) {
            return id;
        }
        int tmp = find(father.get(id));
        father.replace(id, tmp);
        return tmp;
    }

    public void link(int id1, int id2) {
        int id11 = find(id1);
        int id22 = find(id2);
        if (id11 != id22) {
            int newCnt = count.get(id11) + count.get(id22);
            if (count.get(id11) > count.get(id22)) {
                father.replace(id22, id11);
                count.replace(id11, newCnt);
                count.remove(id22);
            } else {
                father.replace(id11, id22);
                count.replace(id22, newCnt);
                count.remove(id11);
            }
        }
    }

    public boolean hasLink(int id1, int id2) {
        if (!father.containsKey(id1) || !father.containsKey(id2)) {
            return false;
        }
        return find(id1) == find(id2);
    }

    public int size() {
        return count.size();
    }
}
