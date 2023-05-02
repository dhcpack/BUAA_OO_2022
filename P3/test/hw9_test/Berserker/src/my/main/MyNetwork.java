package my.main;

import com.oocourse.spec1.exceptions.EqualGroupIdException;
import com.oocourse.spec1.exceptions.EqualPersonIdException;
import com.oocourse.spec1.exceptions.EqualRelationException;
import com.oocourse.spec1.exceptions.GroupIdNotFoundException;
import com.oocourse.spec1.exceptions.PersonIdNotFoundException;
import com.oocourse.spec1.exceptions.RelationNotFoundException;
import com.oocourse.spec1.main.Group;
import com.oocourse.spec1.main.Network;
import com.oocourse.spec1.main.Person;
import my.exceptions.MyEqualGroupIdException;
import my.exceptions.MyEqualPersonIdException;
import my.exceptions.MyEqualRelationException;
import my.exceptions.MyGroupIdNotFoundException;
import my.exceptions.MyPersonIdNotFoundException;
import my.exceptions.MyRelationNotFoundException;

import java.util.HashMap;

public class MyNetwork implements Network
{
    private final HashMap<Integer, Person> people;
    private final HashMap<Integer, Group> groups;
    private DisjointSet<Person> disjointSet;

    public MyNetwork()
    {
        this.people = new HashMap<>();
        this.groups = new HashMap<>();
        this.disjointSet = new DisjointSet<>();
    }

    @Override
    public boolean contains(int id)
    {
        return people.containsKey(id);
    }

    @Override
    public Person getPerson(int id)
    {
        return people.get(id);
    }

    @Override
    public void addPerson(Person person) throws EqualPersonIdException
    {
        // 如果已经有这个人了,那么就要异常
        if (people.containsValue(person))
        {
            throw new MyEqualPersonIdException(person.getId());
        }
        else
        {
            people.put(person.getId(), person);
            disjointSet.addNode(person);
        }
    }

    @Override
    public void addRelation(int id1, int id2, int value)
            throws PersonIdNotFoundException, EqualRelationException
    {
        // 如果没有这个个人,那么就要异常
        if (!people.containsKey(id1))
        {
            throw new MyPersonIdNotFoundException(id1);
        }
        // 如果没有这个人,那么就要异常
        else if (!people.containsKey(id2))
        {
            throw new MyPersonIdNotFoundException(id2);
        }
        // 如果已经有这个关系了,那么就要异常
        else if (people.get(id1).isLinked(people.get(id2)))
        {
            throw new MyEqualRelationException(id1, id2);
        }
        else
        {
            ((MyPerson)people.get(id1)).link(people.get(id2), value);
            ((MyPerson)people.get(id2)).link(people.get(id1), value);
            disjointSet.addEdge(people.get(id1), people.get(id2));
        }
    }

    @Override
    public int queryValue(int id1, int id2)
            throws PersonIdNotFoundException, RelationNotFoundException
    {
        // 如果没有这个人,那么就要报异常
        if (!people.containsKey(id1))
        {
            throw new MyPersonIdNotFoundException(id1);
        }
        // 如果没有这个人,那么就要报异常
        else if (!people.containsKey(id2))
        {
            throw new MyPersonIdNotFoundException(id2);
        }
        // 如果这两个人没有关系,那么就要报异常
        else if (!people.get(id1).isLinked(people.get(id2)))
        {
            throw new MyRelationNotFoundException(id1, id2);
        }
        else
        {
            return people.get(id1).queryValue(people.get(id2));
        }
    }

    @Override
    public int queryPeopleSum()
    {
        return people.size();
    }

    @Override
    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException
    {
        if (!people.containsKey(id1))
        {
            throw new MyPersonIdNotFoundException(id1);
        }
        else if (!people.containsKey(id2))
        {
            throw new MyPersonIdNotFoundException(id2);
        }
        else
        {
            return disjointSet.isLinked(people.get(id1), people.get(id2));
        }
    }

    @Override
    public int queryBlockSum()
    {
        return disjointSet.getBlockSum();
    }

    @Override
    public void addGroup(Group group) throws EqualGroupIdException
    {
        if (groups.containsKey(group.getId()))
        {
            throw new MyEqualGroupIdException(group.getId());
        }
        else
        {
            groups.put(group.getId(), group);
        }
    }

    @Override
    public Group getGroup(int id)
    {
        return groups.get(id);
    }

    @Override
    public void addToGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException
    {
        if (!groups.containsKey(id2))
        {
            throw new MyGroupIdNotFoundException(id2);
        }
        else if (!people.containsKey(id1))
        {
            throw new MyPersonIdNotFoundException(id1);
        }
        else if (groups.get(id2).hasPerson(people.get(id1)))
        {
            throw new MyEqualPersonIdException(id1);
        }
        else if (groups.size() < 1111)
        {
            groups.get(id2).addPerson(people.get(id1));
        }
    }

    @Override
    public void delFromGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException
    {
        if (!groups.containsKey(id2))
        {
            throw new MyGroupIdNotFoundException(id2);
        }
        else if (!people.containsKey(id1))
        {
            throw new MyPersonIdNotFoundException(id1);
        }
        else if (!groups.get(id2).hasPerson(people.get(id1)))
        {
            throw new MyEqualPersonIdException(id1);
        }
        else
        {
            groups.get(id2).delPerson(people.get(id1));
        }
    }
}
