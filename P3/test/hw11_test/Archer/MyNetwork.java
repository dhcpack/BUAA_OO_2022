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

import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.TreeSet;

public class MyNetwork implements Network {
    private HashMap<Integer, Person> people;
    private HashMap<Integer, Group> groups;
    private HashMap<Integer, Integer> fathers;
    private HashMap<Integer, Message> messages;
    private HashMap<Integer, Integer> emoji;//id-heat
    private HashMap<Integer, Integer> ancestors;
    private TreeSet<MyEdge> edges;
    private HashMap<Integer, Integer> dis;//id - dis
    private HashMap<Integer, Boolean> vis;//id - valid
    private PriorityQueue<Pair> queue;
    private int blockSum;
    private final int inf = 2147483647;
    
    public MyNetwork() {
        this.people = new HashMap<>();
        this.groups = new HashMap<>();
        this.fathers = new HashMap<>();
        this.messages = new HashMap<>();
        this.edges = new TreeSet<>();
        this.emoji = new HashMap<>();
        this.blockSum = 0;
        this.dis = new HashMap<>();
        this.vis = new HashMap<>();
    }
    
    public boolean contains(int id) {
        return people.containsKey(id);
    }
    
    public Person getPerson(int id) {
        return people.getOrDefault(id, null);
    }
    
    public void addPerson(Person person) throws EqualPersonIdException {
        if (contains(person.getId())) {
            throw new MyEqualPersonIdException(person.getId());
        } else {
            people.put(person.getId(), person);
            fathers.put(person.getId(), person.getId());//initial
            blockSum++;
        }
    }
    
    public int getFather(int id) {
        if (fathers.get(id) == id) {
            return id;
        } else {
            int father = getFather(fathers.get(id));
            fathers.put(id, father);
            return father;
        }
    }
    
