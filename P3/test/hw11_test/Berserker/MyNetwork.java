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
import com.oocourse.spec3.main.Person;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

public class MyNetwork implements Network {
    private final HashMap<Integer, MyPerson> people;
    private final HashMap<Integer, MyGroup> groups;
    private final HashMap<Integer, MyMessage> messages;
    private final HashMap<Integer, Boolean> emojis;
    private final HashMap<Integer, Integer> emojiHeat;

    private final HashMap<Integer, Integer> minConnection = new HashMap<>();
    private final HashMap<Integer, Boolean> updated = new HashMap<>();
    private final MyUnion union;

    // edges
    private final ArrayList<Integer> edgeX = new ArrayList<>();
    private final ArrayList<Integer> edgeY = new ArrayList<>();

    private int blockSum;
    private int edgeCnt;

    public MyNetwork() {
        blockSum = 0;
        edgeCnt = 0;
        people = new HashMap<>();
        groups = new HashMap<>();
        messages = new HashMap<>();
        emojis = new HashMap<>();
        emojiHeat = new HashMap<>();
        union = new MyUnion();
    }

    @Override
    public boolean contains(int id) {
        return people.get(id) != null;
    }

    @Override
    public Person getPerson(int id) {
        return people.get(id);
    }

    @Override
    public void addPerson(Person person) throws EqualPersonIdException {
        if (people.get(person.getId()) != null) {
            throw new MyEqualPersonIdException(person.getId());
        }
        people.put(person.getId(), (MyPerson) person);
        union.add(person.getId());
        ++blockSum;
    }

    @Override
    public void addRelation(int id1, int id2, int value)
            throws PersonIdNotFoundException, EqualRelationException {
        MyPerson p1 = people.get(id1);
        MyPerson p2 = people.get(id2);
        if (p1 == null) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (p2 == null) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (p1.isLinked(p2)) {
            throw new MyEqualRelationException(id1, id2);
        }
        p1.addLink(p2, value);
        p2.addLink(p1, value);

        if (union.getfa(id1) != union.getfa(id2)) {
            --blockSum;
        }
        union.merge(id1, id2);

        for (MyGroup g : groups.values()) {
            if (g.hasPerson(p1) && g.hasPerson(p2)) {
                g.addValue(value);
            }
        }
        updated.put(union.getfa(id1), false);
        edgeX.add(id1);
        edgeY.add(id2);
        ++edgeCnt;
    }

    @Override
    public int queryValue(int id1, int id2)
            throws PersonIdNotFoundException, RelationNotFoundException {
        MyPerson p1 = people.get(id1);
        MyPerson p2 = people.get(id2);
        if (p1 == null) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (p2 == null) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (!p1.isLinked(p2)) {
            throw new MyRelationNotFoundException(id1, id2);
        }
        return p1.queryValue(p2);
    }

    @Override
    public int queryPeopleSum() {
        return people.size();
    }

