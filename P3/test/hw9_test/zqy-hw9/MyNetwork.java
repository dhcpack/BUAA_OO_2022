import java.util.HashMap;
import java.util.HashSet;
import com.oocourse.spec1.exceptions.EqualGroupIdException;
import com.oocourse.spec1.exceptions.EqualPersonIdException;
import com.oocourse.spec1.exceptions.EqualRelationException;
import com.oocourse.spec1.exceptions.GroupIdNotFoundException;
import com.oocourse.spec1.exceptions.PersonIdNotFoundException;
import com.oocourse.spec1.exceptions.RelationNotFoundException;
import com.oocourse.spec1.main.Group;
import com.oocourse.spec1.main.Network;
import com.oocourse.spec1.main.Person;

public class MyNetwork implements Network {
    private final HashMap<Integer, Person> people = new HashMap<>();
    private final HashMap<Integer, Group> groups = new HashMap<>();

    public MyNetwork() {}

    public boolean contains(int id) {
        return people.containsKey(id);
    }

    public Person getPerson(int id) {
        return people.getOrDefault(id, null);
    }

    public void addPerson(Person person) throws EqualPersonIdException {
        if (contains(person.getId())) {
            throw new MyEqualPersonIdException(person.getId());
        }
        people.put(person.getId(), person);
    }

    public void addRelation(int id1, int id2, int value)
            throws PersonIdNotFoundException, EqualRelationException {
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        if (getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyEqualRelationException(id1, id2);
        }
        MyGlobal.addRelation(id1, id2, value);
    }

    public int queryValue(int id1, int id2)
            throws PersonIdNotFoundException, RelationNotFoundException {
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        Person p1 = getPerson(id1);
        Person p2 = getPerson(id2);
        if (!getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyRelationNotFoundException(id1, id2);
        }
        return p1.queryValue(p2);
    }

    public int queryPeopleSum() {
        return people.size();
    }

    // BUG?: if one person is not MyPerson
    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        return MyGlobal.isCircle(id1, id2);
    }

    // according to someone, this means unique family count
    public int queryBlockSum() {
        int unknowns = 0;
        HashSet<Integer> families = new HashSet<>();
        for (int id : people.keySet()) {
            Integer family = MyGlobal.getFamily(id);
            if (family == null) {
                unknowns++;
            } else {
                families.add(family);
            }
        }
        return unknowns + families.size();
    }

    public void addGroup(Group group) throws EqualGroupIdException {
        if (groups.containsKey(group.getId())) {
            throw new MyEqualGroupIdException(group.getId());
        }
        groups.put(group.getId(), group);
    }

    public Group getGroup(int id) {
        return groups.getOrDefault(id, null);
    }

    public void addToGroup(int personId, int groupId)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        Group group = getGroup(groupId);
        if (group == null) {
            throw new MyGroupIdNotFoundException(groupId);
        }
        Person person = getPerson(personId);
        if (person == null) {
            throw new MyPersonIdNotFoundException(personId);
        }
        if (group.hasPerson(person)) {
            throw new MyEqualPersonIdException(personId);
        }
        // huh, strange
        if (group.getSize() < 1111) {
            group.addPerson(person);
        }
    }

    public void delFromGroup(int personId, int groupId)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        Group group = getGroup(groupId);
        if (group == null) {
            throw new MyGroupIdNotFoundException(groupId);
        }
        Person person = getPerson(personId);
        if (person == null) {
            throw new MyPersonIdNotFoundException(personId);
        }
        if (!group.hasPerson(person)) {
            throw new MyEqualPersonIdException(personId);
        }
        group.delPerson(person);
    }
}
