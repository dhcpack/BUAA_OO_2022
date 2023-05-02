package society.network;

import com.oocourse.spec2.exceptions.EqualGroupIdException;
import com.oocourse.spec2.exceptions.EqualMessageIdException;
import com.oocourse.spec2.exceptions.EqualPersonIdException;
import com.oocourse.spec2.exceptions.EqualRelationException;
import society.exception.EqualSocialMessageIdException;
import com.oocourse.spec2.exceptions.GroupIdNotFoundException;
import com.oocourse.spec2.exceptions.MessageIdNotFoundException;
import com.oocourse.spec2.exceptions.PersonIdNotFoundException;
import com.oocourse.spec2.exceptions.RelationNotFoundException;
import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Network;
import com.oocourse.spec2.main.Person;
import society.exception.EqualSocialGroupIdException;
import society.exception.EqualSocialPersonIdException;
import society.exception.EqualSocialRelationException;
import society.exception.SocialGroupIdNotFoundException;
import society.exception.SocialMessageIdNotFoundException;
import society.exception.SocialPersonIdNotFoundException;
import society.exception.SocialRelationNotFoundException;
import society.graph.Relation;
import society.graph.Root;
import society.group.SocialGroup;
import society.person.SocialPerson;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

public class SocialNetwork implements Network {
    private HashMap<Integer, Person> people;
    private HashMap<Integer, Group> groups;
    private HashMap<Integer, Integer> parent;//祖先节点映射组
    private HashMap<Integer, Root> roots;//根节点组
    private HashMap<Integer, Message> messages;

    public SocialNetwork() {
        this.people = new HashMap<>();
        this.groups = new HashMap<>();
        this.parent = new HashMap<>();
        this.roots = new HashMap<>();
        this.messages = new HashMap<>();
    }

    private void merge(int id1, int id2) {
        int parent1 = find(id1);
        int parent2 = find(id2);
        if (parent1 != parent2) {
            parent.put(parent1, parent2);
            roots.get(parent2).addNum(roots.get(parent1).getNum());
            roots.remove(parent1);
        }
    }

    private int find(int id) {
        if (parent.get(id) == -1) {
            return id;
        }
        int newparent = find(parent.get(id));
        parent.put(id, newparent);
        return newparent;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public boolean contains(int id) {
        return people.containsKey(id);
    }

    @Override
    public Person getPerson(int id) {
        return people.get(id);
    }

    @Override
    public void addPerson(Person person) throws EqualPersonIdException {
        if (contains(person.getId())) {
            throw new EqualSocialPersonIdException(person.getId());
        }
        people.put(person.getId(), person);
        parent.put(person.getId(), -1);
        roots.put(person.getId(), new Root(person.getId()));
    }

    @Override
    public void addRelation(int id1, int id2, int value)
            throws PersonIdNotFoundException, EqualRelationException {
        if (!contains(id1)) {
            throw new SocialPersonIdNotFoundException(id1);
        } else if (!contains(id2)) {
            throw new SocialPersonIdNotFoundException(id2);
        } else {
            Person person1 = people.get(id1);
            Person person2 = people.get(id2);
            if (person1.isLinked(person2)) {
                throw new EqualSocialRelationException(person1.getId(), person2.getId());
            }
            ((SocialPerson) person1).addRelation(person2, value);
            ((SocialPerson) person2).addRelation(person1, value);
            merge(person1.getId(), person2.getId());
        }
    }

    @Override
    public int queryValue(int id1, int id2)
            throws PersonIdNotFoundException, RelationNotFoundException {
        if (!contains(id1)) {
            throw new SocialPersonIdNotFoundException(id1);
        } else if (!contains(id2)) {
            throw new SocialPersonIdNotFoundException(id2);
        } else {
            Person person1 = people.get(id1);
            Person person2 = people.get(id2);
            if (!person1.isLinked(person2)) {
                throw new SocialRelationNotFoundException(person1.getId(), person2.getId());
            }
            return person1.queryValue(person2);
        }
    }

    @Override
    public int queryPeopleSum() {
        return people.size();
    }

    @Override
    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        if (!contains(id1)) {
            throw new SocialPersonIdNotFoundException(id1);
        } else if (!contains(id2)) {
            throw new SocialPersonIdNotFoundException(id2);
        } else {
            return find(id1) == find(id2);
        }
    }

    @Override
    public int queryBlockSum() {
        return roots.size();
    }

    @Override
    public void addGroup(Group group) throws EqualGroupIdException {
        if (groups.containsKey(group.getId())) {
            throw new EqualSocialGroupIdException(group.getId());
        }
        groups.put(group.getId(), group);
    }

    @Override
    public Group getGroup(int id) {
        return groups.get(id);
    }

