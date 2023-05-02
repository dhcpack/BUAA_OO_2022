import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.NoticeMessage;
import com.oocourse.spec3.main.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyPerson implements Person {
    private final int id;
    private final String name;
    private final int age;
    private final HashMap<Integer, Integer> idToValue;

    private int money;
    private int socialValue;
    private final ArrayList<Message> messages;

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.idToValue = new HashMap<>();
        this.money = 0;
        this.socialValue = 0;
        this.messages = new ArrayList<>();
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
        if (obj != null && obj instanceof Person) {
            return (((Person) obj).getId() == id);
        }
        return false;
    }

    public void addLink(Integer personId, Integer value) {
        idToValue.put(personId, value);
    }

    public HashMap<Integer, Integer> getAcquaintance() {
        return idToValue;
    }

    @Override
    public boolean isLinked(Person person) {
        if (idToValue.containsKey(person.getId())) {
            return true;
        }
        return (id == person.getId());
    }

    @Override
    public int queryValue(Person person) {
        int personId = person.getId();
        if (idToValue.containsKey(personId)) {
            return idToValue.get(personId);
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

    @Override
    public List<Message> getReceivedMessages() {
        int len = Integer.min(4, messages.size());
        ArrayList<Message> newList = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            newList.add(messages.get(i));
        }
        //can we use subList here?
        return newList;
    }

    @Override
    public void addMoney(int num) {
        money += num;
    }

    @Override
    public int getMoney() {
        return money;
    }

    public void clearNotices() {
        ArrayList<Message> newList = new ArrayList<>();
        for (Message message : messages) {
            if (!(message instanceof NoticeMessage)) {
                newList.add(message);
            }
        }
        messages.clear();
        for (Message message : newList) {
            messages.add(message);
        }
    }
}
