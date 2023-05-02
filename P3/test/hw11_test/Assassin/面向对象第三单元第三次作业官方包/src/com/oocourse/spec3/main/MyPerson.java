package com.oocourse.spec3.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyPerson implements Person {
    private final int id;
    private final String name;
    private final int age;
    private HashMap<Person, Integer> acquaintanceValueMap;
    private int money;
    private int socialValue;
    private ArrayList<Message> messages;

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.money = 0;
        this.socialValue = 0;
        this.acquaintanceValueMap = new HashMap<>();
        this.messages = new ArrayList<>();
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getAge() {
        return this.age;
    }

    //根据id判断是否相等
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Person)) {
            return false;
        }
        return ((Person) obj).getId() == this.id;
    }

    public HashMap<Person, Integer> getAcquaintanceValueMap() {
        return acquaintanceValueMap;
    }

    //判断是否相连，所有点和自身相连
    public boolean isLinked(Person person) {
        if (person.getId() == this.id) {
            return true;
        }
        return acquaintanceValueMap.containsKey(person);
    }

    public int queryValue(Person person) {
        if (!acquaintanceValueMap.containsKey(person)) {
            return 0;
        }
        return acquaintanceValueMap.get(person);
    }

    public int compareTo(Person p2) {
        return this.name.compareTo(p2.getName());
    }

    @Override
    public void addSocialValue(int num) {
        this.socialValue += num;
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
            return messages.subList(0, messages.size());
        } else {
            return messages.subList(0, 4);
        }
    }

    @Override
    public void addMoney(int num) {
        this.money += num;
    }

    @Override
    public int getMoney() {
        return money;
    }

}
