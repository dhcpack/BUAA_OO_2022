package main;

import java.util.ArrayList;
import java.util.HashMap;

public class DisjointSet<T> {
    /**
     * 并查集的实现
     */
    private final HashMap<T, Integer> dictT2I;
    private final ArrayList<Integer> disjointSet;
    private int numOfElem = 0;
    private int numOfBlock = 0;

    public DisjointSet() {
        dictT2I = new HashMap<>();
        disjointSet = new ArrayList<>();
    }

    public void addElem(T t) {
        disjointSet.add(disjointSet.size());
        dictT2I.put(t, numOfElem++);
        numOfBlock++;
    }

    /**
     * 找到祖宗
     */
    public int father(int n) {
        if (disjointSet.get(n) == n) {
            return n;
        }
        // 非递归压缩
        int ans = n;
        int temp = 0;
        while (disjointSet.get(ans) != ans) {
            ans = disjointSet.get(ans);
        }
        int i = n;
        while (i != ans) {
            temp = disjointSet.get(i);
            disjointSet.set(i, ans);
            i = temp;
        }
        return ans;
    }

    public boolean isConnected(T t1, T t2) {
        // 如果相同也会返回True
        int n1 = dictT2I.get(t1);
        int n2 = dictT2I.get(t2);
        return father(n1) == father(n2);
    }

    public int getNumOfBlocks() {
        return numOfBlock;
    }

    /**
     * 首先确保t2 t2都在集合里面
     */
    public void addRelation(T t1, T t2) {
        int f1 = father(dictT2I.get(t1));
        int f2 = father(dictT2I.get(t2));
        if (f1 != f2) {
            numOfBlock--;
            disjointSet.set(f1, f2);
        }
    }
}
