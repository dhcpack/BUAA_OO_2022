package myinstance.main;

import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Person;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class MyPerson implements Person {

    private final int id;
    private final String name;
    private final int age;
    private Integer valueSum;
    private final HashMap<Integer, Person> acquaintance;
    private final HashMap<Integer, Integer> value;
    private LinkedList<Message> messages;
    private final LinkedList<Message> receivedMessages;
    private int money;
    private int socialValue;

    /* TODO  message */

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.valueSum = 0;
        this.acquaintance = new HashMap<>();
        this.value = new HashMap<>();
        this.messages = new LinkedList<>();
        this.receivedMessages = new LinkedList<>();
    }

    public Integer getValueSum() {
        return valueSum;
    }

    public void addAcquaintance(Person person, int value) {
        acquaintance.put(person.getId(), person);
        this.value.put(person.getId(), value);
        valueSum += value;
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
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof Person)) {
            return false;
        } else {
            return (id == ((Person) obj).getId());
        }
    }

    @Override
    public boolean isLinked(Person person) {
        if (person.getId() == id) {
            return true;
        } else {
            return acquaintance.containsKey(person.getId());
        }
    }

    @Override
    public int queryValue(Person person) {
        if (acquaintance.containsKey(person.getId())) {
            return value.get(person.getId());
        } else {
            return 0;
        }
    }

    @Override
    public int compareTo(Person p2) {
        return name.compareTo(p2.getName());
    }

    @Override
    public void addSocialValue(int num) {
        socialValue = socialValue + num;
    }

    @Override
    public int getSocialValue() {
        return socialValue;
    }

    @Override
    public List<Message> getMessages() {
        LinkedList<Message> messages = new LinkedList<>(this.messages);
        // ArrayList<Message> messages = (ArrayList<Message>) messagesReceived.clone();
        // ArrayList<MyMessage> messagess = (ArrayList<MyMessage>) messagesReceived.clone();
        return messages;
    }


    /* TODO */
    @Override
    public List<Message> getReceivedMessages() {
        receivedMessages.clear();
        for (int i = 0; i < 4 && i < messages.size(); i++) {
            receivedMessages.addLast(messages.get(i));
        }
        return receivedMessages;
    }

    public void receiveMessage(Message message) {
        messages.addFirst(message);
        /*if (receivedMessages.size() < 4) {
            receivedMessages.addFirst(message);
        } else {
            receivedMessages.removeLast();
            receivedMessages.addFirst(message);
        }*/
    }

    @Override
    public void addMoney(int num) {
        money = money + num;
    }

    @Override
    public int getMoney() {
        return money;
    }

    public void deleteNotices() {
        LinkedList<Message> tmpMessage = new LinkedList<>(messages);
        for (Message message : messages) {
            if (message instanceof MyNoticeMessage) {
                tmpMessage.remove(message);
            }
        }
        messages = new LinkedList<>(tmpMessage);
    }

    public void deleteMoney(int num) {
        this.money = this.money - num;
        return;
    }
}
