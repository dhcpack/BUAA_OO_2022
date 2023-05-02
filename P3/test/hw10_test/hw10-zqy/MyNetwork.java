import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import com.oocourse.spec2.exceptions.EqualGroupIdException;
import com.oocourse.spec2.exceptions.EqualMessageIdException;
import com.oocourse.spec2.exceptions.EqualPersonIdException;
import com.oocourse.spec2.exceptions.EqualRelationException;
import com.oocourse.spec2.exceptions.GroupIdNotFoundException;
import com.oocourse.spec2.exceptions.MessageIdNotFoundException;
import com.oocourse.spec2.exceptions.PersonIdNotFoundException;
import com.oocourse.spec2.exceptions.RelationNotFoundException;
import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Network;
import com.oocourse.spec2.main.Person;

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

    // I heard that this is Minimal Spanning Tree (MST)
    public int queryLeastConnection(int begin) throws PersonIdNotFoundException {
        if (!contains(begin)) {
            throw new MyPersonIdNotFoundException(begin);
        }
        // we use the Prim's Algorithm from wiki
        // 1. get the tree of the person we are querying, and create the vis array.
        int treeId = MyGlobal.getFamily(begin);
        HashMap<Integer, Boolean> visited = new HashMap<>();
        // vis = [ ... ]
        MyGlobal.getBigFamily().forEach((personId, familyId) -> {
            if (familyId == treeId) {
                visited.put(personId, false);
            }
        });
        // q = priority_queue()
        PriorityQueue<MyPriorityItem<Integer>> queue = new PriorityQueue<>();
        // q.put((0, begin))
        queue.add(new MyPriorityItem<Integer>(0, begin));
        // ret = 0
        int ret = 0;
        // while not q.empty():
        while (!queue.isEmpty()) {
            // prst = q.get()
            MyPriorityItem<Integer> prst = queue.remove();
            // if vis[prst[1]]: continue
            if (visited.replace(prst.getItem(), true)) {
                continue;
            }
            // after this, vis[prst[1]] must be True
            // ret += prst[0]
            ret += prst.getPriority();
            // for i: {weight, toId} in nodes[prst[1]]
            MyGlobal.getRels().forEach((relation, weight) -> {
                Integer toId = relation.getTheOther(prst.getItem());
                if (toId != null) {
                    // if not vis[i.toId]
                    if (!visited.get(toId)) {
                        // q.put((i.w, i.toId))
                        queue.add(new MyPriorityItem<Integer>(weight, toId));
                    }
                }
            });
        }
        return ret;
    }

    public int queryGroupPeopleSum(int id) throws GroupIdNotFoundException {
        Group group = getGroup(id);
        if (group == null) {
            throw new MyGroupIdNotFoundException(id);
        }
        return group.getSize();
    }

    public int queryGroupValueSum(int id) throws GroupIdNotFoundException {
        Group group = getGroup(id);
        if (group == null) {
            throw new MyGroupIdNotFoundException(id);
        }
        return group.getValueSum();
    }

    public int queryGroupAgeVar(int id) throws GroupIdNotFoundException {
        Group group = getGroup(id);
        if (group == null) {
            throw new MyGroupIdNotFoundException(id);
        }
        return group.getAgeVar();
    }

    public boolean containsMessage(int id) {
        return MyGlobal.containsMessage(id);
    }

    public void addMessage(Message message) throws EqualMessageIdException, EqualPersonIdException {
        MyGlobal.addMessage(message);
    }

    public Message getMessage(int id) {
        return MyGlobal.getMessage(id);
    }

    public void sendMessage(int id) throws RelationNotFoundException, MessageIdNotFoundException,
            PersonIdNotFoundException {
        Message msg = getMessage(id);
        if (msg == null) {
            throw new MyMessageIdNotFoundException(id);
        }
        // according to JML, message is removed after being sent
        MyGlobal.delMessage(id);

        Person person1 = msg.getPerson1();
        if (msg.getType() == 0) {
            Person person2 = msg.getPerson2();
            if (!person1.isLinked(person2)) {
                throw new MyRelationNotFoundException(person1.getId(), person2.getId());
            }
            person1.addSocialValue(msg.getSocialValue());
            person2.addSocialValue(msg.getSocialValue());
            MyGlobal.getMessagesOf(person2.getId()).add(0, msg);
        }
        if (msg.getType() == 1) {
            Group group = msg.getGroup();
            if (!group.hasPerson(person1)) {
                throw new MyPersonIdNotFoundException(person1.getId());
            }
            assert group instanceof MyGroup;
            MyGroup myGroup = (MyGroup) group;
            // according to JML, no message is actually sent.
            // only socialValue is increased
            for (Person targetPerson : myGroup.getPeopleMap().values()) {
                targetPerson.addSocialValue(msg.getSocialValue());
            }
        }
    }

    public int querySocialValue(int id) throws PersonIdNotFoundException {
        Person person = getPerson(id);
        if (person == null) {
            throw new MyPersonIdNotFoundException(id);
        }
        return person.getSocialValue();
    }

    public List<Message> queryReceivedMessages(int id) throws PersonIdNotFoundException {
        Person person = getPerson(id);
        if (person == null) {
            throw new MyPersonIdNotFoundException(id);
        }
        return person.getReceivedMessages();
    }
}
