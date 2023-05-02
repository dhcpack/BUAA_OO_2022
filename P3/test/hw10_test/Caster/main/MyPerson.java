package main;

import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Person;

import java.util.ArrayList;
import java.util.List;

public class MyPerson implements Person {
    private final int id;
    private final String name;
    private final int age;
    private final ArrayList<Person> acquaintance;
    private final ArrayList<Integer> value;
    private int money;
    private int socialValue;
    private final ArrayList<Message> messages;

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        acquaintance = new ArrayList<>();
        value = new ArrayList<>();
        messages = new ArrayList<>();
        money = 0;
        socialValue = 0;
    }

    public void setAcquaintanceAndValue(Person person, int value1) {
        acquaintance.add(person);
        value.add(value1);
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

    public boolean equals(Object obj) {
        if (obj instanceof Person) {
            return ((Person) obj).getId() == id;
        }
        return false;
    }

    @Override
    public boolean isLinked(Person person) {
        for (Person person1 : acquaintance) {
            if (person1.getId() == person.getId()) {
                return true;
            }
        }
        return person.getId() == id;
    }

    @Override
    public int queryValue(Person person) {
        boolean isFamiliar = false;
        int temp = 0;
        int size = acquaintance.size();
        for (int i = 0; i < size; i++) {
            if (acquaintance.get(i).getId() == person.getId()) {
                isFamiliar = true;
                temp = i;
            }
        }
        if (isFamiliar) {
            return value.get(temp);
        }
        return 0;
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
        return messages;
    }

    public void addMessage(Message message) {
        messages.add(0, message);
    }

    @Override
    public List<Message> getReceivedMessages() {
        ArrayList<Message> msg = new ArrayList<>();
        for (int i = 0; i < messages.size() && i <= 3; i++) {
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
        return money;
    }
}
