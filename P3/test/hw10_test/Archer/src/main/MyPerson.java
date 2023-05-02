package main;

import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Person;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class MyPerson implements Person {

    private final int id;
    private final String name;
    private final int age;
    private int money = 0;
    private int socialValue = 0;
    private final HashMap<Person, Integer> acquaintances = new HashMap<>();
    private final ArrayList<Message> messageArrayList = new ArrayList<>();

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
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
        if (obj == null || !(obj instanceof Person)) {
            return false;
        }
        return ((Person) obj).getId() == id;
    }

    @Override
    public boolean isLinked(Person person) {
        return acquaintances.containsKey(person) || person.getId() == id;
    }

    @Override
    public int queryValue(Person person) {
        // 如果this==person,isLinked会返回1，但是acquaintance会返回null
        return acquaintances.getOrDefault(person, 0);
    }

    @Override
    public int compareTo(Person p2) {
        return name.compareTo(p2.getName());
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
        return messageArrayList;
    }

    @Override
    public List<Message> getReceivedMessages() {
        int num = Math.min(4, messageArrayList.size());
        return messageArrayList.subList(0, num);
    }

    @Override
    public void addMoney(int num) {
        money += num;
    }

    @Override
    public int getMoney() {
        return money;
    }

    public void addAcquaintance(Person person, int value) {
        acquaintances.put(person, value);
    }

    public void addMessage(Message message) {
        messageArrayList.add(0, message);
    }

}
