import com.oocourse.spec1.exceptions.EqualPersonIdException;
import com.oocourse.spec1.exceptions.EqualRelationException;
import com.oocourse.spec1.exceptions.PersonIdNotFoundException;
import com.oocourse.spec1.exceptions.RelationNotFoundException;
import com.oocourse.spec1.exceptions.EqualGroupIdException;
import com.oocourse.spec1.exceptions.GroupIdNotFoundException;
import com.oocourse.spec1.main.Group;
import com.oocourse.spec1.main.Network;
import com.oocourse.spec1.main.Person;

import java.util.ArrayList;

public class MyNetwork implements Network {
    private final ArrayList<MyPerson> people;
    private final ArrayList<MyGroup> groups;
    
    public MyNetwork() {
        this.people = new ArrayList<>();
        this.groups = new ArrayList<>();
    }
    
    public boolean contains(int id) {
        for (Person person : people) {
            if (person.getId() == id) {
                return true;
            }
        }
        return false;
    }
    
    public Person getPerson(int id) {
        if (contains(id)) {
            for (Person person : people) {
                if (person.getId() == id) {
                    return person;
                }
            }
        }
        return null;
    }
    
    public MyPerson myGetPerson(int id) {
        for (MyPerson person : people) {
            if (person.getId() == id) {
                return person;
            }
        }
        return null;
    }
    
    public void addPerson(Person person) throws EqualPersonIdException {
        for (Person p : people) {
            if (p.equals(person)) {
                throw new MyEqualPersonIdException(p.getId());
            }
        }
        people.add((MyPerson) person);
    }
    
    public void addRelation(int id1, int id2, int value) throws
            PersonIdNotFoundException, EqualRelationException {
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyEqualRelationException(id1, id2);
        }
        myGetPerson(id1).setLinked(myGetPerson(id2), value);
        myGetPerson(id2).setLinked(myGetPerson(id1), value);
    }
    
    public int queryValue(int id1, int id2) throws
            PersonIdNotFoundException, RelationNotFoundException {
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (!getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyRelationNotFoundException(id1, id2);
        }
        return getPerson(id1).queryValue(getPerson(id2));
    }
    
    public int queryPeopleSum() {
        return people.size();
    }
    
    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        return myGetPerson(id1).isCircle(myGetPerson(id2));
    }
    
    public int queryBlockSum() {
        int sum = 0;
        for (int i = 0; i < people.size(); i++) {
            int flag = 0;
            for (int j = 0; j < i; j++) {
                try {
                    if (isCircle(people.get(i).getId(), people.get(j).getId())) {
                        flag = 1;
                        break;
                    }
                } catch (PersonIdNotFoundException e) {
                    e.print();
                }
            }
            if (flag == 0) {
                sum++;
            }
        }
        return sum;
    }
    
    public void addGroup(Group group) throws EqualGroupIdException {
        for (MyGroup g1 : groups) {
            if (g1.equals(group)) {
                throw new MyEqualGroupIdException(g1.getId());
            }
        }
        groups.add((MyGroup) group);
    }
    
    public Group getGroup(int id) {
        for (MyGroup g1 : groups) {
            if (g1.getId() == id) {
                return g1;
            }
        }
        return null;
    }
    
    public void addToGroup(int id1, int id2) throws GroupIdNotFoundException,
            PersonIdNotFoundException, EqualPersonIdException {
        boolean flag = false;
        for (MyGroup g2 : groups) {
            if (g2.getId() == id2) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            throw new MyGroupIdNotFoundException(id2);
        }
        flag = false;
        for (MyPerson p1 : people) {
            if (p1.getId() == id1) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (getGroup(id2).hasPerson(getPerson(id1))) {
            throw new MyEqualPersonIdException(id1);
        }
        if (getGroup(id2).getSize() < 1111) {
            getGroup(id2).addPerson(getPerson(id1));
        }
    }
    
    public void delFromGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        boolean flag = false;
        for (MyGroup g2 : groups) {
            if (g2.getId() == id2) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            throw new MyGroupIdNotFoundException(id2);
        }
        flag = false;
        for (MyPerson p1 : people) {
            if (p1.getId() == id1) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (!getGroup(id2).hasPerson(getPerson(id1))) {
            throw new MyEqualPersonIdException(id1);
        }
        getGroup(id2).delPerson(getPerson(id1));
    }
}
