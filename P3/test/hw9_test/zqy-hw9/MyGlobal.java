import java.util.HashMap;
import com.oocourse.spec1.main.Group;
import com.oocourse.spec1.main.Person;

// functions:
// 0. manage all instance of group and people
// 1. manage relations between people, and their values
// 2. manage groups of people, can get instances of their Person(s)
// 3. get all relations, which both ends are both in a group
public class MyGlobal {
    // id to person
    private static final HashMap<Integer, Person> PEOPLE = new HashMap<>();
    // id to group
    private static final HashMap<Integer, Group> GROUPS = new HashMap<>();
    // border to value
    private static final HashMap<MyConn, Integer> REL_VAL = new HashMap<>();
    // id to family
    private static final HashMap<Integer, Integer> FAMILY = new HashMap<>();
    private static int MAX_FAMILY = 0;

    public static HashMap<Integer, Person> getPeople() {
        return PEOPLE;
    }

    public static HashMap<Integer, Group> getGroups() {
        return GROUPS;
    }

    public static HashMap<MyConn, Integer> getRels() {
        return REL_VAL;
    }

    public static void addPerson(Person person) {
        PEOPLE.putIfAbsent(person.getId(), person);
    }

    public static void addGroup(Group group) {
        GROUPS.putIfAbsent(group.getId(), group);
    }

    public static boolean haveRelation(int id1, int id2) {
        return REL_VAL.containsKey(new MyConn(id1, id2));
    }

    public static Integer queryValue(int id1, int id2) {
        return REL_VAL.getOrDefault(new MyConn(id1, id2), null);
    }

    public static void addRelation(int id1, int id2, int value) {
        // add relation
        REL_VAL.put(new MyConn(id1, id2), value);
        // update family
        FAMILY.putIfAbsent(id1, MAX_FAMILY);
        FAMILY.putIfAbsent(id2, MAX_FAMILY);
        MAX_FAMILY++;
        int f1 = FAMILY.get(id1);
        int f2 = FAMILY.get(id2);
        if (f1 != f2) {
            FAMILY.replaceAll((id, family) -> family == f1 ? f2 : family);
        }
    }

    public static boolean isCircle(int id1, int id2) {
        int f1 = getFamily(id1);
        int f2 = getFamily(id2);
        return f1 == f2;
    }

    public static Integer getFamily(int id) {
        Integer family = FAMILY.putIfAbsent(id, MAX_FAMILY);
        // previously unknown, now it must be MAX_FAMILY.
        if (family == null) {
            // return and increase
            return MAX_FAMILY++;
        }
        return family;
    }
}
