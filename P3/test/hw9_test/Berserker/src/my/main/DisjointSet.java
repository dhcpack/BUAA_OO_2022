package my.main;

import java.util.ArrayList;
import java.util.HashMap;

public class DisjointSet<T>
{
    private final HashMap<T, Integer> map;
    /**
     * 这个表上登记着两种值，大于 0 的时候指向了其父节点
     * 小于 0 的时候为高度的负数
     */
    private final ArrayList<Integer> disjointSet;
    private int cur;
    private int blockSum;

    public DisjointSet()
    {
        this.map = new HashMap<>();
        this.disjointSet = new ArrayList<>();
        disjointSet.add(0);
        this.cur = 1;
        this.blockSum = 0;
    }

    public void addNode(T element)
    {
        map.put(element, cur);
        disjointSet.add(0);
        cur++;
        blockSum++;
    }

    public void addEdge(T element1, T element2)
    {
        int root1 = find(map.get(element1));
        int root2 = find(map.get(element2));
        if (root1 != root2)
        {
            blockSum--;
            union(root1, root2);
        }
    }

    public int getBlockSum()
    {
        return this.blockSum;
    }

    public boolean isLinked(T element1, T element2)
    {
        int root1 = find(map.get(element1));
        int root2 = find(map.get(element2));
        return root1 == root2;
    }

    /**
     * 利用了路径压缩的方法
     * @param index
     * @return
     */
    private int find(int index)
    {
        if (disjointSet.get(index) <= 0)
        {
            return index;
        }
        else
        {
            disjointSet.set(index, find(disjointSet.get(index)));
            return disjointSet.get(index);
        }
    }

    /**
     * 用于合并两个不相交集合，采用了基于高度的合并
     * @param root1
     * @param root2
     */
    private void union(int root1, int root2)
    {
        if (disjointSet.get(root2) < disjointSet.get(root1))
        {
            disjointSet.set(root1, root2);
        }
        else
        {
            if (disjointSet.get(root2) == disjointSet.get(root1))
            {
                disjointSet.set(root1, disjointSet.get(root1) - 1);
            }
            disjointSet.set(root2, root1);
        }
    }
}
