package mymain;

import com.oocourse.spec2.exceptions.MessageIdNotFoundException;
import com.oocourse.spec2.exceptions.EqualMessageIdException;
import com.oocourse.spec2.exceptions.EqualGroupIdException;
import com.oocourse.spec2.exceptions.EqualPersonIdException;
import com.oocourse.spec2.exceptions.EqualRelationException;
import com.oocourse.spec2.exceptions.GroupIdNotFoundException;
import com.oocourse.spec2.exceptions.PersonIdNotFoundException;
import com.oocourse.spec2.exceptions.RelationNotFoundException;
import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Network;
import com.oocourse.spec2.main.Person;
import myexceptions.MyEqualMessageIdException;
import myexceptions.MyEqualGroupIdException;
import myexceptions.MyRelationNotFoundException;
import myexceptions.MyEqualPersonIdException;
import myexceptions.MyGroupIdNotFoundException;
import myexceptions.MyEqualRelationException;
import myexceptions.MyMessageIdNotFoundException;
import myexceptions.MyPersonIdNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyNetwork implements Network {
    private ArrayList<Person> people;
    private ArrayList<Group> groups;
    private ArrayList<Message> messages;
    private int[] father;
    private int mapSum;
    private int blockSum;
    private HashMap<Integer, Integer> indHashMap;
    private HashMap<Integer, Person> personHashMap;
    private HashMap<Integer, Group> groupHashMap;

    public MyNetwork() {
        people = new ArrayList<>();
        groups = new ArrayList<>();
        messages = new ArrayList<>();
        father = new int[12000];
        int i;
        for (i = 0; i < 1888; i += 1) {
            father[i] = i;
        }
        indHashMap = new HashMap<>();
        mapSum = 0;
        blockSum = 0;
        personHashMap = new HashMap<>();
        groupHashMap = new HashMap<>();
    }

    private int find(int id) {
        if (id != father[id]) {
            father[id] = find(father[id]);
        }
        return father[id];
    }

    private void merge(int id1, int id2) {
        int fa1 = find(id1);
        int fa2 = find(id2);
        if (father[fa1] != fa2) {
            blockSum -= 1;
        }
        father[fa1] = fa2;
        return;
    }

    public boolean contains(int id) {
        //        int i;
        //        for (i = 0; i < people.toArray().length; i += 1) {
        //            if (people.get(i).getId() == id) {
        //                return true;
        //            }
        //        }
        //        return false;
        return personHashMap.containsKey(id);
    }

    public Person getPerson(int id) {
        //        int i;
        //        for (i = 0; i < people.toArray().length; i += 1) {
        //            if (people.get(i).getId() == id) {
        //                return people.get(i);
        //            }
        //        }
        //        return null;
        if (personHashMap.containsKey(id) == true) {
            return personHashMap.get(id);
        }
        else {
            return null;
        }
    }

    public void addPerson(Person person) throws EqualPersonIdException {
        if (!indHashMap.containsKey(person.getId())) {
            indHashMap.put(person.getId(), mapSum);
            mapSum += 1;
        }
        if (personHashMap.containsKey(person.getId()) == false) {
            people.add(person);
            blockSum += 1;
            personHashMap.put(person.getId(), person);
        }
        else {
            throw new MyEqualPersonIdException(person.getId());
        }
    }

    public void addRelation(int id1, int id2, int value)
            throws PersonIdNotFoundException, EqualRelationException {
        if (contains(id1) && contains(id2) && !getPerson(id1).isLinked((getPerson(id2)))) {
            Person person1 = getPerson(id1);
            Person person2 = getPerson(id2);
            ((MyPerson) person1).addRelation(getPerson(id2), value);
            ((MyPerson) person2).addRelation(getPerson(id1), value);
            if (!indHashMap.containsKey(id1)) {
                indHashMap.put(id1, mapSum);
                mapSum += 1;
            }
            if (!indHashMap.containsKey(id2)) {
                indHashMap.put(id2, mapSum);
                mapSum += 1;
            }
            merge(indHashMap.get(id1), indHashMap.get(id2));
        }
        else {
            if (contains(id1) == false) {
                throw new MyPersonIdNotFoundException(id1);
            }
            else if (contains(id2) == false) {
                throw new MyPersonIdNotFoundException(id2);
            }
            else {
                throw new MyEqualRelationException(id1, id2);
            }
        }
    }

    public int queryValue(int id1, int id2)
            throws PersonIdNotFoundException, RelationNotFoundException {
        if (contains(id1) && contains(id2) && getPerson(id1).isLinked(getPerson(id2))) {
            return getPerson(id1).queryValue(getPerson(id2));
        }
        else {
            if (contains(id1) == false) {
                throw new MyPersonIdNotFoundException(id1);
            }
            else if (contains(id2) == false) {
                throw new MyPersonIdNotFoundException(id2);
            }
            else {
                throw new MyRelationNotFoundException(id1, id2);
            }
        }
    }

    public int queryPeopleSum() {
        return people.toArray().length;
    }

    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        if ((contains(id1) == true) && (contains(id2) == true)) {
            if (indHashMap.get(id1) == null || indHashMap.get(id2) == null) {
                return false;
            }
            return find(indHashMap.get(id1)) == find(indHashMap.get(id2));
        }
        else {
            if (!contains(id1)) {
                throw new MyPersonIdNotFoundException(id1);
            }
            else {
                throw new MyPersonIdNotFoundException(id2);
            }
        }
    }

    public int queryBlockSum() {
        return blockSum;
    }

    public int queryLeastConnection(int id) throws PersonIdNotFoundException {
        if (contains(id)) {
            ArrayList<Person> toKruskal = new ArrayList<>();
            for (Person person: people) {
                if (find(indHashMap.get(person.getId())) == find(indHashMap.get(id))) {
                    toKruskal.add(person);
                }
            }
            Kruskal kruskal = new Kruskal(toKruskal);
            return kruskal.calKruskal();
        }
        else {
            throw new MyPersonIdNotFoundException(id);
        }
    }

    public void addGroup(Group group) throws EqualGroupIdException {
        boolean check = groupHashMap.containsKey(group.getId());
        //        for (Group ele: groups) {
        //            if (ele.getId() == group.getId()) {
        //                check = false;
        //                break;
        //            }
        //        }
        if (check == false) {
            groups.add(group);
            groupHashMap.put(group.getId(), group);
        }
        else {
            throw new MyEqualGroupIdException(group.getId());
        }
    }

    public Group getGroup(int id) {
        //        for (Group ele: groups) {
        //            if (ele.getId() == id) {
        //                return ele;
        //            }
        //        }
        //        return null;
        if (groupHashMap.containsKey(id)) {
            return groupHashMap.get(id);
        }
        else {
            return null;
        }
    }

    public void addToGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        //        boolean groupId = false;
        //        for (Group ele: groups) {
        //            if (ele.getId() == id2) {
        //                groupId = true;
        //                break;
        //            }
        //        }
        boolean groupId = groupHashMap.containsKey(id2);
        if (groupId == false) {
            throw new MyGroupIdNotFoundException(id2);
        }
        //        for (Person ele: people) {
        //            if (ele.getId() == id1) {
        //                personId = true;
        //                break;
        //            }
        //        }
        boolean personId = contains(id1);
        if (personId == false) {
            throw new MyPersonIdNotFoundException(id1);
        }
        boolean hasGP = getGroup(id2).hasPerson(getPerson(id1));
        if (hasGP == true) {
            throw new MyEqualPersonIdException(id1);
        }
        if (((MyGroup) getGroup(id2)).getPeopleLen() >= 1111) {
            return;
        }
        getGroup(id2).addPerson(getPerson(id1));
        return;
    }

    public int queryGroupPeopleSum(int id) throws GroupIdNotFoundException {
        //        boolean exist = false;
        //        for (int i = 0; i < groups.toArray().length; i += 1) {
        //            if (groups.get(i).getId() == id) {
        //                exist = true;
        //                break;
        //            }
        //        }
        boolean exist = groupHashMap.containsKey(id);
        if (exist == true) {
            return ((MyGroup) getGroup(id)).getPeopleLen();
        }
        else {
            throw new MyGroupIdNotFoundException(id);
        }
    }

    public int queryGroupValueSum(int id) throws GroupIdNotFoundException {
        //        boolean exist = false;
        //        for (int i = 0; i < groups.toArray().length; i += 1) {
        //            if (groups.get(i).getId() == id) {
        //                exist = true;
        //                break;
        //            }
        //        }
        boolean exist = groupHashMap.containsKey(id);
        if (exist == true) {
            return getGroup(id).getValueSum();
        }
        else {
            throw new MyGroupIdNotFoundException(id);
        }
    }

    public int queryGroupAgeVar(int id) throws GroupIdNotFoundException {
        //        boolean exist = false;
        //        for (int i = 0; i < groups.toArray().length; i += 1) {
        //            if (groups.get(i).getId() == id) {
        //                exist = true;
        //                break;
        //            }
        //        }
        boolean exist = groupHashMap.containsKey(id);
        if (exist == true) {
            return getGroup(id).getAgeVar();
        }
        else {
            throw new MyGroupIdNotFoundException(id);
        }
    }

    public void delFromGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        //        boolean groupId = false;
        //        for (Group ele: groups) {
        //            if (ele.getId() == id2) {
        //                groupId = true;
        //                break;
        //            }
        //        }
        boolean groupId = groupHashMap.containsKey(id2);
        if (groupId == false) {
            throw new MyGroupIdNotFoundException(id2);
        }
        //        for (Person ele: people) {
        //            if (ele.getId() == id1) {
        //                personId = true;
        //                break;
        //            }
        //        }
        boolean personId = contains(id1);
        if (personId == false) {
            throw new MyPersonIdNotFoundException(id1);
        }
        boolean hasGP = getGroup(id2).hasPerson(getPerson(id1));
        if (hasGP == false) {
            throw new MyEqualPersonIdException(id1);
        }
        getGroup(id2).delPerson(getPerson(id1));
    }

    public boolean containsMessage(int id) {
        for (int i = 0; i < messages.toArray().length; i += 1) {
            if (messages.get(i).getId() == id) {
                return true;
            }
        }
        return false;
    }

    public void addMessage(Message message) throws EqualMessageIdException, EqualPersonIdException {
        boolean exist = false;
        for (int i = 0; i < messages.toArray().length; i += 1) {
            if (messages.get(i).equals(message)) {
                exist = true;
                break;
            }
        }
        if (exist == true) {
            throw new MyEqualMessageIdException(message.getId());
        }
        if (message.getType() == 0 && message.getPerson1() == message.getPerson2()) {
            throw new MyEqualPersonIdException(message.getPerson1().getId());
        }
        messages.add(message);
        return;
    }

    public Message getMessage(int id) {
        for (int i = 0; i < messages.toArray().length; i += 1) {
            if (messages.get(i).getId() == id) {
                return messages.get(i);
            }
        }
        return null;
    }

    public void sendMessage(int id)
            throws RelationNotFoundException, MessageIdNotFoundException,
            PersonIdNotFoundException {
        if (!containsMessage(id)) {
            throw new MyMessageIdNotFoundException(id);
        }
        if (getMessage(id).getType() == 0) {
            if (!getMessage(id).getPerson1().isLinked(getMessage(id).getPerson2())) {
                throw new MyRelationNotFoundException(
                        getMessage(id).getPerson1().getId(), getMessage(id).getPerson2().getId());
            }
            if (getMessage(id).getPerson1() != getMessage(id).getPerson2()) {
                getMessage(id).getPerson1().addSocialValue(getMessage(id).getSocialValue());
                getMessage(id).getPerson2().addSocialValue(getMessage(id).getSocialValue());
                ((MyPerson) getMessage(id).getPerson2()).addFirMessages(getMessage(id));
                messages.remove(getMessage(id));
            }
        }
        else {
            if (!(getMessage(id).getGroup().hasPerson(getMessage(id).getPerson1()))) {
                throw new MyPersonIdNotFoundException(getMessage(id).getPerson1().getId());
            }
            int toAddSocialValue = getMessage(id).getSocialValue();
            for (Person person : ((MyGroup) getMessage(id).getGroup()).getPeople()) {
                person.addSocialValue(toAddSocialValue);
            }
            messages.remove(getMessage(id));
        }
    }

    public int querySocialValue(int id) throws PersonIdNotFoundException {
        if (contains(id)) {
            return getPerson(id).getSocialValue();
        }
        else {
            throw new MyPersonIdNotFoundException(id);
        }
    }

    public List<Message> queryReceivedMessages(int id) throws PersonIdNotFoundException {
        if (contains(id)) {
            return getPerson(id).getReceivedMessages();
        }
        else {
            throw new MyPersonIdNotFoundException(id);
        }
    }
}
