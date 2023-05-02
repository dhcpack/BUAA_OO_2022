import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.NoticeMessage;
import com.oocourse.spec3.main.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class MyPerson implements Person {
    private final int id;
    private final String name;
    private final int age;
    private int money = 0;
    private int socialValue = 0;
    // personID socialValue
    private final HashMap<Integer, Integer> acquaintanceValueMap = new HashMap<>();
    private final List<Message> messages = new ArrayList<>();

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
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

    public boolean isLinked(Person person) {
        return person.getId() == id || acquaintanceValueMap.containsKey(person.getId());
    }

    public int queryValue(Person person) {
        if (acquaintanceValueMap.containsKey(person.getId())) {
            return acquaintanceValueMap.get(person.getId());
        }
        return 0;
    }

    public int compareTo(Person p2) {
        return name.compareTo(p2.getName());
    }

    public void addAcquaintance(Person person, int value) {
        acquaintanceValueMap.put(person.getId(), value);
    }

    public void addSocialValue(int num) {
        this.socialValue += num;
    }

    public int getSocialValue() {
        return this.socialValue;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public List<Message> getReceivedMessages() {
        List<Message> res = new ArrayList<>();
        for (int i = 0; i < messages.size() && i <= 3; i++) {
            res.add(messages.get(i));
        }
        return res;
    }

    public void addMessage(Message message) {
        messages.add(0, message);
    }

    public void addMoney(int num) {
        money += num;
    }

    public int getMoney() {
        return money;
    }

    public void clearNotices() {
        messages.removeIf(new Predicate<Message>() {
            @Override
            public boolean test(Message message) {
                return message instanceof NoticeMessage;
            }
        });
    }

    public HashMap<Integer, Integer> getAcquaintanceValueMap() {
        return acquaintanceValueMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MyPerson person = (MyPerson) o;
        return id == person.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
