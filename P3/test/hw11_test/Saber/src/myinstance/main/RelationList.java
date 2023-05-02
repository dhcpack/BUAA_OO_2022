package myinstance.main;

import com.oocourse.spec3.main.Person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

public class RelationList {
    private static final RelationList RELATION_LIST = new RelationList();
    private final ArrayList<Relation> relations;
    // private final Dsu<MyPerson> dsu;

    private RelationList() {
        relations = new ArrayList<>();
        // dsu = Dsu.getInstance();
        // dsu = new Dsu<MyPerson>();
    }

    public static RelationList getInstance() {
        return RELATION_LIST;
    }

    public void addRelation(Relation relation) {
        relations.add(relation);
    }

    public int queryLeastConnection(MyPerson person1,
                                    ArrayList<MyPerson> peopleList,
                                    Dsu<MyPerson> dsu) {
        HashSet<Integer> idHashSet = new HashSet<>();
        Dsu<Integer> integerDsu = new Dsu<>();
        // System.out.println("size of peopleListOut: " + peopleList.size());
        for (MyPerson person : peopleList) {
            if (dsu.isSameSet(person1, person)) {
                integerDsu.addPerson(person.getId());
                // System.out.println("size of integerDsu++");
            }
        }
        // System.out.println("size of person brother:" + integerDsu.si());
        Collections.sort(relations);
        // System.out.println("size of relations: " + relations.size());
        // int cnt = 0;
        int value = 0;
        for (Relation relation : relations) {
            if ((!integerDsu.contains(relation.getId1()))
                    || (!integerDsu.contains(relation.getId2()))) {
                continue;
            }
            if (!integerDsu.isSameSet(relation.getId1(), relation.getId2())) {
                integerDsu.merge(relation.getId1(), relation.getId2());
                // cnt++;
                // System.out.println(relation.getId1()
                // + "\tand: \t" + relation.getId2() +
                // "get value: \t" + relation.getValue());
                value += relation.getValue();
            }
            if (integerDsu.queryBlockSum() == 1) {
                break;
            }
        }

        return value;
    }

    public int getValueSum(HashMap<Integer, Person> personHashMap) {
        // System.out.println("size:" + personHashMap.size());
        int value = 0;
        for (Relation relation : relations) {
            if (personHashMap.containsKey(relation.getId1()) &&
                    personHashMap.containsKey(relation.getId2())) {
                value += 2 * relation.getValue();
            }
        }
        return value;
    }
}
