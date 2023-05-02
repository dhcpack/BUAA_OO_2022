package my.main;

import com.oocourse.spec1.main.Person;

import java.util.HashMap;
import java.util.HashSet;

public class MyPerson implements Person
{
    private final int id;
    private final String name;
    private final int age;
    private final HashMap<Integer, Person> acquaintance;
    private final HashMap<Person, Integer> value;

    // 这里初始化的很神奇
    public MyPerson(int id, String name, int age)
    {
        this.id = id;
        this.name = name;
        this.age = age;
        this.acquaintance = new HashMap<>();
        this.value = new HashMap<>();
    }

    @Override
    public int getId()
    {
        return this.id;
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public int getAge()
    {
        return this.age;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Person)
        {
            return ((Person) obj).getId() == this.id;
        }
        else
        {
            return false;
        }
    }

    // 这里是不是可以不用数组存储，而是用一个其他结构存储（HashMap），这样查询的会快一些
    @Override
    public boolean isLinked(Person person)
    {
        return this.id == person.getId() || acquaintance.containsKey(person.getId());
    }

    @Override
    public int queryValue(Person person)
    {
        if (acquaintance.containsKey(person.getId()))
        {
            return value.get(acquaintance.get(person.getId()));
        }
        return 0;
    }

    @Override
    public int compareTo(Person p2)
    {
        return this.name.compareTo(p2.getName());
    }

    public void link(Person person, int value)
    {
        this.acquaintance.put(person.getId(), person);
        this.value.put(person, value);
    }

    public HashSet<Person> getAcquaintanceSet()
    {
        return new HashSet<>(acquaintance.values());
    }
}