    public void addRelation(int id1, int id2, int value) throws PersonIdNotFoundException,
            EqualRelationException {
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyEqualRelationException(id1, id2);
        } else {
            MyPerson person1 = (MyPerson) getPerson(id1);
            MyPerson person2 = (MyPerson) getPerson(id2);
            updateGroupValueSum(person1, person2, value);
            person1.addRelation(person2, value);
            person2.addRelation(person1, value);
            MyEdge edge = new MyEdge(id1, id2, value);
            edges.add(edge);//添加边
            int idA = getFather(id1);
            int idB = getFather(id2);
            if (idA != idB) {
                fathers.put(idA, idB);
                blockSum--;
            }
        }
    }
    
    public void updateGroupValueSum(Person person1, Person person2, int value) {
        for (Group group : groups.values()) {
            if (group.hasPerson(person1) && group.hasPerson(person2)) {
                ((MyGroup) group).addValueSum(value);
            }
        }
    }
    
    public int queryValue(int id1, int id2) throws PersonIdNotFoundException,
            RelationNotFoundException {
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (!getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyRelationNotFoundException(id1, id2);
        }
        return getPerson(id1).queryValue(getPerson(id2));
    }
    
    public int queryPeopleSum() {
        return people.size();
    }
    
    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        return (getFather(id1) == getFather(id2));
    }
    
    public int queryBlockSum() {
        return blockSum;
    }
    
    public int queryLeastConnection(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        } else {
            int sum = 0;//记录边权和
            int n = 0;
            ancestors = new HashMap<>();
            for (int personId : people.keySet()) {
                if (isCircle(personId, id)) {
                    ancestors.put(personId, personId);//init
                    n++;
                }
            }
            for (MyEdge edge : edges) {
                if (ancestors.containsKey(edge.getStart())
                        && ancestors.containsKey(edge.getTo())) {
                    int start = find(edge.getStart());
                    int to = find(edge.getTo());
                    if (start != to) {
                        merge(start, to);
                        sum += edge.getValue();
                        n--;
                        if (n == 1) {
                            break;
                        }
                    }
                }
            }
            return sum;//最小生成树
        }
    }
    
    public int find(int id) {
        if (ancestors.get(id) == id) {
            return id;
        } else {
            int ancestor = find(ancestors.get(id));
            ancestors.put(id, ancestor);
            return ancestor;
        }
    }
    
    public void merge(int id1, int id2) {
        int ancestor1 = find(id1);
        int ancestor2 = find(id2);
        if (ancestor1 != ancestor2) {
            ancestors.put(ancestor1, ancestor2);
        }
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
    
    public void addToGroup(int id1, int id2) throws GroupIdNotFoundException,
            PersonIdNotFoundException,
            EqualPersonIdException {
        Group group = getGroup(id2);
        Person person = getPerson(id1);
        if (group == null) {
            throw new MyGroupIdNotFoundException(id2);
        } else if (person == null) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (group.hasPerson(person)) {
            throw new MyEqualPersonIdException(id1);
        } else {
            if (group.getSize() < 1111) {
                group.addPerson(person);
            }
        }
    }
    
    public int queryGroupPeopleSum(int id) throws GroupIdNotFoundException {
        Group group = getGroup(id);
        if (group == null) {
            throw new MyGroupIdNotFoundException(id);
        } else {
            return group.getSize();
        }
    }
    
    public int queryGroupValueSum(int id) throws GroupIdNotFoundException {
        Group group = getGroup(id);
        if (group == null) {
            throw new MyGroupIdNotFoundException(id);
        } else {
            return group.getValueSum();
        }
    }
    
    public int queryGroupAgeVar(int id) throws GroupIdNotFoundException {
        Group group = getGroup(id);
        if (group == null) {
            throw new MyGroupIdNotFoundException(id);
        } else {
            return group.getAgeVar();
        }
    }
    
    public void delFromGroup(int id1, int id2) throws GroupIdNotFoundException,
            PersonIdNotFoundException, EqualPersonIdException {
        Group group = getGroup(id2);
        Person person = getPerson(id1);
        if (group == null) {
            throw new MyGroupIdNotFoundException(id2);
        } else if (person == null) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!group.hasPerson(person)) {
            throw new MyEqualPersonIdException(id1);
        } else {
            group.delPerson(person);
        }
    }
    
    public boolean containsMessage(int id) {
        return messages.containsKey(id);
    }
    
    public void addMessage(Message message) throws
            EqualMessageIdException, EmojiIdNotFoundException, EqualPersonIdException {
        if (containsMessage(message.getId())) {
            throw new MyEqualMessageIdException(message.getId());
        } else if ((message instanceof EmojiMessage)
                && !(containsEmojiId(((EmojiMessage) message).getEmojiId()))) {
            throw new MyEmojiIdNotFoundException(((EmojiMessage) message).getEmojiId());
        } else if (message.getType() == 0 && message.getPerson1()
                .equals(message.getPerson2())) {
            throw new MyEqualPersonIdException(message.getPerson1().getId());
        } else {
            messages.put(message.getId(), message);
        }
    }
    
    public Message getMessage(int id) {
        return messages.getOrDefault(id, null);
    }
    
    public void sendMessage(int id) throws
            RelationNotFoundException, MessageIdNotFoundException, PersonIdNotFoundException {
        Message message = getMessage(id);
        if (message == null) {
            throw new MyMessageIdNotFoundException(id);
        }
        Person person1 = message.getPerson1();
        Person person2 = message.getPerson2();
        if (message.getType() == 0 && !(person1.isLinked(person2))) {
            throw new MyRelationNotFoundException(person1.getId(), person2.getId());
        } else if (message.getType() == 1 && !(message.getGroup().hasPerson(person1))) {
            throw new MyPersonIdNotFoundException(person1.getId());
        } else if (message.getType() == 0) {
            dealMessage(person1, person2, message);
        } else if (message.getType() == 1) {
            MyGroup group = (MyGroup) message.getGroup();
            group.addMessageSocialValue(message);
            if (message instanceof RedEnvelopeMessage) {
                group.addMessageMoney((RedEnvelopeMessage) message, person1);
            } else if (message instanceof EmojiMessage) {
                int emojiId = ((EmojiMessage) message).getEmojiId();
                emoji.put(emojiId, emoji.get(emojiId) + 1);
            }
            messages.remove(message.getId());
        }
    }
    
    public void dealMessage(Person person1, Person person2, Message message) {
        person1.addSocialValue(message.getSocialValue());
        person2.addSocialValue(message.getSocialValue());
        if (message instanceof RedEnvelopeMessage) {
            int money = ((RedEnvelopeMessage) message).getMoney();
            person1.addMoney(-money);
            person2.addMoney(money);
        } else if (message instanceof EmojiMessage) {
            int emojiId = ((EmojiMessage) message).getEmojiId();
            emoji.put(emojiId, emoji.get(emojiId) + 1);
        }
        messages.remove(message.getId());
        person2.getMessages().add(0, message);
    }
    
    public int querySocialValue(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        } else {
            return getPerson(id).getSocialValue();
        }
    }
    
    public List<Message> queryReceivedMessages(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        } else {
            return getPerson(id).getReceivedMessages();
        }
    }
    
    public boolean containsEmojiId(int id) {
        return emoji.containsKey(id);
    }
    
    public void storeEmojiId(int id) throws EqualEmojiIdException {
        if (emoji.containsKey(id)) {
            throw new MyEqualEmojiIdException(id);
        } else {
            emoji.put(id, 0);
        }
    }
    
    public int queryMoney(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        } else {
            return getPerson(id).getMoney();
        }
    }
    
    public int queryPopularity(int id) throws EmojiIdNotFoundException {
        if (!emoji.containsKey(id)) {
            throw new MyEmojiIdNotFoundException(id);
        } else {
            return emoji.get(id);
        }
    }
    
    public int deleteColdEmoji(int limit) {
        emoji.entrySet().removeIf(tempEmoji -> (tempEmoji.getValue() < limit));
        messages.entrySet().removeIf(tempMessage -> (tempMessage.getValue() instanceof EmojiMessage
                && !emoji.containsKey(((EmojiMessage) tempMessage.getValue()).getEmojiId())));
        return emoji.size();
    }
    
    public void clearNotices(int personId) throws PersonIdNotFoundException {
        if (!contains(personId)) {
            throw new MyPersonIdNotFoundException(personId);
        } else {
            getPerson(personId).getMessages()
                    .removeIf(temp -> temp instanceof NoticeMessage);
        }
    }
    
    public int shortest(int start, int end) {
        for (Integer id : people.keySet()) {
            dis.put(id, inf);
            vis.put(id, false);
        }
        dis.put(start, 0);
        queue = new PriorityQueue<>();
        queue.add(new Pair(0, start));
        while (!queue.isEmpty()) {
            Pair temp = queue.poll();
            if (vis.get(temp.getTo())) {
                continue;
            }
            if (temp.getTo() == end) {
                break;
            }
            vis.put(temp.getTo(), true);
            MyPerson myPerson = (MyPerson) getPerson(temp.getTo());
            HashMap<Integer, Person> acquaintance = myPerson.getAcquaintance();
            for (Integer id : acquaintance.keySet()) {
                if (vis.get(id)) {
                    continue;
                }
                int tempDis = temp.getDis() + myPerson.queryValue(acquaintance.get(id));
                if (dis.get(id) > tempDis) {
                    dis.put(id, tempDis);
                    queue.add(new Pair(tempDis, id));
                }
            }
        }
        return dis.get(end);
    }
    
    public int sendIndirectMessage(int id) throws
            MessageIdNotFoundException {
        int res = -1;
        if (!containsMessage(id) || (containsMessage(id) && getMessage(id).getType() == 1)) {
            throw new MyMessageIdNotFoundException(id);
        } else {
            Message message = getMessage(id);
            int id1 = message.getPerson1().getId();
            int id2 = message.getPerson2().getId();
            if (getFather(id1) != getFather(id2)) {
                return res;
            } else {
                res = shortest(id1, id2);
                Person person1 = message.getPerson1();
                Person person2 = message.getPerson2();
                dealMessage(person1, person2, message);
            }
        }
        return res;
    }
}