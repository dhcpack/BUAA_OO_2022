package com.oocourse.spec1.mymain;

import com.oocourse.spec1.exceptions.RelationNotFoundException;
import com.oocourse.spec1.exceptions.GroupIdNotFoundException;
import com.oocourse.spec1.exceptions.EqualRelationException;
import com.oocourse.spec1.exceptions.EqualPersonIdException;
import com.oocourse.spec1.exceptions.PersonIdNotFoundException;
import com.oocourse.spec1.exceptions.EqualGroupIdException;
import com.oocourse.spec1.main.Group;
import com.oocourse.spec1.main.Network;
import com.oocourse.spec1.main.Person;
import com.oocourse.spec1.myexception.MyPersonIdNotFoundException;
import com.oocourse.spec1.myexception.MyEqualPersonIdException;
import com.oocourse.spec1.myexception.MyGroupIdNotFoundException;
import com.oocourse.spec1.myexception.MyEqualGroupIdException;
import com.oocourse.spec1.myexception.MyEqualRelationException;
import com.oocourse.spec1.myexception.MyRelationNotFoundException;

import java.util.ArrayList;
import java.util.HashSet;

public class MyNetwork implements Network {
    private ArrayList<Person> people;
    private ArrayList<Group> groups;
    private ArrayList<HashSet<Integer>> circles;

    public MyNetwork() {
        people = new ArrayList<>();
        groups = new ArrayList<>();
        circles = new ArrayList<>();
    }

    @Override
    public boolean contains(int id) {
        for (Person person : people) {
            if (person.getId() == id) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Person getPerson(int id) {
        for (Person person : people) {
            if (person.getId() == id) {
                return person;
            }
        }
        return null;
    }

    @Override
    public void addPerson(Person person) throws EqualPersonIdException {
        for (Person mine : people) {
            if (mine.equals(person)) {
                throw new MyEqualPersonIdException(person.getId());
            }
        }
        HashSet<Integer> circle = new HashSet<>();
        circle.add(person.getId());
        circles.add(circle);
        people.add(person);
    }

    @Override
    public void addRelation(int id1, int id2, int value)
            throws PersonIdNotFoundException, EqualRelationException {
        Person person1;
        Person person2;
        person1 = getPerson(id1);
        if (person1 == null) {
            throw new MyPersonIdNotFoundException(id1);
        }
        person2 = getPerson(id2);
        if (person2 == null) {
            throw new MyPersonIdNotFoundException(id2);
        }
        if (person1.isLinked(person2)) {
            throw new MyEqualRelationException(id1, id2);
        }
        ((MyPerson) person1).addPerson(person2);
        ((MyPerson) person2).addPerson(person1);
        ((MyPerson) person1).addValue(value);
        ((MyPerson) person2).addValue(value);
        HashSet<Integer> circle1 = null;
        HashSet<Integer> circle2 = null;
        for (HashSet<Integer> circle : circles) {
            if (circle.contains(person1.getId())) {
                circle1 = circle;
            }
            if (circle.contains(person2.getId())) {
                circle2 = circle;
            }
        }
        if (!circle1.equals(circle2)) {
            circle1.addAll(circle2);
            circles.remove(circle2);
        }
    }

    @Override
    public int queryValue(int id1, int id2)
            throws PersonIdNotFoundException, RelationNotFoundException {
        Person person1;
        Person person2;
        person1 = getPerson(id1);
        if (person1 == null) {
            throw new MyPersonIdNotFoundException(id1);
        }
        person2 = getPerson(id2);
        if (person2 == null) {
            throw new MyPersonIdNotFoundException(id2);
        }
        if (!person1.isLinked(person2)) {
            throw new MyRelationNotFoundException(id1, id2);
        }
        return person1.queryValue(person2);
    }

    @Override
    public int queryPeopleSum() {
        return people.size();
    }

    @Override
    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        Person person1;
        Person person2;
        person1 = getPerson(id1);
        if (person1 == null) {
            throw new MyPersonIdNotFoundException(id1);
        }
        person2 = getPerson(id2);
        if (person2 == null) {
            throw new MyPersonIdNotFoundException(id2);
        }
        for (HashSet<Integer> circle : circles) {
            if (circle.contains(id1)) {
                if (circle.contains(id2)) {
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    @Override
    public int queryBlockSum() {
        return circles.size();
    }

    @Override
    public void addGroup(Group group) throws EqualGroupIdException {
        for (Group mine : groups) {
            if (mine.equals(group)) {
                throw new MyEqualGroupIdException(group.getId());
            }
        }
        groups.add(group);
    }

    @Override
    public Group getGroup(int id) {
        for (Group group : groups) {
            if (group.getId() == id) {
                return group;
            }
        }
        return null;
    }

    @Override
    public void addToGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        Group group = getGroup(id2);
        if (group == null) {
            throw new MyGroupIdNotFoundException(id2);
        }
        Person person = getPerson(id1);
        if (person == null) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (group.hasPerson(person)) {
            throw new MyEqualPersonIdException(id1);
        }
        if (((MyGroup) group).peopleLen() < 1111) {
            group.addPerson(person);
        }
    }

    @Override
    public void delFromGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
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
}
