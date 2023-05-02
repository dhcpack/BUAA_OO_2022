package myinstance.main;

import com.oocourse.spec1.exceptions.EqualPersonIdException;
import com.oocourse.spec1.exceptions.EqualRelationException;
import com.oocourse.spec1.exceptions.PersonIdNotFoundException;
import com.oocourse.spec1.exceptions.RelationNotFoundException;
import com.oocourse.spec1.exceptions.EqualGroupIdException;
import com.oocourse.spec1.exceptions.GroupIdNotFoundException;

import com.oocourse.spec1.main.Group;
import com.oocourse.spec1.main.Network;
import com.oocourse.spec1.main.Person;
import myinstance.exceptions.MyEqualPersonIdException;
import myinstance.exceptions.MyEqualRelationException;
import myinstance.exceptions.MyPersonIdNotFoundException;
import myinstance.exceptions.MyRelationNotFoundException;
import myinstance.exceptions.MyGroupIdNotFoundException;
import myinstance.exceptions.MyEqualGroupIdException;

import java.util.ArrayList;
import java.util.HashMap;

public class MyNetwork implements Network {
    //private ArrayList<Group> groups;
    private final ArrayList<MyPerson> peopleList;
    private final HashMap<Integer, MyPerson> peopleMap;
    private final HashMap<Integer, Group> groups;
    private final Dsu dsu;

    public MyNetwork() {
        peopleList = new ArrayList<>();
        peopleMap = new HashMap<>();
        groups = new HashMap<>();
        dsu = new Dsu();
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
            /* TODO */
        }
        // return false;
    }

    @Override
    public int queryBlockSum() {
        return dsu.queryBlockSum();
        /*int sum = 0;
        boolean hasRelation = false;
        for (int i = 0; i < peopleList.size(); i++) {
            MyPerson personI = peopleList.get(i);
            for (int j = 0; j < i; j++) {
                MyPerson personJ = peopleList.get(j);
                try {
                    if (isCircle(personI.getId(), personJ.getId())) {
                        hasRelation = true;
                        // sum++;
                        break;
                    }
                } catch (PersonIdNotFoundException e) {
                    e.printStackTrace();
                }
            }
            if (!hasRelation) {
                sum++;
            }
        }
        return sum;*/
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
        /*if (!groups.containsKey(id)) {
            return null;
        }else {
            return groups.get(id);
        }*/
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
}
