import java.math.BigInteger;
import java.util.HashMap;
import com.oocourse.spec1.main.Group;
import com.oocourse.spec1.main.Person;

public class MyGroup implements Group {
    private final int id;
    private final HashMap<Integer, Person> people = new HashMap<>();

    public MyGroup(int id) {
        this.id = id;
        MyGlobal.addGroup(this);
    }

    public int getId() {
        return id;
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Group)) {
            return false;
        }
        return ((Group) obj).getId() == id;
    }

    public void addPerson(Person person) {
        people.putIfAbsent(person.getId(), person);
    }

    public boolean hasPerson(Person person) {
        return people.containsKey(person.getId());
    }

    // BUG: when a Person is not MyPerson
    public int getValueSum() {
        int[] sum = {0};
        MyGlobal.getRels().forEach((conn, value) -> {
            if (people.containsKey(conn.getId1()) && people.containsKey(conn.getId2())) {
                sum[0] += value * 2;
            }
        });
        return sum[0];
    }

    // Q: can we not use BigInteger ?
    public int getAgeMean() {
        if (people.size() == 0) {
            return 0;
        }
        BigInteger sum = BigInteger.ZERO;
        for (Person person : people.values()) {
            sum = sum.add(BigInteger.valueOf(person.getAge()));
        }
        return sum.divide(BigInteger.valueOf(people.size())).intValue();
    }

    public int getAgeVar() {
        if (people.size() == 0) {
            return 0;
        }
        BigInteger avg = BigInteger.valueOf(getAgeMean());
        BigInteger sum = BigInteger.ZERO;
        for (Person person : people.values()) {
            sum = sum.add(BigInteger.valueOf(person.getAge()).subtract(avg).pow(2));
        }
        return sum.divide(BigInteger.valueOf(people.size())).intValue();
    }

    public void delPerson(Person person) {
        people.remove(person.getId());
    }

    public int getSize() {
        return people.size();
    }
}
