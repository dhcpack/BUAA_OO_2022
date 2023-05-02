import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.oocourse.spec2.exceptions.EqualMessageIdException;
import com.oocourse.spec2.exceptions.EqualPersonIdException;
import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Person;

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
    // id to message
    private static final HashMap<Integer, Message> MSGS = new HashMap<>();
    // person id to their messages
    private static final HashMap<Integer, ArrayList<Message>> PMSG = new HashMap<>();

    public static HashMap<Integer, Person> getPeople() {
        return PEOPLE;
    }

    public static HashMap<Integer, Group> getGroups() {
        return GROUPS;
    }

    public static HashMap<MyConn, Integer> getRels() {
        return REL_VAL;
    }

    public static HashMap<Integer, Integer> getBigFamily() {
        return FAMILY;
    }

    public static void addPerson(Person person) {
        if (PEOPLE.putIfAbsent(person.getId(), person) == null) {
            PMSG.putIfAbsent(person.getId(), new ArrayList<>());
        }
    }

    public static List<Message> getMessagesOf(int id) {
        return PMSG.get(id);
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

    public static boolean containsMessage(int id) {
        return MSGS.containsKey(id);
    }

    public static void addMessage(Message message)
            throws EqualMessageIdException, EqualPersonIdException {
        if (containsMessage(message.getId())) {
            throw new MyEqualMessageIdException(message.getId());
        }
        if (message.getType() == 0 && message.getPerson1().equals(message.getPerson2())) {
            throw new MyEqualPersonIdException(message.getPerson1().getId());
        }
        MSGS.put(message.getId(), message);
    }

    public static void delMessage(int id) {
        MSGS.remove(id);
    }

    public static Message getMessage(int id) {
        return MSGS.getOrDefault(id, null);
    }
}
