package my.main;

import com.oocourse.spec1.main.Group;
import com.oocourse.spec1.main.Person;
import java.util.HashSet;

public class MyGroup implements Group
{
    private final int id;
    private final HashSet<Person> people;

    public MyGroup(int id)
    {
        this.id = id;
        this.people = new HashSet<>();
    }

    @Override
    public int getId()
    {
        return this.id;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Group)
        {
            return ((Group) obj).getId() == this.id;
        }
        else
        {
            return false;
        }
    }

    @Override
    public void addPerson(Person person)
    {
        people.add(person);
    }

    @Override
    public boolean hasPerson(Person person)
    {
        return people.contains(person);
    }

    @Override
    public int getValueSum()
    {
        int valueSum = 0;
        for (Person person1 : people)
        {
            for (Person person2 : people)
            {
                valueSum += person1.queryValue(person2);
            }
        }
        return valueSum;
    }

    @Override
    public int getAgeMean()
    {
        int ageSum = 0;
        for (Person person : people)
        {
            ageSum += person.getAge();
        }
        return ageSum / people.size();
    }

    @Override
    public int getAgeVar()
    {
        int var = 0;
        int meanAge = getAgeMean();
        if (people.size() == 0)
        {
            return 0;
        }
        else
        {
            for (Person person : people)
            {
                var += (person.getAge() - meanAge) * (person.getAge() - meanAge);
            }

            return var;
        }

    }

    @Override
    public void delPerson(Person person)
    {
        people.remove(person);
    }

    @Override
    public int getSize()
    {
        return people.size();
    }
}
