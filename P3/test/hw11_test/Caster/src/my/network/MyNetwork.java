package my.network;

import com.oocourse.spec3.exceptions.EmojiIdNotFoundException;
import com.oocourse.spec3.exceptions.EqualEmojiIdException;
import com.oocourse.spec3.exceptions.EqualGroupIdException;
import com.oocourse.spec3.exceptions.EqualMessageIdException;
import com.oocourse.spec3.exceptions.EqualPersonIdException;
import com.oocourse.spec3.exceptions.EqualRelationException;
import com.oocourse.spec3.exceptions.GroupIdNotFoundException;
import com.oocourse.spec3.exceptions.MessageIdNotFoundException;
import com.oocourse.spec3.exceptions.PersonIdNotFoundException;
import com.oocourse.spec3.exceptions.RelationNotFoundException;
import com.oocourse.spec3.main.EmojiMessage;
import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Network;
import com.oocourse.spec3.main.NoticeMessage;
import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.RedEnvelopeMessage;
import my.exceptions.MyEmojiIdNotFoundException;
import my.exceptions.MyEqualEmojiIdException;
import my.exceptions.MyEqualGroupIdException;
import my.exceptions.MyEqualMessageIdException;
import my.exceptions.MyEqualPersonIdException;
import my.exceptions.MyEqualRelationException;
import my.exceptions.MyGroupIdNotFoundException;
import my.exceptions.MyMessageIdNotFoundException;
import my.exceptions.MyPersonIdNotFoundException;
import my.exceptions.MyRelationNotFoundException;
import my.network.message.MyMessage;
import my.network.util.FindUnionSet;
import my.network.util.MyEdge;
import my.network.util.MyEdges;
import my.network.util.Pair;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public class MyNetwork implements Network {
    private final ArrayList<Person> people = new ArrayList<>();
    private final ArrayList<Group> groups = new ArrayList<>();
    private final ArrayList<Message> messages = new ArrayList<>();
    private final ArrayList<Integer> emojiIdList = new ArrayList<>();
    private final ArrayList<Integer> emojiHeatList = new ArrayList<>();
    private final FindUnionSet block = new FindUnionSet();
    private final FindUnionSet tree = new FindUnionSet();
    private final MyEdges treeEdges = new MyEdges();
    private final HashMap<Person, Integer> mapDist = new HashMap<>();
    private final PriorityQueue<Pair<Integer, Person>> mapQueue =
            new PriorityQueue<>(Comparator.comparing(Pair::get1));

    public MyNetwork() {
    }

    @Override
    public boolean contains(int id) {
        return people.stream().anyMatch(person -> person.getId() == id);
    }

    @Override
    public Person getPerson(int id) {
        if (contains(id)) {
            return people.stream().filter(person ->
                    person.getId() == id).findFirst().orElse(null);
        }
        return null;
    }

    @Override
    public void addPerson(Person person) throws EqualPersonIdException {
        if (people.contains(person)) {
            throw new MyEqualPersonIdException(person.getId());
        }
        people.add(person);
        block.add(person);
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
        MyPerson p1 = (MyPerson) getPerson(id1);
        MyPerson p2 = (MyPerson) getPerson(id2);
        block.merge(p1, p2);
        p1.addAcquaint(p2, value);
        p2.addAcquaint(p1, value);
        groups.forEach(g -> ((MyGroup) g).updateValueSum(p1, p2, value));
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
        return people.size();
    }

    @Override
    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        Person p1 = block.find(getPerson(id1));
        Person p2 = block.find(getPerson(id2));
        return p1.equals(p2);
    }

    @Override
    public int queryBlockSum() {
        return (int) people.stream().filter(block::isFather).count();
    }

    @Override
    public int queryLeastConnection(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        return kruskal(getPerson(id));
    }

    public int kruskal(Person root) {
        treeEdges.clear();
        tree.clear();
        people.stream().filter(person -> block.isRelated(person, root)).forEach(tree::add);
        tree.getPeople().forEach(person -> {
            for (Person acquaint : person.getAcquaint()) {
                treeEdges.add(new MyEdge(person, acquaint, person.queryValue(acquaint)));
            }
        });
        treeEdges.sort();
        int ans = 0;
        ArrayList<MyEdge> edges = treeEdges.getEdges();
        for (MyEdge edge : edges) {
            Person p1 = tree.find(edge.getP1());
            Person p2 = tree.find(edge.getP2());
            if (!p1.equals(p2)) {
                tree.merge(p1, p2);
                ans += edge.getValue();
            }
        }
        return ans;
    }

    @Override
    public void addGroup(Group group) throws EqualGroupIdException {
        if (groups.contains(group)) {
            throw new MyEqualGroupIdException(group.getId());
        }
        groups.add(group);
    }

    @Override
    public Group getGroup(int id) {
        return groups.stream().filter(group -> group.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void addToGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        if (getGroup(id2) == null) {
            throw new MyGroupIdNotFoundException(id2);
        }
        if (!contains(id1)) {
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
    public int queryGroupPeopleSum(int id) throws GroupIdNotFoundException {
        if (getGroup(id) == null) {
            throw new MyGroupIdNotFoundException(id);
        }
        return getGroup(id).getSize();
    }

    @Override
    public int queryGroupValueSum(int id) throws GroupIdNotFoundException {
        if (getGroup(id) == null) {
            throw new MyGroupIdNotFoundException(id);
        }
        return getGroup(id).getValueSum();
    }

    @Override
    public int queryGroupAgeVar(int id) throws GroupIdNotFoundException {
        if (getGroup(id) == null) {
            throw new MyGroupIdNotFoundException(id);
        }
        return getGroup(id).getAgeVar();
    }

    @Override
    public void delFromGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        if (getGroup(id2) == null) {
            throw new MyGroupIdNotFoundException(id2);
        }
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (!getGroup(id2).hasPerson(getPerson(id1))) {
            throw new MyEqualPersonIdException(id1);
        }
        getGroup(id2).delPerson(getPerson(id1));
    }

    @Override
    public boolean containsMessage(int id) {
        return messages.stream().anyMatch(message -> message.getId() == id);
    }

    @Override
    public void addMessage(Message message)
            throws EqualMessageIdException, EmojiIdNotFoundException, EqualPersonIdException {
        if (messages.contains(message)) {
            throw new MyEqualMessageIdException(message.getId());
        }
        if (message instanceof EmojiMessage) {
            if (!containsEmojiId(((EmojiMessage) message).getEmojiId())) {
                throw new MyEmojiIdNotFoundException(((EmojiMessage) message).getEmojiId());
            }
        }
        if (message.getType() == 0 && message.getPerson1() == message.getPerson2()) {
            throw new MyEqualPersonIdException(message.getPerson1().getId());
        }
        messages.add(message);
    }

    @Override
    public Message getMessage(int id) {
        return messages.stream().filter(message -> message.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void sendMessage(int id) throws RelationNotFoundException,
            MessageIdNotFoundException, PersonIdNotFoundException {
        if (!containsMessage(id)) {
            throw new MyMessageIdNotFoundException(id);
        }
        Message message = getMessage(id);
        Person p1 = message.getPerson1();
        if (message.getType() == 0) {
            Person p2 = message.getPerson2();
            if (!p1.isLinked(p2)) {
                throw new MyRelationNotFoundException(p1.getId(), p2.getId());
            }
            if (!p1.equals(p2)) {
                sendMessage(message, p1, p2);
            }
        } else if (message.getType() == 1) {
            MyGroup g = (MyGroup) message.getGroup();
            if (!g.hasPerson(p1)) {
                throw new MyPersonIdNotFoundException(p1.getId());
            }
            sendMessage(message, p1, g);
        }
    }

    private void sendMessage(Message message, Person p1, MyGroup g) {
        messages.remove(message);
        updateData(message, p1, g);
    }

    private void sendMessage(Message message, Person p1, Person p2) {
        messages.remove(message);
        p2.getMessages().add(0, message);
        updateData(message, p1, p2);
    }

    private void updateData(Message message, Person p1, MyGroup g) {
        updateSocialValue(message, g);
        updateMoney(message, p1, g);
        updateEmoji(message);
    }

    private void updateData(Message message, Person p1, Person p2) {
        updateSocialValue(message, p1, p2);
        updateMoney(message, p1, p2);
        updateEmoji(message);
    }

    private void updateSocialValue(Message message, Person p1, Person p2) {
        p1.addSocialValue(message.getSocialValue());
        p2.addSocialValue(message.getSocialValue());
    }

    private void updateSocialValue(Message message, MyGroup g) {
        g.addSocialValue(message.getSocialValue());
    }

    private void updateMoney(Message message, Person p1, Person p2) {
        if (message instanceof RedEnvelopeMessage) {
            p1.addMoney(-((RedEnvelopeMessage) message).getMoney());
            p2.addMoney(((RedEnvelopeMessage) message).getMoney());
        }
    }

    private void updateMoney(Message message, Person p1, MyGroup g) {
        if (message instanceof RedEnvelopeMessage) {
            p1.addMoney(-((RedEnvelopeMessage) message).getMoney()
                    / g.getSize() * (g.getSize() - 1));
            p1.addMoney(-((RedEnvelopeMessage) message).getMoney()
                    / g.getSize());
            g.addMoney(((RedEnvelopeMessage) message).getMoney());
        }
    }

    private void updateEmoji(Message message) {
        if (message instanceof EmojiMessage) {
            int emojiId = ((EmojiMessage) message).getEmojiId();
            if (!containsEmojiId(emojiId)) {
                try {
                    storeEmojiId(emojiId);
                } catch (EqualEmojiIdException e) {
                    e.printStackTrace();
                }
            }
            int i = emojiIdList.indexOf(emojiId);
            int val = emojiHeatList.get(i);
            emojiHeatList.set(i, val + 1);
        }
    }

    @Override
    public int querySocialValue(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        return getPerson(id).getSocialValue();
    }

    @Override
    public List<Message> queryReceivedMessages(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        return getPerson(id).getReceivedMessages();
    }

    @Override
    public boolean containsEmojiId(int id) {
        return emojiIdList.contains(id);
    }

    @Override
    public void storeEmojiId(int id) throws EqualEmojiIdException {
        if (containsEmojiId(id)) {
            throw new MyEqualEmojiIdException(id);
        }
        emojiIdList.add(id);
        emojiHeatList.add(0);
    }

    @Override
    public int queryMoney(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        return getPerson(id).getMoney();
    }

    @Override
    public int queryPopularity(int id) throws EmojiIdNotFoundException {
        if (!containsEmojiId(id)) {
            throw new MyEmojiIdNotFoundException(id);
        }
        return emojiHeatList.get(emojiIdList.indexOf(id));
    }

    @Override
    public int deleteColdEmoji(int limit) {
        for (int i = emojiIdList.size() - 1; i >= 0; i--) {
            if (emojiHeatList.get(i) < limit) {
                emojiHeatList.remove(i);
                emojiIdList.remove(i);
            }
        }
        for (int i = messages.size() - 1; i >= 0; i--) {
            if (messages.get(i) instanceof EmojiMessage) {
                if (!containsEmojiId(((EmojiMessage) messages.get(i)).getEmojiId())) {
                    messages.remove(i);
                }
            }
        }
        return emojiIdList.size();
    }

    @Override
    public void clearNotices(int personId) throws PersonIdNotFoundException {
        if (!contains(personId)) {
            throw new MyPersonIdNotFoundException(personId);
        }
        MyPerson person = (MyPerson) getPerson(personId);
        for (Message message : messages) {
            if (person.learns(message) && message instanceof NoticeMessage) {
                person.getMessages().remove(message);
            }
        }
    }

    @Override
    public int sendIndirectMessage(int id) throws MessageIdNotFoundException {
        if (containsMessage(id)) {
            MyMessage message = (MyMessage) getMessage(id);
            if (message.getType() == 0) {
                MyPerson p1 = (MyPerson) message.getPerson1();
                MyPerson p2 = (MyPerson) message.getPerson2();
                try {
                    if (!isCircle(p1.getId(), p2.getId())) {
                        return -1;
                    }
                } catch (PersonIdNotFoundException e) {
                    e.printStackTrace();
                }
                sendMessage(message, p1, p2);
                return spfa(p1, p2);
            }
        }
        throw new MyMessageIdNotFoundException(id);
    }

    public int spfa(MyPerson root, MyPerson leaf) {
        mapDist.clear();
        mapQueue.clear();
        people.forEach(value -> mapDist.put(value, 0x3f3f3f3f));
        mapDist.put(root, 0);
        mapQueue.add(new Pair<>(mapDist.get(root), root));
        while (!mapQueue.isEmpty()) {
            Pair<Integer, Person> pair1 = mapQueue.poll();
            MyPerson person = (MyPerson) pair1.get2();
            for (int j = 0; j < person.size(); j++) {
                MyPerson acquaint = (MyPerson) person.get(j);
                int val = person.queryValue(acquaint);
                if (mapDist.get(acquaint) > pair1.get1() + val) {
                    mapDist.put(acquaint, pair1.get1() + val);
                    mapQueue.add(new Pair<>(mapDist.get(acquaint), acquaint));
                }
            }
        }
        return mapDist.get(leaf);
    }
}
