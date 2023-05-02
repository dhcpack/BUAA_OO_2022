package implement.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class RelationController {
    private final HashMap<Integer, Integer> personMap;
    private final ArrayList<Integer> father;

    public RelationController() {
        this.personMap = new HashMap<>();
        this.father = new ArrayList<>();
    }

    public void addPerson(int id) {
        personMap.put(id, father.size());
        father.add(father.size());
    }

    public void addRelation(int id1, int id2) {
        int i1 = personMap.get(id1);
        int i2 = personMap.get(id2);
        int f1 = getFather(i1);
        int f2 = getFather(i2);
        father.set(f1, f2);
    }

    public int queryBlockSum() {
        HashSet<Integer> blocks = new HashSet<>();
        for (int i = 0; i < father.size(); i++) {
            blocks.add(getFather(i));
        }
        return blocks.size();
    }

    public boolean isCircle(int id1, int id2) {
        int i1 = personMap.get(id1);
        int i2 = personMap.get(id2);
        return getFather(i1) == getFather(i2);
    }

    private int getFather(int i) {
        if (father.get(i) == i) {
            return i;
        }
        else {
            int newValue = getFather(father.get(i));
            father.set(i, newValue);
            return newValue;
        }
    }
}
