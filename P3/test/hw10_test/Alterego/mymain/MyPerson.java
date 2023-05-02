package mymain;

import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Person;

import java.util.ArrayList;
import java.util.List;

public class MyPerson implements Person {
    private int id;
    private String name;
    private int age;
    private ArrayList<Person> acquaintance;
    private ArrayList<Integer> value;
    private int money;
    private int socialValue;
    private ArrayList<Message> messages;

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

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public ArrayList<Person> getAcquaintance() {
        return acquaintance;
    }

    public ArrayList<Integer> getValue() {
        return value;
    }

    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Person) {
            return ((Person) obj).getId() == id;
        }
        else {
            return false;
        }
    }

    public boolean isLinked(Person person) {
        for (int i = 0; i < acquaintance.toArray().length; i += 1) {
            if (acquaintance.get(i).getId() == person.getId()) {
                return true;
            }
        }
        return person.getId() == id;
    }

    public int queryValue(Person person) {
        for (int i = 0; i < acquaintance.toArray().length; i += 1) {
            if (acquaintance.get(i).getId() == person.getId()) {
                return value.get(i);
            }
        }
        return 0;
    }

    public int compareTo(Person p2) {
        return name.compareTo((p2.getName()));
    }

    public void addSocialValue(int num) {
        socialValue = socialValue + num;
    }

    public int getSocialValue() {
        return socialValue;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void addFirMessages(Message message) {
        messages.add(0, message);
    }

    public List<Message> getReceivedMessages() {
        ArrayList<Message> result = new ArrayList<>();
        for (int i = 0; i < messages.toArray().length && i <= 3; i += 1) {
            result.add(messages.get(i));
        }
        return result;
    }

    public void addMoney(int num) {
        money += num;
    }

    public int getMoney() {
        return money;
    }

    public void addRelation(Person person, int w) {
        acquaintance.add(person);
        value.add(w);
    }
}
