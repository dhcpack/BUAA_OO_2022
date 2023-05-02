import java.util.ArrayList;
import java.util.List;
import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Person;

public class MyPerson implements Person {
    private final int id;
    private final String name;
    private final int age;
    private int money;
    private int socialValue;

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        MyGlobal.addPerson(this);
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

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Person)) {
            return false;
        }
        return ((Person) obj).getId() == id;
    }

    public boolean isLinked(Person person) {
        if (id == person.getId()) {
            return true;
        }
        if (person instanceof MyPerson) {
            return MyGlobal.haveRelation(id, person.getId());
        }
        // Exchange
        return person.isLinked(this);
    }

    public int queryValue(Person person) {
        if (person instanceof MyPerson) {
            Integer value = MyGlobal.queryValue(id, person.getId());
            if (value == null) {
                return 0;
            }
            return value;
        }
        // Exchange
        return person.queryValue(this);
    }

    public int compareTo(Person rhs) {
        return name.compareTo(rhs.getName());
    }

    public void addSocialValue(int num) {
        socialValue += num;
    }

    @Override
    public int getSocialValue() {
        return socialValue;
    }

    public List<Message> getMessages() {
        return MyGlobal.getMessagesOf(id);
    }

    public List<Message> getReceivedMessages() {
        // copy the list
        List<Message> msgs = getMessages();
        int toIndex = 4;
        if (toIndex > msgs.size()) {
            toIndex = msgs.size();
        }
        return new ArrayList<>(msgs.subList(0, toIndex));
    }

    public void addMoney(int num) {
        money += num;
    }

    public int getMoney() {
        return money;
    }
}
