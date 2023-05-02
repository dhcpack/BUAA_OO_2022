package myinstance.main;

// import com.oocourse.spec1.main.Person;

import java.util.HashMap;

public class Dsu<T> {
    // private final static Dsu DSU = new Dsu();
    private final HashMap<T, T> father;
    private final HashMap<T, Integer> size;
    private int sum;

    public Dsu() {
        father = new HashMap<>();
        size = new HashMap<>();
        sum = 0;
    }

    /*public static Dsu getInstance() {
        return DSU;
    }*/

    // 路径压缩find()
    public T getFather(T person) {
        if (person == father.get(person)) {
            return person;
        } else {
            father.put(person, getFather(father.get(person)));
            return father.get(person);
        }
    }

    public void addPerson(T person) {
        father.put(person, person);
        size.put(person, 1);
        sum++;
    }

    public void merge(T a, T b) {
        T fatherA = getFather(a);
        T fatherB = getFather(b);

        if (fatherA.equals(fatherB)) {
            return;
        }
        if (size.get(fatherA) < size.get(fatherB)) {
            father.replace(fatherA, fatherB);
            size.replace(fatherB, size.get(fatherA) + size.get(fatherB));
        } else {
            father.replace(fatherB, fatherA);
            size.replace(fatherA, size.get(fatherA) + size.get(fatherB));
        }
        sum--;
    }

    public boolean isSameSet(T a, T b) {
        return (getFather(a).equals(getFather(b)));
        // return false;
    }

    public int queryBlockSum() {
        return sum;
    }

    public int brotherSize(T person) {
        return size.get(father.get(person));
    }

    public boolean contains(T son1) {
        return father.containsKey(son1);
    }

    public int si() {
        return father.size();
    }
}
