package myinstance.main;

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
import com.oocourse.spec3.main.RedEnvelopeMessage;
import myinstance.exceptions.MyEmojiIdNotFoundException;
import myinstance.exceptions.MyEqualEmojiIdException;
import myinstance.exceptions.MyEqualGroupIdException;
import myinstance.exceptions.MyEqualMessageIdException;
import myinstance.exceptions.MyEqualPersonIdException;
import myinstance.exceptions.MyEqualRelationException;
import myinstance.exceptions.MyGroupIdNotFoundException;
import myinstance.exceptions.MyMessageIdNotFoundException;
import myinstance.exceptions.MyPersonIdNotFoundException;
import myinstance.exceptions.MyRelationNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyNetwork implements Network {
    private final ArrayList<MyPerson> peopleList;
    private final HashMap<Integer, MyPerson> peopleMap;
    private final HashMap<Integer, Group> groups;
    private final HashMap<Integer, MyMessage> messages;
    private final Dsu<MyPerson> dsu;
    private final RelationList relationList;
    private final HashMap<Integer, ArrayList<MyEmojiMessage>> emojiIdMap;
    private HashMap<Integer, Integer> emojiIdHeat;
    private final Graph graph = Graph.getInstance();

    public MyNetwork() {
        peopleList = new ArrayList<>();
        peopleMap = new HashMap<>();
        groups = new HashMap<>();
        dsu = new Dsu<MyPerson>();
        messages = new HashMap<>();
        relationList = RelationList.getInstance();
        emojiIdHeat = new HashMap<>();
        emojiIdMap = new HashMap<>();
    }

    @Override
    public boolean contains(int id) {
        return peopleMap.containsKey(id);
    }

    @Override
    public MyPerson getPerson(int id) {
        /*if (people.containsKey(id)){
            return people.get(id);
        }else {
            return null;
        }*/
        return peopleMap.getOrDefault(id, null);
    }

    @Override
    public void addPerson(Person person) throws EqualPersonIdException {
        if (peopleMap.containsKey(person.getId())) {
            throw new MyEqualPersonIdException(person.getId());
        } else {
            peopleList.add((MyPerson) person);
            peopleMap.put(person.getId(), (MyPerson) person);
            dsu.addPerson((MyPerson) person);
            graph.addPerson((MyPerson) person);
        }
    }

    @Override
    public void addRelation(int id1, int id2, int value)
            throws PersonIdNotFoundException, EqualRelationException {
        if (!peopleMap.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!peopleMap.containsKey(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (peopleMap.get(id1).isLinked(peopleMap.get(id2))) {
            throw new MyEqualRelationException(id1, id2);
        } else {
            MyPerson person1 = peopleMap.get(id1);
            MyPerson person2 = peopleMap.get(id2);
            ((MyPerson) person1).addAcquaintance(person2, value);
            ((MyPerson) person2).addAcquaintance(person1, value);
            dsu.merge(person1, person2);
            relationList.addRelation(new Relation(person1.getId(), person2.getId(), value));
            graph.addRelation(person1, person2, value);
            graph.addRelation(person2, person1, value);
        }
    }

    @Override
    public int queryValue(int id1, int id2)
            throws PersonIdNotFoundException, RelationNotFoundException {
        if (!peopleMap.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!peopleMap.containsKey(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (!peopleMap.get(id1).isLinked(peopleMap.get(id2))) {
            throw new MyRelationNotFoundException(id1, id2);
        } else {
            MyPerson person1 = peopleMap.get(id1);
            MyPerson person2 = peopleMap.get(id2);
            return person1.queryValue(person2);
        }
    }

    @Override
    public int queryPeopleSum() {
        return peopleMap.size();
    }

    @Override
    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        if (!peopleMap.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!peopleMap.containsKey(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else {
            return dsu.isSameSet(peopleMap.get(id1), peopleMap.get(id2));
        }
    }

    @Override
    public int queryBlockSum() {
        return dsu.queryBlockSum();
    }

    /* TODO */
    // id Person所在的连通图中最小生成树的权值和
    @Override
    public int queryLeastConnection(int id) throws PersonIdNotFoundException {
        int result = 0;
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        } else {
            // System.out.println("size of peopleMap: " + peopleMap.size());
            // System.out.println("size of peopleListIn: " + peopleList.size());
            result = relationList.queryLeastConnection(peopleMap.get(id), peopleList, dsu);
        }
        return result;
    }

    @Override
    public void addGroup(Group group) throws EqualGroupIdException {
        if (groups.containsValue(group)) {
            throw new MyEqualGroupIdException(group.getId());
        } else {
            groups.put(group.getId(), group);
        }
    }

    @Override
    public Group getGroup(int id) {
        return groups.getOrDefault(id, null);
        // return null;
    }

    @Override
    public void addToGroup(int id1, int id2)
            throws GroupIdNotFoundException,
            PersonIdNotFoundException,
            EqualPersonIdException {
        if (!groups.containsKey(id2)) {
            throw new MyGroupIdNotFoundException(id2);
        } else if (!peopleMap.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (groups.get(id2).hasPerson(peopleMap.get(id1))) {
            throw new MyEqualPersonIdException(id1);
        } else if (groups.get(id2).getSize() < 1111) {
            groups.get(id2).addPerson(peopleMap.get(id1));
        }
    }

    @Override
    public int queryGroupPeopleSum(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new MyGroupIdNotFoundException(id);
        }
        return getGroup(id).getSize();
        // return 0;
    }

    @Override
    public int queryGroupValueSum(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new MyGroupIdNotFoundException(id);
        }
        return getGroup(id).getValueSum();
        // return 0;
    }

    @Override
    public int queryGroupAgeVar(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new MyGroupIdNotFoundException(id);
        }
        return getGroup(id).getAgeVar();
    }

    @Override
    public void delFromGroup(int id1, int id2)
            throws GroupIdNotFoundException,
            PersonIdNotFoundException,
            EqualPersonIdException {
        if (!groups.containsKey(id2)) {
            throw new MyGroupIdNotFoundException(id2);
        } else if (!peopleMap.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!groups.get(id2).hasPerson(peopleMap.get(id1))) {
            throw new MyEqualPersonIdException(id1);
        } else {
            groups.get(id2).delPerson(peopleMap.get(id1));
        }
    }

    @Override
    public boolean containsMessage(int id) {
        return messages.containsKey(id);
    }

    @Override
    public void addMessage(Message message) throws
            EqualMessageIdException,
            EmojiIdNotFoundException,
            EqualPersonIdException {
        if (messages.containsKey(message.getId())) {
            throw new MyEqualMessageIdException(message.getId());
        } else if ((message instanceof EmojiMessage)
                && !containsEmojiId(((EmojiMessage) message).getEmojiId())) {
            throw new MyEmojiIdNotFoundException(((EmojiMessage) message).getEmojiId());
        } else if (message.getType() == 0 && message.getPerson1().equals(message.getPerson2())) {
            throw new MyEqualPersonIdException(message.getPerson1().getId());
        }
        messages.put(message.getId(), (MyMessage) message);
        if (message instanceof EmojiMessage) {
            int emojiId = ((MyEmojiMessage) message).getEmojiId();
            emojiIdMap.get(emojiId).add((MyEmojiMessage) message);
        }
    }

    @Override
    public Message getMessage(int id) {
        return messages.getOrDefault(id, null);
    }

    @Override
    public void sendMessage(int id) throws
            RelationNotFoundException,
            MessageIdNotFoundException,
            PersonIdNotFoundException {
        if (!containsMessage(id)) {
            throw new MyMessageIdNotFoundException(id);
        } else if (getMessage(id).getType() == 0 &&
                !(getMessage(id).getPerson1().isLinked(getMessage(id).getPerson2()))) {
            throw new MyRelationNotFoundException(
                    getMessage(id).getPerson1().getId(),
                    getMessage(id).getPerson2().getId());
        } else if (getMessage(id).getType() == 1 &&
                !(getMessage(id).getGroup().hasPerson(getMessage(id).getPerson1()))) {
            throw new MyPersonIdNotFoundException(getMessage(id).getPerson1().getId());
        }
        Message message = messages.get(id);
        if (message.getType() == 0) {
            MyPerson person1 = (MyPerson) message.getPerson1();
            MyPerson person2 = (MyPerson) message.getPerson2();

            person1.addSocialValue(message.getSocialValue());
            person2.addSocialValue(message.getSocialValue());
            person2.receiveMessage(message);
            if (message instanceof RedEnvelopeMessage) {
                person1.deleteMoney(((MyRedEnvelopeMessage) message).getMoney());
                person2.addMoney(((MyRedEnvelopeMessage) message).getMoney());
            } else if (message instanceof EmojiMessage) {
                int emojiId = ((EmojiMessage) message).getEmojiId();
                emojiIdHeat.merge(emojiId, 1, (oldValue, newValue) -> oldValue + 1);
                emojiIdMap.get(emojiId).remove((MyEmojiMessage) message);
            }
            messages.remove(id);
            // System.out.println("remove the message: " + id);
            // 插入链表头部
        } else if (message.getType() == 1) {
            MyPerson person1 = (MyPerson) message.getPerson1();
            MyGroup group = (MyGroup) message.getGroup();

            for (MyPerson person : peopleList) {
                if (group.hasPerson(person)) {
                    person.addSocialValue(message.getSocialValue());
                }
            }
            if (message instanceof RedEnvelopeMessage) {
                int meanMoney =
                        ((RedEnvelopeMessage) message).getMoney()
                                / message.getGroup().getSize();
                person1.deleteMoney(meanMoney * (message.getGroup().getSize() - 1));
                group.addMoney(meanMoney, person1.getId());
            } else if (message instanceof EmojiMessage) {
                int emojiId = ((EmojiMessage) message).getEmojiId();
                emojiIdHeat.merge(emojiId, 1, (oldValue, newValue) -> oldValue + 1);
                emojiIdMap.get(emojiId).remove((MyEmojiMessage) message);
            }
            // group.addSocialValue(message.getSocialValue());
            messages.remove(id);
            // System.out.println("remove the message: " + id);
        }
    }

    @Override
    public int querySocialValue(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        } else {
            return getPerson(id).getSocialValue();
        }
    }

    @Override
    public List<Message> queryReceivedMessages(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        } else {
            return getPerson(id).getReceivedMessages();
        }
        // return null;
    }

    @Override
    public boolean containsEmojiId(int id) {
        return emojiIdHeat.containsKey(id);
    }

    @Override
    public void storeEmojiId(int id) throws EqualEmojiIdException {
        if (emojiIdHeat.containsKey(id)) {
            throw new MyEqualEmojiIdException(id);
        } else {
            emojiIdHeat.put(id, 0);
            emojiIdMap.put(id, new ArrayList<>());
        }
    }

    @Override
    public int queryMoney(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        } else {
            return peopleMap.get(id).getMoney();
        }
        // return 0;
    }

    @Override
    public int queryPopularity(int id) throws EmojiIdNotFoundException {
        if (!emojiIdHeat.containsKey(id)) {
            throw new MyEmojiIdNotFoundException(id);
        } else {
            return emojiIdHeat.get(id);
        }
    }

    @Override
    public int deleteColdEmoji(int limit) {
        HashMap<Integer, Integer> mapCp = new HashMap<>(emojiIdHeat);
        for (int id : emojiIdHeat.keySet()) {
            if (emojiIdHeat.get(id) < limit) {
                mapCp.remove(id);
                deleteEmojiMessage(id);
            }
        }
        emojiIdHeat = mapCp;
        return emojiIdHeat.size();
    }

    private void deleteEmojiMessage(int emojiId) {
        ArrayList<MyEmojiMessage> list = emojiIdMap.get(emojiId);

        for (MyEmojiMessage myEmojiMessage : list) {
            int messageId = myEmojiMessage.getId();
            messages.remove(messageId);
            // System.out.println("remove the message: " + messageId);
        }

        emojiIdMap.remove(emojiId);
    }

    @Override
    public void clearNotices(int personId) throws PersonIdNotFoundException {
        if (!contains(personId)) {
            throw new MyPersonIdNotFoundException(personId);
        } else {
            peopleMap.get(personId).deleteNotices();
        }
    }

    @Override
    public int sendIndirectMessage(int id) throws MessageIdNotFoundException {
        if (!containsMessage(id) || (containsMessage(id) && getMessage(id).getType() == 1)) {
            throw new MyMessageIdNotFoundException(id);
        }
        MyMessage message = messages.get(id);
        MyPerson person1 = (MyPerson) messages.get(id).getPerson1();
        MyPerson person2 = (MyPerson) messages.get(id).getPerson2();

        try {
            if (!isCircle(person1.getId(), person2.getId())) {
                return -1;
            }
        } catch (PersonIdNotFoundException e) {
            e.print();
        }

        person1.addSocialValue(message.getSocialValue());
        person2.addSocialValue(message.getSocialValue());
        person2.receiveMessage(message);
        if (message instanceof RedEnvelopeMessage) {
            person1.deleteMoney(((MyRedEnvelopeMessage) message).getMoney());
            person2.addMoney(((MyRedEnvelopeMessage) message).getMoney());
        } else if (message instanceof EmojiMessage) {
            int emojiId = ((EmojiMessage) message).getEmojiId();
            emojiIdHeat.merge(emojiId, 1, (oldValue, newValue) -> oldValue + 1);
            emojiIdMap.get(emojiId).remove((MyEmojiMessage) message);
        }
        messages.remove(id);
        // System.out.println("remove the message: " + id);

        return graph.getShortestPathValue(person1, person2);
        // return 0;
    }
}
