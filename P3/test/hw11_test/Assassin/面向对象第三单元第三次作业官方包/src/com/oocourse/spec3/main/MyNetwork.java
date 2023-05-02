package com.oocourse.spec3.main;

import com.oocourse.spec3.exceptions.EmojiIdNotFoundException;
import com.oocourse.spec3.exceptions.EqualEmojiIdException;
import com.oocourse.spec3.exceptions.EqualGroupIdException;
import com.oocourse.spec3.exceptions.EqualMessageIdException;
import com.oocourse.spec3.exceptions.EqualPersonIdException;
import com.oocourse.spec3.exceptions.EqualRelationException;
import com.oocourse.spec3.exceptions.GroupIdNotFoundException;
import com.oocourse.spec3.exceptions.MessageIdNotFoundException;
import com.oocourse.spec3.exceptions.MyEmojiIdNotFoundException;
import com.oocourse.spec3.exceptions.MyEqualEmojiIdException;
import com.oocourse.spec3.exceptions.MyEqualGroupIdException;
import com.oocourse.spec3.exceptions.MyEqualMessageIdException;
import com.oocourse.spec3.exceptions.MyEqualPersonIdException;
import com.oocourse.spec3.exceptions.MyEqualRelationException;
import com.oocourse.spec3.exceptions.MyGroupIdNotFoundException;
import com.oocourse.spec3.exceptions.MyMessageIdNotFoundException;
import com.oocourse.spec3.exceptions.MyPersonIdNotFoundException;
import com.oocourse.spec3.exceptions.MyRelationNotFoundException;
import com.oocourse.spec3.exceptions.PersonIdNotFoundException;
import com.oocourse.spec3.exceptions.RelationNotFoundException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

public class MyNetwork implements Network {
    private HashMap<Integer, Person> people;
    private HashMap<Integer, Group> groups;
    private DisjointSet disjointSet = new DisjointSet();
    private HashMap<Integer, Message> messages;
    private HashSet<Relation> relations;
    private HashMap<Integer, Integer> emojiList;

