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

import exceptions.MyEmojiIdNotFoundException;
import exceptions.MyEqualEmojiIdException;
import exceptions.MyEqualGroupIdException;
import exceptions.MyEqualMessageIdException;
import exceptions.MyEqualPersonIdException;
import exceptions.MyEqualRelationException;
import exceptions.MyGroupIdNotFoundException;
import exceptions.MyMessageIdNotFoundException;
import exceptions.MyPersonIdNotFoundException;
import exceptions.MyRelationNotFoundException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class MyNetwork implements Network {
    private final HashMap<Integer, MyPerson> people = new HashMap<>();
    private final HashMap<Integer, MyGroup> groups = new HashMap<>();
    private final HashMap<Integer, MyMessage> messages = new HashMap<>();
    private final HashMap<Integer, Integer> emojiIdHeatMap = new HashMap<>();
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

        for (MyGroup myGroup : groups.values()) {
            if (myGroup.hasPerson(person1) && myGroup.hasPerson(person2)) {
                myGroup.addRelation(value);
            }
        }
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

        int res = 0;
        HashSet<Integer> visited = new HashSet<>();
        PriorityQueue<TwoTuple> path = new PriorityQueue<>();
        path.add(new TwoTuple(id, 0));

        while (!path.isEmpty()) {
            TwoTuple twoTuple = path.remove();
            int endPersonID = twoTuple.getEnd();
            if (visited.contains(endPersonID)) {
                continue;
            }
            int length = twoTuple.getLength();
            visited.add(endPersonID);
            res += length;

            for (Map.Entry<Integer, Integer> acquaintance : people.get(endPersonID)
                    .getAcquaintanceValueMap().entrySet()) {
                if (!visited.contains(acquaintance.getKey())) {
                    path.add(new TwoTuple(acquaintance.getKey(), acquaintance.getValue()));
                }
            }
        }
        return res;
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

    public void addMessage(Message message)
            throws EqualMessageIdException, EmojiIdNotFoundException, EqualPersonIdException {
        if (messages.containsKey(message.getId())) {
            throw new MyEqualMessageIdException(message.getId());
        } else if (message instanceof EmojiMessage && !containsEmojiId(
                ((EmojiMessage) message).getEmojiId())) {
            throw new MyEmojiIdNotFoundException(((EmojiMessage) message).getEmojiId());
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
        MyPerson person1 = (MyPerson) myMessage.getPerson1();
        if (myMessage.getType() == 0) {
            MyPerson person2 = (MyPerson) myMessage.getPerson2();
            if (!person1.isLinked(person2)) {
                throw new MyRelationNotFoundException(person1.getId(), person2.getId());
            }
            person1.addSocialValue(myMessage.getSocialValue());
            person2.addSocialValue(myMessage.getSocialValue());
            person2.addMessage(myMessage);
            if (myMessage instanceof MyRedEnvelopeMessage) {
                MyRedEnvelopeMessage myRedEnvelopeMessage = (MyRedEnvelopeMessage) myMessage;
                person1.addMoney(-1 * myRedEnvelopeMessage.getMoney());
                person2.addMoney(myRedEnvelopeMessage.getMoney());
            } else if (myMessage instanceof MyEmojiMessage) {
                MyEmojiMessage myEmojiMessage = (MyEmojiMessage) myMessage;
                Integer emojiId = myEmojiMessage.getEmojiId();
                emojiIdHeatMap.put(emojiId, emojiIdHeatMap.get(emojiId) + 1);
            }
        } else {
            MyGroup myGroup = (MyGroup) myMessage.getGroup();
            if (!myGroup.hasPerson(person1)) {
                throw new MyPersonIdNotFoundException(person1.getId());
            }
            myGroup.addSocialValue(myMessage.getSocialValue());
            if (myMessage instanceof MyRedEnvelopeMessage) {
                MyRedEnvelopeMessage myRedEnvelopeMessage = (MyRedEnvelopeMessage) myMessage;
                myGroup.addMoney(person1, myRedEnvelopeMessage.getMoney());
            } else if (myMessage instanceof MyEmojiMessage) {
                MyEmojiMessage myEmojiMessage = (MyEmojiMessage) myMessage;
                Integer emojiId = myEmojiMessage.getEmojiId();
                emojiIdHeatMap.put(emojiId, emojiIdHeatMap.get(emojiId) + 1);
            }
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

    public boolean containsEmojiId(int id) {
        return emojiIdHeatMap.containsKey(id);
    }

    public void storeEmojiId(int id) throws EqualEmojiIdException {
        if (containsEmojiId(id)) {
            throw new MyEqualEmojiIdException(id);
        }
        emojiIdHeatMap.put(id, 0);
    }

    public int queryMoney(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        return people.get(id).getMoney();
    }

    public int queryPopularity(int id) throws EmojiIdNotFoundException {
        if (!containsEmojiId(id)) {
            throw new MyEmojiIdNotFoundException(id);
        }
        return emojiIdHeatMap.get(id);
    }

    public int deleteColdEmoji(int limit) {
        Iterator<Map.Entry<Integer, Integer>> iterator1 = emojiIdHeatMap.entrySet().iterator();
        Map.Entry<Integer, Integer> entry1;
        while (iterator1.hasNext()) {
            entry1 = iterator1.next();
            if (entry1.getValue() < limit) {
                iterator1.remove();
            }
        }

        Iterator<Map.Entry<Integer, MyMessage>> iterator2 = messages.entrySet().iterator();
        Map.Entry<Integer, MyMessage> entry2;
        while (iterator2.hasNext()) {
            entry2 = iterator2.next();
            MyMessage myMessage = entry2.getValue();
            if (myMessage instanceof MyEmojiMessage && !containsEmojiId(
                    ((MyEmojiMessage) myMessage).getEmojiId())) {
                iterator2.remove();
            }
        }

        return emojiIdHeatMap.size();
    }

    public void clearNotices(int personId) throws PersonIdNotFoundException {
        if (!contains(personId)) {
            throw new MyPersonIdNotFoundException(personId);
        }
        MyPerson person = people.get(personId);
        person.clearNotices();
    }

    public int sendIndirectMessage(int id) throws MessageIdNotFoundException {
        if ((!containsMessage(id)) || (messages.get(id).getType() == 1)) {
            throw new MyMessageIdNotFoundException(id);
        }
        MyMessage myMessage = messages.get(id);
        MyPerson person1 = (MyPerson) myMessage.getPerson1();
        MyPerson person2 = (MyPerson) myMessage.getPerson2();
        try {
            if (!isCircle(person1.getId(), person2.getId())) {
                return -1;
            }
        } catch (PersonIdNotFoundException e) {
            throw new RuntimeException(e);
        }
        person1.addSocialValue(myMessage.getSocialValue());
        person2.addSocialValue(myMessage.getSocialValue());
        if (myMessage instanceof MyRedEnvelopeMessage) {
            MyRedEnvelopeMessage message = (MyRedEnvelopeMessage) myMessage;
            person1.addMoney(-message.getMoney());
            person2.addMoney(message.getMoney());
        } else if (myMessage instanceof MyEmojiMessage) {
            MyEmojiMessage myEmojiMessage = (MyEmojiMessage) myMessage;
            Integer emojiId = myEmojiMessage.getEmojiId();
            emojiIdHeatMap.put(emojiId, emojiIdHeatMap.get(emojiId) + 1);
        }
        person2.addMessage(myMessage);
        messages.remove(id);


        HashSet<Integer> visited = new HashSet<>();
        PriorityQueue<TwoTuple> path = new PriorityQueue<>();
        path.add(new TwoTuple(person1.getId(), 0));

        while (true) {
            TwoTuple twoTuple = path.remove();
            int endPersonID = twoTuple.getEnd();
            if (visited.contains(endPersonID)) {
                continue;
            }

            int length = twoTuple.getLength();
            if (endPersonID == person2.getId()) {
                return length;
            }
            visited.add(endPersonID);
            for (Map.Entry<Integer, Integer> acquaintance : people.get(endPersonID)
                    .getAcquaintanceValueMap().entrySet()) {
                if (!visited.contains(acquaintance.getKey())) {
                    path.add(new TwoTuple(acquaintance.getKey(), acquaintance.getValue() + length));
                }
            }
        }
    }
}
