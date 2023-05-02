package implement.entity;

import com.oocourse.spec1.exceptions.EqualPersonIdException;
import com.oocourse.spec1.exceptions.EqualRelationException;
import com.oocourse.spec1.exceptions.EqualGroupIdException;
import com.oocourse.spec1.exceptions.GroupIdNotFoundException;
import com.oocourse.spec1.exceptions.PersonIdNotFoundException;
import com.oocourse.spec1.exceptions.RelationNotFoundException;
import com.oocourse.spec1.main.Group;
import com.oocourse.spec1.main.Network;
import com.oocourse.spec1.main.Person;
import implement.myexceptions.MyEqualPersonIdException;
import implement.myexceptions.MyEqualRelationException;
import implement.myexceptions.MyEqualGroupIdException;
import implement.myexceptions.MyGroupIdNotFoundException;
import implement.myexceptions.MyPersonIdNotFoundException;
import implement.myexceptions.MyRelationNotFoundException;

import java.util.HashMap;

public class MyNetwork implements Network {
    private final HashMap<Integer, Person> people;
    private final HashMap<Integer, Group> groups;
    private final RelationController relationController
            = new RelationController();

    public MyNetwork() {
        this.people = new HashMap<>();
        this.groups = new HashMap<>();
    }

    public boolean contains(int id) {
        return people.containsKey(id);
    }

    public Person getPerson(int id) {
        return people.get(id);
    }

    @Override
    public void addPerson(Person person) throws EqualPersonIdException {
        if (people.containsKey(person.getId())) {
            throw new MyEqualPersonIdException(person.getId());
        }
        else {
            people.put(person.getId(), person);
            relationController.addPerson(person.getId());
        }
    }

    public void addRelation(int id1, int id2, int value)
            throws PersonIdNotFoundException, EqualRelationException {
        // exceptional_behavior
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        else if (contains(id1) && !contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        else if (getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyEqualRelationException(id1, id2);
        }
        else {
            relationController.addRelation(id1, id2);
            ((MyPerson) getPerson(id1)).addAcquaintance(getPerson(id2), value);
            ((MyPerson) getPerson(id2)).addAcquaintance(getPerson(id1), value);
        }
    }

    public int queryValue(int id1, int id2) throws
            PersonIdNotFoundException, RelationNotFoundException {
        // exceptional_behavior
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        else if (contains(id1) && !contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        else if (!getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyRelationNotFoundException(id1, id2);
        }
        else {
            return getPerson(id1).queryValue(getPerson(id2));
        }
    }

    @Override
    public int queryPeopleSum() {
        return people.size();
    }

    public boolean isCircle(int id1, int id2) throws
            PersonIdNotFoundException {
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        else if (contains(id1) && !contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        else {
            return relationController.isCircle(id1, id2);
        }
    }

    public int queryBlockSum() {
        return relationController.queryBlockSum();
    }

    @Override
    public void addGroup(Group group) throws EqualGroupIdException {
        // exceptional_behavior
        if (groups.containsKey(group.getId())) {
            throw new MyEqualGroupIdException(group.getId());
        }
        groups.put(group.getId(), group);
    }

    @Override
    public Group getGroup(int id) {
        // 流式查询
        return groups.get(id);
    }

    @Override
    public void addToGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        if (getGroup(id2) == null) {
            throw new MyGroupIdNotFoundException(id2);
        }
        else if (getPerson(id1) == null) {
            throw new MyPersonIdNotFoundException(id1);
        }
        else if (getGroup(id2).hasPerson(getPerson(id1))) {
            throw new MyEqualPersonIdException(id1);
        }
        else {
            getGroup(id2).addPerson(getPerson(id1));
        }
    }

    @Override
    public void delFromGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        if (getGroup(id2) == null) {
            throw new MyGroupIdNotFoundException(id2);
        }
        else if (getPerson(id1) == null) {
            throw new MyPersonIdNotFoundException(id1);
        }
        else if (!getGroup(id2).hasPerson(getPerson(id1))) {
            throw new MyEqualPersonIdException(id1);
        }
        else {
            getGroup(id2).delPerson(getPerson(id1));
        }
    }
}