    public MyNetwork() {
        people = new HashMap<>();
        groups = new HashMap<>();
        messages = new HashMap<>();
        relations = new HashSet<>();
        emojiList = new HashMap<>();
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
        }
        people.put(person.getId(), person);
        disjointSet.addItem(person);
    }

    public void addRelation(int id1, int id2, int value) throws
            PersonIdNotFoundException, EqualRelationException {
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (contains(id1) && !contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (contains(id1) && contains(id2) &&
                getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyEqualRelationException(id1, id2);
        }
        MyPerson person1 = (MyPerson) people.get(id1);
        MyPerson person2 = (MyPerson) people.get(id2);
        person1.getAcquaintanceValueMap().put(person2, value);
        person2.getAcquaintanceValueMap().put(person1, value);
        Relation relation = new Relation(person1, person2);
        relations.add(relation);
        disjointSet.merge(person1, person2);
        for (Group group : groups.values()) {
            if (group.hasPerson(person1) && group.hasPerson(person2)) {
                int temp = ((MyGroup) group).getSumOfValue();
                ((MyGroup) group).setSumOfValue(temp + value);
            }
        }
    }

    public int queryValue(int id1, int id2) throws
            PersonIdNotFoundException, RelationNotFoundException {
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (contains(id1) && !contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (!getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyRelationNotFoundException(id1, id2);
        }
        return getPerson(id1).queryValue(getPerson(id2));
    }

    public int queryPeopleSum() {
        return people.size();
    }

    public boolean isCircle(int id1, int id2) throws MyPersonIdNotFoundException {
        if (contains(id1) && contains(id2)) {
            return disjointSet.isCircle(people.get(id1), people.get(id2));
        } else if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else {
            throw new MyPersonIdNotFoundException(id2);
        }
    }

    public int queryBlockSum() {
        return disjointSet.getBlockSum();
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
            PersonIdNotFoundException, EqualPersonIdException {
        MyGroup group = (MyGroup) getGroup(id2);
        if (group == null) {
            throw new MyGroupIdNotFoundException(id2);
        } else if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (group.hasPerson(people.get(id1))) {
            throw new MyEqualPersonIdException(id1);
        }
        MyPerson person = (MyPerson) people.get(id1);
        if (contains(id1) && !group.hasPerson(person) && group.getPeople().size() < 1111) {
            group.addPerson(person);
        }
    }

    public void delFromGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException,
            EqualPersonIdException {
        MyGroup group = (MyGroup) getGroup(id2);
        if (group == null) {
            throw new MyGroupIdNotFoundException(id2);
        } else if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!group.hasPerson(people.get(id1))) {
            throw new MyEqualPersonIdException(id1);
        }
        group.delPerson(people.get(id1));
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

    private ArrayList<Relation> getPersonRelations(Person person) {
        ArrayList<Relation> ans = new ArrayList<>();
        HashSet<Person> persons = disjointSet.getBlock(person).getItems();
        for (Relation relation : relations) {
            if (persons.contains(relation.getCouple().get(0))) {
                ans.add(relation);
            }
        }
        ans.sort(Comparator.comparingInt(Relation::getValue));
        return ans;
    }

    public int queryLeastConnection(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        Person person = getPerson(id);
        Person father = disjointSet.find(person);
        if (disjointSet.getBlock(disjointSet.find(person)).getSign() == 0) {
            return disjointSet.getBlock(father).getLeastValue();
        }
        ArrayList<Relation> personRelations = getPersonRelations(person);
        HashSet<Person> persons = disjointSet.getBlock(person).getItems();
        DisjointSet newDisjointSet = new DisjointSet();
        newDisjointSet.addAll(persons);
        int ans = 0;
        int lenOfTree = 0;
        for (int i = 0; i < personRelations.size() && lenOfTree != persons.size() - 1; i++) {
            Relation relation = personRelations.get(i);
            Person person1 = relation.getCouple().get(0);
            Person person2 = relation.getCouple().get(1);
            if (!newDisjointSet.isCircle(person1, person2)) {
                newDisjointSet.merge(person1, person2);
                ans += relation.getValue();
                ++lenOfTree;
            }
        }
        disjointSet.getBlock(father).setLeastValue(ans);
        disjointSet.getBlock(father).setSign(0);
        return ans;
    }

    public boolean containsMessage(int id) {
        return messages.containsKey(id);
    }

    public void addMessage(Message message) throws EqualMessageIdException,
            EqualPersonIdException, EmojiIdNotFoundException {
        if (containsMessage(message.getId())) {
            throw new MyEqualMessageIdException(message.getId());
        } else if (message instanceof EmojiMessage &&
                !containsEmojiId(((EmojiMessage) message).getEmojiId())) {
            throw new MyEmojiIdNotFoundException(((EmojiMessage) message).getEmojiId());
        } else if (message.getType() == 0 && message.getPerson1().equals(message.getPerson2())) {
            throw new MyEqualPersonIdException(message.getPerson1().getId());
        }
        messages.put(message.getId(), message);
    }

    public Message getMessage(int id) {
        return messages.getOrDefault(id, null);
    }

    public void sendMessage(int id) throws RelationNotFoundException,
            MessageIdNotFoundException, PersonIdNotFoundException {
        if (!containsMessage(id)) {
            throw new MyMessageIdNotFoundException(id);
        }
        Message message = messages.get(id);
        Person person1 = message.getPerson1();
        Person person2 = message.getPerson2();
        Group group = message.getGroup();
        if (message.getType() == 0 && !person1.isLinked(person2)) {
            throw new MyRelationNotFoundException(person1.getId(), person2.getId());
        } else if (message.getType() == 1 && !group.hasPerson(person1)) {
            throw new MyPersonIdNotFoundException(person1.getId());
        }
        if (message.getType() == 0 && person1.isLinked(person2)
                && !person1.equals(person2)) {
            extractedSendMessageTypeZero(id, message, person1, person2);
        }
        if (message.getType() == 1 && group.hasPerson(person1)) {
            extractedSendMessageTypeOne(id, message, person1, group);
        }
    }

    private void extractedSendMessageTypeZero(int id, Message message,
                                              Person person1, Person person2) {
        messages.remove(id);
        person1.addSocialValue(message.getSocialValue());
        person2.addSocialValue(message.getSocialValue());
        person2.getMessages().add(0, message);
        if (message instanceof EmojiMessage) {
            emojiList.merge(((EmojiMessage) message).getEmojiId(), 1, Integer::sum);
        } else if (message instanceof RedEnvelopeMessage) {
            person1.addMoney(-((RedEnvelopeMessage) message).getMoney());
            person2.addMoney(((RedEnvelopeMessage) message).getMoney());
        }
    }

    private void extractedSendMessageTypeOne(int id, Message message, Person person1, Group group) {
        messages.remove(id);
        if (message instanceof EmojiMessage) {
            for (Person person : ((MyGroup) group).getPeople().keySet()) {
                person.addSocialValue(message.getSocialValue());
            }
            emojiList.merge(((EmojiMessage) message).getEmojiId(), 1, Integer::sum);
        } else if (message instanceof RedEnvelopeMessage) {
            int i = ((RedEnvelopeMessage) message).getMoney() / group.getSize();
            person1.addMoney(-i * (group.getSize() - 1));
            for (Person person : ((MyGroup) group).getPeople().keySet()) {
                person.addSocialValue(message.getSocialValue());
                if (!person.equals(person1)) {
                    person.addMoney(i);
                }
            }
        } else {
            for (Person person : ((MyGroup) group).getPeople().keySet()) {
                person.addSocialValue(message.getSocialValue());
            }
        }
    }

    public int querySocialValue(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        return people.get(id).getSocialValue();
    }

    public List<Message> queryReceivedMessages(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        return people.get(id).getReceivedMessages();
    }

    public boolean containsEmojiId(int id) {
        return emojiList.containsKey(id);
    }

    public void storeEmojiId(int id) throws EqualEmojiIdException {
        if (containsEmojiId(id)) {
            throw new MyEqualEmojiIdException(id);
        }
        SignOfEmoji signOfEmoji = new SignOfEmoji();
        emojiList.put(id, 0);
    }

    public int queryMoney(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        return getPerson(id).getMoney();
    }

    public int queryPopularity(int id) throws EmojiIdNotFoundException {
        if (!containsEmojiId(id)) {
            throw new MyEmojiIdNotFoundException(id);
        }
        return emojiList.get(id);
    }

    public int deleteColdEmoji(int limit) {
        emojiList.entrySet().removeIf((entry) -> entry.getValue() < limit);
        messages.entrySet().removeIf((entry) -> entry.getValue() instanceof EmojiMessage &&
                !containsEmojiId(((EmojiMessage) entry.getValue()).getEmojiId()));
        return emojiList.size();
    }

    public void clearNotices(int personId) throws PersonIdNotFoundException {
        if (!contains(personId)) {
            throw new MyPersonIdNotFoundException(personId);
        }
        ArrayList<Message> messages = (ArrayList<Message>)
                getPerson(personId).getMessages();
        ArrayList<Message> newMessages = new ArrayList<>();
        for (Message message : messages) {
            if (!(message instanceof NoticeMessage)) {
                newMessages.add(message);
            }
        }
        ((MyPerson) getPerson(personId)).setMessages(newMessages);
    }

    public int sendIndirectMessage(int id) throws MessageIdNotFoundException {
        if (!containsMessage(id) || containsMessage(id) && getMessage(id).getType() == 1) {
            throw new MyMessageIdNotFoundException(id);
        }
        Message message = getMessage(id);
        Person person1 = message.getPerson1();
        Person person2 = message.getPerson2();
        if (!disjointSet.isCircle(person1, person2)) {
            return -1;
        }
        extractedSendMessageTypeZero(id, message, person1, person2);
        return dijkstra(person1.getId(), person2.getId());
    }

    private int dijkstra(int person1Id, int person2Id) {
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(Node::getDistance));
        HashMap<Integer, Integer> distance = new HashMap<>();
        HashSet<Integer> visited = new HashSet<>();
        distance.put(person1Id, 0);
        queue.offer(new Node(person1Id, 0));

        while (!queue.isEmpty()) {
            int personId = (queue.poll()).getId();
            if (!visited.contains(personId) && personId != person2Id) {
                visited.add(personId);
                HashMap<Person, Integer> acquaintance =
                        ((MyPerson) getPerson(personId)).getAcquaintanceValueMap();
                int personDistance = distance.get(personId);
                for (Person p : acquaintance.keySet()) {
                    int id = p.getId();
                    int value = personDistance + acquaintance.get(p);
                    if (!(distance.containsKey(id)) || distance.get(id) > value) {
                        distance.put(id, value);
                        queue.offer(new Node(id, value));
                    }
                }
            } else if (personId == person2Id) {
                break;
            }
        }
        return distance.getOrDefault(person2Id, 2147483647);
    }
}
