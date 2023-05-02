import com.oocourse.spec1.exceptions.EqualGroupIdException;
import com.oocourse.spec1.exceptions.EqualPersonIdException;
import com.oocourse.spec1.exceptions.EqualRelationException;
import com.oocourse.spec1.exceptions.GroupIdNotFoundException;
import com.oocourse.spec1.exceptions.PersonIdNotFoundException;
import com.oocourse.spec1.exceptions.RelationNotFoundException;
import com.oocourse.spec1.main.Group;
import com.oocourse.spec1.main.Network;
import com.oocourse.spec1.main.Person;

import java.util.HashMap;
import java.util.HashSet;

public class MyNetwork implements Network {
    private final HashMap<Integer, MyPerson> people = new HashMap<>();
    private final HashMap<Integer, MyGroup> groups = new HashMap<>();
    private final HashMap<Integer, Integer> father = new HashMap<>();  // key: personId  value: root

    public boolean contains(int id) {
        return people.containsKey(id);
    }

    public Person getPerson(int id) {
        if (contains(id)) {
            return people.get(id);
        }
        return null;
    }

    public void addPerson(Person person) throws EqualPersonIdException {
        if (contains(person.getId())) {
            throw new MyEqualPersonIdException(person.getId());
        }
        people.put(person.getId(), (MyPerson) person);
        father.put(person.getId(), person.getId());
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
        addToFather(id1, id2);
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
        return findFather(id1) == findFather(id2);
    }

    public int queryBlockSum() {
        HashSet<Integer> roots = new HashSet<>();
        for (Integer id : people.keySet()) {
            roots.add(findFather(id));
        }
        return roots.size();
    }

    public void addGroup(Group group) throws EqualGroupIdException {
        if (groups.containsKey(group.getId())) {
            throw new MyEqualGroupIdException(group.getId());
        }
        groups.put(group.getId(), (MyGroup) group);
    }

    public Group getGroup(int id) {
        if (groups.containsKey(id)) {
            return groups.get(id);
        }
        return null;
    }

    public void addToGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException,
            EqualPersonIdException {
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

    public void delFromGroup(int id1, int id2)
            throws MyGroupIdNotFoundException, MyPersonIdNotFoundException,
            MyEqualPersonIdException {
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

    private void addToFather(int id1, int id2) {
        if (findFather(id1) == findFather(id2)) {
            return;
        }
        father.put(findFather(id1), findFather(id2));
    }

    private int findFather(int id) {
        if (id == father.get(id)) {  // root
            return id;
        } else {
            father.put(id, findFather(father.get(id)));
            return father.get(id);
        }
    }
}
