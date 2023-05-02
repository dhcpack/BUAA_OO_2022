import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Person;

public class MyMessage implements Message {
    private final int type;
    private final int id;
    private final int socialValue;
    private final Person person1;
    private final Person person2;
    private final Group group;

    public MyMessage(int messageId, int messageSocialValue, Person messagePerson1,
            Person messagePerson2) {
        type = 0;
        id = messageId;
        socialValue = messageSocialValue;
        person1 = messagePerson1;
        person2 = messagePerson2;
        group = null;
    }

    public MyMessage(int messageId, int messageSocialValue, Person messagePerson1,
            Group messageGroup) {
        type = 1;
        id = messageId;
        socialValue = messageSocialValue;
        person1 = messagePerson1;
        person2 = null;
        group = messageGroup;
    }

    public int getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public int getSocialValue() {
        return socialValue;
    }

    public Person getPerson1() {
        return person1;
    }

    public Person getPerson2() {
        return person2;
    }

    public Group getGroup() {
        return group;
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Message)) {
            return false;
        }
        return ((Message) obj).getId() == id;
    }
}
