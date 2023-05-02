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

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

public class MyNetwork implements Network {
    private final HashSet<MyPerson> people;
    private final HashSet<MyGroup> groups;
    private final HashSet<Message> messages;

    private final HashMap<Integer, HashSet<MyGroup>> groupOfPeople;
    private HashMap<Integer, Message> idToMessage;
    private HashMap<Integer, Person> idToPerson;
    private HashMap<Integer, Group> idToGroup;
    //groupofpeople

    public MyNetwork() {
        people = new HashSet<>();
        groups = new HashSet<>();
        messages = new HashSet<>();

        groupOfPeople = new HashMap<>();
        idToMessage = new HashMap<>();
        idToPerson = new HashMap<>();
        idToGroup = new HashMap<>();
        //edges = new HashMap<>();
    }

    @Override
    public boolean contains(int id) {
        return idToPerson.containsKey(id);
    }

    @Override
    public MyPerson getPerson(int id) {
        return (MyPerson) idToPerson.get(id);
    }

    @Override
    public void addPerson(Person person) throws EqualPersonIdException {
        if (contains(person.getId())) {
            throw new MyEqualPersonIdException(person.getId());
        }
        groupOfPeople.computeIfAbsent(person.getId(), k -> new HashSet<>());
        people.add((MyPerson) person);
        idToPerson.put(person.getId(), person);
    }

    @Override
    public void addRelation(int id1, int id2, int value)
            throws PersonIdNotFoundException, EqualRelationException {
        if (!contains(id1) || !contains(id2) || getPerson(id1).isLinked(getPerson(id2))) {
            if (!contains(id1)) {
                throw new MyPersonIdNotFoundException(id1);
            } else if (!contains(id2)) {
                throw new MyPersonIdNotFoundException(id2);
            } else {
                throw new MyEqualRelationException(id1, id2);
            }
        }
        getPerson(id1).link(getPerson(id2), value);
        getPerson(id2).link(getPerson(id1), value);
        getPerson(find(id1)).setFather(find(id2));

        HashSet<MyGroup> set1 = groupOfPeople.get(id1);
        HashSet<MyGroup> set2 = groupOfPeople.get(id2);
        HashSet<MyGroup> intercept = new HashSet<>(set1);
        intercept.retainAll(set2);
        for (MyGroup group : intercept) {
            group.setValueSum(group.getValueSum() + 2 * value);
        }
    }

    @Override
    public int queryValue(int id1, int id2)
            throws PersonIdNotFoundException, RelationNotFoundException {
        if (contains(id1) && contains(id2) && getPerson(id1).isLinked(getPerson(id2))) {
            return getPerson(id1).queryValue(getPerson(id2));
        }
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else {
            throw new MyRelationNotFoundException(id1, id2);
        }
    }

    @Override
    public int queryPeopleSum() {
        return people.size();
    }

