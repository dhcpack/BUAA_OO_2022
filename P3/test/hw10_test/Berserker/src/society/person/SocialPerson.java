package society.person;

import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Person;
import society.graph.Relation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SocialPerson implements Person {
    private int id;
    private String name;
    private int age;
    private HashMap<Integer, Relation> acquaintance;
    private int money;
    private int socialValue;
    private ArrayList<Message> messages;

    public SocialPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.acquaintance = new HashMap<>();
        this.money = 0;
        this.socialValue = 0;
        this.messages = new ArrayList<>();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Person && ((Person) obj).getId() == id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getAge() {
        return age;
    }

    @Override
    public boolean isLinked(Person person) {
        return id == person.getId() || acquaintance.containsKey(person.getId());
    }

    @Override
    public int queryValue(Person person) {
        if (acquaintance.containsKey(person.getId())) {
            return acquaintance.get(person.getId()).getValue();
        }
        return 0;
    }

    @Override
    public int compareTo(Person p2) {
        return name.compareTo(p2.getName());
    }

    public void addRelation(Person person, int value) {
        acquaintance.put(person.getId(), new Relation(value, person));
    }

    @Override
    public void addSocialValue(int num) {
        socialValue += num;
    }

    @Override
    public int getSocialValue() {
        return socialValue;
    }

    @Override
    public List<Message> getMessages() {
        return messages;
    }

    @Override
    public List<Message> getReceivedMessages() {
        if (messages.size() < 4) {
            return messages;
        }
        return messages.subList(0, 4);
    }

    @Override
    public void addMoney(int num) {
        money += num;
    }

    @Override
    public int getMoney() {
        return money;
    }

    public HashMap<Integer, Relation> getAcquaintance() {
        return acquaintance;
    }
}