    @Override
    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        MyPerson p1 = people.get(id1);
        MyPerson p2 = people.get(id2);
        if (p1 == null) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (p2 == null) {
            throw new MyPersonIdNotFoundException(id2);
        }
        return union.getfa(id1) == union.getfa(id2);
    }

    @Override
    public int queryBlockSum() {
        return blockSum;
    }

    private boolean isUpdated(int id) {
        return updated.get(id) != null && updated.get(id);
    }

    public int edgeValue(int edgeId) {
        return people.get(edgeX.get(edgeId)).queryValue(people.get(edgeY.get(edgeId)));
    }

    private void update(int id) {
        MyUnion u = new MyUnion();
        int sum = 0;
        int cnt = 0;
        PriorityQueue<Integer> edges =
                new PriorityQueue<>(Comparator.comparingInt(this::edgeValue));
        for (MyPerson p : people.values()) {
            if (union.getfa(p.getId()) == union.getfa(id)) {
                u.add(p.getId());
                ++cnt;
            }
        }
        for (int i = 0; i < edgeCnt; i++) {
            int x = edgeX.get(i);
            if (union.getfa(x) == union.getfa(id)) {
                edges.add(i);
            }
        }
        while (!edges.isEmpty() && cnt > 1) {
            int i = edges.poll();
            int x = edgeX.get(i);
            int y = edgeY.get(i);
            if (union.getfa(x) == union.getfa(id) &&
                    union.getfa(y) == union.getfa(id) &&
                    u.getfa(x) != u.getfa(y)) {
                u.merge(x, y);
                sum += edgeValue(i);
                --cnt;
            }
        }
        minConnection.put(union.getfa(id), sum);
        updated.put(union.getfa(id), true);
    }

    @Override
    public int queryLeastConnection(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        int fa = union.getfa(id);
        if (!isUpdated(fa)) {
            update(fa);
        }
        return minConnection.get(fa);
    }

    @Override
    public void addGroup(Group group) throws EqualGroupIdException {
        if (groups.get(group.getId()) != null) {
            throw new MyEqualGroupIdException(group.getId());
        }
        groups.put(group.getId(), (MyGroup) group);
    }

    @Override
    public Group getGroup(int id) {
        return groups.get(id);
    }

    @Override
    public void addToGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        MyPerson p = people.get(id1);
        MyGroup g = groups.get(id2);
        if (g == null) {
            throw new MyGroupIdNotFoundException(id2);
        } else if (p == null) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (g.hasPerson(p)) {
            throw new MyEqualPersonIdException(id1);
        }
        g.addPerson(p);
    }

    @Override
    public int queryGroupPeopleSum(int id) throws GroupIdNotFoundException {
        if (groups.get(id) == null) {
            throw new MyGroupIdNotFoundException(id);
        }
        return groups.get(id).getSize();
    }

    @Override
    public int queryGroupValueSum(int id) throws GroupIdNotFoundException {
        if (groups.get(id) == null) {
            throw new MyGroupIdNotFoundException(id);
        }
        return groups.get(id).getValueSum();
    }

    @Override
    public int queryGroupAgeVar(int id) throws GroupIdNotFoundException {
        if (groups.get(id) == null) {
            throw new MyGroupIdNotFoundException(id);
        }
        return groups.get(id).getAgeVar();
    }

    @Override
    public void delFromGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        MyPerson p = people.get(id1);
        MyGroup g = groups.get(id2);
        if (g == null) {
            throw new MyGroupIdNotFoundException(id2);
        } else if (p == null) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!g.hasPerson(p)) {
            throw new MyEqualPersonIdException(id1);
        }
        g.delPerson(p);
    }

    @Override
    public boolean containsMessage(int id) {
        return messages.get(id) != null;
    }

    @Override
    public void addMessage(Message message) throws EqualMessageIdException,
            EmojiIdNotFoundException, EqualPersonIdException {
        if (messages.get(message.getId()) != null) {
            throw new MyEqualMessageIdException(message.getId());
        } else if ((message instanceof MyEmojiMessage) &&
                !containsEmojiId(((MyEmojiMessage) message).getEmojiId())) {
            throw new MyEmojiIdNotFoundException(((MyEmojiMessage) message).getEmojiId());
        } else if (message.getType() == 0 && message.getPerson1().equals(message.getPerson2())) {
            throw new MyEqualPersonIdException(message.getPerson1().getId());
        }
        messages.put(message.getId(), (MyMessage) message);
    }

    @Override
    public Message getMessage(int id) {
        return messages.get(id);
    }

    void solveMessage(MyMessage m) {
        MyPerson p1 = (MyPerson) m.getPerson1();
        MyPerson p2 = (MyPerson) m.getPerson2();
        MyGroup g = (MyGroup) m.getGroup();
        messages.remove(m.getId());
        if (m.getType() == 0) {
            if (m instanceof MyRedEnvelopeMessage) {
                p1.addMoney(-((MyRedEnvelopeMessage) m).getMoney());
                p2.addMoney(((MyRedEnvelopeMessage) m).getMoney());
            } else if (m instanceof MyEmojiMessage) {
                emojiHeat.merge(((MyEmojiMessage) m).getEmojiId(), 1, Integer::sum);
            }
            p1.addSocialValue(m.getSocialValue());
            p2.addSocialValue(m.getSocialValue());
            p2.addMessage(m);
        } else {
            if (m instanceof MyRedEnvelopeMessage) {
                int i = ((MyRedEnvelopeMessage) m).getMoney() / g.getSize();
                p1.addMoney(-i * (g.getSize() - 1));
                for (Person person : g.getPeople().values()) {
                    if (!person.equals(p1)) {
                        person.addMoney(i);
                    }
                }
            } else if (m instanceof MyEmojiMessage) {
                emojiHeat.merge(((MyEmojiMessage) m).getEmojiId(), 1, Integer::sum);
            }
            for (Person person : g.getPeople().values()) {
                person.addSocialValue(m.getSocialValue());
            }
        }
    }

    @Override
    public void sendMessage(int id) throws RelationNotFoundException,
            MessageIdNotFoundException, PersonIdNotFoundException {
        MyMessage m = messages.get(id);
        if (m == null) {
            throw new MyMessageIdNotFoundException(id);
        } else if (m.getType() == 0 &&
                !(m.getPerson1().isLinked(m.getPerson2()))) {
            throw new MyRelationNotFoundException(m.getPerson1().getId(),
                    m.getPerson2().getId());
        } else if (m.getType() == 1 &&
                !(m.getGroup().hasPerson(m.getPerson1()))) {
            throw new MyPersonIdNotFoundException(m.getPerson1().getId());
        }
        solveMessage(m);
    }

    @Override
    public int querySocialValue(int id) throws PersonIdNotFoundException {
        if (people.get(id) == null) {
            throw new MyPersonIdNotFoundException(id);
        }
        return people.get(id).getSocialValue();
    }

    @Override
    public List<Message> queryReceivedMessages(int id) throws PersonIdNotFoundException {
        if (people.get(id) == null) {
            throw new MyPersonIdNotFoundException(id);
        }
        return people.get(id).getReceivedMessages();
    }

    @Override
    public boolean containsEmojiId(int id) {
        return emojis.get(id) != null && emojis.get(id);
    }

    @Override
    public void storeEmojiId(int id) throws EqualEmojiIdException {
        if (emojis.get(id) != null) {
            throw new MyEqualEmojiIdException(id);
        }
        emojis.put(id, true);
        emojiHeat.put(id, 0);
    }

    @Override
    public int queryMoney(int id) throws PersonIdNotFoundException {
        if (people.get(id) == null) {
            throw new MyPersonIdNotFoundException(id);
        }
        return people.get(id).getMoney();
    }

    @Override
    public int queryPopularity(int id) throws EmojiIdNotFoundException {
        if (emojis.get(id) == null) {
            throw new MyEmojiIdNotFoundException(id);
        }
        return emojiHeat.get(id);
    }

    @Override
    public int deleteColdEmoji(int limit) {
        for (Iterator<Integer> iterator = emojiHeat.keySet().iterator(); iterator.hasNext(); ) {
            int i = iterator.next();
            if (emojiHeat.get(i) < limit) {
                emojis.remove(i);
                iterator.remove();
            }
        }
        HashMap<Integer, MyMessage> ms = (HashMap<Integer, MyMessage>) messages.clone();
        for (MyMessage m : ms.values()) {
            if (m instanceof EmojiMessage &&
                    !containsEmojiId(((MyEmojiMessage) m).getEmojiId())) {
                messages.remove(m.getId());
            }
        }
        return emojis.size();
    }

    @Override
    public void clearNotices(int personId) throws PersonIdNotFoundException {
        if (people.get(personId) == null) {
            throw new MyPersonIdNotFoundException(personId);
        }
        people.get(personId).clearNotices();
    }

    private int getDis(int st, int ed) {
        HashMap<Integer, Integer> dis = new HashMap<>();
        PriorityQueue<MyPoint> queue = new PriorityQueue<>();
        queue.add(new MyPoint(st, 0));
        dis.put(st, 0);
        while (!queue.isEmpty()) {
            MyPoint point = queue.poll();
            if (point.getDis() != dis.get(point.getId())) {
                continue;
            }
            MyPerson x = people.get(point.getId());
            for (Person p : x.getAcquaintance().values()) {
                if (dis.get(p.getId()) == null ||
                        dis.get(p.getId()) > point.getDis() + x.queryValue(p)) {
                    dis.put(p.getId(), point.getDis() + x.queryValue(p));
                    queue.add(new MyPoint(p.getId(), point.getDis() + x.queryValue(p)));
                }
            }
        }
        return dis.get(ed);
    }

    @Override
    public int sendIndirectMessage(int id) throws MessageIdNotFoundException {
        if (!containsMessage(id) || containsMessage(id) && messages.get(id).getType() == 1) {
            throw new MyMessageIdNotFoundException(id);
        }
        MyMessage m = messages.get(id);
        if (union.getfa(m.getPerson1().getId()) != union.getfa(m.getPerson2().getId())) {
            return -1;
        }
        solveMessage(m);
        return getDis(m.getPerson1().getId(), m.getPerson2().getId());
    }
}
