import com.oocourse.spec2.exceptions.RelationNotFoundException;
import com.oocourse.spec2.exceptions.EqualMessageIdException;
import com.oocourse.spec2.exceptions.MessageIdNotFoundException;
import com.oocourse.spec2.exceptions.EqualPersonIdException;
import com.oocourse.spec2.exceptions.EqualGroupIdException;
import com.oocourse.spec2.exceptions.EqualRelationException;
import com.oocourse.spec2.exceptions.GroupIdNotFoundException;
import com.oocourse.spec2.exceptions.PersonIdNotFoundException;
import com.oocourse.spec2.main.Message;
import exceptions.MyRelationNotFoundException;
import exceptions.MyEqualPersonIdException;
import exceptions.MyEqualMessageIdException;
import exceptions.MyPersonIdNotFoundException;
import exceptions.MyMessageIdNotFoundException;
import exceptions.MyEqualGroupIdException;
import exceptions.MyEqualRelationException;
import exceptions.MyGroupIdNotFoundException;
import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Network;
import com.oocourse.spec2.main.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyNetwork implements Network {
    private HashMap<Integer, Person> people;
    private HashMap<Integer, Group> groups;
    private HashMap<Integer, Message> messages;
    private JoinSearchSet searchSet;
    //private MsTree tree;

    public MyNetwork() {
        people = new HashMap<>();
        groups = new HashMap<>();
        searchSet = new JoinSearchSet();
        this.messages = new HashMap<>();
        //tree = new MsTree(searchSet, people);
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
            throw new MyEqualPersonIdException(person.getId());
        }
        people.put(person.getId(), person);
        searchSet.add(person.getId());
    }

    @Override
    public void addRelation(int id1, int id2, int value) throws
            PersonIdNotFoundException, EqualRelationException {
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        if (getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyEqualRelationException(id1, id2);
        }
        Person person1 = getPerson(id1);
        Person person2 = getPerson(id2);
        if (person1 instanceof MyPerson) {
            MyPerson p1 = (MyPerson) person1;
            if (person2 instanceof MyPerson) {
                MyPerson p2 = (MyPerson) person2;
                p1.link(p2, value);
                p2.link(p1, value);
                searchSet.link(p1.getId(), p2.getId());
            }
        }
        //注意group增加relation
        for (Group group : this.groups.values()) {
            if (group.hasPerson(person1) && group.hasPerson(person2)) {
                ((MyGroup) group).addRelatioin(value);
            }
        }
    }

    @Override
    public int queryValue(int id1, int id2) throws
            PersonIdNotFoundException, RelationNotFoundException {
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
    public boolean isCircle(int id1, int id2) throws
            PersonIdNotFoundException {
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        return searchSet.hasLink(id1, id2);
    }

    @Override
    public int queryBlockSum() {
        return searchSet.size();
    }

    @Override
    public void addGroup(Group group) throws
            EqualGroupIdException {
        if (groups.containsKey(group.getId())) {
            throw new MyEqualGroupIdException(group.getId());
        }
        groups.put(group.getId(), group);
    }

    @Override
    public Group getGroup(int id) {
        return groups.get(id);
    }

    //把id1的人加入到id2的组中，如果组里人数超过1111了不加
    @Override
    public void addToGroup(int id1, int id2) throws
            GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        Group group = getGroup(id2);
        if (group == null) {
            throw new MyGroupIdNotFoundException(id2);
        }
        Person person = getPerson(id1);
        if (person == null) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (group.hasPerson(person)) {
            throw new MyEqualPersonIdException(person.getId());
        }
        if (group.getSize() < 1111) {
            group.addPerson(person);
        }
    }

    //把id1的人从id2的组里删了
    @Override
    public void delFromGroup(int id1, int id2) throws
            GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        Group group = getGroup(id2);
        if (group == null) {
            throw new MyGroupIdNotFoundException(id2);
        }
        Person person = getPerson(id1);
        if (person == null) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (!group.hasPerson(person)) {
            throw new MyEqualPersonIdException(id1);
        }
        group.delPerson(person);
    }

    @Override
    public boolean containsMessage(int id) {
        return messages.containsKey(id);
    }

    @Override
    public int queryLeastConnection(int id) throws PersonIdNotFoundException {
        Person person = this.people.get(id);
        if (person == null) {
            throw new MyPersonIdNotFoundException(id);
        }

        //优先队列，每次都得重排
        int father = this.searchSet.find(id);
        ArrayList<Person> people = new ArrayList<>();//所有和id有关联的节点
        for (Person person1 : this.people.values()) {
            if (isCircle(father, person1.getId())) {
                people.add(person1);
            }
        }
        MsTree tree = new MsTree(people);
        return tree.create();
    }

    @Override
    public int queryGroupPeopleSum(int id) throws GroupIdNotFoundException {
        Group group = getGroup(id);
        if (group == null) {
            throw new MyGroupIdNotFoundException(id);
        }
        return group.getSize();
    }

    @Override
    public int queryGroupValueSum(int id) throws GroupIdNotFoundException {
        Group group = getGroup(id);
        if (group == null) {
            throw new MyGroupIdNotFoundException(id);
        }
        return group.getValueSum();
    }

    @Override
    public int queryGroupAgeVar(int id) throws GroupIdNotFoundException {
        Group group = getGroup(id);
        if (group == null) {
            throw new MyGroupIdNotFoundException(id);
        }
        return group.getAgeVar();
    }

    @Override
    public void addMessage(Message message) throws EqualMessageIdException, EqualPersonIdException {
        int id = message.getId();
        Message mes = messages.get(id);
        if (mes != null) {
            throw new MyEqualMessageIdException(message.getId());
        }
        if (message.getType() == 0 && message.getPerson1() == message.getPerson2()) {
            throw new MyEqualPersonIdException(message.getPerson1().getId());
        }
        this.messages.put(id, message);
        //TODO 也许还有些要做的
    }

    @Override
    public Message getMessage(int id) {
        return messages.get(id);
    }

    @Override
    public void sendMessage(int id) throws
            RelationNotFoundException, MessageIdNotFoundException, PersonIdNotFoundException {
        Message message = messages.get(id);
        if (message == null) {
            throw new MyMessageIdNotFoundException(id);
        }
        int type = message.getType();
        Person person1 = message.getPerson1();
        Person person2 = message.getPerson2();
        if (type == 0) {
            if (!person1.isLinked(person2)) {
                throw new MyRelationNotFoundException(person1.getId(), person2.getId());
            }
            if (!person1.equals(person2)) {
                //实际功能，私聊
                person1.addSocialValue(message.getSocialValue());
                person2.addSocialValue(message.getSocialValue());
                ((MyPerson) person2).addMessage(message);
                this.messages.remove(id);
            }
        }
        if (type == 1) {
            Group group = message.getGroup();
            if (!group.hasPerson(person1)) {
                throw new MyPersonIdNotFoundException(person1.getId());
            }
            //实际功能，group内的广播
            ((MyGroup) group).addSocialValue(message.getSocialValue());
            //最后删除该message
            this.messages.remove(id);
        }
    }

    @Override
    public int querySocialValue(int id) throws PersonIdNotFoundException {
        Person person = people.get(id);
        if (person == null) {
            throw new MyPersonIdNotFoundException(id);
        }
        return person.getSocialValue();
    }

    @Override
    public List<Message> queryReceivedMessages(int id) throws PersonIdNotFoundException {
        Person person = people.get(id);
        if (person == null) {
            throw new MyPersonIdNotFoundException(id);
        }
        return person.getReceivedMessages();
    }
}
