import com.oocourse.spec3.exceptions.EmojiIdNotFoundException;
import com.oocourse.spec3.exceptions.MessageIdNotFoundException;
import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Network;
import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.RedEnvelopeMessage;
import com.oocourse.spec3.main.EmojiMessage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Objects;

public class MyNetwork implements Network {
    private final HashMap<Integer, Person> idToPerson;
    private final HashMap<Integer, Group> idToGroup;
    private final HashMap<Integer, Message> idToMessage;
    private final HashMap<Integer, Integer> idToEmojiHeat;

    public MyNetwork() {
        idToPerson = new HashMap<>();
        idToGroup = new HashMap<>();
        idToMessage = new HashMap<>();
        idToEmojiHeat = new HashMap<>();
    }

    @Override
    public boolean contains(int id) {
        return idToPerson.containsKey(id);
    }

    @Override
    public Person getPerson(int id) {
        if (contains(id)) {
            return idToPerson.get(id);
        }
        return null;
    }

    @Override
    public void addPerson(Person person) throws MyEqualPersonIdException {
        int personId = person.getId();
        if (!contains(personId)) {
            idToPerson.put(personId, person);
        } else {
            throw new MyEqualPersonIdException(personId);
        }
    }

    @Override
    public void addRelation(int id1, int id2, int value)
            throws MyPersonIdNotFoundException, MyEqualRelationException {
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyEqualRelationException(id1, id2);
        } else {
            ((MyPerson) getPerson(id1)).addLink(id2, value);
            ((MyPerson) getPerson(id2)).addLink(id1, value);
            for (Integer id : idToGroup.keySet()) {
                Group group = idToGroup.get(id);
                if (group.hasPerson(getPerson(id1)) && group.hasPerson(getPerson(id2))) {
                    ((MyGroup) group).addValSum(2 * getPerson(id1).queryValue(getPerson(id2)));
                }
            }
        }
    }

    @Override
    public int queryValue(int id1, int id2)
            throws MyPersonIdNotFoundException, MyRelationNotFoundException {
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (!getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyRelationNotFoundException(id1, id2);
        } else {
            return getPerson(id1).queryValue(getPerson(id2));
        }
    }

    @Override
    public int queryPeopleSum() {
        return idToPerson.size();
    }

    private void dfs(int id, HashSet<Integer> visitedId) {
        visitedId.add(id);
        for (Integer linkId : ((MyPerson) getPerson(id)).getAcquaintance().keySet()) {
            if (!visitedId.contains(linkId)) {
                dfs(linkId, visitedId);
            }
        }
    }

    @Override
    public boolean isCircle(int id1, int id2) throws MyPersonIdNotFoundException {
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else {
            HashSet<Integer> visitedId = new HashSet<>();
            dfs(id1, visitedId);
            return visitedId.contains(id2);
        }
    }

    @Override
    public int queryBlockSum() {
        int blockSum = 0;
        HashSet<Integer> visitedId = new HashSet<>();
        for (Integer id : idToPerson.keySet()) {
            if (!visitedId.contains(id)) {
                dfs(id, visitedId);
                blockSum++;
            }
        }
        return blockSum;
    }

    @Override
    public int queryLeastConnection(int id) throws MyPersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        } else {
            return prim(id);
        }
    }

    public int prim(int id) {
        HashSet<Integer> visitedId = new HashSet<>();
        dfs(id, visitedId);
        int len = visitedId.size();
        int sum = 0;
        int[] personId = new int[len];
        int[][] relation = new int[len][len];
        int[] minDist = new int[len];
        //initialize Map
        HashMap<Integer, Integer> idToIndex = new HashMap<>();
        {
            int i = 0;
            for (Integer vid : visitedId) {
                idToIndex.put(vid, i);
                personId[i] = vid;
                minDist[i] = Integer.MAX_VALUE;
                relation[i][i] = Integer.MAX_VALUE;
                i++;
            }
        }
        //Initialize graph
        for (int i = 0; i < len; i++) {
            for (int j = i + 1; j < len; j++) {
                Person person1 = getPerson(personId[i]);
                Person person2 = getPerson(personId[j]);
                if (person1.isLinked(person2)) {
                    relation[i][j] = person1.queryValue(person2);
                } else {
                    relation[i][j] = Integer.MAX_VALUE;
                }
                relation[j][i] = relation[i][j];
            }
        }
        //Prim
        int curIndex = idToIndex.get(id);
        visitedId.remove(personId[curIndex]);
        while (!visitedId.isEmpty()) {
            int min = Integer.MAX_VALUE;
            int minIndex = -1;
            //update minDist
            for (Integer id2 : visitedId) {
                int index = idToIndex.get(id2);
                if (relation[curIndex][index] < minDist[index]) {
                    minDist[index] = relation[curIndex][index];
                }
                if (minDist[index] < min) {
                    min = minDist[index];
                    minIndex = index;
                }
            }
            sum += min;
            curIndex = minIndex;
            visitedId.remove(personId[curIndex]);
        }
        return sum;
    }

    @Override
    public void addGroup(Group group) throws MyEqualGroupIdException {
        int groupId = group.getId();
        if (!idToGroup.containsKey(groupId)) {
            idToGroup.put(groupId, group);
        } else {
            throw new MyEqualGroupIdException(groupId);
        }
    }

    @Override
    public Group getGroup(int id) {
        if (idToGroup.containsKey(id)) {
            return idToGroup.get(id);
        }
        return null;
    }

    @Override
    public void addToGroup(int id1, int id2) throws MyGroupIdNotFoundException,
            MyPersonIdNotFoundException, MyEqualPersonIdException {
        if (!idToGroup.containsKey(id2)) {
            throw new MyGroupIdNotFoundException(id2);
        } else if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (getGroup(id2).hasPerson(getPerson(id1))) {
            throw new MyEqualPersonIdException(id1);
        } else if (getGroup(id2).getSize() < 1111) {
            getGroup(id2).addPerson(getPerson(id1));
        }
    }

    @Override
    public int queryGroupPeopleSum(int id) throws MyGroupIdNotFoundException {
        if (idToGroup.containsKey(id)) {
            return idToGroup.get(id).getSize();
        } else {
            throw new MyGroupIdNotFoundException(id);
        }
    }

    @Override
    public int queryGroupValueSum(int id) throws MyGroupIdNotFoundException {
        if (idToGroup.containsKey(id)) {
            return idToGroup.get(id).getValueSum();
        } else {
            throw new MyGroupIdNotFoundException(id);
        }
    }

    @Override
    public int queryGroupAgeVar(int id) throws MyGroupIdNotFoundException {
        if (idToGroup.containsKey(id)) {
            return idToGroup.get(id).getAgeVar();
        } else {
            throw new MyGroupIdNotFoundException(id);
        }
    }

    @Override
    public void delFromGroup(int id1, int id2) throws MyGroupIdNotFoundException,
            MyPersonIdNotFoundException, MyEqualPersonIdException {
        if (!idToGroup.containsKey(id2)) {
            throw new MyGroupIdNotFoundException(id2);
        } else if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!getGroup(id2).hasPerson(getPerson(id1))) {
            throw new MyEqualPersonIdException(id1);
        } else {
            getGroup(id2).delPerson(getPerson(id1));
        }
    }

    @Override
    public boolean containsMessage(int id) {
        return idToMessage.containsKey(id);
    }

    @Override
    public void addMessage(Message message)
            throws MyEqualMessageIdException, MyEmojiIdNotFoundException, MyEqualPersonIdException {
        int msgId = message.getId();
        if (containsMessage(msgId)) {
            throw new MyEqualMessageIdException(msgId);
        } else if ((message instanceof EmojiMessage) &&
                !containsEmojiId(((EmojiMessage) message).getEmojiId())) {
            throw new MyEmojiIdNotFoundException(((EmojiMessage) message).getEmojiId());
        } else if (message.getType() == 0 && message.getPerson1() == message.getPerson2()) {
            throw new MyEqualPersonIdException(message.getPerson1().getId());
        } else {
            idToMessage.put(msgId, message);
        }
    }

    @Override
    public Message getMessage(int id) {
        if (containsMessage(id)) {
            return idToMessage.get(id);
        } else {
            return null;
        }
    }

    @Override
    public void sendMessage(int id) throws MyRelationNotFoundException,
            MyMessageIdNotFoundException, MyPersonIdNotFoundException {
        if (!containsMessage(id)) {
            throw new MyMessageIdNotFoundException(id);
        } else if (getMessage(id).getType() == 0 &&
                !(getMessage(id).getPerson1().isLinked(getMessage(id).getPerson2()))) {
            throw new MyRelationNotFoundException(getMessage(id).getPerson1().getId(),
                    getMessage(id).getPerson2().getId());
        } else if (getMessage(id).getType() == 1 &&
                !(getMessage(id).getGroup().hasPerson(getMessage(id).getPerson1()))) {
            throw new MyPersonIdNotFoundException(getMessage(id).getPerson1().getId());
        } else {
            Message message = getMessage(id);
            idToMessage.remove(id);
            if (message.getType() == 0) {
                message.getPerson1().addSocialValue(message.getSocialValue());
                message.getPerson2().addSocialValue(message.getSocialValue());
                message.getPerson2().getMessages().add(0, message);
                if (message instanceof RedEnvelopeMessage) {
                    message.getPerson1().addMoney(-((RedEnvelopeMessage) message).getMoney());
                    message.getPerson2().addMoney(((RedEnvelopeMessage) message).getMoney());
                } else if (message instanceof EmojiMessage) {
                    int emojiId = ((EmojiMessage) message).getEmojiId();
                    int emojiHeat = idToEmojiHeat.get(emojiId);
                    idToEmojiHeat.put(emojiId, emojiHeat + 1);
                }
            } else {
                for (Integer personId : idToPerson.keySet()) {
                    Person person = getPerson(personId);
                    if (message.getGroup().hasPerson(person)) {
                        person.addSocialValue(message.getSocialValue());
                    }
                }
                if (message instanceof RedEnvelopeMessage) {
                    int groupSize = message.getGroup().getSize();
                    int averageMoney = ((RedEnvelopeMessage) message).getMoney() / groupSize;
                    message.getPerson1().addMoney(-averageMoney * groupSize);
                    for (Integer personId : idToPerson.keySet()) {
                        Person person = getPerson(personId);
                        if (message.getGroup().hasPerson(person)) {
                            person.addMoney(averageMoney);
                        }
                    }
                } else if (message instanceof EmojiMessage) {
                    int emojiId = ((EmojiMessage) message).getEmojiId();
                    int emojiHeat = idToEmojiHeat.get(emojiId);
                    idToEmojiHeat.put(emojiId, emojiHeat + 1);
                }
            }
        }
    }

    @Override
    public int querySocialValue(int id) throws MyPersonIdNotFoundException {
        if (contains(id)) {
            return getPerson(id).getSocialValue();
        } else {
            throw new MyPersonIdNotFoundException(id);
        }
    }

    @Override
    public List<Message> queryReceivedMessages(int id) throws MyPersonIdNotFoundException {
        if (contains(id)) {
            return getPerson(id).getReceivedMessages();
        } else {
            throw new MyPersonIdNotFoundException(id);
        }
    }

    @Override
    public boolean containsEmojiId(int id) {
        return idToEmojiHeat.containsKey(id);
    }

    @Override
    public void storeEmojiId(int id) throws MyEqualEmojiIdException {
        if (containsEmojiId(id)) {
            throw new MyEqualEmojiIdException(id);
        } else {
            idToEmojiHeat.put(id, 0);
        }
    }

    @Override
    public int queryMoney(int id) throws MyPersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        } else {
            return getPerson(id).getMoney();
        }
    }

    @Override
    public int queryPopularity(int id) throws EmojiIdNotFoundException {
        if (!containsEmojiId(id)) {
            throw new MyEmojiIdNotFoundException(id);
        } else {
            return idToEmojiHeat.get(id);
        }
    }

    @Override
    public int deleteColdEmoji(int limit) {
        HashSet<Integer> deleteEmojiId = new HashSet<>();
        HashSet<Integer> deleteMessageId = new HashSet<>();
        for (Integer emojiId : idToEmojiHeat.keySet()) {
            if (idToEmojiHeat.get(emojiId) < limit) {
                deleteEmojiId.add(emojiId);
            }
        }
        for (Integer emojiId : deleteEmojiId) {
            idToEmojiHeat.remove(emojiId);
        }
        for (Integer messageId : idToMessage.keySet()) {
            Message message = getMessage(messageId);
            if ((message instanceof EmojiMessage) &&
                    deleteEmojiId.contains(((EmojiMessage) message).getEmojiId())) {
                deleteMessageId.add(messageId);
            }
        }
        for (Integer messageId : deleteMessageId) {
            idToMessage.remove(messageId);
        }
        return idToEmojiHeat.size();
    }

    @Override
    public void clearNotices(int personId) throws MyPersonIdNotFoundException {
        if (!contains(personId)) {
            throw new MyPersonIdNotFoundException(personId);
        } else {
            MyPerson myPerson = (MyPerson) getPerson(personId);
            myPerson.clearNotices();
        }
    }

    @Override
    public int sendIndirectMessage(int id) throws MessageIdNotFoundException {
        if (!containsMessage(id) || containsMessage(id) && getMessage(id).getType() == 1) {
            throw new MyMessageIdNotFoundException(id);
        } else {
            Message message = getMessage(id);
            Person person1 = message.getPerson1();
            Person person2 = message.getPerson2();
            HashSet<Integer> visitedId = new HashSet<>();
            dfs(person1.getId(), visitedId);
            if (!visitedId.contains(person2.getId())) {
                return -1;
            } else {
                idToMessage.remove(id);
                message.getPerson1().addSocialValue(message.getSocialValue());
                message.getPerson2().addSocialValue(message.getSocialValue());
                if (message instanceof RedEnvelopeMessage) {
                    message.getPerson1().addMoney(-((RedEnvelopeMessage) message).getMoney());
                    message.getPerson2().addMoney(((RedEnvelopeMessage) message).getMoney());
                } else if (message instanceof EmojiMessage) {
                    int emojiId = ((EmojiMessage) message).getEmojiId();
                    int emojiHeat = idToEmojiHeat.get(emojiId);
                    idToEmojiHeat.put(emojiId, emojiHeat + 1);
                }
                message.getPerson2().getMessages().add(0, message);
                return dijkstra(visitedId, person1.getId(), person2.getId());
            }
        }
    }

    private int dijkstra(HashSet<Integer> remainId, int beginId, int endId) {
        HashMap<Integer, Dot> idToDot = new HashMap<>();
        PriorityQueue<Dot> priorityQueue = new PriorityQueue<>();
        int curId = beginId;
        for (Integer id : remainId) {
            idToDot.put(id, new Dot(id, Integer.MAX_VALUE));
            priorityQueue.add(idToDot.get(id));
        }
        idToDot.get(beginId).setDist(0);
        remainId.remove(beginId);
        priorityQueue.remove(idToDot.get(beginId));
        while (!remainId.isEmpty()) {
            Person curPerson = getPerson(curId);
            Dot curDot = idToDot.get(curId);
            for (Integer id : ((MyPerson) curPerson).getAcquaintance().keySet()) {
                if (remainId.contains(id)) {
                    Person person = getPerson(id);
                    Dot dot = idToDot.get(id);
                    int updateDist = Integer.min(dot.getDist(),
                            curDot.getDist() + person.queryValue(curPerson));
                    if (updateDist < dot.getDist()) {
                        dot.setDist(updateDist);
                        priorityQueue.remove(dot);
                        priorityQueue.add(dot);
                    }
                }
            }
            curId = Objects.requireNonNull(priorityQueue.poll()).getId();
            remainId.remove(curId);
            if (curId == endId) {
                return idToDot.get(curId).getDist();
            }
        }
        return idToDot.get(endId).getDist();
    }
}