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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class MyNetwork implements Network {
    private final HashMap<Integer, MyPerson> people = new HashMap<>();
    private final HashMap<Integer, MyGroup> groups = new HashMap<>();
    private final HashMap<Integer, MyMessage> messages = new HashMap<>();
    private final ArrayList<Edge> edges = new ArrayList<>();

    private final Union networkUnion = new Union();

    public MyNetwork() {
    }

    public boolean contains(int id) {
        return people.containsKey(id);
    }

    public Person getPerson(int id) {
        return people.get(id);
    }

    public void addPerson(Person person) throws EqualPersonIdException {
        if (contains(person.getId())) {
            throw new MyEqualPersonIdException(person.getId());
        }
        people.put(person.getId(), (MyPerson) person);
        networkUnion.addNewUnion(person.getId());
    }

    public void addRelation(int id1, int id2, int value)
            throws PersonIdNotFoundException, EqualRelationException {
        if (!people.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!people.containsKey(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        MyPerson person1 = people.get(id1);
        MyPerson person2 = people.get(id2);
        if (person1.isLinked(person2)) {
            throw new MyEqualRelationException(id1, id2);
        }
        person1.addAcquaintance(person2, value);
        person2.addAcquaintance(person1, value);
        networkUnion.addNewRelation(id1, id2);
        edges.add(new Edge(person1.getId(), person2.getId(), value));
    }

    public int queryValue(int id1, int id2)
            throws PersonIdNotFoundException, RelationNotFoundException {
        if (!people.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!people.containsKey(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        MyPerson person1 = people.get(id1);
        MyPerson person2 = people.get(id2);
        if (!person1.isLinked(person2)) {
            throw new MyRelationNotFoundException(id1, id2);
        }
        return person1.queryValue(person2);
    }

    public int queryPeopleSum() {
        return people.size();
    }

    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        if (!people.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!people.containsKey(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        return networkUnion.findRoot(id1) == networkUnion.findRoot(id2);
    }

    public int queryBlockSum() {
        return networkUnion.getBlockSum();
    }

    public int queryLeastConnection(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        }

        HashSet<Integer> subgroup = new HashSet<>();
        subgroup.add(id);
        for (MyPerson person : people.values()) {
            try {
                if (isCircle(id, person.getId())) {
                    subgroup.add(person.getId());
                }
            } catch (PersonIdNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        if (subgroup.size() == 1) {
            return 0;
        }

        ArrayList<Integer> edgeSelected = new ArrayList<>();
        Union union = new Union(subgroup);
        Collections.sort(edges);

        for (Edge edge : edges) {
            if (!(subgroup.contains(edge.getPoint1()) && subgroup.contains(edge.getPoint2()))) {
                continue;
            }
            if (union.findRoot(edge.getPoint1()) != union.findRoot(edge.getPoint2())) {
                edgeSelected.add(edge.getWeight());
                union.addNewRelation(edge.getPoint1(), edge.getPoint2());
                if (edgeSelected.size() == subgroup.size() - 1) {
                    break;
                }
            }
        }
        return edgeSelected.stream().mapToInt(t -> t).sum();
    }

    public void addGroup(Group group) throws EqualGroupIdException {
        if (groups.containsKey(group.getId())) {
            throw new MyEqualGroupIdException(group.getId());
        }
        groups.put(group.getId(), (MyGroup) group);
    }

    public Group getGroup(int id) {
        return groups.get(id);
    }

    public void addToGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        if (!groups.containsKey(id2)) {
            throw new MyGroupIdNotFoundException(id2);
        } else if (!people.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        MyGroup group = groups.get(id2);
        MyPerson person = people.get(id1);
        if (group.hasPerson(person)) {
            throw new MyEqualPersonIdException(id1);
        }
        if (group.getSize() < 1111) {
            group.addPerson(person);
        }
    }

    public int queryGroupPeopleSum(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new MyGroupIdNotFoundException(id);
        }
        return groups.get(id).getSize();
    }

    public int queryGroupValueSum(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new MyGroupIdNotFoundException(id);
        }
        return groups.get(id).getValueSum();
    }

    public int queryGroupAgeVar(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new MyGroupIdNotFoundException(id);
        }
        return groups.get(id).getAgeVar();
    }

    public void delFromGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        if (!groups.containsKey(id2)) {
            throw new MyGroupIdNotFoundException(id2);
        } else if (!people.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        MyGroup group = groups.get(id2);
        MyPerson person = people.get(id1);
        if (!group.hasPerson(person)) {
            throw new MyEqualPersonIdException(id1);
        }
        group.delPerson(person);
    }

    public boolean containsMessage(int id) {
        return messages.containsKey(id);
    }

    public void addMessage(Message message) throws EqualMessageIdException, EqualPersonIdException {
        if (messages.containsKey(message.getId())) {
            throw new MyEqualMessageIdException(message.getId());
        } else if (message.getType() == 0 && message.getPerson1().equals(message.getPerson2())) {
            throw new MyEqualPersonIdException(message.getPerson1().getId());
        }
        messages.put(message.getId(), (MyMessage) message);
    }

    public Message getMessage(int id) {
        return messages.get(id);
    }

    public void sendMessage(int id)
            throws RelationNotFoundException, MessageIdNotFoundException,
            PersonIdNotFoundException {
        if (!messages.containsKey(id)) {
            throw new MyMessageIdNotFoundException(id);
        }
        MyMessage myMessage = messages.get(id);
        if (myMessage.getType() == 0) {
            MyPerson person1 = (MyPerson) myMessage.getPerson1();
            MyPerson person2 = (MyPerson) myMessage.getPerson2();
            if (!person1.isLinked(person2)) {
                throw new MyRelationNotFoundException(person1.getId(), person2.getId());
            }
            person1.addSocialValue(myMessage.getSocialValue());
            person2.addSocialValue(myMessage.getSocialValue());
            person2.addMessage(myMessage);
        } else {
            MyGroup myGroup = (MyGroup) myMessage.getGroup();
            MyPerson person1 = (MyPerson) myMessage.getPerson1();
            if (!myGroup.hasPerson(person1)) {
                throw new MyPersonIdNotFoundException(person1.getId());
            }
            myGroup.addSocialValue(myMessage.getSocialValue());
        }
        messages.remove(id);
    }

    public int querySocialValue(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        return getPerson(id).getSocialValue();
    }

    public List<Message> queryReceivedMessages(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        return getPerson(id).getReceivedMessages();
    }
}
