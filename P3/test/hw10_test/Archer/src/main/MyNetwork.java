package main;

import com.oocourse.spec2.exceptions.*;
import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Network;
import com.oocourse.spec2.main.Person;
import com.oocourse.spec2.exceptions.PersonIdNotFoundException;
import exceptions.*;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

public class MyNetwork implements Network {

    private final DisjointSet<Integer> relations = new DisjointSet<>();
    private final HashMap<Integer, Person> persons = new HashMap<>();
    private final HashMap<Integer, Group> groups = new HashMap<>();
    private final HashMap<Integer, Message> messages = new HashMap<>();

    @Override
    public boolean contains(int id) {
        return persons.containsKey(id);
    }

    @Override
    public Person getPerson(int id) {
        if (persons.containsKey(id)) {
            return persons.get(id);
        }
        return null;
    }

    @Override
    public void addPerson(Person person) throws EqualPersonIdException {
        if (contains(person.getId())) {
            throw new MyEqualPersonIdException(person.getId());
        } else {
            relations.addElem(person.getId());
            persons.put(person.getId(), person);
        }
    }

    @Override
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
        relations.addRelation(id1, id2);
        ((MyPerson) getPerson(id1)).addAcquaintance(getPerson(id2), value); //添加到这个人的熟人列表里面
        ((MyPerson) getPerson(id2)).addAcquaintance(getPerson(id1), value); //添加到这个人的熟人列表里面
    }

    @Override
    public int queryValue(int id1, int id2)
            throws PersonIdNotFoundException, RelationNotFoundException {

        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        if (!getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyRelationNotFoundException(id1, id2);
        }
        return getPerson(id1).queryValue(getPerson(id2));
    }

    @Override
    public int queryPeopleSum() {
        return persons.size();
    }

    @Override
    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }

        return relations.isConnected(id1, id2);
    }

    @Override
    public int queryBlockSum() {
        // 求并查集中连通分量的数目
        return relations.getNumOfBlocks();
    }

    @Override
    public void addGroup(Group group) throws EqualGroupIdException {
        if (groups.containsKey(group.getId())) {
            throw new MyEqualGroupIdException(group.getId());
        }
        groups.put(group.getId(), group);
    }

    @Override
    public Group getGroup(int id) {
        if (groups.containsKey(id)) {
            return groups.get(id);
        }
        return null;
    }

    @Override
    public void addToGroup(int id1, int id2)
            throws GroupIdNotFoundException,
            PersonIdNotFoundException,
            EqualPersonIdException {
        // groups id2; people id1
        if (!groups.containsKey(id2)) {
            throw new MyGroupIdNotFoundException(id2);
        }
        if (!persons.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (getGroup(id2).hasPerson(getPerson(id1))) {
            throw new MyEqualPersonIdException(id1);
        }
        if (getGroup(id2).getSize() < 1111) {
            getGroup(id2).addPerson(getPerson(id1));
        }
    }

    @Override
    public void delFromGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        if (!groups.containsKey(id2)) {
            throw new MyGroupIdNotFoundException(id2);
        }
        if (!persons.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (!getGroup(id2).hasPerson(getPerson(id1))) {
            throw new MyEqualPersonIdException(id1);
        }
        getGroup(id2).delPerson(getPerson(id1));
    }

    @Override
    public int queryLeastConnection(int id) throws PersonIdNotFoundException {
        if (!persons.containsKey(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        // 已经可以用并查集得到联通子图了，那就用克鲁斯卡算法比较快
        ArrayList<Person> connect = getConnectedPeople(id);
        ArrayList<Relation> edges = new ArrayList<>();
        // TODO 优化
        for (int i = 0; i < connect.size(); i++) {
            for (int j = i + 1; j < connect.size(); j++) {
                if (connect.get(i).isLinked(connect.get(j))) {
                    edges.add(new Relation(connect.get(i), connect.get(j)));
                }
            }
        }
        edges.sort(Comparator.naturalOrder());
        DisjointSet<Person> disjointSet = new DisjointSet<>();
        int sum = 0;
        for (Person p : connect) {
            disjointSet.addElem(p);
        }
        for (Relation e : edges) {
            if (!disjointSet.isConnected(e.getPerson1(), e.getPerson2())) {
                disjointSet.addRelation(e.getPerson1(), e.getPerson2());
                sum += e.getValue();
            }
            // TODO 退出条件 优化 加完所有人就可以退出循环了
        }
        return sum;
    }

    public ArrayList<Person> getConnectedPeople(Integer personid) {
        ArrayList<Person> arr = new ArrayList<>();
        for (int id : persons.keySet()) {
            if (relations.isConnected(personid, id)) {
                arr.add(persons.get(id));
            }
        }
        return arr;
    }

    @Override
    public int queryGroupPeopleSum(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new MyGroupIdNotFoundException(id);
        }
        return groups.get(id).getSize();
    }

    @Override
    public int queryGroupValueSum(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new MyGroupIdNotFoundException(id);
        }
        return groups.get(id).getValueSum();
    }

    @Override
    public int queryGroupAgeVar(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new MyGroupIdNotFoundException(id);
        }
        return groups.get(id).getAgeVar();
    }

    @Override
    public boolean containsMessage(int id) {
        return messages.containsKey(id);
    }

    @Override
    public void addMessage(Message message) throws EqualMessageIdException, EqualPersonIdException {
        if (messages.containsKey(message.getId())) {
            throw new MyEqualMessageIdException(message.getId());
        }
        if (message.getType() == 0 && message.getPerson1().equals(message.getPerson2())) {
            throw new MyEqualPersonIdException(message.getPerson2().getId());
        }
        messages.put(message.getId(), message);
    }

    @Override
    public Message getMessage(int id) {
        return messages.getOrDefault(id, null);
    }

    @Override
    public void sendMessage(int id)
            throws RelationNotFoundException,
            MessageIdNotFoundException,
            PersonIdNotFoundException {
        if (!containsMessage(id)) {
            throw new MyMessageIdNotFoundException(id);
        }
        Message message = getMessage(id);
        if (message.getType() == 0) {
            if (!(message.getPerson1().isLinked(message.getPerson2()))) {
                throw new MyRelationNotFoundException(
                        message.getPerson1().getId(), message.getPerson2().getId()
                );
            }
            message.getPerson1().addSocialValue(message.getSocialValue());
            message.getPerson2().addSocialValue(message.getSocialValue());
            ((MyPerson) message.getPerson2()).addMessage(message);

        } else {
            if (!(message.getGroup().hasPerson(message.getPerson1()))) {
                throw new MyPersonIdNotFoundException(message.getPerson1().getId());
            }
            ((MyGroup) message.getGroup()).addSocialValue(message.getSocialValue());
        }

        // remove this message from messages
        messages.remove(id);
    }

    @Override
    public int querySocialValue(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        return persons.get(id).getSocialValue();
    }

    @Override
    public List<Message> queryReceivedMessages(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        return persons.get(id).getReceivedMessages();
    }
}