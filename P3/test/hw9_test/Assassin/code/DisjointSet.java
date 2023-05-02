import java.util.HashMap;

public class DisjointSet {
    private HashMap<Integer, Integer> rank;
    private HashMap<Integer, Integer> father;
    private int blockNum;

    public DisjointSet() {
        this.rank = new HashMap<>();
        this.father = new HashMap<>();
        this.blockNum = 0;
    }

    public void addNode(int id) {
        father.put(id, id);
        rank.put(id, 1);
        blockNum++;
    }

    public int find(int id) {
        int idFather = father.get(id);
        if (id == idFather) {
            return id;
        } else {
            return find(idFather);
        }
    }

    public void merge(int id1, int id2) {
        int id1Father = find(id1);
        int id2Father = find(id2);
        int id1Rank = rank.get(id1);
        int id2Rank = rank.get(id2);
        if (id1Father != id2Father) {
            blockNum--;
        } else {
            return;
        }
        if (id1Rank <= id2Rank) {
            father.put(id1Father, id2Father);
        } else {
            father.put(id2Father, id1Father);
        }
        if (id1Rank == id2Rank && id1 != id2) {
            rank.put(id2Father, id2Rank + 1);
        }
    }

    public int getBlockNum() {
        return blockNum;
    }
}