    @Override
    public void addToGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        if (!groups.containsKey(id2)) {
            throw new SocialGroupIdNotFoundException(id2);
        } else if (!people.containsKey(id1)) {
            throw new SocialPersonIdNotFoundException(id1);
        } else {
            Group group = groups.get(id2);
            Person person = people.get(id1);
            if (group.hasPerson(person)) {
                throw new EqualSocialPersonIdException(person.getId());
            }
            group.addPerson(person);
        }
    }

    @Override
    public void delFromGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        if (!groups.containsKey(id2)) {
            throw new SocialGroupIdNotFoundException(id2);
        } else if (!people.containsKey(id1)) {
            throw new SocialPersonIdNotFoundException(id1);
        } else {
            Group group = groups.get(id2);
            Person person = people.get(id1);
            if (!group.hasPerson(person)) {
                throw new EqualSocialPersonIdException(person.getId());
            }
            group.delPerson(person);
        }
    }

    //prim+优先队列
    @Override
    public int queryLeastConnection(int id) throws PersonIdNotFoundException {
        if (!people.containsKey(id)) {
            throw new SocialPersonIdNotFoundException(id);
        }
        HashSet<Integer> chosenSet = new HashSet<>();
        int nextId = id;
        int edgeSum = roots.get(find(id)).getNum() - 1;
        ArrayList<Relation> chosenEdges = new ArrayList<>();
        HashMap<Integer, Relation> edgeMap = new HashMap<>();
        PriorityQueue<Relation> relations = new PriorityQueue<>(
                Comparator.comparingInt(Relation::getValue)
        );
        for (int edgeNum = 0;edgeNum < edgeSum;edgeNum++) {
            chosenSet.add(nextId); //加入新节点
            for (Relation relation:
                    ((SocialPerson) (people.get(nextId))).getAcquaintance().values()) { //遍历所有与其相连的边
                int personId = relation.getPerson().getId();
                if (!chosenSet.contains(personId)) { //连接的点不能在已选集合内
                    //当前集合无到该节点的边或用更小权值的边替代
                    if (!edgeMap.containsKey(personId)
                            || edgeMap.get(personId).getValue() > relation.getValue()) {
                        edgeMap.put(personId, relation);
                    }
                    relations.add(relation);
                }
            }
            Relation chosenRelation;
            while (true) { //选择那条最小权值的边
                chosenRelation = relations.poll();
                if (!chosenSet.contains(chosenRelation.getPerson().getId())) {
                    break;
                }
            }
            chosenSet.add(chosenRelation.getPerson().getId());
            chosenEdges.add(chosenRelation);
            edgeMap.remove(chosenRelation.getPerson().getId());
            nextId = chosenRelation.getPerson().getId();
        }
        int sumValue = 0;
        for (Relation relation: chosenEdges) {
            sumValue += relation.getValue();
        }
        return sumValue;
    }

    @Override
    public int queryGroupPeopleSum(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new SocialGroupIdNotFoundException(id);
        }
        return groups.get(id).getSize();
    }

    @Override
    public int queryGroupValueSum(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new SocialGroupIdNotFoundException(id);
        }
        return groups.get(id).getValueSum();
    }

    @Override
    public int queryGroupAgeVar(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new SocialGroupIdNotFoundException(id);
        }
        return groups.get(id).getAgeVar();
    }

    @Override
    public boolean containsMessage(int id) {
        return messages.containsKey(id);
    }

    @Override
    public void addMessage(Message message) throws EqualMessageIdException, EqualPersonIdException {
        if (containsMessage(message.getId())) {
            throw new EqualSocialMessageIdException(message.getId());
        }
        if (message.getType() == 0 && message.getPerson1().equals(message.getPerson2())) {
            throw new EqualSocialPersonIdException(message.getPerson1().getId());
        }
        messages.put(message.getId(), message);
    }

    @Override
    public Message getMessage(int id) {
        return messages.get(id);
    }

    @Override
    public void sendMessage(int id) throws RelationNotFoundException,
            MessageIdNotFoundException, PersonIdNotFoundException {
        if (!containsMessage(id)) {
            throw new SocialMessageIdNotFoundException(id);
        }
        Message message = messages.get(id);
        if (message.getType() == 0) {
            Person person1 = message.getPerson1();
            Person person2 = message.getPerson2();
            if (!person1.isLinked(person2)) {
                throw new SocialRelationNotFoundException(person1.getId(), person2.getId());
            }
            communicate(person1, person2, message);
        } else if (message.getType() == 1) {
            Person person = message.getPerson1();
            Group group = message.getGroup();
            if (!group.hasPerson(person)) {
                throw new SocialPersonIdNotFoundException(person.getId());
            }
            broadcast(group, message);
        }
    }

    private void communicate(Person person1, Person person2, Message message) {
        messages.remove(message.getId());
        person1.addSocialValue(message.getSocialValue());
        person2.addSocialValue(message.getSocialValue());
        person2.getMessages().add(0, message);
    }

    private void broadcast(Group group, Message message) {
        messages.remove(message.getId());
        for (Person receiver: ((SocialGroup) group).getPeople().values()) {
            receiver.addSocialValue(message.getSocialValue());
        }
    }

    @Override
    public int querySocialValue(int id) throws PersonIdNotFoundException {
        if (!people.containsKey(id)) {
            throw new SocialPersonIdNotFoundException(id);
        }
        return people.get(id).getSocialValue();
    }

    @Override
    public List<Message> queryReceivedMessages(int id) throws PersonIdNotFoundException {
        if (!people.containsKey(id)) {
            throw new SocialPersonIdNotFoundException(id);
        }
        return people.get(id).getReceivedMessages();
    }
}