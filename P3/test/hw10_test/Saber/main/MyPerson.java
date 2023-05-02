package main;

import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyPerson implements Person {
    private final int id;
    private final String name;
    private final int age;
    private final HashMap<Person, Integer> linkage = new HashMap<>();
    private int money = 0;
    private int socialValue = 0;
    private final ArrayList<Message> messages = new ArrayList<>();

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getAge() {
        return this.age;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Person)) {
            return false;
        }
        return ((Person) obj).getId() == id;
    }

    @Override
    public boolean isLinked(Person person) {
        return linkage.containsKey(person) || this.equals(person);
    }

    @Override
    public int queryValue(Person person) {
        if (linkage.containsKey(person)) {
            return linkage.get(person);
        }
        return 0;
    }

    @Override
    public int compareTo(Person p2) {
        return name.compareTo(p2.getName());
    }

    public void addLink(Person person, int value) {
        linkage.put(person, value);
    }

    @Override
    public void addSocialValue(int num) {
        socialValue += num;
    }

    @Override
    public int getSocialValue() {
        return this.socialValue;
    }

    @Override
    public List<Message> getMessages() {
        return this.messages;
    }

    public void addMessageHead(Message msg) {
        messages.add(0, msg);
    }

    // 最多取前4条信息
    @Override
    public List<Message> getReceivedMessages() {
        ArrayList<Message> msg = new ArrayList<>();
        int count = Math.min(messages.size(), 4);
        for (int i = 0; i < count; i++) {
            msg.add(messages.get(i));
        }
        return msg;
    }

    @Override
    public void addMoney(int num) {
        money += num;
    }

    @Override
    public int getMoney() {
        return this.money;
    }

    @Override
    public String toString() {
        return id + " " + name + " " + age;
    }
}
