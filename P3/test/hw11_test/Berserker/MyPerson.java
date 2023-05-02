import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Person;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class MyPerson implements Person {
    private final int id;
    private final String name;
    private final int age;
    private final HashMap<Integer, Person> acquaintance = new HashMap<>();
    private final HashMap<Integer, Integer> value = new HashMap<>();
    private int money;
    private int socialValue;
    private LinkedList<Message> messages = new LinkedList<>();

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.socialValue = 0;
        this.money = 0;
    }

    public void addMessage(MyMessage message) {
        messages.addFirst(message);
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
        if (!(obj instanceof Person)) {
            return false;
        }
        return ((Person) obj).getId() == id;
    }

    public void addLink(Person person, int value) {
        acquaintance.put(person.getId(), person);
        this.value.put(person.getId(), value);
    }

    public void clearNotices() {
        LinkedList<Message> ms = (LinkedList<Message>) messages.clone();
        messages.clear();
        for (int i = 0; i < ms.size(); i++) {
            MyMessage m = (MyMessage) ms.get(i);
            if (m instanceof MyNoticeMessage) {
                continue;
            }
            messages.addLast(m);
        }
    }

    public HashMap<Integer, Person> getAcquaintance() {
        return acquaintance;
    }

    @Override
    public boolean isLinked(Person person) {
        if (person.getId() == id) {
            return true;
        }
        return acquaintance.get(person.getId()) != null;
    }

    @Override
    public int queryValue(Person person) {
        if (acquaintance.get(person.getId()) == null) {
            return 0;
        }
        return value.get(person.getId());
    }

    @Override
    public int compareTo(Person p2) {
        return getName().compareTo(p2.getName());
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
        LinkedList<Message> ret = new LinkedList<>();
        for (int i = 0; i < 4 && i < messages.size(); i++) {
            ret.addLast(messages.get(i));
        }
        return ret;
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