    @Override
    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        int father1 = find(id1);
        int father2 = find(id2);
        return father1 == father2;
    }

    public int find(int id) {
        if (id == getPerson(id).getFather()) {
            return id;
        }
        int temp = find(getPerson(id).getFather());
        getPerson(id).setFather(temp);
        return temp;
    }

    public int findDst(int id) {
        if (id == getPerson(id).getDst()) {
            return id;
        }
        int temp = findDst(getPerson(id).getDst());
        getPerson(id).setDst(temp);
        return temp;
    }

    @Override
    public int queryBlockSum() {
        HashSet<Integer> fathers = new HashSet<>();
        for (Person person : people) {
            fathers.add(find(person.getId()));
        }
        return fathers.size();
    }

    @Override
    public int queryLeastConnection(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        }

        int minCost = 0;

        HashSet<Person> connections = new HashSet<>();

        //find all relevant people
        connections.add(getPerson(id));
        for (Person person : people) {
            if (isCircle(person.getId(), id) && id != person.getId()) {
                connections.add(person);
            }
        }

        PriorityQueue<Edge> q = new PriorityQueue<>(Comparator.comparing(Edge::getValue));
        HashSet<Edge> edges = new HashSet<>();

        for (Person person : connections) {
            MyPerson one = (MyPerson) person;
            HashSet<MyPerson> myGraph = one.getAcquaintance();

            if (myGraph.size() == 0) {
                continue;
            }

            for (MyPerson person1 : myGraph) {
                int value = ((MyPerson) person).getIdToValue().get(person1.getId());
                if (connections.contains(person1)) {
                    Edge edge = new Edge(person.getId(), person1.getId(), value);
                    Edge edge2 = new Edge(person1.getId(), person.getId(), value);
                    if (edges.contains(edge) || edges.contains(edge2)) {
                        continue;
                    }
                    edges.add(edge);
                    edges.add(edge2);
                    q.add(edge);
                }
            }
        }

        if (q.size() == 0) {
            return 0;
        }

        for (Person person : connections) {
            MyPerson one = (MyPerson) person;
            one.setDst(person.getId());
        }

        int count = 0;
        while (count != connections.size() - 1) {
            Edge temp = q.poll();
            assert temp != null;
            int id1 = temp.getId1();
            int id2 = temp.getId2();

            if (findDst(id1) != findDst(id2)) {
                getPerson(findDst(id1)).setDst(findDst(id2));
                count++;
                minCost += temp.getValue();
            }
        }

        for (Person person : connections) {
            MyPerson one = (MyPerson) person;
            one.setDst(person.getId());
        }

        return minCost;
    }

    @Override
    public void addGroup(Group group) throws EqualGroupIdException {
        if (idToGroup.containsValue(group)) {
            throw new MyEqualGroupIdException(group.getId());
        }
        groups.add((MyGroup) group);
        idToGroup.put(group.getId(), group);
    }

    @Override
    public Group getGroup(int id) {
        return idToGroup.get(id);
    }

    @Override
    public void addToGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        if (!idToGroup.containsKey(id2)) {
            throw new MyGroupIdNotFoundException(id2);
        }
        if (!idToPerson.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (getGroup(id2).hasPerson(getPerson(id1))) {
            throw new MyEqualPersonIdException(id1);
        }
        if (getGroup(id2).getSize() < 1111) {
            getGroup(id2).addPerson(getPerson(id1));
            groupOfPeople.get(id1).add((MyGroup) getGroup(id2));
        }
    }

    @Override
    public int queryGroupPeopleSum(int id) throws GroupIdNotFoundException {
        if (idToGroup.containsKey(id)) {
            return getGroup(id).getSize();
        }
        throw new MyGroupIdNotFoundException(id);
    }

    @Override
    public int queryGroupValueSum(int id) throws GroupIdNotFoundException {
        if (idToGroup.containsKey(id)) {
            return getGroup(id).getValueSum();
        }
        throw new MyGroupIdNotFoundException(id);
    }

    @Override
    public int queryGroupAgeVar(int id) throws GroupIdNotFoundException {
        if (idToGroup.containsKey(id)) {
            return getGroup(id).getAgeVar();
        }
        throw new MyGroupIdNotFoundException(id);
    }

    @Override
    public void delFromGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        if (!idToGroup.containsKey(id2)) {
            throw new MyGroupIdNotFoundException(id2);
        }
        if (!idToPerson.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (!getGroup(id2).hasPerson(getPerson(id1))) {
            throw new MyEqualPersonIdException(id1);
        }
        getGroup(id2).delPerson(getPerson(id1));
        groupOfPeople.get(id1).remove((MyGroup) getGroup(id2));
        // TODO: 2022/5/14 update group&person
    }

    @Override
    public boolean containsMessage(int id) {
        return idToMessage.containsKey(id);
    }

    @Override
    public void addMessage(Message message) throws EqualMessageIdException, EqualPersonIdException {
        if (containsMessage(message.getId())) {
            throw new MyEqualMessageIdException(message.getId());
        }
        if (message.getType() == 0 && message.getPerson1() == message.getPerson2()) {
            throw new MyEqualPersonIdException(message.getPerson1().getId());
        }
        messages.add(message);
        idToMessage.put(message.getId(), message);
    }

    @Override
    public Message getMessage(int id) {
        if (containsMessage(id)) {
            return idToMessage.get(id);
        }
        return null;
    }

    @Override
    public void sendMessage(int id) throws RelationNotFoundException, MessageIdNotFoundException,
            PersonIdNotFoundException {
        if (!containsMessage(id)) {
            throw new MyMessageIdNotFoundException(id);
        } else if (getMessage(id).getType() == 0 &&
                !(getMessage(id).getPerson1().isLinked(getMessage(id).getPerson2()))) {
            throw new MyRelationNotFoundException(getMessage(id).getPerson1().getId(),
                    getMessage(id).getPerson2().getId());
        } else if (getMessage(id).getType() == 1 &&
                !(getMessage(id).getGroup().hasPerson(getMessage(id).getPerson1()))) {
            throw new MyPersonIdNotFoundException(getMessage(id).getPerson1().getId());
        }

        if (getMessage(id).getType() == 1) {
            MyGroup group = (MyGroup) getMessage(id).getGroup();
            for (MyPerson p : group.getPeople()) {
                p.setSocialValue(p.getSocialValue() + getMessage(id).getSocialValue());
            }
        }

        if (getMessage(id).getType() == 0 &&
                getMessage(id).getPerson1() != getMessage(id).getPerson2()) {
            MyPerson myPerson1 = (MyPerson) getMessage(id).getPerson1();
            MyPerson myPerson2 = (MyPerson) getMessage(id).getPerson2();

            int messageValue = getMessage(id).getSocialValue();
            int person1Value = getMessage(id).getPerson1().getSocialValue();
            int person2Value = getMessage(id).getPerson2().getSocialValue();

            myPerson1.setSocialValue(messageValue + person1Value);
            myPerson2.setSocialValue(messageValue + person2Value);
            myPerson2.addMessage(getMessage(id));
        }

        idToMessage.remove(id);
        messages.remove(getMessage(id));
    }

    @Override
    public int querySocialValue(int id) throws PersonIdNotFoundException {
        if (contains(id)) {
            return getPerson(id).getSocialValue();
        }
        throw new MyPersonIdNotFoundException(id);
    }

    @Override
    public List<Message> queryReceivedMessages(int id) throws PersonIdNotFoundException {
        if (contains(id)) {
            return getPerson(id).getReceivedMessages();
        }
        throw new MyPersonIdNotFoundException(id);
    }
}
