package mycode;

import com.oocourse.spec1.exceptions.EqualGroupIdException;
import com.oocourse.spec1.exceptions.EqualPersonIdException;
import com.oocourse.spec1.exceptions.GroupIdNotFoundException;
import com.oocourse.spec1.exceptions.PersonIdNotFoundException;
import com.oocourse.spec1.exceptions.RelationNotFoundException;
import com.oocourse.spec1.main.Group;
import com.oocourse.spec1.main.Network;
import com.oocourse.spec1.main.Person;

import java.util.HashMap;

public class MyNetwork implements Network {
    private HashMap<Integer, Person> personMap;
    private HashMap<Integer, Integer> netMap;
    private HashMap<Integer, Group> groupMap;
    private int blockSum;

    public MyNetwork() {
        personMap = new HashMap<>();
        netMap = new HashMap<>();
        groupMap = new HashMap<>();
        blockSum = 0;
    }

    public boolean contains(int id) {
        return personMap.containsKey(id);
    }

    public Person getPerson(int id) {
        if (this.contains(id)) {
            return personMap.get(id);
        } else {
            return null;
        }
    }

    public void addPerson(Person person) throws EqualPersonIdException {
        if (personMap.containsKey(person.getId())) {
            throw new MyEqualPersonIdException(person.getId());
        } else {
            personMap.put(person.getId(), person);
            netMap.put(person.getId(), person.getId());
            blockSum++;
        }
    }

    public void addRelation(int id1, int id2, int value) throws
            PersonIdNotFoundException, MyEqualRelationException {
        if (this.contains(id1) && this.contains(id2)
                && !this.getPerson(id1).isLinked(getPerson(id2))) {
            ((MyPerson) (personMap.get(id1))).addAcc(personMap.get(id2), value);
            ((MyPerson) (personMap.get(id2))).addAcc(personMap.get(id1), value);
            if (getUlti(id1) != getUlti(id2)) {
                blockSum--;
            }
            netMap.replace(getUlti(id1), getUlti(id2));
            for (int i : groupMap.keySet()) {
                ((MyGroup) (groupMap.get(i))).addValueSum(id1, id2, value);
            }
        } else if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyEqualRelationException(id1, id2);
        } else {
            return;
        }
    }

    public int queryValue(int id1, int id2) throws
            PersonIdNotFoundException, RelationNotFoundException {
        if (this.contains(id1) && this.contains(id2) &&
                getPerson(id1).isLinked(getPerson(id2))) {
            return getPerson(id1).queryValue(getPerson(id2));
        } else if (!this.contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!this.contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (!this.getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyRelationNotFoundException(id1, id2);
        } else {
            return 0;
        }
    }

    public int queryPeopleSum() {
        return personMap.size();
    }

    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        if (this.contains(id1) && this.contains(id2)) {
            return getUlti(id1) == getUlti(id2);
        } else if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else {
            return false;
        }
    }

    public int getUlti(int id) {
        if (netMap.get(id) == id) {
            return id;
        } else {
            int tmp = getUlti(netMap.get(id));
            return tmp;
        }
    }

    public int queryBlockSum() {
        return blockSum;
    }

    public void addGroup(Group group) throws EqualGroupIdException {
        //        System.out.println("group id" + group.getId());
        if (groupMap.containsKey(group.getId())) {
            throw new MyEqualGroupIdException(group.getId());
        } else {
            groupMap.put(group.getId(), group);
        }
    }

    public Group getGroup(int id) {
        if (groupMap.containsKey(id)) {
            return groupMap.get(id);
        } else {
            return null;
        }
    }

    public void addToGroup(int id1, int id2) throws GroupIdNotFoundException,
            PersonIdNotFoundException, EqualPersonIdException {
        if (groupMap.containsKey(id2) && personMap.containsKey(id1) &&
                getGroup(id2).hasPerson(getPerson(id1)) == false &&
                getGroup(id2).getSize() < 1111) {
            groupMap.get(id2).addPerson(getPerson(id1));
        } else if (!groupMap.containsKey(id2)) {
            throw new MyGroupIdNotFoundException(id2);
        } else if (!personMap.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (getGroup(id2).hasPerson(getPerson(id1))) {
            throw new MyEqualPersonIdException(id1);
        }
    }

    public void delFromGroup(int id1, int id2) throws GroupIdNotFoundException,
            PersonIdNotFoundException, EqualPersonIdException {
        if (groupMap.containsKey(id2) && personMap.containsKey(id1) &&
                getGroup(id2).hasPerson(getPerson(id1)) == true) {
            getGroup(id2).delPerson(getPerson(id1));
        } else if (!groupMap.containsKey(id2)) {
            throw new MyGroupIdNotFoundException(id2);
        } else if (!personMap.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else {
            throw new MyEqualPersonIdException(id1);
        }
    }
}
