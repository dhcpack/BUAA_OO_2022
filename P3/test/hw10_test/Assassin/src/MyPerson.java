import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class MyPerson implements Person {
    private int id;
    private String name;
    private int age;
    private HashMap<Integer, Person> acquaintance;
    private HashMap<Integer, Integer> value;
    private int money;
    private int socialValue;
    private LinkedList<Message> messages;

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        acquaintance = new HashMap<>();
        value = new HashMap<>();
        this.money = 0;
        this.socialValue = 0;
        this.messages = new LinkedList<>();
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

    public HashMap<Integer, Integer> getValues() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MyPerson) {
            return ((Person) obj).getId() == id;
        }
        return false;
    }

    @Override
    public boolean isLinked(Person person) {
        if (id == person.getId()) {
            //自己和自己
            return true;
        }
        return acquaintance.containsKey(person.getId());
    }

    @Override
    public int queryValue(Person person) {
        if (value.containsKey(person.getId())) {
            return value.get(person.getId());
        }
        return 0;
    }

    @Override
    public int compareTo(Person p2) {
        return name.compareTo(p2.getName());
    }

    public void link(Person person, int value) {
        acquaintance.put(person.getId(), person);
        this.value.put(person.getId(), value);
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
    public LinkedList<Message> getMessages() {
        return messages;//TODO 拷贝还是引用？本次用不到？
    }

    @Override
    public List<Message> getReceivedMessages() {
        ArrayList<Message> ans = new ArrayList<>();
        if (messages.size() <= 4) {
            ans.addAll(messages);
        } else {
            for (int i = 0; i < 4; i++) {
                ans.add(messages.get(i));
            }
        }
        return ans;
    }

    @Override
    public void addMoney(int num) {
        this.money += num;
    }

    @Override
    public int getMoney() {
        return money;
    }

    public void addMessage(Message message) {
        this.messages.addFirst(message);
    }
}
