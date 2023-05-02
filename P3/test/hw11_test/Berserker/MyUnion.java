import java.util.HashMap;

public class MyUnion {
    private final HashMap<Integer, Integer> fa;
    private final HashMap<Integer, Integer> size;

    public MyUnion() {
        fa = new HashMap<>();
        size = new HashMap<>();
    }

    public void add(int id) {
        fa.put(id, id);
        size.put(id, 1);
    }

    public int getfa(int id) {
        if (fa.get(id) == id) {
            return id;
        }
        int father = getfa(fa.get(id));
        fa.put(id, father);
        return father;
    }

    public void merge(int id1, int id2) {
        int fa1 = getfa(id1);
        int fa2 = getfa(id2);
        if (fa1 == fa2) {
            return;
        }
        if (size.get(fa1) > size.get(fa2)) {
            int fa = fa1;
            fa1 = fa2;
            fa2 = fa;
        }
        fa.put(fa1, fa2);
        size.merge(fa2, size.get(fa1), Integer::sum);
    }

}
