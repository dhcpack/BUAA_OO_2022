import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.RedEnvelopeMessage;

import java.util.HashMap;

public class MyGroup implements Group {
    private int id;
    private HashMap<Integer, Person> people;
    private int valueSum;
    private int ageSum;
    
    public MyGroup(int id) {
        this.id = id;
        this.valueSum = 0;
        this.ageSum = 0;
        this.people = new HashMap<>();
    }
    
    public int getId() {
        return this.id;
    }
    
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (!(obj instanceof Group)) {
            return false;
        } else {
            return (((Group) obj).getId() == id);
        }
    }
    
    public void addMessageSocialValue(Message message) {
        for (Person person: people.values()) {
            person.addSocialValue(message.getSocialValue());
        }
    }
    
    public void addMessageMoney(RedEnvelopeMessage message, Person person) {
        int length = getSize();//it can't be 0
        int piece = message.getMoney() / length;
        for (Person person1 : people.values()) {
            if (person1.equals(person)) {
                person1.addMoney(-(piece * (length - 1)));
            } else {
                person1.addMoney(piece);
            }
        }
    }
    
    public void addPerson(Person person) {
        this.people.put(person.getId(), person);
        for (Person per : people.values()) {
            valueSum += person.queryValue(per);
        }
        ageSum += person.getAge();
    }
    
    public boolean hasPerson(Person person) {
        return people.containsKey(person.getId());
    }
    
    public void addValueSum(int value) {
        this.valueSum += value;
    }
    
    public int getValueSum() {
        return valueSum * 2;
    }
    
    public int getAgeMean() {
        int length = people.size();
        if (length == 0) {
            return 0;
        } else {
            return this.ageSum / length;
        }
    }
    
    public int getAgeVar() {
        int length = people.size();
        if (length == 0) {
            return 0;
        } else {
            int var = 0;
            int mean = getAgeMean();
            for (Person person : people.values()) {
                var += (person.getAge() - mean) * (person.getAge() - mean);
            }
            var = var / length;
            return var;
        }
    }
    
    public void delPerson(Person person) {
        people.remove(person.getId());
        for (Person per : people.values()) {
            valueSum -= person.queryValue(per);
        }
        ageSum -= person.getAge();
    }
    
    public int getSize() {
        return people.size();
    }
}
