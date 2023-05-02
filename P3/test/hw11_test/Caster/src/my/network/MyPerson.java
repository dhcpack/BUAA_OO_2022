package my.network;

import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MyPerson implements Person {
    private final ArrayList<Person> acquaintance = new ArrayList<>();
    private final ArrayList<Integer> value = new ArrayList<>();
    private final int id;
    private final String name;
    private final int age;
    private final ArrayList<Message> messages = new ArrayList<>();
    private int money = 0;
    private int socialValue = 0;

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    @Override
    public ArrayList<Message> getMessages() {
        return messages;
    }

    @Override
    public List<Message> getReceivedMessages() {
        return messages.stream().limit(4).collect(Collectors.toList());
    }

    @Override
    public int getMoney() {
        return money;
    }

    @Override
    public int getSocialValue() {
        return socialValue;
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
    public boolean equals(Object obj) {
        if (obj instanceof Person) {
            return (((Person) obj).getId() == id);
        }
        return false;
    }

    @Override
    public boolean isLinked(Person person) {
        return this.equals(person) || knows(person)
                || ((MyPerson) person).knows(this);
    }

    @Override
    public int queryValue(Person person) {
        int pos = acquaintance.indexOf(person);
        if (pos != -1) {
            return value.get(pos);
        }
        return 0;
    }

    @Override
    public int compareTo(Person p2) {
        return name.compareTo(p2.getName());
    }

    @Override
    public void addMoney(int num) {
        money += num;
    }

    @Override
    public void addSocialValue(int num) {
        socialValue += num;
    }

    public void addAcquaint(Person person, int val) {
        acquaintance.add(person);
        value.add(val);
    }

    public boolean knows(Person person) {
        return acquaintance.contains(person);
    }

    public boolean learns(Message message) {
        return messages.contains(message);
    }

    public Iterable<Person> getAcquaint() {
        return acquaintance;
    }

    public Person get(int i) {
        return acquaintance.get(i);
    }

    public int getValue(int i) {
        return value.get(i);
    }

    public int size() {
        return acquaintance.size();
    }

    @Override
    public String toString() {
        return "MyPerson{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", money=" + money +
                ", socialValue=" + socialValue +
                '}';
    }
}
