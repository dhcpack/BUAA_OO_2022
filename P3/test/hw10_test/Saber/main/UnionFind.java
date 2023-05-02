package main;

import java.util.HashMap;

public class UnionFind<T> {
    private final HashMap<T, T> roots = new HashMap<>();
    private final HashMap<T, Integer> setSize = new HashMap<>();

    public T findRoot(T t) {
        T root = roots.get(t);
        return root.equals(t) ? root : findRoot(root);
    }

    public void union(T a, T b) {
        T ra = findRoot(a);
        T rb = findRoot(b);
        if (ra.equals(rb)) {
            return;
        }
        if (setSize.get(ra) <= setSize.get(rb)) {
            roots.replace(ra, rb);
            setSize.replace(rb, setSize.get(ra) + setSize.get(rb));
        } else {
            roots.replace(rb, ra);
            setSize.replace(ra, setSize.get(ra) + setSize.get(rb));
        }
    }

    public void addElement(T t) {
        roots.put(t, t);
        setSize.put(t, 1);
    }

    public int cntBlock() {
        int cnt = 0;
        for (T x: roots.keySet()) {
            if (roots.get(x).equals(x)) {
                cnt++;
            }
        }
        return cnt;
    }

}
