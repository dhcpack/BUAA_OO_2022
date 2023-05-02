import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class MyPerson implements Person {
    private final int id;
    private final String name;
    private final int age;
    private int money;
    private int socialValue;
    private int father;
    private int dst;
    private final ArrayList<Message> messages2;
    private final HashSet<Integer> acquaintanceId;
    private final HashMap<Integer, Integer> idToValue;
    private final HashSet<Message> messages;
    private final HashSet<MyPerson> acquaintance;
    private final HashSet<Integer> value;

    public int getDst() {
        return dst;
    }

    public void setDst(int dst) {
        this.dst = dst;
    }

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;

        acquaintance = new HashSet<>();
        value = new HashSet<>();
        messages2 = new ArrayList<>();
        messages = new HashSet<>();
        acquaintanceId = new HashSet<>();
        idToValue = new HashMap<>();

        this.father = id;
    }

    public HashSet<MyPerson> getAcquaintance() {
        return acquaintance;
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
    public boolean isLinked(Person person) {
        if (person.getId() == id || acquaintanceId.contains(person.getId())) {
            return true;
        }
        return false;
    }

    @Override
    public int queryValue(Person person) {
        if (acquaintanceId.contains(person.getId())) {
            return idToValue.get(person.getId());
        }
        return 0;
    }

    public void link(Person person, int myValue) {
        acquaintance.add((MyPerson) person);
        value.add(myValue);
        acquaintanceId.add(person.getId());
        idToValue.put(person.getId(), myValue);
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
    public ArrayList<Message> getMessages() {
        return messages2;
    }

    public HashMap<Integer, Integer> getIdToValue() {
        return idToValue;
    }

    @Override
    public ArrayList<Message> getReceivedMessages() {
        if (messages.size() < 4) {
            return messages2;
        } else {
            int i;
            ArrayList<Message> temp = new ArrayList<>();
            for (i = 0; i < 4; i++) {
                temp.add(messages2.get(i));
            }
            return temp;
        }
    }

    @Override
    public void addMoney(int num) {
        money += num;
    }

    @Override
    public int getMoney() {
        return money;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MyPerson)) {
            return false;
        }
        MyPerson myPerson = (MyPerson) o;
        return getId() == myPerson.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public int getFather() {
        return father;
    }

    public void setFather(int father) {
        this.father = father;
    }

    public void setSocialValue(int socialValue) {
        this.socialValue = socialValue;
    }

    public void addMessage(Message message) {
        messages.add(message);
        messages2.add(message);
    }
}