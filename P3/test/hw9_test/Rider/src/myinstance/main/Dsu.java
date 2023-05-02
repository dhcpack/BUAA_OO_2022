package myinstance.main;

// import com.oocourse.spec1.main.Person;

import java.util.HashMap;

public class Dsu {
    private final HashMap<MyPerson, MyPerson> father;
    private final HashMap<MyPerson, Integer> size;
    private int sum;

    public Dsu() {
        father = new HashMap<>();
        size = new HashMap<>();
        sum = 0;
    }

    // 路径压缩find()
    public MyPerson getFather(MyPerson person) {
        if (person == father.get(person)) {
            return person;
        } else {
            father.put(person, getFather(father.get(person)));
            return father.get(person);
        }
    }

    public void addPerson(MyPerson person) {
        father.put(person, person);
        size.put(person, 1);
        sum++;
    }

    public void merge(MyPerson a, MyPerson b) {
        MyPerson fatherA = getFather(a);
        MyPerson fatherB = getFather(b);

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

    public boolean isSameSet(MyPerson a, MyPerson b) {
        return (getFather(a).equals(getFather(b)));
        // return false;
    }

    public int queryBlockSum() {
        return sum;
    }
}
