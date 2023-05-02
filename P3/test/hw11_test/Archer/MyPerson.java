import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Person;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class MyPerson implements Person {
    private int id;
    private String name;
    private int age;
    private HashMap<Integer, Person> acquaintance;
    private HashMap<Person, Integer> value;
    private int money;
    private int socialValue;
    private LinkedList<Message> messages;
    
    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.acquaintance = new HashMap<>();
        this.value = new HashMap<>();
        this.messages = new LinkedList<>();
        this.socialValue = 0;
        this.money = 0;
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
    
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (!(obj instanceof Person)) {
            return false;
        } else {
            return (((Person) obj).getId() == this.id);
        }
    }
    
    public boolean isLinked(Person person) {
        if (person.getId() == this.id) {
            return true;
        } else {
            return acquaintance.containsKey(person.getId());
        }
    }
    
    public void addRelation(Person person, Integer value) {
        this.acquaintance.put(person.getId(), person);
        this.value.put(person, value);
    }
    
    public int queryValue(Person person) {
        if (acquaintance.containsKey(person.getId())) {
            return value.get(acquaintance.get(person.getId()));
        }
        return 0;
    }
    
    public int compareTo(Person p2) {
        return this.name.compareTo(p2.getName());
    }
    
    public void addSocialValue(int num) {
        this.socialValue += num;
    }
    
    public int getSocialValue() {
        return this.socialValue;
    }
    
    public List<Message> getMessages() {
        return this.messages;
    }
    
    public List<Message> getReceivedMessages() {
        int size = Math.min(messages.size(), 4);
        LinkedList<Message> subMessages = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            subMessages.add(messages.get(i));
        }
        return subMessages;
    }
    
    public void addMoney(int num) {
        this.money += num;
    }
    
    public int getMoney() {
        return this.money;
    }
    
    public HashMap<Integer, Person> getAcquaintance() {
        return this.acquaintance;
    }
}
