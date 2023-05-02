import java.util.HashMap;
import java.util.HashSet;

public class Union {
    private final HashMap<Integer, Integer> union = new HashMap<>();  // key: personId  value: root
    private final HashMap<Integer, Integer> rank = new HashMap<>();  // key: personId  value: rank
    private int blockSum = 0;

    public Union() {
    }

    public Union(HashSet<Integer> initialUnion) {
        for (Integer id : initialUnion) {
            addNewUnion(id);
        }
    }

    public void addNewUnion(int id) {
        union.put(id, id);  // root设为自己
        rank.put(id, 1);    // rank设为1
        blockSum++;         // 块数+1
    }

    public void addNewRelation(int id1, int id2) {
        int root1 = findRoot(id1);
        int root2 = findRoot(id2);
        if (root1 == root2) {
            return;
        }
        if (rank.get(root1) < rank.get(root2)) {
            union.put(root1, root2);
        } else if (rank.get(root1) > rank.get(root2)) {
            union.put(root2, root1);
        } else {
            union.put(root1, root2);
            rank.put(root2, rank.get(root2) + 1);  // 按秩优化
        }
        blockSum--;
    }

    public int findRoot(int id) {
        if (id == union.get(id)) {  // root
            return id;
        } else {
            int ans = findRoot(union.get(id));
            union.put(id, ans);  // 路径压缩
            return union.get(id);
        }
    }

    public int getBlockSum() {
        return blockSum;
    }
}
