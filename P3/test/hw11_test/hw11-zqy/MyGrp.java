import java.math.BigInteger;
import java.util.HashMap;
import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Person;

public class MyGrp implements Group {
    private final int id;
    private final HashMap<Integer, MyPpl> people = new HashMap<>();

    public MyGrp(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof Group)) {
            return false;
        }
        return ((Group) o).getId() == id;
    }

    public boolean hasPerson(Person p) {
        return people.containsKey(p.getId());
    }

    public int getValueSum() {
        int[] sum = {0};
        MyGlb.myGetRels().forEach((conn, value) -> {
            if (people.containsKey(conn.getId1()) && people.containsKey(conn.getId2())) {
                sum[0] += value * 2;
            }
        });
        return sum[0];
    }

    private int tmpAgeTotal = 0;

    public int getAgeMean() {
        int n = getSize();
        return n == 0 ? 0 : tmpAgeTotal / n;
    }

    public int getAgeVar() {
        int n = getSize();
        if (n == 0) {
            return 0;
        }
        int avg = getAgeMean();
        BigInteger[] sum = {BigInteger.ZERO};
        people.values().forEach(p -> {
            sum[0] = sum[0].add(BigInteger.valueOf(p.getAge() - avg).pow(2));
        });
        return sum[0].divide(BigInteger.valueOf(n)).intValue();
    }

    public void addPerson(Person p) {
        people.put(p.getId(), (MyPpl) p);
        tmpAgeTotal += p.getAge();
    }

    public void delPerson(Person p) {
        people.remove(p.getId());
        tmpAgeTotal -= p.getAge();
    }

    public int getSize() {
        return people.size();
    }

    // below for my use

    public HashMap<Integer, MyPpl> myGetPeople() {
        return people;
    }
}
