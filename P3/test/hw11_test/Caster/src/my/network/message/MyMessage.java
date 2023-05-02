package my.network.message;

import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Person;

public class MyMessage implements Message {
    private final int id;
    private final int socialValue;
    private final int type;
    private final Person person1;
    private final Person person2;
    private final Group group;

    public MyMessage(int messageId, int messageSocialValue,
                     Person messagePerson1, Person messagePerson2) {
        type = 0;
        group = null;
        id = messageId;
        socialValue = messageSocialValue;
        person1 = messagePerson1;
        person2 = messagePerson2;
    }

    public MyMessage(int messageId, int messageSocialValue,
                     Person messagePerson1, Group messageGroup) {
        type = 1;
        person2 = null;
        id = messageId;
        socialValue = messageSocialValue;
        person1 = messagePerson1;
        group = messageGroup;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getSocialValue() {
        return socialValue;
    }

    @Override
    public Person getPerson1() {
        return person1;
    }

    @Override
    public Person getPerson2() {
        return person2;
    }

    @Override
    public Group getGroup() {
        return group;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Message) {
            return (((Message) obj).getId() == id);
        }
        return false;
    }

    @Override
    public String toString() {
        return "MyMessage{" +
                "id=" + id +
                ", socialValue=" + socialValue +
                ", type=" + type +
                ", person1=" + person1 +
                ", person2=" + person2 +
                ", group=" + group +
                '}';
    }
}
